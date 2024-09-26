package ac.rino.rinoac.checks.impl.misc;

import ac.rino.rinoac.RinoAPI;
import ac.rino.rinoac.checks.Check;
import ac.rino.rinoac.checks.impl.exploit.ExploitA;
import ac.rino.rinoac.checks.type.PacketCheck;
import ac.rino.rinoac.player.RinoPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ClientBrand extends Check implements PacketCheck {
    String brand = "vanilla";
    boolean hasBrand = false;

    public ClientBrand(RinoPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.PLUGIN_MESSAGE) {
            WrapperPlayClientPluginMessage packet = new WrapperPlayClientPluginMessage(event);
            String channelName = packet.getChannelName();
            handle(channelName, packet.getData());
        }
    }

    public void handle(String channel, byte[] data) {
        if (channel.equalsIgnoreCase("minecraft:brand") || // 1.13+
                channel.equals("MC|Brand")) { // 1.12
            if (data.length > 64 || data.length == 0) {
                brand = "sent " + data.length + " bytes as brand";
            } else if (!hasBrand) {
                byte[] minusLength = new byte[data.length - 1];
                System.arraycopy(data, 1, minusLength, 0, minusLength.length);

                brand = new String(minusLength).replace(" (Velocity)", ""); //removes velocity's brand suffix
                if (player.checkManager.getPrePredictionCheck(ExploitA.class).checkString(brand)) brand = "sent log4j";
                if (!RinoAPI.INSTANCE.getConfigManager().isIgnoredClient(brand)) {
                    String message = RinoAPI.INSTANCE.getConfigManager().getConfig().getStringElse("client-brand-format", "%prefix% &f%player% joined using %brand%");
                    message = RinoAPI.INSTANCE.getExternalAPI().replaceVariables(getPlayer(), message, true);
                    // sendMessage is async safe while broadcast isn't due to adventure
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.hasPermission("grim.brand")) {
                            player.sendMessage(message);
                        }
                    }
                }
            }

            hasBrand = true;
        }
    }

    public String getBrand() {
        return brand;
    }
}

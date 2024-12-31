package ac.grim.grimac.checks.impl.misc;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ClientBrand extends Check implements PacketCheck {
    @Getter
    private String brand = "vanilla";
    private boolean hasBrand = false;

    public ClientBrand(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.PLUGIN_MESSAGE) {
            WrapperPlayClientPluginMessage packet = new WrapperPlayClientPluginMessage(event);
            handle(packet.getChannelName(), packet.getData());
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
                brand = ChatColor.stripColor(brand); //strip color codes from client brand
                if (!GrimAPI.INSTANCE.getConfigManager().isIgnoredClient(brand)) {
                    String message = GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse("client-brand-format", "%prefix% &f%player% joined using %brand%");
                    message = MessageUtil.replacePlaceholders(player, message);

                    Component component = MessageUtil.miniMessage(message);

                    // sendMessage is async safe while broadcast isn't due to adventure
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.hasPermission("grim.brand")) {
                            MessageUtil.sendMessage(player, component);
                        }
                    }
                }
            }

            hasBrand = true;
        }
    }
}

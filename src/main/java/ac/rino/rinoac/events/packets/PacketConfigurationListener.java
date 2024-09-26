package ac.rino.rinoac.events.packets;

import ac.rino.rinoac.RinoAPI;
import ac.rino.rinoac.checks.impl.misc.ClientBrand;
import ac.rino.rinoac.player.RinoPlayer;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.configuration.client.WrapperConfigClientPluginMessage;

public class PacketConfigurationListener extends PacketListenerAbstract {

    public PacketConfigurationListener() {
        super(PacketListenerPriority.LOW);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Configuration.Client.PLUGIN_MESSAGE) {
            RinoPlayer player = RinoAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
            if (player == null) return;
            //
            WrapperConfigClientPluginMessage wrapper = new WrapperConfigClientPluginMessage(event);
            String channelName = wrapper.getChannelName();
            byte[] data = wrapper.getData();
            if (channelName.equalsIgnoreCase("minecraft:brand") || channelName.equals("MC|Brand")) {
                player.checkManager.getPacketCheck(ClientBrand.class).handle(channelName, data);
            }
        }
    }

}

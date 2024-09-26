package ac.rino.rinoac.checks.impl.badpackets;

import ac.rino.rinoac.checks.Check;
import ac.rino.rinoac.checks.CheckData;
import ac.rino.rinoac.checks.type.PacketCheck;
import ac.rino.rinoac.player.RinoPlayer;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Client;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction.Action;

@CheckData(name = "BadPacketsQ")
public class BadPacketsQ extends Check implements PacketCheck {
    public BadPacketsQ(final RinoPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == Client.ENTITY_ACTION) {
            WrapperPlayClientEntityAction wrapper = new WrapperPlayClientEntityAction(event);

            if (wrapper.getJumpBoost() < 0 || wrapper.getJumpBoost() > 100 || wrapper.getEntityId() != player.entityID || (wrapper.getAction() != Action.START_JUMPING_WITH_HORSE && wrapper.getJumpBoost() != 0)) {
                if (flagAndAlert("boost=" + wrapper.getJumpBoost() + ", action=" + wrapper.getAction() + ", entity=" + wrapper.getEntityId()) && shouldModifyPackets()) {
                    event.setCancelled(true);
                    player.onPacketCancel();
                }
            }
        }
    }
}

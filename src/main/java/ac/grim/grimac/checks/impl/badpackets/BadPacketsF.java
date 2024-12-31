package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;

@CheckData(name = "BadPacketsF", description = "Sent duplicate sprinting status")
public class BadPacketsF extends Check implements PacketCheck {
    public boolean lastSprinting;
    public boolean exemptNext = true; // Support 1.14+ clients starting on either true or false sprinting, we don't know

    public BadPacketsF(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION) {
            WrapperPlayClientEntityAction packet = new WrapperPlayClientEntityAction(event);

            if (packet.getAction() == WrapperPlayClientEntityAction.Action.START_SPRINTING) {
                if (lastSprinting) {
                    if (exemptNext) {
                        exemptNext = false;
                        return;
                    }
                    if (flagAndAlert("state=true") && shouldModifyPackets()) {
                        event.setCancelled(true);
                        player.onPacketCancel();
                    }
                }

                lastSprinting = true;
            } else if (packet.getAction() == WrapperPlayClientEntityAction.Action.STOP_SPRINTING) {
                if (!lastSprinting) {
                    if (exemptNext) {
                        exemptNext = false;
                        return;
                    }
                    if (flagAndAlert("state=false") && shouldModifyPackets()) {
                        event.setCancelled(true);
                        player.onPacketCancel();
                    }
                }

                lastSprinting = false;
            }
        }
    }
}

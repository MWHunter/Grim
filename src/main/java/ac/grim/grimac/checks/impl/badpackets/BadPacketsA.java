package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;

@CheckData(name = "BadPacketsA", description = "Sent duplicate slot id")
public class BadPacketsA extends Check implements PacketCheck {
    int lastSlot = -1;

    public BadPacketsA(final GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.HELD_ITEM_CHANGE) {
            final int slot = new WrapperPlayClientHeldItemChange(event).getSlot();

            if (slot == lastSlot && flagAndAlert("slot=" + slot) && shouldModifyPackets()) {
                event.setCancelled(true);
                player.onPacketCancel();
            }

            lastSlot = slot;
        }
    }
}

package ac.grim.grimac.checks.impl.multiactions;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.packetevents.protocol.packettype.PacketType;

@CheckData(name = "MultiActionsD", description = "Closed inventory while performing other actions", experimental = true)
public class MultiActionsD extends Check implements PacketCheck {
    public MultiActionsD(GrimPlayer player) {
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.CLOSE_WINDOW) {
            String verbose = player.isSprinting && !player.isSwimming ? "sprinting" : "";

            if (player.packetStateData.isSlowedByUsingItem()) {
                verbose += (verbose.isEmpty() ? "" : ", ") + "using";
            }

            if (!verbose.isEmpty() && flagAndAlert(verbose) && shouldModifyPackets()) {
                event.setCancelled(true);
                player.onPacketCancel();
            }
        }
    }
}

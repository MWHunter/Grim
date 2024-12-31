package ac.grim.grimac.events.packets;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.packetevents.event.PacketListenerAbstract;
import ac.grim.grimac.shaded.com.packetevents.event.PacketListenerPriority;
import ac.grim.grimac.shaded.com.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.packetevents.wrapper.play.server.WrapperPlayServerBlockAction;
import ac.grim.grimac.utils.data.ShulkerData;
import ac.grim.grimac.utils.nmsutil.Materials;

// If a player doesn't get this packet, then they don't know the shulker box is currently opened
// Meaning if a player enters a chunk with an opened shulker box, they see the shulker box as closed.
//
// Exempting the player on shulker boxes is an option... but then you have people creating PvP arenas
// on shulker boxes to get high lenience.
//
public class PacketBlockAction extends PacketListenerAbstract {
    public PacketBlockAction() {
        super(PacketListenerPriority.HIGH);
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.BLOCK_ACTION) {
            GrimPlayer player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
            if (player == null) return;

            WrapperPlayServerBlockAction blockAction = new WrapperPlayServerBlockAction(event);
            Vector3i blockPos = blockAction.getBlockPosition();

            player.latencyUtils.addRealTimeTask(player.lastTransactionSent.get(), () -> {
                // The client ignores the state sent to the client.
                WrappedBlockState existing = player.compensatedWorld.getWrappedBlockStateAt(blockPos);
                if (Materials.isShulker(existing.getType())) {
                    // Param is the number of viewers of the shulker box.
                    // Hashset with .equals() set to be position
                    if (blockAction.getActionData() >= 1) {
                        ShulkerData data = new ShulkerData(blockPos, player.lastTransactionSent.get(), false);
                        player.compensatedWorld.openShulkerBoxes.remove(data);
                        player.compensatedWorld.openShulkerBoxes.add(data);
                    } else {
                        // The shulker box is closing
                        ShulkerData data = new ShulkerData(blockPos, player.lastTransactionSent.get(), true);
                        player.compensatedWorld.openShulkerBoxes.remove(data);
                        player.compensatedWorld.openShulkerBoxes.add(data);
                    }
                }
            });
        }
    }
}

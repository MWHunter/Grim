package ac.rino.rinoac.events.packets;

import ac.rino.rinoac.RinoAPI;
import ac.rino.rinoac.player.RinoPlayer;
import ac.rino.rinoac.utils.data.ShulkerData;
import ac.rino.rinoac.utils.nmsutil.Materials;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockAction;

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
            RinoPlayer player = RinoAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
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

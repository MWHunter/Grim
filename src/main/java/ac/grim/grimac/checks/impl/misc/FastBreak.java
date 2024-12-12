package ac.grim.grimac.checks.impl.misc;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.BlockBreakCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.BlockBreak;
import ac.grim.grimac.utils.math.GrimMath;
import ac.grim.grimac.utils.nmsutil.BlockBreakSpeed;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerAcknowledgeBlockChanges;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockChange;
import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

// Based loosely off of Hawk BlockBreakSpeedSurvival
// Also based loosely off of NoCheatPlus FastBreak
// Also based off minecraft wiki: https://minecraft.wiki/w/Breaking#Instant_breaking
@CheckData(name = "FastBreak")
public class FastBreak extends Check implements BlockBreakCheck {
    public FastBreak(GrimPlayer playerData) {
        super(playerData);
    }

    // The block the player is currently breaking
    Vector3i targetBlock = null;
    // The material of block the player is currently breaking
    StateType targetType = null;

    // The maximum amount of damage the player deals to the block
    //
    double maximumBlockDamage = 0;
    // The last time a finish digging packet was sent, to enforce 0.3-second delay after non-instabreak
    long lastFinishBreak = 0;
    // The time the player started to break the block, to know how long the player waited until they finished breaking the block
    long startBreak = 0;

    // The buffer to this check
    double blockBreakBalance = 0;
    double blockDelayBalance = 0;

    @Override
    public void onBlockBreak(BlockBreak blockBreak) {
        if (blockBreak.action == DiggingAction.START_DIGGING) {
            WrappedBlockState block = blockBreak.block;

            // Exempt all blocks that do not exist in the player version
            if (WrappedBlockState.getDefaultState(player.getClientVersion(), block.getType()).getType() == StateTypes.AIR) {
                return;
            }

            startBreak = System.currentTimeMillis() - (targetBlock == null ? 50 : 0); // ???
            targetBlock = blockBreak.position;
            targetType = block.getType();

            maximumBlockDamage = BlockBreakSpeed.getBlockDamage(player, targetBlock);

            double breakDelay = System.currentTimeMillis() - lastFinishBreak;

            if (breakDelay >= 275) { // Reduce buffer if "close enough"
                blockDelayBalance *= 0.9;
            } else { // Otherwise, increase buffer
                blockDelayBalance += 300 - breakDelay;
            }

            if (blockDelayBalance > 1000) { // If more than a second of advantage
                flagAndAlert("delay=" + breakDelay + ", type=" + targetType);
                if (shouldModifyPackets()) {
                    blockBreak.cancel();
                }
            }

            clampBalance();
        }

        if (blockBreak.action == DiggingAction.FINISHED_DIGGING && targetBlock != null) {
            double predictedTime = Math.ceil(1 / maximumBlockDamage) * 50;
            double realTime = System.currentTimeMillis() - startBreak;
            double diff = predictedTime - realTime;

            clampBalance();

            if (diff < 25) {  // Reduce buffer if "close enough"
                blockBreakBalance *= 0.9;
            } else { // Otherwise, increase buffer
                blockBreakBalance += diff;
            }

            if (blockBreakBalance > 1000) { // If more than a second of advantage
                if (flagAndAlert("diff=" + diff + ", balance=" + blockBreakBalance
                        + ", maxBlockDamage=" + maximumBlockDamage + ", type=" + targetType) && shouldModifyPackets()) {
                    blockBreak.cancel();
                }
            }

            lastFinishBreak = System.currentTimeMillis();
        }
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        // Find the most optimal block damage using the animation packet, which is sent at least once a tick when breaking blocks
        // On 1.8 clients, via screws with this packet meaning we must fall back to the 1.8 idle flying packet
        if ((player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9) ? event.getPacketType() == PacketType.Play.Client.ANIMATION : WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) && targetBlock != null) {
            maximumBlockDamage = Math.max(maximumBlockDamage, BlockBreakSpeed.getBlockDamage(player, targetBlock));
        }

        if (event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING) {
            WrapperPlayClientPlayerDigging digging = new WrapperPlayClientPlayerDigging(event);
            final Vector3i blockPosition = digging.getBlockPosition();

            if (digging.getAction() == DiggingAction.START_DIGGING) {
                WrappedBlockState block = player.compensatedWorld.getWrappedBlockStateAt(blockPosition);

                // Exempt all blocks that do not exist in the player version
                if (WrappedBlockState.getDefaultState(player.getClientVersion(), block.getType()).getType() == StateTypes.AIR) {
                    return;
                }

                startBreak = System.currentTimeMillis() - (targetBlock == null ? 50 : 0); // ???
                targetBlock = blockPosition;
                targetType = block.getType();

                maximumBlockDamage = BlockBreakSpeed.getBlockDamage(player, targetBlock);

                double breakDelay = System.currentTimeMillis() - lastFinishBreak;

                if (breakDelay >= 275) { // Reduce buffer if "close enough"
                    blockDelayBalance *= 0.9;
                } else { // Otherwise, increase buffer
                    blockDelayBalance += 300 - breakDelay;
                }

                if (blockDelayBalance > 1000) { // If more than a second of advantage
                    flagAndAlert("delay=" + breakDelay + ", type=" + targetType);
                    if (shouldModifyPackets()) {
                        event.setCancelled(true); // Cancelling start digging will cause server to reject block break
                        player.onPacketCancel();
                    }
                }

                clampBalance();
            }

            if (digging.getAction() == DiggingAction.FINISHED_DIGGING && targetBlock != null) {
                double predictedTime = Math.ceil(1 / maximumBlockDamage) * 50;
                double realTime = System.currentTimeMillis() - startBreak;
                double diff = predictedTime - realTime;

                clampBalance();

                if (diff < 25) {  // Reduce buffer if "close enough"
                    blockBreakBalance *= 0.9;
                } else { // Otherwise, increase buffer
                    blockBreakBalance += diff;
                }

                if (blockBreakBalance > 1000) { // If more than a second of advantage
                    FoliaScheduler.getEntityScheduler().execute(player.bukkitPlayer, GrimAPI.INSTANCE.getPlugin(), () -> {
                        Player bukkitPlayer = player.bukkitPlayer;
                        if (bukkitPlayer == null || !bukkitPlayer.isOnline()) return;

                        if (bukkitPlayer.getLocation().distance(new Location(bukkitPlayer.getWorld(), blockPosition.getX(), blockPosition.getY(), blockPosition.getZ())) < 64) {
                            final int chunkX = blockPosition.getX() >> 4;
                            final int chunkZ = blockPosition.getZ() >> 4;
                            if (!bukkitPlayer.getWorld().isChunkLoaded(chunkX, chunkZ)) return; // Don't load chunks sync

                            Chunk chunk = bukkitPlayer.getWorld().getChunkAt(chunkX, chunkZ);
                            Block block = chunk.getBlock(blockPosition.getX() & 15, blockPosition.getY(), blockPosition.getZ() & 15);

                            int blockId;

                            if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13)) {
                                // Cache this because strings are expensive
                                blockId = WrappedBlockState.getByString(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), block.getBlockData().getAsString(false)).getGlobalId();
                            } else {
                                blockId = (block.getType().getId() << 4) | block.getData();
                            }

                            player.user.sendPacket(new WrapperPlayServerBlockChange(blockPosition, blockId));

                            if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_19)) { // Via will handle this for us pre-1.19
                                player.user.sendPacket(new WrapperPlayServerAcknowledgeBlockChanges(digging.getSequence())); // Make 1.19 clients apply the changes
                            }
                        }
                    }, null, 0);

                    if (flagAndAlert("diff=" + diff + ", balance=" + blockBreakBalance
                            + ", maxBlockDamage=" + maximumBlockDamage + ", type=" + targetType) && shouldModifyPackets()) {
                        event.setCancelled(true);
                        player.onPacketCancel();
                    }
                }

                lastFinishBreak = System.currentTimeMillis();
            }
        }
    }

    private void clampBalance() {
        double balance = Math.max(1000, (player.getTransactionPing()));
        blockBreakBalance = GrimMath.clamp(blockBreakBalance, -balance, balance); // Clamp not Math.max in case other logic changes
        blockDelayBalance = GrimMath.clamp(blockDelayBalance, -balance, balance);
    }
}

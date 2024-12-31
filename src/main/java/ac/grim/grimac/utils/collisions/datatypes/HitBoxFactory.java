package ac.grim.grimac.utils.collisions.datatypes;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.packetevents.protocol.world.states.type.StateType;

public interface HitBoxFactory {
    CollisionBox fetch(GrimPlayer player, StateType heldItem, ClientVersion version, WrappedBlockState block, int x, int y, int z);
}

package ac.rino.rinoac.utils.collisions.datatypes;

import ac.rino.rinoac.player.RinoPlayer;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;

public interface CollisionFactory {
    CollisionBox fetch(RinoPlayer player, ClientVersion version, WrappedBlockState block, int x, int y, int z);
}
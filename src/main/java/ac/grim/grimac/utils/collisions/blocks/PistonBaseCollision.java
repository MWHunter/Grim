package ac.grim.grimac.utils.collisions.blocks;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.CollisionFactory;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;

public class PistonBaseCollision implements CollisionFactory {

    @Override
    public CollisionBox fetch(GrimPlayer player, ClientVersion version, WrappedBlockState block, int x, int y, int z) {
        return !block.isExtended() ? new SimpleCollisionBox(0, 0, 0, 1, 1, 1, true) : switch (block.getFacing()) {
            case UP -> SimpleCollisionBox.hex(0, 0, 0, 16, 12, 16);
            case NORTH -> SimpleCollisionBox.hex(0, 0, 4, 16, 16, 16);
            case SOUTH -> SimpleCollisionBox.hex(0, 0, 0, 16, 16, 12);
            case WEST -> SimpleCollisionBox.hex(4, 0, 0, 16, 16, 16);
            case EAST -> SimpleCollisionBox.hex(0, 0, 0, 12, 16, 16);
            default -> SimpleCollisionBox.hex(0, 4, 0, 16, 16, 16);
        };
    }
}

package ac.grim.grimac.utils.collisions.blocks;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.collisions.datatypes.CollisionBox;
import ac.grim.grimac.utils.collisions.datatypes.CollisionFactory;
import ac.grim.grimac.utils.collisions.datatypes.SimpleCollisionBox;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.protocol.world.states.enums.Type;

// In 1.12, chests don't have data that say what type of chest they are, other than direction
// In 1.13, chests store whether they are left or right
// With 1.12 clients on 1.13+ servers, the client checks NORTH and WEST for chests before SOUTH and EAST
// With 1.13+ clients on 1.12 servers, ViaVersion checks NORTH and WEST for chests before SOUTH and EAST
public class DynamicChest implements CollisionFactory {
    public CollisionBox fetch(GrimPlayer player, ClientVersion version, WrappedBlockState chest, int x, int y, int z) {
        // 1.13+ clients on 1.13+ servers
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13)
                && version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
            if (chest.getTypeData() == Type.SINGLE) {
                return SimpleCollisionBox.hex(1, 0, 1, 15, 14, 15);
            }

            if (chest.getFacing() == BlockFace.SOUTH && chest.getTypeData() == Type.RIGHT || chest.getFacing() == BlockFace.NORTH && chest.getTypeData() == Type.LEFT) {
                return SimpleCollisionBox.hex(1, 0, 1, 16, 14, 15); // Connected to the east face
            } else if (chest.getFacing() == BlockFace.SOUTH && chest.getTypeData() == Type.LEFT || chest.getFacing() == BlockFace.NORTH && chest.getTypeData() == Type.RIGHT) {
                return SimpleCollisionBox.hex(0, 0, 1, 15, 14, 15); // Connected to the west face
            } else if (chest.getFacing() == BlockFace.WEST && chest.getTypeData() == Type.RIGHT || chest.getFacing() == BlockFace.EAST && chest.getTypeData() == Type.LEFT) {
                return SimpleCollisionBox.hex(1, 0, 1, 15, 14, 16); // Connected to the south face
            } else {
                return SimpleCollisionBox.hex(1, 0, 0, 15, 14, 15); // Connected to the north face
            }
        }

        // 1.12 clients on 1.12 servers
        // 1.12 clients on 1.12 servers
        // 1.13 clients on 1.12 servers
        if (chest.getFacing() == BlockFace.EAST || chest.getFacing() == BlockFace.WEST) {
            WrappedBlockState westState = player.compensatedWorld.getBlock(x - 1, y, z);

            if (westState.getType() == chest.getType()) {
                return SimpleCollisionBox.hex(0, 0, 1, 15, 14, 15); // Connected to the west face
            }

            WrappedBlockState eastState = player.compensatedWorld.getBlock(x + 1, y, z);
            if (eastState.getType() == chest.getType()) {
                return SimpleCollisionBox.hex(1, 0, 1, 16, 14, 15); // Connected to the east face
            }
        } else {
            WrappedBlockState northState = player.compensatedWorld.getBlock(x, y, z - 1);
            if (northState.getType() == chest.getType()) {
                return SimpleCollisionBox.hex(1, 0, 0, 15, 14, 15); // Connected to the north face
            }

            WrappedBlockState southState = player.compensatedWorld.getBlock(x, y, z + 1);
            if (southState.getType() == chest.getType()) {
                return SimpleCollisionBox.hex(1, 0, 1, 15, 14, 16); // Connected to the south face
            }
        }

        // Single chest
        return SimpleCollisionBox.hex(1, 0.0D, 1, 15, 14, 15);
    }
}

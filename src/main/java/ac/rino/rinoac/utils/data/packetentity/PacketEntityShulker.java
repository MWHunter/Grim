package ac.rino.rinoac.utils.data.packetentity;

import ac.rino.rinoac.player.RinoPlayer;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.world.BlockFace;

import java.util.UUID;

public class PacketEntityShulker extends PacketEntity {
    public BlockFace facing = BlockFace.DOWN;

    public PacketEntityShulker(RinoPlayer player, UUID uuid, EntityType type, double x, double y, double z) {
        super(player, uuid, type, x, y, z);
    }
}

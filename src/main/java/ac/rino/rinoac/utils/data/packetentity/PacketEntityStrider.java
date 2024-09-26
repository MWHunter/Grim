package ac.rino.rinoac.utils.data.packetentity;

import ac.rino.rinoac.player.RinoPlayer;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;

import java.util.UUID;

public class PacketEntityStrider extends PacketEntityRideable {
    public boolean isShaking = false;

    public PacketEntityStrider(RinoPlayer player, UUID uuid, EntityType type, double x, double y, double z) {
        super(player, uuid, type, x, y, z);
    }
}

package ac.rino.rinoac.utils.data.packetentity.dragon;

import ac.rino.rinoac.player.RinoPlayer;
import ac.rino.rinoac.utils.data.packetentity.PacketEntity;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;

public final class PacketEntityEnderDragonPart extends PacketEntity {

    private final DragonPart part;
    private final float width, height;

    public PacketEntityEnderDragonPart(RinoPlayer player, DragonPart part, double x, double y, double z, float width, float height) {
        super(player, null, EntityTypes.ENDER_DRAGON, x, y, z);
        this.part = part;
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public DragonPart getPart() {
        return part;
    }
}

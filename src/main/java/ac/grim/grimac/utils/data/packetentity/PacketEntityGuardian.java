package ac.grim.grimac.utils.data.packetentity;

import ac.grim.grimac.player.GrimPlayer;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;

public class PacketEntityGuardian extends PacketEntity {
    // this is only actually stored as a field in legacy versions (1.8 - 1.10.2)
    // in newer versions Elder Guardians are separate entities, we use this field regardless for simplicity
    public boolean isElder;

    public PacketEntityGuardian(GrimPlayer player, EntityType type, boolean isElder) {
        super(player, type);
        this.isElder = isElder;
    }
}

package ac.grim.grimac.utils.data;

import ac.grim.grimac.shaded.com.packetevents.protocol.teleport.RelativeFlag;
import ac.grim.grimac.shaded.com.packetevents.util.Vector3d;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class TeleportData {
    Vector3d location;
    RelativeFlag flags;
    @Setter
    int transaction;
    @Setter
    int teleportId;

    public boolean isRelative() {
        return isRelativeX() || isRelativeY() || isRelativeZ();
    }

    public boolean isRelativeX() {
        return flags.has(RelativeFlag.X.getMask());
    }

    public boolean isRelativeY() {
        return flags.has(RelativeFlag.Y.getMask());
    }

    public boolean isRelativeZ() {
        return flags.has(RelativeFlag.Z.getMask());
    }
}

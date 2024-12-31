package ac.grim.grimac.utils.anticheat;

import ac.grim.grimac.shaded.com.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.packetevents.manager.server.ServerVersion;

public class Version {
    private static final boolean IS_FLAT = PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13);

    public static boolean isFlat() {
        return IS_FLAT;
    }
}

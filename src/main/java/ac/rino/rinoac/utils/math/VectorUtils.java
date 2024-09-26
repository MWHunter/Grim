package ac.rino.rinoac.utils.math;

import ac.rino.rinoac.utils.collisions.datatypes.SimpleCollisionBox;
import com.github.retrooper.packetevents.util.Vector3d;
import org.bukkit.util.Vector;

public class VectorUtils {
    public static Vector cutBoxToVector(Vector vectorToCutTo, Vector min, Vector max) {
        SimpleCollisionBox box = new SimpleCollisionBox(min, max).sort();
        return cutBoxToVector(vectorToCutTo, box);
    }

    public static Vector cutBoxToVector(Vector vectorCutTo, SimpleCollisionBox box) {
        return new Vector(RinoMath.clamp(vectorCutTo.getX(), box.minX, box.maxX),
                RinoMath.clamp(vectorCutTo.getY(), box.minY, box.maxY),
                RinoMath.clamp(vectorCutTo.getZ(), box.minZ, box.maxZ));
    }

    public static Vector fromVec3d(Vector3d vector3d) {
        return new Vector(vector3d.getX(), vector3d.getY(), vector3d.getZ());
    }

    // Clamping stops the player from causing an integer overflow and crashing the netty thread
    public static Vector3d clampVector(Vector3d toClamp) {
        double x = RinoMath.clamp(toClamp.getX(), -3.0E7D, 3.0E7D);
        double y = RinoMath.clamp(toClamp.getY(), -2.0E7D, 2.0E7D);
        double z = RinoMath.clamp(toClamp.getZ(), -3.0E7D, 3.0E7D);

        return new Vector3d(x, y, z);
    }
}

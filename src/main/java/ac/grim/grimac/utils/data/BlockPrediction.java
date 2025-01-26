package ac.grim.grimac.utils.data;

import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3i;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class BlockPrediction {
    public List<Vector3i> forBlockUpdate;
    public int originalBlockId;
    public final Vector3d playerPosition;
}

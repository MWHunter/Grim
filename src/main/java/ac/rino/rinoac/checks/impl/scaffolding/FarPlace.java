package ac.rino.rinoac.checks.impl.scaffolding;

import ac.rino.rinoac.checks.CheckData;
import ac.rino.rinoac.checks.type.BlockPlaceCheck;
import ac.rino.rinoac.player.RinoPlayer;
import ac.rino.rinoac.utils.anticheat.update.BlockPlace;
import ac.rino.rinoac.utils.collisions.datatypes.SimpleCollisionBox;
import ac.rino.rinoac.utils.math.VectorUtils;
import com.github.retrooper.packetevents.protocol.attribute.Attributes;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import com.github.retrooper.packetevents.util.Vector3i;
import org.bukkit.util.Vector;

@CheckData(name = "FarPlace")
public class FarPlace extends BlockPlaceCheck {
    public FarPlace(RinoPlayer player) {
        super(player);
    }

    @Override
    public void onBlockPlace(final BlockPlace place) {
        Vector3i blockPos = place.getPlacedAgainstBlockLocation();

        if (place.getMaterial() == StateTypes.SCAFFOLDING) return;

        double min = Double.MAX_VALUE;
        for (double d : player.getPossibleEyeHeights()) {
            SimpleCollisionBox box = new SimpleCollisionBox(blockPos);
            Vector eyes = new Vector(player.x, player.y + d, player.z);
            Vector best = VectorUtils.cutBoxToVector(eyes, box);
            min = Math.min(min, eyes.distanceSquared(best));
        }

        // getPickRange() determines this?
        // With 1.20.5+ the new attribute determines creative mode reach using a modifier
        double maxReach = player.compensatedEntities.getSelf().getAttributeValue(Attributes.PLAYER_BLOCK_INTERACTION_RANGE);
        double threshold = player.getMovementThreshold();
        maxReach += Math.hypot(threshold, threshold);

        if (min > maxReach * maxReach) { // fail
            if (flagAndAlert() && shouldModifyPackets() && shouldCancel()) {
                place.resync();
            }
        }
    }
}

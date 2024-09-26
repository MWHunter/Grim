package ac.rino.rinoac.predictionengine.predictions;

import ac.rino.rinoac.player.RinoPlayer;
import ac.rino.rinoac.utils.data.VectorData;
import ac.rino.rinoac.utils.math.RinoMath;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class PredictionEngineLava extends PredictionEngine {
    @Override
    public void addJumpsToPossibilities(RinoPlayer player, Set<VectorData> existingVelocities) {
        for (VectorData vector : new HashSet<>(existingVelocities)) {
            if (player.couldSkipTick && vector.isZeroPointZeroThree()) {
                double extraVelFromVertTickSkipUpwards = RinoMath.clamp(player.actualMovement.getY(), vector.vector.clone().getY(), vector.vector.clone().getY() + 0.05f);
                existingVelocities.add(new VectorData(vector.vector.clone().setY(extraVelFromVertTickSkipUpwards), vector, VectorData.VectorType.Jump));
            } else {
                existingVelocities.add(new VectorData(vector.vector.clone().add(new Vector(0, 0.04f, 0)), vector, VectorData.VectorType.Jump));
            }

            if (player.slightlyTouchingLava && player.lastOnGround && !player.onGround) {
                Vector withJump = vector.vector.clone();
                super.doJump(player, withJump);
                existingVelocities.add(new VectorData(withJump, vector, VectorData.VectorType.Jump));
            }
        }
    }
}

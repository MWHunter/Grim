package ac.rino.rinoac.predictionengine.predictions.rideable;

import ac.rino.rinoac.player.RinoPlayer;
import ac.rino.rinoac.predictionengine.predictions.PredictionEngineNormal;
import ac.rino.rinoac.utils.data.VectorData;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Set;

public class PredictionEngineRideableNormal extends PredictionEngineNormal {

    Vector movementVector;

    public PredictionEngineRideableNormal(Vector movementVector) {
        this.movementVector = movementVector;
    }

    @Override
    public void addJumpsToPossibilities(RinoPlayer player, Set<VectorData> existingVelocities) {
        PredictionEngineRideableUtils.handleJumps(player, existingVelocities);
    }

    @Override
    public List<VectorData> applyInputsToVelocityPossibilities(RinoPlayer player, Set<VectorData> possibleVectors, float speed) {
        return PredictionEngineRideableUtils.applyInputsToVelocityPossibilities(movementVector, player, possibleVectors, speed);
    }
}

package ac.grim.grimac.predictionengine.predictions.rideable;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.predictions.PredictionEngineWaterLegacy;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.vector.Vector3D;

import java.util.List;
import java.util.Set;

public class PredictionEngineRideableWaterLegacy extends PredictionEngineWaterLegacy {
    Vector3D movementVector;

    public PredictionEngineRideableWaterLegacy(Vector3D movementVector) {
        this.movementVector = movementVector;
    }

    @Override
    public void addJumpsToPossibilities(GrimPlayer player, Set<VectorData> existingVelocities) {
        PredictionEngineRideableUtils.handleJumps(player, existingVelocities);
    }

    @Override
    public List<VectorData> applyInputsToVelocityPossibilities(GrimPlayer player, Set<VectorData> possibleVectors, float speed) {
        return PredictionEngineRideableUtils.applyInputsToVelocityPossibilities(movementVector, player, possibleVectors, speed);
    }
}

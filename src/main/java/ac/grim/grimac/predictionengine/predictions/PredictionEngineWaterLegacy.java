package ac.grim.grimac.predictionengine.predictions;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.data.VectorData;
import ac.grim.grimac.utils.vector.Vector3D;
import ac.grim.grimac.utils.vector.Vector3D;

import java.util.HashSet;
import java.util.Set;

import static ac.grim.grimac.utils.vector.VectorFactory.newVector3D;
import static java.lang.Math.sqrt;

public class PredictionEngineWaterLegacy extends PredictionEngine {
    double playerGravity;
    float swimmingSpeed;
    float swimmingFriction;
    double lastY;

    public void guessBestMovement(float swimmingSpeed, GrimPlayer player, double playerGravity, float swimmingFriction, double lastY) {
        this.playerGravity = playerGravity;
        this.swimmingSpeed = swimmingSpeed;
        this.swimmingFriction = swimmingFriction;
        this.lastY = lastY;
        super.guessBestMovement(swimmingSpeed, player);
    }

    // This is just the vanilla equation for legacy water movement
    @Override
    public Vector3D getMovementResultFromInput(GrimPlayer player, Vector3D inputVector, float f, float f2) {
        float lengthSquared = (float) inputVector.lengthSquared();

        if (lengthSquared >= 1.0E-4F) {
            lengthSquared = (float) sqrt(lengthSquared);

            if (lengthSquared < 1.0F) {
                lengthSquared = 1.0F;
            }

            lengthSquared = swimmingSpeed / lengthSquared;
            inputVector.multiply(lengthSquared);
            float sinResult = player.trigHandler.sin(player.xRot * 0.017453292F);
            float cosResult = player.trigHandler.cos(player.xRot * 0.017453292F);

            return newVector3D(inputVector.getX() * cosResult - inputVector.getZ() * sinResult, inputVector.getY(), inputVector.getZ() * cosResult + inputVector.getX() * sinResult);
        }

        return newVector3D();
    }


    @Override
    public void addJumpsToPossibilities(GrimPlayer player, Set<VectorData> existingVelocities) {
        for (VectorData vector : new HashSet<>(existingVelocities)) {
            existingVelocities.add(new VectorData(vector.vector.clone().add(newVector3D(0, 0.04f, 0)), vector, VectorData.VectorType.Jump));

            if (player.skippedTickInActualMovement) {
                existingVelocities.add(new VectorData(vector.vector.clone().add(newVector3D(0, 0.02f, 0)), vector, VectorData.VectorType.Jump));
            }
        }
    }

    @Override
    public void endOfTick(GrimPlayer player, double playerGravity) {
        super.endOfTick(player, playerGravity);

        for (VectorData vector : player.getPossibleVelocitiesMinusKnockback()) {
            vector.vector.multiply(newVector3D(swimmingFriction, 0.8F, swimmingFriction));

            // Gravity
            vector.vector.setY(vector.vector.getY() - 0.02D);
        }
    }
}

package ac.rino.rinoac.predictionengine.movementtick;

import ac.rino.rinoac.player.RinoPlayer;
import ac.rino.rinoac.predictionengine.predictions.PredictionEngineLava;
import ac.rino.rinoac.predictionengine.predictions.PredictionEngineNormal;
import ac.rino.rinoac.predictionengine.predictions.PredictionEngineWater;
import ac.rino.rinoac.predictionengine.predictions.PredictionEngineWaterLegacy;
import ac.rino.rinoac.utils.nmsutil.BlockProperties;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;

public class MovementTickerPlayer extends MovementTicker {
    public MovementTickerPlayer(RinoPlayer player) {
        super(player);
    }

    @Override
    public void doWaterMove(float swimSpeed, boolean isFalling, float swimFriction) {
        if (player.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13)) {
            new PredictionEngineWater().guessBestMovement(swimSpeed, player, isFalling, player.gravity, swimFriction, player.lastY);
        } else {
            new PredictionEngineWaterLegacy().guessBestMovement(swimSpeed, player, player.gravity, swimFriction, player.lastY);
        }
    }

    @Override
    public void doLavaMove() {
        new PredictionEngineLava().guessBestMovement(0.02F, player);
    }

    @Override
    public void doNormalMove(float blockFriction) {
        new PredictionEngineNormal().guessBestMovement(BlockProperties.getFrictionInfluencedSpeed(blockFriction, player), player);
    }
}

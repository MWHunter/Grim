package ac.rino.rinoac.checks.type;

import ac.rino.rinoac.utils.anticheat.update.PredictionComplete;

public interface PostPredictionCheck extends PacketCheck {

    default void onPredictionComplete(final PredictionComplete predictionComplete) {
    }
}

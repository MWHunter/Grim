package ac.rino.rinoac.checks.impl.movement;

import ac.rino.rinoac.checks.Check;
import ac.rino.rinoac.checks.type.PositionCheck;
import ac.rino.rinoac.player.RinoPlayer;
import ac.rino.rinoac.utils.anticheat.update.PositionUpdate;

public class PredictionRunner extends Check implements PositionCheck {
    public PredictionRunner(RinoPlayer playerData) {
        super(playerData);
    }

    @Override
    public void onPositionUpdate(final PositionUpdate positionUpdate) {
        if (!player.compensatedEntities.getSelf().inVehicle()) {
            player.movementCheckRunner.processAndCheckMovementPacket(positionUpdate);
        }
    }
}

package ac.rino.rinoac.checks.impl.movement;

import ac.rino.rinoac.checks.Check;
import ac.rino.rinoac.checks.type.VehicleCheck;
import ac.rino.rinoac.player.RinoPlayer;
import ac.rino.rinoac.utils.anticheat.update.PositionUpdate;
import ac.rino.rinoac.utils.anticheat.update.VehiclePositionUpdate;

public class VehiclePredictionRunner extends Check implements VehicleCheck {
    public VehiclePredictionRunner(RinoPlayer playerData) {
        super(playerData);
    }

    @Override
    public void process(final VehiclePositionUpdate vehicleUpdate) {
        // Vehicle onGround = false always
        // We don't do vehicle setbacks because vehicle netcode sucks.
        player.movementCheckRunner.processAndCheckMovementPacket(new PositionUpdate(vehicleUpdate.getFrom(), vehicleUpdate.getTo(), false, null, null, vehicleUpdate.isTeleport()));
    }
}

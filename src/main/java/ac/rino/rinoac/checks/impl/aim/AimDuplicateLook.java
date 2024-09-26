package ac.rino.rinoac.checks.impl.aim;

import ac.rino.rinoac.checks.Check;
import ac.rino.rinoac.checks.CheckData;
import ac.rino.rinoac.checks.type.RotationCheck;
import ac.rino.rinoac.player.RinoPlayer;
import ac.rino.rinoac.utils.anticheat.update.RotationUpdate;

@CheckData(name = "AimDuplicateLook")
public class AimDuplicateLook extends Check implements RotationCheck {
    public AimDuplicateLook(RinoPlayer playerData) {
        super(playerData);
    }

    boolean exempt = false;

    @Override
    public void process(final RotationUpdate rotationUpdate) {
        if (player.packetStateData.lastPacketWasTeleport || player.packetStateData.lastPacketWasOnePointSeventeenDuplicate || player.compensatedEntities.getSelf().getRiding() != null) {
            exempt = true;
            return;
        }

        if (exempt) { // Exempt for a tick on teleport
            exempt = false;
            return;
        }

        if (rotationUpdate.getFrom().equals(rotationUpdate.getTo())) {
            flagAndAlert();
        }
    }
}

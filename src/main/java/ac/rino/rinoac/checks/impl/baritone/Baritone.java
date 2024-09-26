package ac.rino.rinoac.checks.impl.baritone;

import ac.rino.rinoac.checks.Check;
import ac.rino.rinoac.checks.CheckData;
import ac.rino.rinoac.checks.impl.aim.processor.AimProcessor;
import ac.rino.rinoac.checks.type.RotationCheck;
import ac.rino.rinoac.player.RinoPlayer;
import ac.rino.rinoac.utils.anticheat.update.RotationUpdate;
import ac.rino.rinoac.utils.data.HeadRotation;
import ac.rino.rinoac.utils.math.RinoMath;

// This check has been patched by Baritone for a long time and it also seems to false with cinematic camera now, so it is disabled.
@CheckData(name = "Baritone")
public class Baritone extends Check implements RotationCheck {
    private int verbose;

    public Baritone(RinoPlayer playerData) {
        super(playerData);
    }

    @Override
    public void process(final RotationUpdate rotationUpdate) {
        final HeadRotation from = rotationUpdate.getFrom();
        final HeadRotation to = rotationUpdate.getTo();

        final float deltaPitch = Math.abs(to.getPitch() - from.getPitch());

        // Baritone works with small degrees, limit to 1 degrees to pick up on baritone slightly moving aim to bypass anticheats
        if (rotationUpdate.getDeltaXRot() == 0 && deltaPitch > 0 && deltaPitch < 1 && Math.abs(to.getPitch()) != 90.0f) {
            if (rotationUpdate.getProcessor().divisorY < RinoMath.MINIMUM_DIVISOR) {
                verbose++;
                if (verbose > 8) {
                    flagAndAlert("Divisor " + AimProcessor.convertToSensitivity(rotationUpdate.getProcessor().divisorX));
                }
            } else {
                verbose = 0;
            }
        }
    }
}

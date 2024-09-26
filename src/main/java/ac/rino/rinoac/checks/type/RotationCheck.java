package ac.rino.rinoac.checks.type;

import ac.grim.grimac.api.AbstractCheck;
import ac.rino.rinoac.utils.anticheat.update.RotationUpdate;

public interface RotationCheck extends AbstractCheck {

    default void process(final RotationUpdate rotationUpdate) {
    }
}

package ac.rino.rinoac.checks.type;

import ac.grim.grimac.api.AbstractCheck;
import ac.rino.rinoac.utils.anticheat.update.PositionUpdate;

public interface PositionCheck extends AbstractCheck {

    default void onPositionUpdate(final PositionUpdate positionUpdate) {
    }
}

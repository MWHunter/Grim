package ac.rino.rinoac.checks.type;

import ac.grim.grimac.api.AbstractCheck;
import ac.rino.rinoac.utils.anticheat.update.VehiclePositionUpdate;

public interface VehicleCheck extends AbstractCheck {

    void process(final VehiclePositionUpdate vehicleUpdate);
}

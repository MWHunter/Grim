package ac.rino.rinoac.checks.impl.aim;

import ac.rino.rinoac.checks.Check;
import ac.rino.rinoac.checks.CheckData;
import ac.rino.rinoac.checks.type.RotationCheck;
import ac.rino.rinoac.player.RinoPlayer;
import ac.rino.rinoac.utils.anticheat.update.RotationUpdate;

// Based on Kauri AimA,
// I also discovered this flaw before open source Kauri, but did not want to open source its detection.
// It works on clients who % 360 their rotation.
@CheckData(name = "AimModulo360", decay = 0.005)
public class AimModulo360 extends Check implements RotationCheck {
    float lastDeltaYaw;

    public AimModulo360(RinoPlayer playerData) {
        super(playerData);
    }

    @Override
    public void process(final RotationUpdate rotationUpdate) {
        lastDeltaYaw = rotationUpdate.getDeltaXRot();
        if (player.packetStateData.lastPacketWasTeleport) return;
        if (player.xRot < 360 && player.xRot > -360 && Math.abs(rotationUpdate.getDeltaXRot()) > 320 && Math.abs(lastDeltaYaw) < 30) {
            flagAndAlert();
        } else {
            reward();
        }
    }
}

package ac.grim.grimac.checks.impl.aim;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.RotationCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.anticheat.update.RotationUpdate;

//This may have some uncertainty "Don't quote me on that"
//TODO: This will need some work.
//But from my testing, it works pretty well, but I would still keep it experimental.
@CheckData(name = "AimRounded", experimental = true)
public class AimRounded extends Check implements RotationCheck {
    public AimRounded(GrimPlayer playerData) {
        super(playerData);
    }

    boolean exempt = false;

    @Override
    public void process(final RotationUpdate rotationUpdate) {
        if (player.packetStateData.lastPacketWasTeleport) {
            exempt = true;
            return;
        }

        if (exempt) { // Exempt for a tick on teleport
            exempt = false;
            return;
        }

        float pitch = rotationUpdate.getTo().getPitch();
        float pitchABS = Math.abs(pitch);

        float yaw = rotationUpdate.getTo().getYaw();
        float yawABS = Math.abs(yaw);

        if (Math.round(pitch) == pitch && pitchABS != 90.0 || Math.round(yaw) == yaw && yawABS != 0.0) {
            flagAndAlert();
        } else {
            reward();
        }
    }
}

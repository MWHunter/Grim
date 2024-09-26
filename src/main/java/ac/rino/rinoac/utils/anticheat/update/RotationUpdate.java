package ac.rino.rinoac.utils.anticheat.update;

import ac.rino.rinoac.checks.impl.aim.processor.AimProcessor;
import ac.rino.rinoac.utils.data.HeadRotation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class RotationUpdate {
    private HeadRotation from, to;
    private AimProcessor processor;
    private float deltaYRot, deltaXRot;
    private boolean isCinematic;
    private double sensitivityX, sensitivityY;

    public RotationUpdate(HeadRotation from, HeadRotation to, float deltaXRot, float deltaYRot) {
        this.from = from;
        this.to = to;
        this.deltaXRot = deltaXRot;
        this.deltaYRot = deltaYRot;
    }

    public float getDeltaXRotABS() {
        return Math.abs(deltaXRot);
    }

    public float getDeltaYRotABS() {
        return Math.abs(deltaYRot);
    }
}

package ac.grim.grimac.utils.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class CooldownData {
    @Getter
    private int ticksRemaining;
    public final int transaction;

    public void tick() {
        ticksRemaining--;
    }
}

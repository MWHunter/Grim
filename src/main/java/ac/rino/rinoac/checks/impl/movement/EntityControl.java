package ac.rino.rinoac.checks.impl.movement;

import ac.rino.rinoac.checks.Check;
import ac.rino.rinoac.checks.CheckData;
import ac.rino.rinoac.checks.type.PostPredictionCheck;
import ac.rino.rinoac.player.RinoPlayer;

@CheckData(name = "Entity control", configName = "EntityControl")
public class EntityControl extends Check implements PostPredictionCheck {
    public EntityControl(RinoPlayer player) {
        super(player);
    }

    public void rewardPlayer() {
        reward();
    }
}

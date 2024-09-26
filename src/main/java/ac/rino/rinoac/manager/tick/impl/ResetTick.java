package ac.rino.rinoac.manager.tick.impl;

import ac.rino.rinoac.RinoAPI;
import ac.rino.rinoac.manager.tick.Tickable;
import ac.rino.rinoac.player.RinoPlayer;

public class ResetTick implements Tickable {
    @Override
    public void tick() {
        for (RinoPlayer player : RinoAPI.INSTANCE.getPlayerDataManager().getEntries()) {
            player.checkManager.getEntityReplication().tickStartTick();
        }
    }
}

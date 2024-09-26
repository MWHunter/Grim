package ac.rino.rinoac.checks.impl.movement;

import ac.rino.rinoac.checks.CheckData;
import ac.rino.rinoac.checks.type.PostPredictionCheck;
import ac.rino.rinoac.player.RinoPlayer;
import ac.rino.rinoac.utils.anticheat.update.PredictionComplete;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;

@CheckData(name = "NegativeTimer", configName = "NegativeTimer", setback = 10, experimental = true)
public class NegativeTimerCheck extends TimerCheck implements PostPredictionCheck {

    public NegativeTimerCheck(RinoPlayer player) {
        super(player);
        timerBalanceRealTime = System.nanoTime() + clockDrift;
    }

    @Override
    public void onPredictionComplete(final PredictionComplete predictionComplete) {
        // We can't negative timer check a 1.9+ player who is standing still.
        if (!player.canThePlayerBeCloseToZeroMovement(2) || !predictionComplete.isChecked()) {
            timerBalanceRealTime = System.nanoTime() + clockDrift;
        }

        if (timerBalanceRealTime < lastMovementPlayerClock - clockDrift) {
            int lostMS = (int) ((System.nanoTime() - timerBalanceRealTime) / 1e6);
            flagAndAlert("-" + lostMS);
            timerBalanceRealTime += 50e6;
        }
    }

    @Override
    public void doCheck(final PacketReceiveEvent event) {
        // We don't know if the player is ticking stable, therefore we must wait until prediction
        // determines this.  Do nothing here!
    }

    @Override
    public void reload() {
        super.reload();
        clockDrift = (long) (getConfig().getDoubleElse(getConfigName() + ".drift", 1200.0) * 1e6);
    }
}

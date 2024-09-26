package ac.rino.rinoac.manager.init.start;

import ac.rino.rinoac.RinoAPI;
import ac.rino.rinoac.manager.init.Initable;
import ac.rino.rinoac.player.RinoPlayer;
import io.github.retrooper.packetevents.util.folia.FoliaScheduler;

public class PacketLimiter implements Initable {
    @Override
    public void start() {
        FoliaScheduler.getAsyncScheduler().runAtFixedRate(RinoAPI.INSTANCE.getPlugin(), (dummy) -> {
            for (RinoPlayer player : RinoAPI.INSTANCE.getPlayerDataManager().getEntries()) {
                // Avoid concurrent reading on an integer as it's results are unknown
                player.cancelledPackets.set(0);
            }
        }, 1, 20);
    }
}

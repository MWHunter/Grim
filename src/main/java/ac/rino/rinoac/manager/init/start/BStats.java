package ac.rino.rinoac.manager.init.start;

import ac.rino.rinoac.RinoAPI;
import ac.rino.rinoac.manager.init.Initable;

public class BStats implements Initable {
    @Override
    public void start() {
        int pluginId = 12820; // <-- Replace with the id of your plugin!
        try {
            new io.github.retrooper.packetevents.bstats.Metrics(RinoAPI.INSTANCE.getPlugin(), pluginId);
        } catch (Exception ignored) {
        }
    }
}

package ac.rino.rinoac.manager.init.start;

import ac.rino.rinoac.RinoAPI;
import ac.rino.rinoac.manager.init.Initable;
import ac.rino.rinoac.utils.anticheat.LogUtil;
import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import org.bukkit.Bukkit;

public class TickRunner implements Initable {
    @Override
    public void start() {
        LogUtil.info("Registering tick schedulers...");

        if (FoliaScheduler.isFolia()) {
            FoliaScheduler.getAsyncScheduler().runAtFixedRate(RinoAPI.INSTANCE.getPlugin(), (dummy) -> {
                RinoAPI.INSTANCE.getTickManager().tickSync();
                RinoAPI.INSTANCE.getTickManager().tickAsync();
            }, 1, 1);
        } else {
            Bukkit.getScheduler().runTaskTimer(RinoAPI.INSTANCE.getPlugin(), () -> RinoAPI.INSTANCE.getTickManager().tickSync(), 0, 1);
            Bukkit.getScheduler().runTaskTimerAsynchronously(RinoAPI.INSTANCE.getPlugin(), () -> RinoAPI.INSTANCE.getTickManager().tickAsync(), 0, 1);
        }
    }
}

package ac.rino.rinoac.manager.init.load;

import ac.rino.rinoac.RinoAPI;
import ac.rino.rinoac.manager.init.Initable;
import ac.rino.rinoac.utils.anticheat.LogUtil;
import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;

public class PacketEventsInit implements Initable {
    @Override
    public void start() {
        LogUtil.info("Loading PacketEvents...");
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(RinoAPI.INSTANCE.getPlugin()));
        PacketEvents.getAPI().getSettings()
                .bStats(true)
                .fullStackTrace(true)
                .kickOnPacketException(true)
                .checkForUpdates(false)
                .debug(false);
        PacketEvents.getAPI().load();
    }
}

package ac.rino.rinoac.manager.init.start;

import ac.rino.rinoac.RinoAPI;
import ac.rino.rinoac.events.bukkit.PistonEvent;
import ac.rino.rinoac.manager.init.Initable;
import ac.rino.rinoac.utils.anticheat.LogUtil;
import org.bukkit.Bukkit;

public class EventManager implements Initable {
    public void start() {
        LogUtil.info("Registering singular bukkit event... (PistonEvent)");

        Bukkit.getPluginManager().registerEvents(new PistonEvent(), RinoAPI.INSTANCE.getPlugin());
    }
}

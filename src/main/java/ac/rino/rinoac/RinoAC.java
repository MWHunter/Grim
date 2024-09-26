package ac.rino.rinoac;

import org.bukkit.plugin.java.JavaPlugin;

public final class RinoAC extends JavaPlugin {

    @Override
    public void onLoad() {
        RinoAPI.INSTANCE.load(this);
    }

    @Override
    public void onDisable() {
        RinoAPI.INSTANCE.stop(this);
    }

    @Override
    public void onEnable() {
        RinoAPI.INSTANCE.start(this);
    }
}

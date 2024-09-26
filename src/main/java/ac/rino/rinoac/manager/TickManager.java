package ac.rino.rinoac.manager;

import ac.rino.rinoac.manager.tick.Tickable;
import ac.rino.rinoac.manager.tick.impl.ClientVersionSetter;
import ac.rino.rinoac.manager.tick.impl.ResetTick;
import ac.rino.rinoac.manager.tick.impl.TickInventory;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;

public class TickManager {
    // Overflows after 4 years of uptime
    public int currentTick;
    ClassToInstanceMap<Tickable> syncTick;
    ClassToInstanceMap<Tickable> asyncTick;

    public TickManager() {
        syncTick = new ImmutableClassToInstanceMap.Builder<Tickable>()
                .put(ResetTick.class, new ResetTick())
                .build();

        asyncTick = new ImmutableClassToInstanceMap.Builder<Tickable>()
                .put(ClientVersionSetter.class, new ClientVersionSetter()) // Async because permission lookups might take a while, depending on the plugin
                .put(TickInventory.class, new TickInventory()) // Async because I've never gotten an exception from this.  It's probably safe.
                .build();
    }

    public void tickSync() {
        currentTick++;
        syncTick.values().forEach(Tickable::tick);
    }

    public void tickAsync() {
        asyncTick.values().forEach(Tickable::tick);
    }
}

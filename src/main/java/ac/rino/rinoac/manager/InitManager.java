package ac.rino.rinoac.manager;

import ac.rino.rinoac.RinoAPI;
import ac.rino.rinoac.RinoExternalAPI;
import ac.rino.rinoac.manager.init.Initable;
import ac.rino.rinoac.manager.init.load.PacketEventsInit;
import ac.rino.rinoac.manager.init.start.*;
import ac.rino.rinoac.manager.init.stop.TerminatePacketEvents;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;

public class InitManager {
    ClassToInstanceMap<Initable> initializersOnLoad;
    ClassToInstanceMap<Initable> initializersOnStart;
    ClassToInstanceMap<Initable> initializersOnStop;

    public InitManager() {
        initializersOnLoad = new ImmutableClassToInstanceMap.Builder<Initable>()
                .put(PacketEventsInit.class, new PacketEventsInit())
                .build();

        initializersOnStart = new ImmutableClassToInstanceMap.Builder<Initable>()
                .put(ExemptOnlinePlayers.class, new ExemptOnlinePlayers())
                .put(EventManager.class, new EventManager())
                .put(PacketManager.class, new PacketManager())
                .put(ViaBackwardsManager.class, new ViaBackwardsManager())
                .put(TickRunner.class, new TickRunner())
                .put(TickEndEvent.class, new TickEndEvent())
                .put(CommandRegister.class, new CommandRegister())
                .put(BStats.class, new BStats())
                .put(PacketLimiter.class, new PacketLimiter())
                .put(DiscordManager.class, RinoAPI.INSTANCE.getDiscordManager())
                .put(SpectateManager.class, RinoAPI.INSTANCE.getSpectateManager())
                .put(RinoExternalAPI.class, RinoAPI.INSTANCE.getExternalAPI())
                .put(JavaVersion.class, new JavaVersion())
                .build();

        initializersOnStop = new ImmutableClassToInstanceMap.Builder<Initable>()
                .put(TerminatePacketEvents.class, new TerminatePacketEvents())
                .build();
    }

    public void load() {
        for (Initable initable : initializersOnLoad.values()) {
            initable.start();
        }
    }

    public void start() {
        for (Initable initable : initializersOnStart.values()) {
            initable.start();
        }
    }

    public void stop() {
        for (Initable initable : initializersOnStop.values()) {
            initable.start();
        }
    }
}
package ac.grim.grimac.manager.init.stop;

import ac.grim.grimac.manager.init.Initable;
import ac.grim.grimac.shaded.com.packetevents.PacketEvents;
import ac.grim.grimac.utils.anticheat.LogUtil;

public class TerminatePacketEvents implements Initable {
    @Override
    public void start() {
        LogUtil.info("Terminating PacketEvents...");
        PacketEvents.getAPI().terminate();
    }
}

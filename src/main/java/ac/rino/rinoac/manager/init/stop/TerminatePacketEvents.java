package ac.rino.rinoac.manager.init.stop;

import ac.rino.rinoac.manager.init.Initable;
import ac.rino.rinoac.utils.anticheat.LogUtil;
import com.github.retrooper.packetevents.PacketEvents;

public class TerminatePacketEvents implements Initable {
    @Override
    public void start() {
        LogUtil.info("Terminating PacketEvents...");
        PacketEvents.getAPI().terminate();
    }
}

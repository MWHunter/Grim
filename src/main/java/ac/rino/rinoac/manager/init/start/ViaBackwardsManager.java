package ac.rino.rinoac.manager.init.start;

import ac.rino.rinoac.manager.init.Initable;

public class ViaBackwardsManager implements Initable {
    @Override
    public void start() {
        System.setProperty("com.viaversion.handlePingsAsInvAcknowledgements", "true");
    }
}

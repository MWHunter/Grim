package ac.rino.rinoac.manager.init.start;

import ac.rino.rinoac.RinoAPI;
import ac.rino.rinoac.commands.*;
import ac.rino.rinoac.manager.init.Initable;
import co.aikar.commands.PaperCommandManager;

public class CommandRegister implements Initable {
    @Override
    public void start() {
        // This does not make Grim require paper
        // It only enables new features such as asynchronous tab completion on paper
        PaperCommandManager commandManager = new PaperCommandManager(RinoAPI.INSTANCE.getPlugin());

        commandManager.registerCommand(new RinoPerf());
        commandManager.registerCommand(new RinoDebug());
        commandManager.registerCommand(new RinoAlerts());
        commandManager.registerCommand(new RinoProfile());
        commandManager.registerCommand(new RinoSendAlert());
        commandManager.registerCommand(new RinoHelp());
        commandManager.registerCommand(new RinoReload());
        commandManager.registerCommand(new RinoSpectate());
        commandManager.registerCommand(new RinoStopSpectating());
        commandManager.registerCommand(new RinoLog());
        commandManager.registerCommand(new RinoVerbose());
    }
}

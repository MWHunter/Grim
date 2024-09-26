package ac.rino.rinoac.commands;

import ac.rino.rinoac.RinoAPI;
import ac.rino.rinoac.utils.anticheat.LogUtil;
import ac.rino.rinoac.utils.anticheat.MessageUtil;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.entity.Player;

@CommandAlias("grim|grimac")
public class RinoSendAlert extends BaseCommand {
    @Subcommand("sendalert")
    @CommandPermission("grim.sendalert")
    public void sendAlert(String string) {
        string = MessageUtil.format(string);

        for (Player bukkitPlayer : RinoAPI.INSTANCE.getAlertManager().getEnabledAlerts()) {
            bukkitPlayer.sendMessage(string);
        }

        if (RinoAPI.INSTANCE.getConfigManager().getConfig().getBooleanElse("alerts.print-to-console", true)) {
            LogUtil.console(string); // Print alert to console
        }
    }
}

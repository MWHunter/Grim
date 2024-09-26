package ac.rino.rinoac.commands;

import ac.rino.rinoac.RinoAPI;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.entity.Player;

@CommandAlias("grim|grimac")
public class RinoAlerts extends BaseCommand {
    @Subcommand("alerts")
    @CommandPermission("grim.alerts")
    public void onAlerts(Player player) {
        RinoAPI.INSTANCE.getAlertManager().toggleAlerts(player);
    }
}

package ac.rino.rinoac.commands;

import ac.rino.rinoac.RinoAPI;
import ac.rino.rinoac.utils.anticheat.MessageUtil;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("grim|grimac")
public class RinoStopSpectating extends BaseCommand {
    @Subcommand("stopspectating")
    @CommandPermission("grim.spectate")
    @CommandCompletion("here")
    public void onStopSpectate(CommandSender sender, String[] args) {
        String string = args.length > 0 ? args[0] : null;
        if (!(sender instanceof Player player)) return;
        if (RinoAPI.INSTANCE.getSpectateManager().isSpectating(player.getUniqueId())) {
            boolean teleportBack = string == null || !string.equalsIgnoreCase("here");
            RinoAPI.INSTANCE.getSpectateManager().disable(player, teleportBack);
        } else {
            String message = RinoAPI.INSTANCE.getConfigManager().getConfig().getStringElse("cannot-spectate-return", "%prefix% &cYou can only do this after spectating a player.");
            sender.sendMessage(MessageUtil.format(message));
        }
    }
}


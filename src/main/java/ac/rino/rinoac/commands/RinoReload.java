package ac.rino.rinoac.commands;

import ac.rino.rinoac.RinoAPI;
import ac.rino.rinoac.utils.anticheat.MessageUtil;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandAlias("grim|grimac")
public class RinoReload extends BaseCommand {
    @Subcommand("reload")
    @CommandPermission("grim.reload")
    public void onReload(CommandSender sender) {
        //reload config
        try {
            RinoAPI.INSTANCE.getExternalAPI().reload();
        } catch (RuntimeException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            return;
        }

        sender.sendMessage(MessageUtil.format("%prefix% &fConfig has been reloaded."));
    }

}

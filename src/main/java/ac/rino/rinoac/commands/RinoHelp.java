package ac.rino.rinoac.commands;

import ac.rino.rinoac.RinoAPI;
import ac.rino.rinoac.utils.anticheat.MessageUtil;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.command.CommandSender;

@CommandAlias("grim|grimac")
public class RinoHelp extends BaseCommand {
    @Default
    @Subcommand("help")
    @CommandPermission("grim.help")
    public void onHelp(CommandSender sender) {
        for (String string : RinoAPI.INSTANCE.getConfigManager().getConfig().getStringList("help")) {
            string = MessageUtil.format(string);
            sender.sendMessage(string);
        }
    }
}

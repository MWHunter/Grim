package ac.rino.rinoac.commands;

import ac.rino.rinoac.RinoAPI;
import ac.rino.rinoac.player.RinoPlayer;
import ac.rino.rinoac.utils.anticheat.MessageUtil;
import ac.rino.rinoac.utils.anticheat.MultiLibUtil;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("grim|grimac")
public class RinoProfile extends BaseCommand {
    @Subcommand("profile")
    @CommandPermission("grim.profile")
    @CommandCompletion("@players")
    public void onConsoleDebug(CommandSender sender, OnlinePlayer target) {
        Player player = null;
        if (sender instanceof Player) player = (Player) sender;

        // Short circuit due to minimum java requirements for MultiLib
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_18) && MultiLibUtil.isExternalPlayer(target.getPlayer())) {
            String alertString = RinoAPI.INSTANCE.getConfigManager().getConfig().getStringElse("player-not-this-server", "%prefix% &cThis player isn't on this server!");
            sender.sendMessage(MessageUtil.format(alertString));
            return;
        }

        RinoPlayer rinoPlayer = RinoAPI.INSTANCE.getPlayerDataManager().getPlayer(target.getPlayer());
        if (rinoPlayer == null) {
            String message = RinoAPI.INSTANCE.getConfigManager().getConfig().getStringElse("player-not-found", "%prefix% &cPlayer is exempt or offline!");
            sender.sendMessage(MessageUtil.format(message));
            return;
        }

        for (String message : RinoAPI.INSTANCE.getConfigManager().getConfig().getStringList("profile")) {
            message = RinoAPI.INSTANCE.getExternalAPI().replaceVariables(rinoPlayer, message, true);
            sender.sendMessage(message);
        }
    }
}

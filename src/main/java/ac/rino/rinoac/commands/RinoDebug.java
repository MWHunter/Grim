package ac.rino.rinoac.commands;

import ac.rino.rinoac.RinoAPI;
import ac.rino.rinoac.player.RinoPlayer;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@CommandAlias("grim|grimac")
public class RinoDebug extends BaseCommand {
    @Subcommand("debug")
    @CommandPermission("grim.debug")
    @CommandCompletion("@players")
    public void onDebug(CommandSender sender, @Optional OnlinePlayer target) {
        Player player = null;
        if (sender instanceof Player) player = (Player) sender;

        RinoPlayer rinoPlayer = parseTarget(sender, player, target);
        if (rinoPlayer == null) return;

        if (sender instanceof ConsoleCommandSender) { // Just debug to console to reduce complexity...
            rinoPlayer.checkManager.getDebugHandler().toggleConsoleOutput();
        } else { // This sender is a player
            rinoPlayer.checkManager.getDebugHandler().toggleListener(player);
        }
    }

    private RinoPlayer parseTarget(CommandSender sender, Player player, OnlinePlayer target) {
        Player targetPlayer = target == null ? player : target.getPlayer();
        if (player == null && target == null) {
            sender.sendMessage(ChatColor.RED + "You must specify a target as the console!");
            return null;
        }

        RinoPlayer rinoPlayer = RinoAPI.INSTANCE.getPlayerDataManager().getPlayer(targetPlayer);
        if (rinoPlayer == null) {
            User user = PacketEvents.getAPI().getPlayerManager().getUser(targetPlayer);
            sender.sendMessage(ChatColor.RED + "This player is exempt from all checks!");

            if (user == null) {
                sender.sendMessage(ChatColor.RED + "Unknown PacketEvents user");
            } else {
                boolean isExempt = RinoAPI.INSTANCE.getPlayerDataManager().shouldCheck(user);
                if (!isExempt) {
                    sender.sendMessage(ChatColor.RED + "User connection state: " + user.getConnectionState());
                }
            }
        }

        return rinoPlayer;
    }

    @Subcommand("consoledebug")
    @CommandPermission("grim.consoledebug")
    @CommandCompletion("@players")
    public void onConsoleDebug(CommandSender sender, @Optional OnlinePlayer target) {
        Player player = null;
        if (sender instanceof Player) player = (Player) sender;

        RinoPlayer rinoPlayer = parseTarget(sender, player, target);
        if (rinoPlayer == null) return;

        boolean isOutput = rinoPlayer.checkManager.getDebugHandler().toggleConsoleOutput();

        sender.sendMessage("Console output for " + (rinoPlayer.bukkitPlayer == null ? rinoPlayer.user.getProfile().getName() : rinoPlayer.bukkitPlayer.getName()) + " is now " + isOutput);
    }
}
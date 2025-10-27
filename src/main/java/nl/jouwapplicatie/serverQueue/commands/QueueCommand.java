package nl.jouwapplicatie.serverQueue.commands;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import nl.jouwapplicatie.serverQueue.ServerQueue;
import nl.jouwapplicatie.serverQueue.System.Bungeecord;
import nl.jouwapplicatie.serverQueue.checkers.QueueChecker;
import nl.jouwapplicatie.serverQueue.data.Queue;
import nl.jouwapplicatie.serverQueue.data.User;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;

import java.awt.*;

public class QueueCommand implements CommandExecutor {

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player)sender;

            if (!ServerQueue.instance.getConfigC().system) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerQueue.instance.getConfigC().Prefix + ServerQueue.instance.getConfigC().Queue_Paused));
                return true;
            }
            if (args.length > 1) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', ServerQueue.instance.getConfigC().Prefix + ServerQueue.instance.getConfigC().Error_FillServer));
                return true;
            }
            if (args.length > 0) {
                final User user = ServerQueue.instance.getUserManager().getUser(player);
                final String argument1 = args[0].toLowerCase();
                final Queue queue = ServerQueue.instance.getQueueManager().getQueue(argument1);

                if (queue == null) {
                    player.sendMessage(ServerQueue.instance.getConfigC().Prefix +
                            ChatColor.translateAlternateColorCodes('&', "This server is not in a queue."));
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                            ServerQueue.instance.getConfigC().Prefix + "§cThis server is not in a queue."));
                    return true;
                }

                if (user.getPosition() != 0) {
                    player.sendMessage(ServerQueue.instance.getConfigC().Prefix + ChatColor.translateAlternateColorCodes(
                            '&', "You are already in a queue."));
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                            ServerQueue.instance.getConfigC().Prefix + "§cYou are already in a queue."));
                } else {
                    if (ServerQueue.instance.getQueueManager().getQueue(argument1) != null) {
                        if (!player.hasPermission(ServerQueue.instance.getConfigC().Bypass_Perm)) {
                            final Queue queues = ServerQueue.instance.getQueueManager().getQueue(argument1);
                            queues.AddQueue(player);
                            user.setQueue(argument1);
                            final int queuesize = queues.getTotalQueue().size();
                            user.setPosition(queuesize);
                            player.sendMessage(ServerQueue.instance.getConfigC().Prefix + ServerQueue.instance.getConfigC().Queue_Added.replace("%position%", String.valueOf(queuesize)).replace("%maxpos%", String.valueOf(queuesize)));
                        } else {
                            String playerVersion = nl.jouwapplicatie.serverQueue.events.ConnectListener.getPlayerVersion(player);

                            if (playerVersion.equals("UNSUPPORTED") ||
                                    !nl.jouwapplicatie.serverQueue.checkers.VersionUtil.isAllowed(playerVersion, queue.getVersionMode())) {

                                // Action bar
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ServerQueue.instance.getConfigC().Prefix + ServerQueue.instance.getConfigC().Error_NotCompatibleVersionActionBar));

                                // Title (main title + subtitle)
                                String Error_VersionSubtitle = ServerQueue.instance.getConfigC().Error_NotCompatibleVersionSubtitle
                                        .replace("%version%", playerVersion);
                                player.sendTitle(
                                        ServerQueue.instance.getConfigC().Error_NotCompatibleVersionTitle,
                                        Error_VersionSubtitle, 10, 70, 20);

                                // Chat message
                                player.sendMessage(ServerQueue.instance.getConfigC().Prefix
                                        + "§cYour version (" + playerVersion + ") is not compatible with server §e"
                                        + queue.getQueueServer() + " §7[" + queue.getVersionMode() + "]");

                                String fallbackCommand = ServerQueue.instance.getConfigC().Incompatible_Command;

                                if (fallbackCommand != null && !fallbackCommand.trim().isEmpty()) {
                                    ServerQueue.instance.getLogger().info(ServerQueue.instance.getConfigC().Prefix
                                            + player.getName() + " is using an incompatible version. Executing fallback command: /" + fallbackCommand);

                                    Bukkit.getScheduler().runTaskLater(ServerQueue.instance, () -> {
                                        player.performCommand(fallbackCommand);
                                    }, 5L);
                                } else {
                                    ServerQueue.instance.getLogger().info(ServerQueue.instance.getConfigC().Prefix
                                            + "No fallback command set for incompatible version for player " + player.getName());
                                }

                                return true;
                            }
                        }
                        player.sendMessage(ServerQueue.instance.getConfigC().Prefix + ServerQueue.instance.getConfigC().Queue_Done);
                        // Title (main title + subtitle)
                        player.sendTitle(
                                ServerQueue.instance.getConfigC().Prefix + ServerQueue.instance.getConfigC().Queue_Done,
                                "", 10, 70, 20);


                        Bungeecord.sendPlayerToServer(player, argument1);
                    }
                }
            } else {
                player.sendMessage(ServerQueue.instance.getConfigC().Prefix +
                        ServerQueue.instance.getConfigC().Error_NoServerSpecified);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ServerQueue.instance.getConfigC().Prefix +
                        ServerQueue.instance.getConfigC().Error_NoServerSpecified ));
            }

        } else {
            sender.sendMessage(ServerQueue.instance.getConfigC().Prefix + "§cYou have to be a player to do this.");
        }
        return false;
    }
}
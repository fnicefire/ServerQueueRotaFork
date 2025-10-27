package nl.jouwapplicatie.serverQueue.checkers;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import nl.jouwapplicatie.serverQueue.ServerQueue;
import nl.jouwapplicatie.serverQueue.System.Bungeecord;
import nl.jouwapplicatie.serverQueue.data.Queue;
import nl.jouwapplicatie.serverQueue.data.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class QueueChecker {

    public static void QueueLoop(final List<String> servers) {
        new BukkitRunnable() {
            public void run() {
                if (ServerQueue.instance.getConfigC().system) {
                    for (final String s : servers) {
                        final Queue queue = ServerQueue.instance.getQueueManager().getQueue(s.toLowerCase());

                        // If the queue is empty, skip processing and wait for the next cycle
                        if (queue.getTotalQueue().isEmpty()) {
                            continue; // move to the next server in the list
                        }

                        // First, check if there are priority players in the queue
                        Player priorityPlayer = null;
                        for (Player p : queue.getTotalQueue()) {
                            if (p.hasPermission(ServerQueue.instance.getConfigC().PriorityPermission)) {
                                priorityPlayer = p;
                                break; // take the first priority player
                            }
                        }

                        // If there is a priority player, send them through
                        if (priorityPlayer != null) {
                           final User user = ServerQueue.instance.getUserManager().getUser(priorityPlayer);
                            user.setQueue("");
                            user.setPosition(0);
                            priorityPlayer.sendMessage(ServerQueue.instance.getConfigC().Prefix + ServerQueue.instance.getConfigC().Queue_Done);
                            Bungeecord.sendPlayerToServer(priorityPlayer, queue.getQueueServer());
                            queue.RemoveQueue(priorityPlayer);

                            // Update positions for the rest
                            for (final Player player2 : queue.getTotalQueue()) {
                                final User user2 = ServerQueue.instance.getUserManager().getUser(player2);
                                int newPos = user2.getPosition() - 1;
                                user2.setPosition(newPos);
                                player2.sendMessage(ServerQueue.instance.getConfigC().Prefix + ServerQueue.instance.getConfigC().Queue_Move
                                        .replace("%position%", String.valueOf(newPos))
                                        .replace("%maxpos%", String.valueOf(queue.getTotalQueue().size())));
                                String actionBarMessage = ServerQueue.instance.getConfigC().Queue_ActionBar
                                        .replace("%position%", String.valueOf(newPos))
                                        .replace("%maxpos%", String.valueOf(queue.getTotalQueue().size()));
                                player2.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                        new TextComponent(ChatColor.translateAlternateColorCodes('&', actionBarMessage)));
                            }
                            return; // skip the normal queue check for this loop
                        }

                        // No priority player → do the normal queue (like you had)
                        for (final Player player : Bukkit.getOnlinePlayers()) {
                            if (queue.getTotalQueue().contains(player)) {
                                final User user = ServerQueue.instance.getUserManager().getUser(player);
                                if (user.getPosition() != 1) {
                                    continue;
                                }

                                user.setQueue("");
                                user.setPosition(0);
                                player.sendMessage(ServerQueue.instance.getConfigC().Prefix + ServerQueue.instance.getConfigC().Queue_Done);
                                Bungeecord.sendPlayerToServer(player, queue.getQueueServer());
                                queue.RemoveQueue(player);

                                for (final Player player2 : Bukkit.getOnlinePlayers()) {
                                    final User user2 = ServerQueue.instance.getUserManager().getUser(player2);
                                    if (user2.getQueue().equalsIgnoreCase(queue.getQueueServer())) {
                                        final int Position = user2.getPosition() - 1;
                                        user2.setPosition(Position);
                                        player2.sendMessage(ServerQueue.instance.getConfigC().Prefix + ServerQueue.instance.getConfigC().Queue_Move
                                                .replace("%position%", String.valueOf(Position))
                                                .replace("%maxpos%", String.valueOf(queue.getTotalQueue().size())));
                                        String actionBarMessage = ServerQueue.instance.getConfigC().Queue_ActionBar
                                                .replace("%position%", String.valueOf(Position))
                                                .replace("%maxpos%", String.valueOf(queue.getTotalQueue().size()));

                                        player2.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                                new TextComponent(ChatColor.translateAlternateColorCodes('&', actionBarMessage)));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(ServerQueue.instance, 0L, 120L);
    }
}

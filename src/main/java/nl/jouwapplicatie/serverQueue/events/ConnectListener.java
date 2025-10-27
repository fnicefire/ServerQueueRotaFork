package nl.jouwapplicatie.serverQueue.events;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import nl.jouwapplicatie.serverQueue.ServerQueue;
import nl.jouwapplicatie.serverQueue.data.Queue;
import nl.jouwapplicatie.serverQueue.data.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import nl.jouwapplicatie.serverQueue.checkers.VersionUtil;

import static nl.jouwapplicatie.serverQueue.maps.ProtocolsMap.mapProtocolToVersion;

public class ConnectListener implements Listener {


    // get player version
    public static String getPlayerVersion(Player player) {
        int protocol = Via.getAPI().getPlayerVersion(player.getUniqueId());
        if (protocol == -1) return "UNSUPPORTED";
        return mapProtocolToVersion(protocol);
    }


    @EventHandler
    private void onPlayerJoin(final PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        ServerQueue.instance.getUserManager().addUser(player);
        final User user = ServerQueue.instance.getUserManager().getUser(player);


        if (user.getQueue() == null || user.getQueue().isEmpty()) {
            return;
        }

        final Queue queue = ServerQueue.instance.getQueueManager().getQueue(user.getQueue());
        if (queue == null) {
            return;
        }

        String playerVersion = getPlayerVersion(player);

        if (playerVersion.equals("UNSUPPORTED") ||
                !VersionUtil.isAllowed(playerVersion, queue.getVersionMode())) {

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    new TextComponent(ServerQueue.instance.getConfigC().Prefix + "§cIncompatible version!"));

            player.sendTitle("§cVersion not allowed",
                    "§7Your version: " + playerVersion, 10, 70, 20);

            player.sendMessage(ServerQueue.instance.getConfigC().Prefix
                    + "§cYour version (" + playerVersion + ") is not compatible with server §e"
                    + queue.getQueueServer() + " §7[" + queue.getVersionMode() + "]");

            // Fallback command (customizable in config)
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

            return;
        }


        queue.AddQueue(player);
        int queueSize = queue.getTotalQueue().size();
        user.setPosition(queueSize);

        player.sendMessage(ServerQueue.instance.getConfigC().Prefix
                + ServerQueue.instance.getConfigC().Queue_Added
                .replace("%position%", String.valueOf(queueSize))
                .replace("%maxpos%", String.valueOf(queueSize)));
    }

    @EventHandler
    private void onPlayerQuit(final PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        final User user = ServerQueue.instance.getUserManager().getUser(player);
        if (user == null) return;

        if (user.getPosition() != 0) {
            final int position = user.getPosition();
            final Queue queue = ServerQueue.instance.getQueueManager().getQueue(user.getQueue());
            if (queue != null) {
                queue.RemoveQueue(player);
                for (final Player p2 : Bukkit.getOnlinePlayers()) {
                    final User u2 = ServerQueue.instance.getUserManager().getUser(p2);
                    if (u2 != null
                            && u2.getPosition() != 1
                            && u2.getPosition() >= position
                            && u2.getQueue().equalsIgnoreCase(queue.getQueueServer())) {
                        final int newPos = u2.getPosition() - 1;
                        u2.setPosition(newPos);
                        p2.sendMessage(ServerQueue.instance.getConfigC().Prefix
                                + ServerQueue.instance.getConfigC().Queue_Move
                                .replace("%position%", String.valueOf(newPos))
                                .replace("%maxpos%", String.valueOf(queue.getTotalQueue().size())));
                    }
                }
            }
        }
        ServerQueue.instance.getUserManager().removeUser(player);
    }
}

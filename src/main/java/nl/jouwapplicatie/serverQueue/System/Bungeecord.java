package nl.jouwapplicatie.serverQueue.System;

import net.md_5.bungee.api.ChatColor;
import nl.jouwapplicatie.serverQueue.ServerQueue;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;


public class Bungeecord {

    public static void sendPlayerToServer(final Player player, final String server) {
        String prefix = ServerQueue.instance.getConfigC().Prefix;

        ServerQueue.instance.getLogger().info(prefix + player.getName() + " has queued to join server '" + server + "' (connecting in 3 seconds).");

        Bukkit.getScheduler().runTaskLater(ServerQueue.instance, () -> {
            try {
                final ByteArrayOutputStream b = new ByteArrayOutputStream();
                final DataOutputStream out = new DataOutputStream(b);
                out.writeUTF("Connect");
                out.writeUTF(server);
                player.sendPluginMessage(ServerQueue.instance, "BungeeCord", b.toByteArray());
                b.close();
                out.close();

                ServerQueue.instance.getLogger().info(prefix + player.getName() + " has been sent to server '" + server + "'.");
            } catch (Exception e) {
                player.sendMessage(ChatColor.RED + "An error occurred while trying to connect to " + server + ".");
                ServerQueue.instance.getLogger().warning(prefix + " Failed to send " + player.getName() + " to server '" + server + "': " + e.getMessage());
            }
        }, 60L); // Delay: 60 ticks = 3 seconds
    }
}

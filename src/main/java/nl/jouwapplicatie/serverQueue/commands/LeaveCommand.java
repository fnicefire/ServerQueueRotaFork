package nl.jouwapplicatie.serverQueue.commands;

import nl.jouwapplicatie.serverQueue.ServerQueue;
import nl.jouwapplicatie.serverQueue.data.Queue;
import nl.jouwapplicatie.serverQueue.data.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        User user = ServerQueue.instance.getUserManager().getUser(player);
        String queueServer = user.getQueue();

        if (queueServer == null || queueServer.isEmpty()) {
            player.sendMessage(ServerQueue.instance.getConfigC().Prefix + "You are not in any queue.");
            return true;
        }

        Queue queue = ServerQueue.instance.getQueueManager().getQueue(queueServer.toLowerCase());
        if (queue != null) {
            queue.RemoveQueue(player); // Remove the player from the queue
        }

        user.setQueue("");
        user.setPosition(0);
        player.sendMessage(ServerQueue.instance.getConfigC().Prefix + "You have left the queue for: " + queueServer);

        // Update positions of other players in the queue
        if (queue != null) {
            for (Player p : queue.getTotalQueue()) {
                User u = ServerQueue.instance.getUserManager().getUser(p);
                int newPos = u.getPosition() - 1;
                u.setPosition(newPos);
                p.sendMessage(ServerQueue.instance.getConfigC().Prefix + ServerQueue.instance.getConfigC().Queue_Move
                        .replace("%position%", String.valueOf(newPos))
                        .replace("%maxpos%", String.valueOf(queue.getTotalQueue().size())));
            }
        }

        return true;
    }
}

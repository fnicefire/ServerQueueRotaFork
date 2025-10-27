package nl.jouwapplicatie.serverQueue.data;

import org.bukkit.entity.Player;
import java.util.ArrayList;

public class Queue {

    private final String queueServer;
    private final String versionMode;
    private final ArrayList<Player> totalQueue;

    public Queue(final String queue) {
        this(queue, "all-mode");
    }

    public Queue(final String queue, final String versionMode) {
        this.queueServer = queue;
        this.versionMode = versionMode;
        this.totalQueue = new ArrayList<>();
    }

    public String getQueueServer() {
        return this.queueServer;
    }

    public String getVersionMode() {
        return this.versionMode;
    }

    public ArrayList<Player> getTotalQueue() {
        return this.totalQueue;
    }

    public void RemoveQueue(final Player player) {
        this.totalQueue.remove(player);
    }

    public void AddQueue(final Player player) {
        this.totalQueue.add(player);
    }
}

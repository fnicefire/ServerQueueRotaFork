package nl.jouwapplicatie.serverQueue.System;

import net.md_5.bungee.api.ChatColor;
import nl.jouwapplicatie.serverQueue.ServerQueue;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.List;

public class Config {

    public String Prefix;
    public String Queue_Added;
    public String Queue_Move;
    public String Queue_Done;
    public String Queue_Paused;
    public String Queue_Perms;
    public String Queue_ActionBar;
    public String QueueSystemOnline;
    public String QueueSystemOffline;
    public String Error_NoServerSpecified;
    public String Error_FillServer;
    public String Error_NotCompatibleVersionActionBar;
    public String Error_NotCompatibleVersionTitle;
    public String Error_NotCompatibleVersionSubtitle;
    public String Bypass_Perm;
    public String Incompatible_Command;
    public String PriorityPermission = "queue.priority";
    public String pluginversion;
    public List<String> servers;
    public boolean system;

    public void ReloadConfig() {
        this.ReloadConfigData();
        ServerQueue.instance.reloadConfig();
        ServerQueue.instance.getConfig().options().copyDefaults();
        ServerQueue.instance.saveDefaultConfig();
    }

    private void ReloadConfigData() {
        this.Prefix = ChatColor.translateAlternateColorCodes('&', ServerQueue.instance.c.getString("Prefix"));
        this.Queue_Added = ChatColor.translateAlternateColorCodes('&', ServerQueue.instance.c.getString("Queue_Added"));
        this.Queue_Move = ChatColor.translateAlternateColorCodes('&', ServerQueue.instance.c.getString("Queue_Move"));
        this.Queue_Done = ChatColor.translateAlternateColorCodes('&', ServerQueue.instance.c.getString("Queue_Done"));
        this.Queue_Paused = ChatColor.translateAlternateColorCodes('&', ServerQueue.instance.c.getString("Queue_Paused"));
        this.Queue_Perms = ChatColor.translateAlternateColorCodes('&', ServerQueue.instance.c.getString("Queue_Perms"));
        this.Queue_ActionBar = ChatColor.translateAlternateColorCodes('&', ServerQueue.instance.c.getString("Queue_ActionBar"));
        this.QueueSystemOnline = ChatColor.translateAlternateColorCodes('&', ServerQueue.instance.c.getString("QueueSystemOnline"));
        this.QueueSystemOffline = ChatColor.translateAlternateColorCodes('&', ServerQueue.instance.c.getString("QueueSystemOffline"));
        this.Error_NoServerSpecified = ChatColor.translateAlternateColorCodes('&', ServerQueue.instance.c.getString("Error_NoServerSpecified"));
        this.Error_FillServer = ChatColor.translateAlternateColorCodes('&', ServerQueue.instance.c.getString("Error_FillServer"));
        this.Error_NotCompatibleVersionActionBar = ChatColor.translateAlternateColorCodes('&', ServerQueue.instance.c.getString("Error_NotCompatibleVersionActionBar"));
        this.Error_NotCompatibleVersionTitle = ChatColor.translateAlternateColorCodes('&', ServerQueue.instance.c.getString("Error_NotCompatibleVersionTitle"));
        this.Error_NotCompatibleVersionSubtitle = ChatColor.translateAlternateColorCodes('&', ServerQueue.instance.c.getString("Error_NotCompatibleVersionSubtitle"));
        this.PriorityPermission = ServerQueue.instance.c.getString("Priority_Permission");
        this.Bypass_Perm = ServerQueue.instance.c.getString("Bypass_Perm");
        this.Incompatible_Command = ServerQueue.instance.c.getString("Incompatible_Command", "");
        final PluginDescriptionFile pdf = ServerQueue.instance.getDescription();
        this.pluginversion = pdf.getVersion();
        this.servers = (List<String>)ServerQueue.instance.c.getStringList("Servers");
        if (ServerQueue.instance.c.getConfigurationSection("Servers") != null) {
            for (String serverName : ServerQueue.instance.c.getConfigurationSection("Servers").getKeys(false)) {
                String versionMode = ServerQueue.instance.c.getString("Servers." + serverName + ".versions", "all-mode");
                ServerQueue.instance.getQueueManager().addQueue(serverName.toLowerCase(), versionMode);
                System.out.println("[ServerQueue] Loaded server: " + serverName.toLowerCase() + " with version mode: " + versionMode);
            }
        }
        Registerer.RegisterEvents();
        this.system = true;
    }

}

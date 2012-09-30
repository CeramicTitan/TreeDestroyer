package me.ceramictitan.TreeDestroyer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MyPlayerListener implements Listener{
	
private TreeDestroyer plugin;
	
	public MyPlayerListener(TreeDestroyer td) { 
		plugin = td; 
	}	
		@EventHandler
		public void onPlayerJoin(PlayerJoinEvent event)
		{
			Player player = event.getPlayer();
		  if(plugin.getConfig().getBoolean("defaults.motd")){
			  String prefix = ChatColor.GOLD + "[TD] ";
			  player.sendMessage(prefix + ChatColor.YELLOW + "This server is Running version " + ChatColor.DARK_AQUA + plugin.getDescription().getVersion() + ChatColor.YELLOW + " of " + plugin.getDescription().getName());
		  }
		  if(player.hasPermission("treedestroyer.update") && TreeDestroyer.update && plugin.getConfig().getBoolean("defaults.auto-update"))
		  {
		    player.sendMessage(ChatColor.GREEN + "An update is available: " + TreeDestroyer.name + "(" + TreeDestroyer.size + " bytes" + ")");
		    // Will look like - An update is available: AntiCheat v1.3.6 (93738 bytes)
		    player.sendMessage(ChatColor.GREEN + "Type /update if you would like to update.");
		  }
		}
}

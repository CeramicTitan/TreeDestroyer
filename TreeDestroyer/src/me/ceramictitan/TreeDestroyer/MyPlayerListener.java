package me.ceramictitan.TreeDestroyer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MyPlayerListener implements Listener {

	private TreeDestroyer plugin;

	public MyPlayerListener(TreeDestroyer td) {
		plugin = td;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (plugin.getConfig().getBoolean("defaults.motd")) {
			String prefix = ChatColor.GOLD + "[TD] ";
			player.sendMessage(prefix + ChatColor.YELLOW
					+ "This server is Running version " + ChatColor.DARK_AQUA
					+ plugin.getDescription().getVersion() + ChatColor.YELLOW
					+ " of " + plugin.getDescription().getName());
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (TreeDestroyer.TreeDestroyerUsers.contains(event.getPlayer()
				.getName())) {
			TreeDestroyer.TreeDestroyerUsers
					.remove(event.getPlayer().getName());
		}
	}
}

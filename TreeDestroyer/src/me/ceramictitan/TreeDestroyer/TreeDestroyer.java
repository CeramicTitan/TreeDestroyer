package me.ceramictitan.TreeDestroyer;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TreeDestroyer extends JavaPlugin {

	public final MyBlockListener blockListener = new MyBlockListener(this);
	public final MyPlayerListener playerListener = new MyPlayerListener(this);
	public static Set<String> TreeDestroyerUsers = new HashSet<String>();

	protected Updater updateChecker;
	private String prefix = "[Tree Destroyer]";

	public List<Integer> tool_list = Arrays.asList(new Integer[] { 286, 258,
			271, 275 });

	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		getLogger().info(pdfFile.getName() + " is now disabled.");
	}

	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		getLogger().info(
				pdfFile.getName() + " Version " + pdfFile.getVersion()
						+ " is now enabled.");
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this.blockListener, this);
		pm.registerEvents(this.playerListener, this);
		this.updateChecker = new Updater(this,
				"http://dev.bukkit.org/server-mods/tree-destroyer/files.rss");
		if (this.updateChecker.updateNeeded()
				&& getConfig().getBoolean("Settings.updateCheck") == true) {
			Bukkit.getConsoleSender().sendMessage(
					ChatColor.WHITE + prefix + ChatColor.RED
							+ " A new Version of Tree Destroyer "
							+ ChatColor.AQUA + "v"
							+ this.updateChecker.getVersion() + ChatColor.RED
							+ " is now available!");
			Bukkit.getConsoleSender().sendMessage(
					ChatColor.WHITE + prefix + ChatColor.RED
							+ " Download it from BukkitDev: " + ChatColor.AQUA
							+ this.updateChecker.getLink());

		}
		loadConfig();
		startMetrics();
	}

	private void loadConfig() {
		getConfig().addDefault("defaults.tree-destroyer", tool_list);
		getConfig().addDefault("defaults.motd", true);
		getConfig().addDefault("metrics.enable", true);
		getConfig().addDefault("Settings.updateCheck", true);
		getConfig().options().copyDefaults(true);
		saveConfig();
		getLogger().info("Successfully loaded config! :)");
	}

	private void startMetrics() {
		if (getConfig().getBoolean("metrics.enable")) {
			try {
				getLogger().info("[TreeDestroyer] starting metrics!");
				MetricsLite metrics = new MetricsLite(this);
				metrics.start();
			} catch (IOException e) {
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("treereload")) {
			reloadConfig();
			sender.sendMessage(ChatColor.AQUA + "[" + ChatColor.LIGHT_PURPLE
					+ "TD" + ChatColor.AQUA + "]" + ChatColor.DARK_GREEN
					+ " Config Reloaded");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("treeinfo")) {
			if (sender.hasPermission("treedestroyer.info")) {
				sender.sendMessage(ChatColor.RED
						+ "-----TreeDestroyer v6.4-----");
				sender.sendMessage(ChatColor.AQUA + "Latest version ===> "
						+ updateChecker.getVersion());
				sender.sendMessage(ChatColor.AQUA + "Current version ===> "
						+ this.getDescription().getVersion());
				sender.sendMessage(ChatColor.RED
						+ "-----Developed by CeramicTitan-----");
				return true;
			} else {
				sender.sendMessage(ChatColor.RED + "No Permission!");
			}
		}
		if (cmd.getName().equalsIgnoreCase("Destroy")) {
			if (sender.hasPermission("treedestroyer.destroy")) {
				toggleTreeDestroyer(sender);
				return true;
			} else {
				sender.sendMessage(ChatColor.RED + "No Permission!");
			}
		}
		return false;
	}

	public void toggleTreeDestroyer(CommandSender sender) {
		if (!(TreeDestroyerUsers.contains(sender.getName()))) {
			TreeDestroyerUsers.add(sender.getName());
			sender.sendMessage(ChatColor.AQUA + "[" + ChatColor.LIGHT_PURPLE
					+ "TD" + ChatColor.AQUA + "]" + ChatColor.GREEN
					+ "Tree Destroyer is now enabled.");
		} else {
			if (TreeDestroyerUsers.contains(sender.getName())) {
				TreeDestroyerUsers.remove(sender.getName());
				sender.sendMessage(ChatColor.AQUA + "["
						+ ChatColor.LIGHT_PURPLE + "TD" + ChatColor.AQUA + "]"
						+ ChatColor.RED + "Tree Destroyer is now disabled.");
			}
		}

	}
}

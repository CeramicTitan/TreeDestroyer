package me.ceramictitan.TreeDestroyer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;


import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TreeDestroyer extends JavaPlugin {
  public static TreeDestroyer plugin;
  public final Logger log = Logger.getLogger("Minecraft");
  public final MyBlockListener blockListener = new MyBlockListener(this);
  public final MyPlayerListener playerListener = new MyPlayerListener(this);
  public static HashMap<Player, ArrayList<Player>> TreeDestroyerUsers = new HashMap<Player, ArrayList<Player>>();
  String[] lines = new String[5];
  public static boolean update = false;
  public static String name = "";
  public static long size = 0;
  
  @Override
  public void onDisable() {
    PluginDescriptionFile pdfFile = this.getDescription();
    log.info(pdfFile.getName() + " is now disabled.");
  }

  @Override
  public void onEnable() {
    PluginDescriptionFile pdfFile = this.getDescription();
    this.log.info(pdfFile.getName() + " Version " + pdfFile.getVersion() +" is now enabled.");
    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(this.blockListener, this);
    pm.registerEvents(this.playerListener, this);
    Updater updater = new Updater(this, "tree-destroyer", this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false); // Start Updater but just do a version check
    update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE; // Determine if there is an update ready for us
    name = updater.getLatestVersionString(); // Get the latest version
    size = updater.getFileSize(); // Get latest size
    lines[0] = ChatColor.DARK_GRAY + "#########"+ ChatColor.DARK_PURPLE + this.getDescription().getName() + ChatColor.DARK_GRAY +"#########";
	lines[1] = ChatColor.AQUA + "Latest Version =>" + " " + updater.getLatestVersionString();
    lines[2] = ChatColor.GOLD +"Version Installed =>"+ " " + this.getDescription().getName()+ " " + "v" + this.getDescription().getVersion();
    lines[3] = ChatColor.RED + "Authors =>" + " " + ChatColor.RED + ("CeramicTitan + zack6849");
    lines[4] = ChatColor.DARK_GRAY + "###############################";
    getConfig().addDefault("defaults.tree-destroyer", 286);
    getConfig().addDefault("defaults.auto-update", true);
    getConfig().addDefault("defaults.motd", true);
    getConfig().addDefault("metrics.enable", true);
    getConfig().options().copyDefaults(true);
    saveConfig();
    startMetrics();
  }
  private void startMetrics() {
      if (getConfig().getBoolean("metrics.enable")) {
          try {
        	  log.info("starting metrics!");
              MetricsLite metrics = new MetricsLite(this);
              metrics.start();
          } catch (IOException e) {}
      }
  }
  
  public boolean noneTree(Block base, World world) {
		if (getConfig().getBoolean("defaults.onlyontree") == true) {
			Block check = base;
			int height = 1;
			for (int i = 1; check.getTypeId() == 17; i++) {
				check = world.getBlockAt(base.getX(), base.getY() + i, base.getZ());
				height = i;
			}
			if (world.getBlockAt(base.getX(), base.getY() + height, base.getZ()).getTypeId() == 18) {
				return true;
			}
			return false;
		} else {
			return true;
		}
	}
  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
	  if(cmd.getName().equalsIgnoreCase("update")){
		  if(sender.hasPermission("treedestroyer.update")){
			  sender.sendMessage(ChatColor.GREEN + "Download has started!");
			  sender.sendMessage(ChatColor.GREEN + "Check console for status!");
			Updater updater = new Updater(this, "tree-destroyer", this.getFile(), Updater.UpdateType.NO_VERSION_CHECK, true); // Go straight to downloading, and announce progress to console.
		  }else {
			  sender.sendMessage(ChatColor.RED+"No Permission!");
		  }
	  }
	  if(cmd.getName().equalsIgnoreCase("treereload")){
		  reloadConfig();
		  sender.sendMessage(ChatColor.AQUA + "[" + ChatColor.LIGHT_PURPLE + "TD" + ChatColor.AQUA + "]" + ChatColor.DARK_GREEN + "Config Reloaded");
	  }
    if(cmd.getName().equalsIgnoreCase("treeinfo")){
      if(sender.hasPermission("treedestroyer.info")){
        sender.sendMessage(lines);
      }else{
    	  sender.sendMessage(ChatColor.RED+"No Permission!");
      }
      }
    if(cmd.getName().equalsIgnoreCase("Destroy")){
           if(sender.hasPermission("treedestroyer.destroy")){
             toggleTreeDestroyer(sender);
           }else{
        	   sender.sendMessage(ChatColor.RED+"No Permission!");
                return true;
              } 
    return true;
    }
    return true;
  }

public void toggleTreeDestroyer(CommandSender sender) {
    if(!enabled ((Player) sender)) {
      TreeDestroyerUsers.put((Player) sender, null);
      ((Player) sender).sendMessage(ChatColor.AQUA + "[" + ChatColor.LIGHT_PURPLE + "TD" + ChatColor.AQUA + "]" + ChatColor.GREEN + "Tree Destroyer is now enabled.");  
    }else{ 
      TreeDestroyerUsers.remove((Player) sender);
      ((Player) sender).sendMessage(ChatColor.AQUA + "[" + ChatColor.LIGHT_PURPLE + "TD" + ChatColor.AQUA + "]" + ChatColor.RED + "Tree Destroyer is now disabled.");
      
    }
      
  }
  public static boolean enabled(Player player) {
    return TreeDestroyerUsers.containsKey(player);
  }
}
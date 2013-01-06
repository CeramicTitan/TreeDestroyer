package me.ceramictitan.TreeDestroyer;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class MyBlockListener implements Listener {

	private TreeDestroyer plugin;

	public MyBlockListener(TreeDestroyer td) {
		plugin = td;
	}

	@EventHandler
	public boolean logBreaking(BlockBreakEvent event) {
		int distance = 0;
		Block baseLog = event.getBlock();
		Block next;
		while (true) {
			distance++;
			next = baseLog.getRelative(BlockFace.UP, distance);
			if (next.getType() == Material.LOG) {
				continue;
			} else if (next.getType() != Material.LEAVES) {
				return true;
			} else {
				if (TreeDestroyer.TreeDestroyerUsers.contains(event.getPlayer()
						.getName())) {
					Material block = event.getBlock().getType();
					Player player = event.getPlayer();
					if (block == Material.LOG
							&& plugin
									.getConfig()
									.getList("defaults.tree-destroyer")
									.contains(
											player.getItemInHand().getTypeId())) {
						Location blockLocation = event.getBlock().getLocation();
						double y = blockLocation.getBlockY();
						double x = blockLocation.getBlockX();
						double z = blockLocation.getBlockZ();

						World currentWorld = event.getPlayer().getWorld();
						boolean logsLeft = true;

						while (logsLeft == true) {
							y++;
							Location blockAbove = new Location(currentWorld, x,
									y, z);
							Material blockAboveType = blockAbove.getBlock()
									.getType();
							byte blockAboveData = blockAbove.getBlock()
									.getData();
							if (blockAboveType == Material.LOG) {
								ItemStack droppedItem = new ItemStack(
										blockAboveType, 1, blockAboveData);
								currentWorld.playEffect(blockAbove,
										Effect.ENDER_SIGNAL, 5);
								blockAbove.getBlock().setType(Material.AIR);
								currentWorld.dropItem(blockAbove, droppedItem);
								logsLeft = true;
							} else {
								logsLeft = false;
							}
						}
					}
				}
			}
		}
	}
}
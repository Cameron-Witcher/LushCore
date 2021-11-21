package net.lushmc.spigot.core.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.lushmc.spigot.core.utils.CoreUtils;

public class PlayerListener implements Listener {

	public PlayerListener(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (e.getBlock().getType().equals(Material.CHEST)) {
			if (e.getPlayer().hasMetadata("comchest")) {
				CoreUtils.createCommunityChest(e.getBlock());
			}
		}

	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if(e.getBlock().getType().equals(Material.CHEST)) {
			CoreUtils.removeCommunityChest(e.getBlock());
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		CoreUtils.updateCommunityChests();
	}
}

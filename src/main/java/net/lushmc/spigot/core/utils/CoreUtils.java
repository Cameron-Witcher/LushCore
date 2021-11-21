package net.lushmc.spigot.core.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import net.lushmc.spigot.core.LushCore;
import net.md_5.bungee.api.ChatColor;

public class CoreUtils {

	public static final String PREFIX = colorize("&a&lLush&2&lMC &f> &7");
	private static LushCore plugin;
	static Map<Block, Hologram> chests = new HashMap<>();

	public static void init(LushCore main) {
		plugin = main;

		try {
			for (String s : plugin.getConfig().getStringList("CommunityChests")) {
				log("Looking for chest at " + s);
				if (CoreUtils.decryptLocation(s).getBlock().getType().equals(Material.CHEST)) {
					log("Adding a chest at " + s);
					chests.put(CoreUtils.decryptLocation(s).getBlock(),
							HologramsAPI.createHologram(plugin, CoreUtils.decryptLocation(s).clone().add(0, 2, 0)));
				}
			}
		} catch (Exception ex) {
			// No problem. Just means nothing has been saved yet.
		}

	}

	public static LushCore getPlugin() {
		return plugin;
	}

	public static String colorize(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	public static void log(String message) {
		log(Level.INFO, message);
	}

	public static void log(Level level, String message) {
		plugin.getLogger().log(level, message);
	}

	public static boolean update() {

		boolean success = true;
		InputStream in = null;
		FileOutputStream out = null;

		try {

			URL myUrl = new URL(
					"https://jenkins.mysticcloud.net/job/LushCore/lastSuccessfulBuild/artifact/target/LushCore.jar");
			HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
			conn.setDoOutput(true);
			conn.setReadTimeout(30000);
			conn.setConnectTimeout(30000);
			conn.setUseCaches(false);
			conn.setAllowUserInteraction(false);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestMethod("GET");
			in = conn.getInputStream();
			out = new FileOutputStream("plugins/LushCore.jar");
			int c;
			byte[] b = new byte[1024];
			while ((c = in.read(b)) != -1)
				out.write(b, 0, c);

		}

		catch (Exception ex) {
			log(Level.SEVERE, "There was an error updating. Check console for details.");
			ex.printStackTrace();
			success = false;
		}

		finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					log(Level.SEVERE, "There was an error updating. Check console for details.");
					e.printStackTrace();
				}
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					log(Level.SEVERE, "There was an error updating. Check console for details.");
					e.printStackTrace();
				}
		}
		return success;
	}

	public static String encryptLocation(Location loc) {
		String r = loc.getWorld().getName() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":"
				+ loc.getPitch() + ":" + loc.getYaw();
		r = r.replaceAll("\\.", ",");
		r = "location:" + r;
		return r;
	}

	public static Location decryptLocation(String s) {
		if (s.startsWith("location:"))
			s = s.replaceAll("location:", "");

		if (s.contains(","))
			s = s.replaceAll(",", ".");
		String[] args = s.split(":");
		Location r = new Location(Bukkit.getWorld(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]),
				Double.parseDouble(args[3]));
		if (args.length >= 5) {
			r.setPitch(Float.parseFloat(args[4]));
			r.setYaw(Float.parseFloat(args[5]));
		}
		return r;
	}

	public static void createCommunityChest(Block block) {
		List<String> locs = plugin.getConfig().getStringList("CommunityChests");
		locs.add(encryptLocation(block.getLocation()));
		plugin.getConfig().set("CommunityChests", locs);
		plugin.saveConfig();
		if (!chests.containsKey(block))
			chests.put(block, HologramsAPI.createHologram(plugin, block.getLocation().clone().add(0, 2, 0)));
	}

	public static void removeCommunityChest(Block block) {
		List<String> locs = plugin.getConfig().getStringList("CommunityChests");
		if (locs.contains(encryptLocation(block.getLocation()))) {
			locs.remove(encryptLocation(block.getLocation()));
			plugin.getConfig().set("CommunityChests", locs);
			plugin.saveConfig();

		}
		if (chests.containsKey(block))
			chests.remove(block);

	}

	public static void updateCommunityChests() {
		log("Updating chests...");
		for (Entry<Block, Hologram> e : chests.entrySet()) {
			log("Checking if key is a chest.");
			if (e.getKey() instanceof Chest) {
				log("updating....");
				e.getValue().clearLines();
				for (ItemStack i : ((Chest) e.getKey()).getInventory().getContents()) {
					if (i == null || i.getType().equals(Material.AIR))
						continue;
					e.getValue().appendTextLine("&6" + i.getType() + (i.getAmount() == 1 ? "" : "&7x" + i.getAmount()));
				}
			}
		}
	}

}

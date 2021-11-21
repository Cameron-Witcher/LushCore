package net.lushmc.spigot.core.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

import net.lushmc.spigot.core.LushCore;
import net.md_5.bungee.api.ChatColor;

public class CoreUtils {

	public static final String PREFIX = colorize("&a&lLush&2&lMC &f> &7");
	private static LushCore plugin;

	public static void init(LushCore main) {
		plugin = main;
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

}

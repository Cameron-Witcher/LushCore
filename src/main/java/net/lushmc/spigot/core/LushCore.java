package net.lushmc.spigot.core;

import org.bukkit.plugin.java.JavaPlugin;

import net.lushmc.spigot.core.commands.AdminCommands;
import net.lushmc.spigot.core.utils.CoreUtils;
import net.md_5.bungee.api.ChatColor;

public class LushCore extends JavaPlugin {

	public void onEnable() {
		CoreUtils.init(this);

		new AdminCommands(this, "lushcore", "communitychest");

		CoreUtils.log(ChatColor.GREEN + "LushCore has been enabled.");
	}

}

package net.lushmc.spigot.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import net.lushmc.spigot.core.utils.CoreUtils;
import net.lushmc.spigot.core.utils.Perm;

public class AdminCommands implements CommandExecutor {

	public AdminCommands(JavaPlugin plugin, String... cmds) {
		for (String cmd : cmds) {
			plugin.getCommand(cmd).setExecutor(this);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("communitychest")) {
			if (sender.hasPermission(Perm.ADMIN) && sender instanceof Player) {
				Player player = (Player)sender;
				if(player.hasMetadata("comchest")) {
					player.sendMessage(CoreUtils.PREFIX + "Exiting community chest editor.");
					player.removeMetadata("comchest", CoreUtils.getPlugin());
				} else {
					player.sendMessage(CoreUtils.PREFIX + "Entering community chest editor. Place a chest to create a community chest.");
					player.setMetadata("comchest", new FixedMetadataValue(CoreUtils.getPlugin(), "true"));
				}
			} else {
				sender.sendMessage(CoreUtils.colorize(CoreUtils.PREFIX + "Sorry, you can't use that command."));
			}
		}
		if (cmd.getName().equalsIgnoreCase("lushcore")) {
			if (args.length == 0) {

				return true;
			}
			if (args[0].equalsIgnoreCase("update")) {
				if (CoreUtils.update()) {
					sender.sendMessage(CoreUtils.colorize(CoreUtils.PREFIX
							+ "Successfully downloaded LushCore.jar. Please restart the server as soon as possible to avoid any fatal bugs"));
				}
			}
		}
		return true;
	}
}

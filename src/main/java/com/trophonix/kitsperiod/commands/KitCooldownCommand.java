package com.trophonix.kitsperiod.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.trophonix.kitsperiod.Main;
import com.trophonix.kitsperiod.Utils;

public class KitCooldownCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if ((sender instanceof Player) && !(sender.hasPermission("kitsperiod.cooldown") || sender.isOp())) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to set kit cooldowns.");
			return true;
		}
		
		if (args.length == 1) {
			if (!Main.getInstance().getKitManager().kitExists(args[0])) {
				sender.sendMessage(ChatColor.RED + "Kit with name " + ChatColor.GRAY + args[0] + ChatColor.RED + " does not exist.");
				return true;
			}
			
			Main.getInstance().getKitManager().setCooldown(args[0], 0);
			sender.sendMessage(ChatColor.YELLOW + "Cooldown of kit " + ChatColor.BLUE + args[0] + ChatColor.YELLOW + " has been removed.");
			return true;
		}
		
		if (args.length > 1) {
			if (!Main.getInstance().getKitManager().kitExists(args[0])) {
				sender.sendMessage(ChatColor.RED + "Kit with name " + ChatColor.GRAY + args[0] + ChatColor.RED + " does not exist.");
				return true;
			}
			
			Main.getInstance().getKitManager().setCooldown(args[0], Utils.timestampToSeconds(args[1]));
			sender.sendMessage(ChatColor.YELLOW + "Cooldown of kit " + ChatColor.BLUE + args[0] + ChatColor.YELLOW + " has been set to " + args[1]);
			return true;
		}
		
		sender.sendMessage(ChatColor.RED + "Invalid command. Usage: /kitcooldown <name> <timestamp (#d#h#m#s)>");
		return true;
	}
	
}

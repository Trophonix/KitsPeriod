package com.trophonix.kitsperiod.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.trophonix.kitsperiod.Main;

public class KitPermCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) { 
		
		if ((sender instanceof Player) && !(sender.hasPermission("kitsperiod.kitperm") || sender.isOp())) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to set kit permissions.");
			return true;
		}
		
		if (args.length == 1) {
			if (!Main.getInstance().getKitManager().kitExists(args[0])) {
				sender.sendMessage(ChatColor.RED + "Kit with name " + ChatColor.GRAY + args[0] + ChatColor.RED + " does not exist.");
				return true;
			}
			
			Main.getInstance().getKitManager().clearPerm(args[0]);
			sender.sendMessage(ChatColor.YELLOW + "Permission of kit " + ChatColor.BLUE + args[0] + ChatColor.YELLOW + " has been cleared.");
		} else if (args.length > 1) {
			if (!Main.getInstance().getKitManager().kitExists(args[0])) {
				sender.sendMessage(ChatColor.RED + "Kit with name " + ChatColor.GRAY + args[0] + ChatColor.RED + " does not exist.");
				return true;
			}
			
			Main.getInstance().getKitManager().setPerm(args[0], args[1]);
			sender.sendMessage(ChatColor.YELLOW + "Permission of kit " + ChatColor.BLUE + args[0] + ChatColor.YELLOW + " has been set to " + args[1]);
			return true;
		}
		
		sender.sendMessage(ChatColor.RED + "Invalid command. Usage: /kitperm <name> <permission>");
		return true;
	}
	
}

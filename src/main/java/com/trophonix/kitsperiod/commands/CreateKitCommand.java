package com.trophonix.kitsperiod.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.trophonix.kitsperiod.KitManager;

public class CreateKitCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if ((sender instanceof Player) && !(sender.hasPermission("kitsperiod.create") || sender.isOp())) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to create kits!");
		}
		
		if (args.length > 0) {
			if (KitManager.createKit(args[0])) {
				sender.sendMessage(ChatColor.YELLOW + "Kit with name " + ChatColor.BLUE + args[0] + ChatColor.YELLOW + " has been created!");
				return true;
			} else {
				sender.sendMessage(ChatColor.RED + "Kit with name " + ChatColor.GRAY + args[0] + ChatColor.RED + " already exists.");
				return true;
			}
		}
			
		sender.sendMessage(ChatColor.RED + "Invalid command. Usage: /createkit <name>");
		return true;
	}
	
}

package com.trophonix.kitsperiod.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.trophonix.kitsperiod.Main;

public class DeleteKitCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if ((sender instanceof Player) && !(sender.hasPermission("kitsperiod.delete") || sender.isOp())) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to delete kits.");
			return true;
		}
		
		if (args.length > 0) {
			if (!Main.getInstance().getKitManager().kitExists(args[0])) {
				sender.sendMessage(ChatColor.RED + "Kit with name " + ChatColor.GRAY + args[0] + ChatColor.RED + " does not exist.");
				return true;
			}
			
			Main.getInstance().getKitManager().deleteKit(args[0]);
			sender.sendMessage(ChatColor.YELLOW + "Kit with name " + ChatColor.BLUE + args[0] + ChatColor.YELLOW + " has been deleted.");
			return true;
		}
		
		sender.sendMessage(ChatColor.RED + "Invalid command. Usage: /deletekit <name>");
		return true;
	}
	
}

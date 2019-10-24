package com.trophonix.kitsperiod.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.trophonix.kitsperiod.KitManager;

public class KitIconCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if (!(sender.hasPermission("kitsperiod.icon") || sender.isOp())) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to set kit icons.");
			return true;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command must be executed by a player.");
			return true;
		}
		
		Player p = (Player)sender;
		
		if (args.length > 0) {
			if (!KitManager.kitExists(args[0])) {
				p.sendMessage(ChatColor.RED + "Kit with name " + ChatColor.GRAY + args[0] + ChatColor.RED + " does not exist.");
				return true;
			}
			
			if (p.getItemInHand() == null) {
				p.sendMessage(ChatColor.RED + "You must hold an item in your hand to set the kit icon.");
				return true;
			}
			
			KitManager.kitIcon(args[0], new ItemStack(p.getItemInHand().getType(), 1));
			p.sendMessage(ChatColor.YELLOW + "Icon of kit " + ChatColor.BLUE + args[0] + ChatColor.YELLOW + " has been set to the item in your hand.");
			return true;
		}
		
		sender.sendMessage(ChatColor.RED + "Invalid command. Usage: /kiticon <name>");
		return true;
	}
	
}

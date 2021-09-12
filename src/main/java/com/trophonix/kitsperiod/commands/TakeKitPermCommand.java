package com.trophonix.kitsperiod.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.trophonix.kitsperiod.KitManager;
import com.trophonix.kitsperiod.Main;

public class TakeKitPermCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if ((sender instanceof Player) && !(sender.hasPermission("kitsperiod.giveperm") || sender.isOp())) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to take players kit permissions.");
			return true;
		}
		
		Player p = Bukkit.getPlayer(args[0]);
		
		if (args.length > 1) {
			if (Bukkit.getPlayer(args[0]) == null) {
				sender.sendMessage(ChatColor.RED + "Cannot find player with name " + ChatColor.GRAY + args[0]);
				return true;
			}
			
			if (!Main.getInstance().getKitManager().kitExists(args[1])) {
				p.sendMessage(ChatColor.RED + "Kit with name " + ChatColor.GRAY + args[1] + ChatColor.RED + " does not exist.");
				return true;
			}
			
			if (Main.getInstance().getKitManager().getPerm(args[1]) == null) {
				p.sendMessage(ChatColor.RED + "Kit with name " + ChatColor.GRAY + args[1] + ChatColor.RED + " does not require a permission.");
				return true;
			}
			
			p.addAttachment(Main.getInstance(), Main.getInstance().getKitManager().getPerm(args[0]), false);
		}
		
		sender.sendMessage(ChatColor.RED + "Invalid command. Usage: /takekitperm <player name> <kit name>");
		return true;
	}
	
}

package com.trophonix.kitsperiod.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.trophonix.kitsperiod.KitManager;
import com.trophonix.kitsperiod.Main;

public class ModifyKitCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Kits can only be modified by a player.");
			return true;
		}
		Player p = (Player)sender;
		
		if (!(p.hasPermission("kitsperiod.modify") || p.isOp())) {
			p.sendMessage(ChatColor.RED + "You do not have permission to modify kits!");
			return true;
		}
		
		if (args.length > 0) {
			if (!Main.getInstance().getKitManager().kitExists(args[0])) {
				p.sendMessage(ChatColor.RED + "That kit does not exist. Use /createkit " + args[0] + " to make it.");
				return true;
			}
			p.openInventory(Main.getInstance().getKitManager().modifyKit(args[0]));
			return true;
		}
		
		p.sendMessage(ChatColor.RED + "Invalid command. Usage: /modifykit <name>");
		return true;
	}

}

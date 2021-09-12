package com.trophonix.kitsperiod.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.trophonix.kitsperiod.Main;

public class KitCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Kits can only be used by a player.");
			return true;
		}
		
		Player p = (Player)sender;
		
		if (!(p.hasPermission("kitsperiod.kit") || p.isOp())) {
			p.sendMessage(ChatColor.RED + "You do not have permission for that command.");
			return true;
		}
		if(!Main.getInstance().getConfigFile().contains("kitlist")) {
			p.sendMessage(ChatColor.RED + "No Kits has been created yet!");
			return true;
		}
		if(Main.getInstance().getConfigFile().getStringList("kitlist").size() == 0) {
			p.sendMessage(ChatColor.RED + "No Kits has been created yet!");
			return true;
		}
		p.openInventory(Main.getInstance().getKitManager().kitSelection(p));
		return true;
	}
	
}

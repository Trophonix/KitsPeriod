package com.trophonix.kitsperiod;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Liztener implements Listener {

	public Liztener() {
		Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
	}

	@EventHandler
	public void clickInventory(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		Inventory inv = e.getClickedInventory();

		if (e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null
				|| e.getCurrentItem().getItemMeta().getDisplayName() == null)
			return;

		if (inv.getTitle().equalsIgnoreCase("kit selection")) {
			e.setCancelled(true);
			String kitName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

			if (!KitManager.cooldownOver(p, kitName)) {
				p.closeInventory();
				p.sendMessage(ChatColor.GRAY + "You can use that kit again in: " + ChatColor.RED
						+ Utils.secondsToTimestamp(KitManager.getCooldown(kitName, p)));
				return;
			}

			List<ItemStack> kit = KitManager.getContents(kitName);

			for (int i = 0; i < kit.size(); i++) {
				p.getInventory().addItem(kit.get(i));
			}
			p.sendMessage(ChatColor.YELLOW + "You have been given the contents of kit " + ChatColor.BLUE + kitName);
			p.closeInventory();

			KitManager.putOnCooldown(p, kitName);
		}
	}

	@EventHandler
	public void closeInventory(InventoryCloseEvent e) {
		Inventory inv = e.getInventory();

		if (inv.getTitle().toLowerCase().contains("modify")) {
			String name = inv.getTitle().replace("Modify ", "");
			KitManager.saveKit(name, inv.getContents());
			e.getPlayer().sendMessage(ChatColor.YELLOW + "Kit with name " + ChatColor.BLUE + name + ChatColor.YELLOW
					+ " has been saved.");
			Main.save();
		}
	}

}

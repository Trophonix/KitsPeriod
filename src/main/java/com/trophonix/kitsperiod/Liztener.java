package com.trophonix.kitsperiod;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class Liztener implements Listener {
	
	private Main plugin;
	private KitManager kitManager;

	public Liztener(Main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
		this.kitManager = plugin.getKitManager();
	}

	@EventHandler
	public void clickInventory(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		InventoryView inv = e.getView();

		if (e.getCurrentItem() == null || e.getCurrentItem().getItemMeta() == null
				|| e.getCurrentItem().getItemMeta().getDisplayName() == null)
			return;

		if (inv.getTitle().equalsIgnoreCase("kit selection")) {
			e.setCancelled(true);
			String kitName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

			if (!kitManager.cooldownOver(p, kitName)) {
				p.closeInventory();
				p.sendMessage(ChatColor.GRAY + "You can use that kit again in: " + ChatColor.RED
						+ Utils.secondsToTimestamp(kitManager.getCooldown(kitName, p)));
				return;
			}

			List<ItemStack> kit = kitManager.getContents(kitName);

			for (int i = 0; i < kit.size(); i++) {
				p.getInventory().addItem(kit.get(i));
			}
			p.sendMessage(ChatColor.YELLOW + "You have been given the contents of kit " + ChatColor.BLUE + kitName);
			p.closeInventory();

			kitManager.putOnCooldown(p, kitName);
		}
	}

	@EventHandler
	public void closeInventory(InventoryCloseEvent e) {
		Inventory inv = e.getInventory();
		InventoryView view = e.getView();

		if (view.getTitle().toLowerCase().contains("modify")) {
			String name = view.getTitle().replace("Modify ", "");
			kitManager.saveKit(name, inv.getContents());
			e.getPlayer().sendMessage(ChatColor.YELLOW + "Kit with name " + ChatColor.BLUE + name + ChatColor.YELLOW
					+ " has been saved.");
			this.plugin.save();
		}
	}

}

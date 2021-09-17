package com.trophonix.kitsperiod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KitManager {
	
	private Main plugin;
	
	public KitManager(Main plugin) {
		this.plugin = plugin;
	}

	public boolean createKit(String name) {
		List<String> kits = new ArrayList<String>();
		if (this.plugin.getKitConfigFile().contains("kitlist"))
			kits = this.plugin.getKitConfigFile().getStringList("kitlist");
		if (kits.contains(name))
			return false;
		else
			kits.add(name);
		this.plugin.getKitConfigFile().set("kitlist", kits);
		this.plugin.getKitConfigFile().set("kits." + name + ".cooldown", 0);
		this.plugin.save();
		return true;
	}

	public void deleteKit(String name) {
		List<String> kits = new ArrayList<String>();
		if (this.plugin.getKitConfigFile().contains("kitlist"))
			kits = this.plugin.getKitConfigFile().getStringList("kitlist");
		if (kits.contains(name))
			kits.remove(name);
		this.plugin.getKitConfigFile().set("kitlist", kits);
		this.plugin.getKitConfigFile().set("kits." + name, null);
		this.plugin.save();
	}

	public Inventory modifyKit(String name) {
		List<String> kits = new ArrayList<String>();
		if (this.plugin.getKitConfigFile().contains("kitlist"))
			kits = this.plugin.getKitConfigFile().getStringList("kitlist");
		if (!kits.contains(name))
			return null;

		Map<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
		for (int i = 0; i < 36; i++) {
			if (!this.plugin.getKitConfigFile().contains("kits." + name + ".item." + i))
				items.put(i, new ItemStack(Material.AIR));
			else
				items.put(i, this.plugin.getKitConfigFile().getItemStack("kits." + name + ".item." + i));
		}

		Inventory inventory = Bukkit.createInventory(null, 36, "Modify " + name);

		for (int i = 0; i < items.size(); i++) {
			if (items.get(i) == null)
				continue;
			inventory.setItem(i, items.get(i));
		}

		return inventory;
	}

	public Inventory kitSelection(Player player)  {
		List<String> kits = new ArrayList<String>();
		if (this.plugin.getKitConfigFile().contains("kitlist"))
			kits = this.plugin.getKitConfigFile().getStringList("kitlist");

		Inventory inventory = Bukkit.createInventory(null, (int) (Math.ceil(kits.size() / 9d) * 9), "Kit Selection");

		for (int i = 0; i < kits.size(); i++) {
			if (this.plugin.getKitConfigFile().contains("kits." + kits.get(i) + ".permission")
					&& !(player.hasPermission(this.plugin.getKitConfigFile().getString("kits." + kits.get(i) + ".permission"))
							|| player.isOp()))
				continue;
			ItemStack item = new ItemStack(Material.CLAY);
			if (this.plugin.getKitConfigFile().contains("kits." + kits.get(i) + ".icon"))
				item = this.plugin.getKitConfigFile().getItemStack("kits." + kits.get(i) + ".icon");
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.BLUE + kits.get(i));
			if (cooldownOver(player, kits.get(i))) {
				meta.setLore(Arrays.asList("", ChatColor.BLUE + "Next Use: " + ChatColor.AQUA + "none", ""));
			} else {
				meta.setLore(Arrays.asList("", ChatColor.GRAY + "Next Use: " + ChatColor.RED + Utils.secondsToTimestamp(getCooldown(kits.get(i), player)), ""));
			}
			item.setItemMeta(meta);
			inventory.addItem(item);
		}

		return inventory;
	}

	public void putOnCooldown(Player player, String kit) {
		FileConfiguration config = Utils.getPlayerConfig(player.getUniqueId());
		config.set(kit,(int)
				(Calendar.getInstance().getTimeInMillis() / 1000));
		plugin.savePlayerConfig(config, player.getUniqueId());
	}

	public boolean cooldownOver(Player player, String kit)  {
		FileConfiguration config = Utils.getPlayerConfig(player.getUniqueId());
		if (config.contains(kit)) {
			if (!hasCooldown(kit))
				return true;
			if (config.getInt(kit) + getCooldown(kit) > Calendar.getInstance().getTimeInMillis() / 1000)
				return false;
		}
		return true;
	}

	public boolean hasCooldown(String name) {
		if (!(this.plugin.getKitConfigFile().getInt("kits." + name + ".cooldown") == 0))
			return true;
		return false;
	}

	public void kitIcon(String name, ItemStack icon) {
		this.plugin.getKitConfigFile().set("kits." + name + ".icon", icon);
		this.plugin.save();
	}

	public void setPerm(String name, String node) {
		this.plugin.getKitConfigFile().set("kits." + name + ".permission", node);
		this.plugin.save();
	}

	public String getPerm(String name) {
		if (!this.plugin.getKitConfigFile().contains("kits." + name + ".permission"))
			return null;

		return this.plugin.getKitConfigFile().getString("kits." + name + ".permission");
	}

	public void clearPerm(String name) {
		this.plugin.getKitConfigFile().set("kits." + name + ".permission", null);
		this.plugin.save();
	}

	public void setCooldown(String name, int cooldownSeconds) {
		this.plugin.getKitConfigFile().set("kits." + name + ".cooldown", cooldownSeconds);
		this.plugin.save();
	}

	public int getCooldown(String name) {
		if (!this.plugin.getKitConfigFile().contains("kits." + name + ".cooldown"))
			return 0;

		return this.plugin.getKitConfigFile().getInt("kits." + name + ".cooldown");
	}
	
	public int getCooldown(String name, Player player) {
		if (!hasCooldown(name))
			return 0;
		FileConfiguration config = Utils.getPlayerConfig(player.getUniqueId());
		if (!config.contains(name))
			return 0;
		
		return (int) (config.getInt(name) + this.plugin.getKitConfigFile().getInt("kits." + name + ".cooldown") - (Calendar.getInstance().getTimeInMillis() / 1000));
	}

	public List<ItemStack> getContents(String name) {
		List<ItemStack> contents = new ArrayList<ItemStack>();
		for (int i = 0; i < 36; i++) {
			if (!this.plugin.getKitConfigFile().contains("kits." + name + ".item." + i))
				continue;
			contents.add(this.plugin.getKitConfigFile().getItemStack("kits." + name + ".item." + i));
		}
		return contents;
	}

	public void saveKit(String name, ItemStack[] items) {
		for (int i = 0; i < items.length; i++) {
			this.plugin.getKitConfigFile().set("kits." + name + ".item." + i, items[i]);
		}
		this.plugin.save();
	}

	public boolean kitExists(String name) {
		List<String> kits = new ArrayList<String>();
		if (this.plugin.getKitConfigFile().contains("kitlist"))
			kits = this.plugin.getKitConfigFile().getStringList("kitlist");
		if (kits.contains(name))
			return true;
		return false;
	}
}

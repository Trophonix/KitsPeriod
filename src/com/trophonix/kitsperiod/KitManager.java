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

	public static boolean createKit(String name) {
		List<String> kits = new ArrayList<String>();
		if (Main.config.contains("kitlist"))
			kits = Main.config.getStringList("kitlist");
		if (kits.contains(name))
			return false;
		else
			kits.add(name);
		Main.config.set("kitlist", kits);
		Main.config.set("kits." + name + ".cooldown", 0);
		Main.save();
		return true;
	}

	public static void deleteKit(String name) {
		List<String> kits = new ArrayList<String>();
		if (Main.config.contains("kitlist"))
			kits = Main.config.getStringList("kitlist");
		if (kits.contains(name))
			kits.remove(name);
		Main.config.set("kitlist", kits);
		Main.config.set("kits." + name, null);
		Main.save();
	}

	public static Inventory modifyKit(String name) {
		List<String> kits = new ArrayList<String>();
		if (Main.config.contains("kitlist"))
			kits = Main.config.getStringList("kitlist");
		if (!kits.contains(name))
			return null;

		Map<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
		for (int i = 0; i < 36; i++) {
			if (!Main.config.contains("kits." + name + ".item." + i))
				items.put(i, new ItemStack(Material.AIR));
			else
				items.put(i, Main.config.getItemStack("kits." + name + ".item." + i));
		}

		Inventory inventory = Bukkit.createInventory(null, 36, "Modify " + name);

		for (int i = 0; i < items.size(); i++) {
			if (items.get(i) == null)
				continue;
			inventory.setItem(i, items.get(i));
		}

		return inventory;
	}

	public static Inventory kitSelection(Player player)  {
		List<String> kits = new ArrayList<String>();
		if (Main.config.contains("kitlist"))
			kits = Main.config.getStringList("kitlist");

		Inventory inventory = Bukkit.createInventory(null, (int) (Math.ceil(kits.size() / 9d) * 9), "Kit Selection");

		for (int i = 0; i < kits.size(); i++) {
			if (Main.config.contains("kits." + kits.get(i) + ".permission")
					&& !(player.hasPermission(Main.config.getString("kits." + kits.get(i) + ".permission"))
							|| player.isOp()))
				continue;
			ItemStack item = new ItemStack(Material.STAINED_CLAY);
			if (Main.config.contains("kits." + kits.get(i) + ".icon"))
				item = Main.config.getItemStack("kits." + kits.get(i) + ".icon");
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

	public static void putOnCooldown(Player player, String kit) {
		FileConfiguration config = Utils.getPlayerConfig(player);
		config.set(kit,(int)
				(Calendar.getInstance().getTimeInMillis() / 1000));
		Utils.savePlayerConfig(config, player);
	}

	public static boolean cooldownOver(Player player, String kit)  {
		FileConfiguration config = Utils.getPlayerConfig(player);
		if (config.contains(kit)) {
			if (!hasCooldown(kit))
				return true;
			if (config.getInt(kit) + getCooldown(kit) > Calendar.getInstance().getTimeInMillis() / 1000)
				return false;
		}
		return true;
	}

	public static boolean hasCooldown(String name) {
		if (!(Main.config.getInt("kits." + name + ".cooldown") == 0))
			return true;
		return false;
	}

	public static void kitIcon(String name, ItemStack icon) {
		Main.config.set("kits." + name + ".icon", icon);
		Main.save();
	}

	public static void setPerm(String name, String node) {
		Main.config.set("kits." + name + ".permission", node);
		Main.save();
	}

	public static String getPerm(String name) {
		if (!Main.config.contains("kits." + name + ".permission"))
			return null;

		return Main.config.getString("kits." + name + ".permission");
	}

	public static void clearPerm(String name) {
		Main.config.set("kits." + name + ".permission", null);
		Main.save();
	}

	public static void setCooldown(String name, int cooldownSeconds) {
		Main.config.set("kits." + name + ".cooldown", cooldownSeconds);
		Main.save();
	}

	public static int getCooldown(String name) {
		if (!Main.config.contains("kits." + name + ".cooldown"))
			return 0;

		return Main.config.getInt("kits." + name + ".cooldown");
	}
	
	public static int getCooldown(String name, Player player) {
		if (!hasCooldown(name))
			return 0;
		FileConfiguration config = Utils.getPlayerConfig(player);
		if (!config.contains(name))
			return 0;
		
		return (int) (config.getInt(name) + Main.config.getInt("kits." + name + ".cooldown") - (Calendar.getInstance().getTimeInMillis() / 1000));
	}

	public static List<ItemStack> getContents(String name) {
		List<ItemStack> contents = new ArrayList<ItemStack>();
		for (int i = 0; i < 36; i++) {
			if (!Main.config.contains("kits." + name + ".item." + i))
				continue;
			contents.add(Main.config.getItemStack("kits." + name + ".item." + i));
		}
		return contents;
	}

	public static void saveKit(String name, ItemStack[] items) {
		for (int i = 0; i < items.length; i++) {
			Main.config.set("kits." + name + ".item." + i, items[i]);
		}
		Main.save();
	}

	public static boolean kitExists(String name) {
		List<String> kits = new ArrayList<String>();
		if (Main.config.contains("kitlist"))
			kits = Main.config.getStringList("kitlist");
		if (kits.contains(name))
			return true;
		return false;
	}
}

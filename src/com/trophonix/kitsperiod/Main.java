package com.trophonix.kitsperiod;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.trophonix.kitsperiod.commands.CreateKitCommand;
import com.trophonix.kitsperiod.commands.DeleteKitCommand;
import com.trophonix.kitsperiod.commands.KitCommand;
import com.trophonix.kitsperiod.commands.KitCooldownCommand;
import com.trophonix.kitsperiod.commands.KitIconCommand;
import com.trophonix.kitsperiod.commands.KitPermCommand;
import com.trophonix.kitsperiod.commands.ModifyKitCommand;

public class Main extends JavaPlugin {

	public static FileConfiguration config;
	
	private static Main instance;
	
	@Override
	public void onEnable() {
		System.out.println("[Kits Period] Initializing...");
		try {
			initialize();
			System.out.println("[Kits Period] Successfully Initialized!");
		} catch (Exception ex) {
			System.out.println("[Kits Period] Initialization Failed.");
			ex.printStackTrace();
		}
	}
	
	@Override
	public void onDisable() {
		save();
		for (Player p : Bukkit.getOnlinePlayers()) {
			FileConfiguration config = Utils.getPlayerConfig(p);
			Utils.savePlayerConfig(config, p);
		}
	}
	
	public void initialize() {
		config = YamlConfiguration.loadConfiguration(new File("plugins" + File.separator + "kitsperiod" + File.separator + "config" + ".yml"));
		instance = this;
		
		getCommand("createkit").setExecutor(new CreateKitCommand());
		getCommand("modifykit").setExecutor(new ModifyKitCommand());
		getCommand("kit").setExecutor(new KitCommand());
		getCommand("deletekit").setExecutor(new DeleteKitCommand());
		getCommand("kiticon").setExecutor(new KitIconCommand());
		getCommand("kitperm").setExecutor(new KitPermCommand());
		getCommand("kitcooldown").setExecutor(new KitCooldownCommand());
		
		new Liztener();
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	public static void save() {
		try {
			config.save(new File("plugins" + File.separator + "kitsperiod" + File.separator + "config" + ".yml"));
		} catch (Exception ex) {
			
		}
	}
}

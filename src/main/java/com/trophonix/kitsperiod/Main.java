package com.trophonix.kitsperiod;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

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

	private FileConfiguration kitConfig;
	private FileConfiguration config;
	private KitManager kitManager;
	private static Main main;
	
	@Override
	public void onEnable() {
		System.out.println("[Kits Period] Initializing...");
		main = this;
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
		try {
			config.save(new File("plugins" + File.separator + "kitsperiod" + File.separator + "kitConfig" + ".yml"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		for (Player p : Bukkit.getOnlinePlayers()) {
			FileConfiguration config = Utils.getPlayerConfig(p.getUniqueId());
			savePlayerConfigAsync(config, p.getUniqueId());
		}
	}
	
	public void initialize() {
		Config con = new Config(this);
		config = con.getConfig();
		kitConfig = YamlConfiguration.loadConfiguration(new File("plugins" + File.separator + "kitsperiod" + File.separator + "kitConfig" + ".yml"));
		this.kitManager = new KitManager(this);
		getCommand("createkit").setExecutor(new CreateKitCommand());
		getCommand("modifykit").setExecutor(new ModifyKitCommand());
		getCommand("kit").setExecutor(new KitCommand());
		getCommand("deletekit").setExecutor(new DeleteKitCommand());
		getCommand("kiticon").setExecutor(new KitIconCommand());
		getCommand("kitperm").setExecutor(new KitPermCommand());
		getCommand("kitcooldown").setExecutor(new KitCooldownCommand());
		//Need to create listener in the listener class for OnJoin, check if player has a config file, and if not, give them any first join kits.
		new Liztener(this);
	}
	
	public void save() {
		Bukkit.getScheduler().runTaskAsynchronously(this, () -> saveAsync(this.kitConfig));
		for (Player p : Bukkit.getOnlinePlayers()) {
			FileConfiguration config = Utils.getPlayerConfig(p.getUniqueId());
			savePlayerConfig(config, p.getUniqueId());
		}
	}
	
	private void saveAsync(FileConfiguration config) {
		try {
			config.save(new File("plugins" + File.separator + "kitsperiod" + File.separator + "kitConfig" + ".yml"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void savePlayerConfig(FileConfiguration config, UUID id) {
		Bukkit.getScheduler().runTaskAsynchronously(this, () -> savePlayerConfigAsync(config, id));
	}
	
    private void savePlayerConfigAsync(FileConfiguration config, UUID id) {
		File file = new File("plugins" + File.separator + "kitsperiod" + File.separator + "playerdata" + File.separator
				+ id.toString() + ".yml");
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public FileConfiguration getKitConfigFile() {
		return this.kitConfig;
	}
	
	public FileConfiguration getConfigFile() {
		return this.config;
	}
	
	public KitManager getKitManager() {
		return this.kitManager;
	}
	
	public static Main getInstance() {
		return main;
	}
}

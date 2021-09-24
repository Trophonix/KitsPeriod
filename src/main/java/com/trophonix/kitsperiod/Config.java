package com.trophonix.kitsperiod;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config
{
	private static final String config = "config.yml";
	/** The FileConfiguration containing all titles */
    private FileConfiguration pluginConfig = null;
    /** The File which contains the titleConfig */
    private File pluginConfigFile = null;
    public boolean savePeriodically = false;
    public int saveTime = 600;
    public boolean giveKitOnFirstJoin = false;

    public Config(Main plugin) {
    	if(pluginConfigFile==null) {
    		pluginConfigFile = new File("plugins" + File.separator + "kitsperiod" + File.separator + config);
    	}
    	if (!pluginConfigFile.exists()) {
    		plugin.saveResource(config, false);
    	}
    	pluginConfig = YamlConfiguration.loadConfiguration(pluginConfigFile);
    	loadConfig();
    }
    
    public void loadConfig() {
    	savePeriodically = Boolean.getBoolean(pluginConfig.getString("SavePeriodically"));
    	saveTime = pluginConfig.getInt("SaveTime");
    }
    
    public FileConfiguration getConfig() {
    	return this.pluginConfig;
    }
    
}
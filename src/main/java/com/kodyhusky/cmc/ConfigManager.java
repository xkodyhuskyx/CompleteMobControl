/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kodyhusky.cmc;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


/**
 *
 * @author Kody
 */
public class ConfigManager {
    
    private CompleteMobControl plugin;
    private FileConfiguration config = null;
    
    private List<String> repellerKeys = Stream.of("english", "spanish").collect(Collectors.toList());
    
    public ConfigManager(CompleteMobControl plugin) {
        
        this.plugin = plugin;
        
        // Load config.yml
        plugin.log(Level.INFO, "Loading config.yml data...", null);
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try {
                plugin.saveDefaultConfig();
                config = plugin.getConfig();
            } catch (Exception e) {
                plugin.log(Level.SEVERE, "Unable to save default config file (plugin.yml).", e);
                return;
            }
        }
        
        // Load all repeller configuration files
        plugin.log(Level.INFO, "Loading repeller data...", null);
        File repellersFolder = new File(plugin.getDataFolder(), "repellers");
        if (!repellersFolder.exists()) {
            repellersFolder.mkdirs();
        }
        if (repellersFolder.exists()) {
            for (File repellerFile : (new File(plugin.getDataFolder(), "repellers")).listFiles()) {
                if (repellerFile.isFile() || repellerFile.getName().endsWith(".yml")) {
                    FileConfiguration repellerConfig = checkRepellerFile(repellerFile);
                    if (repellerConfig != null) {
                        // NEEDS COMPLETING
                    }
                }
            }
        } else {
            plugin.log(Level.SEVERE, "Unable to create repeller configuration directory (/repellers).", null);
        }
    }
    
    public FileConfiguration checkRepellerFile(File repellerFile) {
        FileConfiguration repellerConfig = YamlConfiguration.loadConfiguration(repellerFile);
        for (String key : repellerKeys) {
            if (!repellerConfig.contains(key) || repellerConfig.get(key) == null) {
                plugin.log(Level.WARNING, "Repeller configuration file (" + repellerFile.getName() + ") is missing a required key (" + key + ") and was not loaded.", null);
                return null;
            }
        }
        return repellerConfig;
    }
    
    public boolean isLoaded() {
        return config != null;
    }
    
    public boolean isDebugEnabled() {
        return config.getBoolean("debug", false);
    }
    
    public boolean isFeatureEnabled(String featureID) {
    	return config.getBoolean(featureID, true);
    }

	public boolean removeCustomNamedMobs() {
		// TODO Auto-generated method stub
		return false;
	}
}

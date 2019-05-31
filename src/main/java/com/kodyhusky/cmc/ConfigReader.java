/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kodyhusky.cmc;

import java.io.File;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;


/**
 *
 * @author Kody
 */
public class ConfigReader {
    
    private CompleteMobControl plugin;
    private FileConfiguration config = null;
    
    public ConfigReader(CompleteMobControl plugin) {
        this.plugin = plugin;
        config = plugin.getConfig();
        
        File configfile = new File(plugin.getDataFolder(), "config.yml");
        if (!configfile.exists()) {
            try {
                plugin.saveDefaultConfig();
            } catch (Exception e) {
                plugin.log(Level.SEVERE, "Unable to save default config file (plugin.yml).", e);
                Bukkit.getPluginManager().disablePlugin(plugin);
            }
        }
        
        
    }

    public boolean debugEnabled() {
        return false;
    }

    String getLangName() {
        return "";
    }
    
    public boolean isLoaded() {
        return config != null;
    }
    
}

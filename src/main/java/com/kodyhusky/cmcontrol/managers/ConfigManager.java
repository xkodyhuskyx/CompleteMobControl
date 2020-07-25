package com.kodyhusky.cmcontrol.managers;

import com.kodyhusky.cmcontrol.CompleteMobControl;
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Manages all configuration values and storage.
 * 
 * @author xkodyhuskyx
 */
public class ConfigManager {

    private CompleteMobControl plugin;
    private FileConfiguration config;

    public ConfigManager(CompleteMobControl plugin) {

        this.plugin = plugin;

        // Read from config.yml
        plugin.logToConsole("Reading config.yml data...");
        File cfile = new File(plugin.getDataFolder(), "config.yml");
        if (!cfile.exists()) {
            try { plugin.saveDefaultConfig(); config = plugin.getConfig();
            } catch (Exception e) {
                plugin.logToConsole("-- An error occurred while creating file (config.yml).");
                return;
            }
        }
        plugin.logToConsole("Config data loaded sucessfully.");
    }

    public boolean debugEnabled() {return config.getBoolean("debug", false);}
}

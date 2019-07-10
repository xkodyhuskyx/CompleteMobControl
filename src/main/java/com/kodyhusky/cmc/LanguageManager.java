package com.kodyhusky.cmc;

import java.io.File;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class LanguageManager {
    
    private CompleteMobControl plugin;
    private FileConfiguration language = null;
    
    public LanguageManager(CompleteMobControl plugin) {
        
        this.plugin = plugin;
        
        // Check For Sucessful Config Load
        if (plugin.getConfigManager().isLoaded()) {
            
            // Load Language Config File
            plugin.log(Level.INFO, "Loading language data...", null);
            File languageFile = new File(plugin.getDataFolder(), "language.yml");
            if (!languageFile.exists()) {
                plugin.saveResource("language.yml", false);
            }
            if (languageFile.exists()) {
                language = YamlConfiguration.loadConfiguration(languageFile);
            } else {
                plugin.log(Level.SEVERE, "Unable to create language configuration file (language.yml).", null);
            }
        }
    }
    
    public boolean isLoaded() {
        return language != null;
    }
}
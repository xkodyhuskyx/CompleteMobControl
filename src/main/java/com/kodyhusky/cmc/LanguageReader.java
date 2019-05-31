package com.kodyhusky.cmc;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.configuration.file.FileConfiguration;

public class LanguageReader {
    
    private CompleteMobControl plugin;
    private FileConfiguration config = null;
    
    // Add to this if a new embeded language file is added!
    private List<String> languages = Stream.of("english", "spanish").collect(Collectors.toList());
    
    public LanguageReader(CompleteMobControl plugin, String lang) {
        
        // Initialize LanguageReader class
        this.plugin = plugin;
        
        // Check For Sucessful Config Load
        if (!plugin.getConfigReader().isLoaded()) {
            return;
        }
        
        // Check for language folder
        File langfolder = new File(plugin.getDataFolder(), "lang");
        if (!langfolder.exists()) {
            try {
                langfolder.mkdirs();
                // Save Default Language Files
                languages.forEach((filename) -> {
                    try {
                        plugin.saveResource((new StringBuilder().append("lang").append(File.separator).append(filename).append(".yml")).toString(), false);
                    } catch (Exception e) {
                        plugin.log(Level.WARNING, "Unable to save language file (" + filename + ".yml).", e);
                    }
                });
            } catch (Exception e) {
                plugin.log(Level.SEVERE, "Unable to create language folder.", e);
            }
        }
    }
    
    public boolean isLoaded() {
        return config != null;
    }
}
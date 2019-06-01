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
    
    public ConfigManager(CompleteMobControl plugin) {
        this.plugin = plugin;
        plugin.log(Level.INFO, "Loading config.yml data...", null);
        
        File configfile = new File(plugin.getDataFolder(), "config.yml");
        if (!configfile.exists()) {
            try {
                plugin.saveDefaultConfig();
            } catch (Exception e) {
                plugin.log(Level.SEVERE, "Unable to save default config file (plugin.yml).", e);
                return;
            }
        }
        config = plugin.getConfig();
        if (config != null) {
            plugin.log(Level.INFO, "Loading repeller data...", null);
            File repellerfile = new File(plugin.getDataFolder(), "repellers" + File.separator + "config.yml");
            if (!repellerfile.exists()) {
                try {
                    plugin.saveResource("repellers" + File.separator + "example.yml", true);
                } catch (Exception e) {
                    plugin.log(Level.WARNING, "Unable to save example repeller file (repellers/example.yml).", e);
                }
            }
        }
        loadRepellers();
        
    }
    
    private void loadRepellers() {
        File[] repellerfiles = (new File(plugin.getDataFolder(), "repellers")).listFiles();
        
        for (File file : repellerfiles) {
            if (file.isFile()) {
                if (!"example.yml".equals(file.getName()) && file.getName().endsWith(".yml")) {
                    FileConfiguration rconfig = YamlConfiguration.loadConfiguration(file);
                    List<String> checks = Stream.of("english", "spanish").collect(Collectors.toList());
                    checks.forEach((key) -> {
                        if (!rconfig.contains(key) || rconfig.get(key) == null) {
                            plugin.log(Level.WARNING, "Repeller config file (" + file.getName() + ") is missing a required key (" + key + ") and was not loaded.", null);
                        } else {
                            // DO DOMETHING HERE
                        }
                    });
                }
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
        return (config != null && plugin.getLangManager().isLoaded());
    }
    
}

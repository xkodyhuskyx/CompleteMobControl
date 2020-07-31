/*
 * Copyright (C) 2012-2020 Jeffery Hancock (xkodyhuskyx)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.kodyhusky.cmcontrol.managers;

import com.kodyhusky.cmcontrol.CompleteMobControl;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;

/**
 * Manages all configuration values and storage.
 * 
 * @author xkodyhuskyx
 */
public class ConfigManager {

    private CompleteMobControl plugin;
    private FileConfiguration config;
    
    private HashMap<String,Integer> entity_types;
    private List<String> ward_options;

    public ConfigManager(CompleteMobControl plugin) {

        this.plugin = plugin;

        // Read from config.yml
        plugin.logToConsole("Reading config.yml data...");
        File cfile = new File(plugin.getDataFolder(), "config.yml");
        if (!cfile.exists()) {
            try {
                plugin.saveDefaultConfig();
                config = plugin.getConfig();
                ward_options = new ArrayList();
                ward_options.addAll(config.getStringList("mob_ward.default_options"));
            } catch (Exception e) {
                plugin.logToConsole("-- An error occurred while creating file (config.yml).");
                return;
            }
        }
        
        File efile = new File(plugin.getDataFolder(), "entities.yml");
        YamlConfiguration etypes = new YamlConfiguration();
        try {
            etypes.load(efile);
            entity_types = new HashMap<>();
            etypes.getStringList("passive").forEach(etype -> {
                entity_types.put(etype, 0);
            });
            etypes.getStringList("neutral").forEach(etype -> {
                entity_types.put(etype, 1);
            });
            etypes.getStringList("hostile").forEach(etype -> {
                entity_types.put(etype, 2);
            });
            etypes.getStringList("boss").forEach(etype -> {
                entity_types.put(etype, 3);
            });
        } catch (IOException | InvalidConfigurationException ex) {
            Logger.getLogger(ConfigManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        plugin.logToConsole("Config data loaded sucessfully.");
    }
    
    public int getEntityType(Entity entity) {
        if (entity_types.containsKey(entity.getType().toString().toUpperCase())) {
            return entity_types.get(entity.getName().toUpperCase());
        } else {
            plugin.logToConsole("Entity Type Not Found: " + entity.getName().toUpperCase(), true); // WARN CONSOLE OF MISSING ENTITY RECORD
        }
        return 2; // DEFAULT TO HOSTILE
    }
    
    public boolean isWardOptionSet(String option) {
        return ward_options.contains(option);
    }
    

    public boolean debugEnabled() {return config.getBoolean("debug", false);}
}

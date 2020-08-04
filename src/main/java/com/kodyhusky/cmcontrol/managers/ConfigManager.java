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
import com.kodyhusky.cmcontrol.util.Pair;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

/**
 * Manages all configuration values and storage.
 * 
 * @author xkodyhuskyx
 */
public class ConfigManager {

    private CompleteMobControl plugin;
    private FileConfiguration config;
    private HashMap<String, List<String>> entitygroups = new HashMap<>();
    

    public ConfigManager(CompleteMobControl plugin) {

        this.plugin = plugin;

        // Read from config.yml
        plugin.logToConsole("Reading config.yml data...");
        File cfile = new File(plugin.getDataFolder(), "config.yml");
        if (!cfile.exists()) {
            try {
                plugin.saveDefaultConfig();
                config = plugin.getConfig();
            } catch (Exception e) {
                plugin.logToConsole("-- An error occurred while creating file (config.yml).");
                return;
            }
        }
        config = plugin.getConfig();
        
        plugin.logToConsole("Loading entity groups...");
        File efile = new File(plugin.getDataFolder(), "entity_groups.yml");
        YamlConfiguration egroups = new YamlConfiguration();
        try {
            egroups.load(efile);
            egroups.getKeys(false).forEach(key -> {
                List<String> entities = new ArrayList<>();
                egroups.getStringList(key).forEach(entity -> {
                    if (isValidEntityType(entity)) {
                        entities.add(entity.toUpperCase());
                    } else {
                        plugin.logToConsole("WARN: Cannot identify entity (" + entity + ") listed in (config.yml).");
                    }
                });
                entitygroups.put(key.toUpperCase(), entities);
            });
        } catch (IOException | InvalidConfigurationException ex) {
            plugin.logToConsole("WARN: Cannot load entity groups file (entity_groups.yml).");
            plugin.getPluginLoader().disablePlugin(plugin);
        }
        plugin.logToConsole("Config data loaded sucessfully.");
    }
    
    private Boolean isValidEntityType(String type) {
        try {
            EntityType etype = EntityType.valueOf(type.toUpperCase());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public HashMap<String, List<String>> getEntityGroups() {
        return entitygroups;
    }
    
    
    

    public boolean debugEnabled() {
        return config.getBoolean("debug", false);
    }

    @SuppressWarnings("null")
    public HashMap<Material, Pair<String,Integer>> getMobWardTypes() {
        if (config.isConfigurationSection("mobward.types")) {
            Set<String> types = config.getConfigurationSection("mobward.types").getKeys(false);
            if (!types.isEmpty()) {
                HashMap<Material,Pair<String,Integer>> newtype = new HashMap<>();
                types.forEach(type -> {
                    String blocktype = config.getString("mobward.types." + type + ".material", "is-invalid");
                    int radius = config.getInt("mobward.types." + type + ".radius", 1000000000);
                    if (radius != 1000000000 || !"is-invalid".equals(blocktype)) {
                        Material material = Material.getMaterial(blocktype);
                        if (material != null) {
                            newtype.put(material, new Pair(type.toUpperCase(),radius));
                        } else {
                            plugin.logToConsole("WARN: A MobWard type contains an invalid material in (config.yml). Skipping...");
                        }
                    } else {
                        plugin.logToConsole("WARN: A MobWard type contains invalid data in (config.yml). Skipping...");
                    }
                });
                if (!newtype.isEmpty()) {
                    return newtype;
                }
            }
        }
        plugin.logToConsole("WARN: No MobWard sizes listed in (config.yml). Loading defaults...");
        HashMap<Material,Pair<String,Integer>> defaults = new HashMap<>();
        defaults.put(Material.IRON_BLOCK, new Pair("MINI",25));
        defaults.put(Material.GOLD_BLOCK, new Pair("SMALL",50));
        defaults.put(Material.DIAMOND_BLOCK, new Pair("MEDIUM",75));
        defaults.put(Material.EMERALD_BLOCK, new Pair("LARGE",100));
        return defaults;
    }

    int getMobWardRadiusType() {
        if (config.contains("mobward.radius_type")) {
            String type = config.getString("mobward.radius_type");
            if (!"SPHERE".equals(type)) {
                return 1;
            }
        }
        return 0;
    }
}

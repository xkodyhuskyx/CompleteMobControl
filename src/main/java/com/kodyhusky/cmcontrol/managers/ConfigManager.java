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
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.configuration.ConfigurationSection;
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

    private final CompleteMobControl plugin;
    private FileConfiguration config;
    private FileConfiguration language;
    private final HashMap<String, List<String>> entitygroups = new HashMap<>();

    public ConfigManager(CompleteMobControl plugin) {
        this.plugin = plugin;
    }

    /**
     * Loads all plugin configuration data.
     */
    public void load() {
        File configfile = new File(plugin.getDataFolder(), "config.yml");
        if (!configfile.exists()) {
            plugin.logToConsole(Level.CONFIG, "Creating Default Configuration File (config.yml).", false);
            plugin.saveDefaultConfig();
        }
        config = plugin.getConfig();
        if (config == null) {
            plugin.logToConsole(Level.SEVERE, "Unable to Read Configuration File (config.yml)! Disabling Plugin!", true);
            return;
        }
        File egroupfile = new File(plugin.getDataFolder(), "entity-groups.yml");
        FileConfiguration egroupconfig = new YamlConfiguration();
        if (!egroupfile.exists()) {
            plugin.logToConsole(Level.CONFIG, "Creating Default Entity Groups File (entity_groups.yml).", false);
            plugin.saveResource("entity-groups.yml", false);
        }
        try {
            egroupconfig.load(egroupfile);
        } catch (IOException | InvalidConfigurationException ex) {
            plugin.logToConsole(Level.SEVERE, "Unable To Read Entity Groups File (entity_groups.yml)! Disabling Plugin!", false);
            plugin.logToConsole(Level.SEVERE, "ISSUE: " + ex.getLocalizedMessage(), true);
            return;
        }
        if (!egroupconfig.getKeys(false).isEmpty()) {
            egroupconfig.getKeys(false).forEach(group -> {
                if (group.contains(" ") || group.contains("-")) {
                    plugin.logToConsole(Level.WARNING, "Group Names May Not Contain Spaces Or Dashes! Found Group Name (" + group.toUpperCase() + ")! Skipping...", false);
                } else {
                    List<String> entities = new ArrayList<>();
                    if (!egroupconfig.getStringList(group).isEmpty()) {
                        egroupconfig.getStringList(group).forEach(entity -> {
                            try {
                                EntityType etype = EntityType.valueOf(entity.toUpperCase());
                                entities.add(entity.toUpperCase());
                            } catch (IllegalArgumentException | NullPointerException e) {
                                plugin.logToConsole(Level.WARNING, "Invalid Entity Type (" + entity.toUpperCase() + ") Found In Group (" + group.toUpperCase() + "! Skipping...", false);
                            }
                        });
                    } else {
                        plugin.logToConsole(Level.WARNING, "Entity Group (" + group.toUpperCase() + ") Is Empty! Possible Error?", false);
                    }
                    entitygroups.put(group.toUpperCase(), entities);
                }
            });
        } else {
            plugin.logToConsole(Level.WARNING, "Entity Groups File (entity_groups.yml) Is Empty! Possible Error?", false);
        }
    }

    /**
     * Gets if a feature is enabled.
     *
     * @param feature Feature
     * @return is enabled
     */
    public Boolean isFeatureEnabled(String feature) {
        return config.getBoolean("features." + feature, true);
    }

    /**
     * Get a list of all entity groups loaded from entity_groups.yml.
     *
     * @return list of all groups
     */
    public Set<String> getEntityGroups() {
        return entitygroups.keySet();
    }

    /**
     * Get a list of all entities in the specified group.
     *
     * @param group entity group
     * @return List - list of all entities in group
     */
    public List<String> getEntityGroupEntities(String group) {
        return entitygroups.get(group);
    }

    /**
     * Get if debug mode is enabled in the plugin configuration file.
     *
     * @return Boolean - debug enabled
     */
    public boolean debugEnabled() {
        return config.getBoolean("debug", false);
    }

    /**
     * Returns the MobWard types configuration section.
     *
     * @return ConfigurationSection - MobWard types data
     */
    public ConfigurationSection getMobWardTypesConfig() {
        if (config.isConfigurationSection("mobward.types")) {
            return config.getConfigurationSection("mobward.types");
        }
        return null;
    }

    /**
     * Returns the MobWard radius value defined in the plugin configuration.
     * <br><br><b>Possible Return Types</b>
     * <br>0 - CUBE
     * <br>1 - SPHERE (Default)
     *
     * @return radius type
     */
    public Integer getMobWardRadiusType() {
        if (config.contains("mobward.radius_type")) {
            String type = config.getString("mobward.radius_type");
            if (type != null && ("CUBE".equals(type.toUpperCase()) || "SPHERE".equals(type.toUpperCase()))) {
                if ("CUBE".equals(type.toUpperCase())) {
                    return 0;
                }
            } else {
                plugin.logToConsole(Level.WARNING, "MobWard Radius Type Is Invalid! Defaulting To SPHERE...", false);
            }
        }
        return 1;
    }
}

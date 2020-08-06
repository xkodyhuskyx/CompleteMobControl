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
package com.kodyhusky.cmcontrol;

import com.kodyhusky.cmcontrol.commands.MobWardCommand;
import com.kodyhusky.cmcontrol.listeners.EntitySpawnListener;
import com.kodyhusky.cmcontrol.managers.MobWardManager;
import com.kodyhusky.cmcontrol.managers.ConfigManager;
import com.kodyhusky.cmcontrol.managers.LanguageManager;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main starting point and control for all plugin functions.
 *
 * @author xkodyhuskyx
 */
public class CompleteMobControl extends JavaPlugin {

    private ConfigManager config;
    private MobWardManager wards;
    private LanguageManager language;

    @Override
    public void onEnable() {
        logToConsole(Level.INFO, "Starting CompleteMobControl Initialization!", false);

        // Write World File
        FileConfiguration worldconfig = new YamlConfiguration();
        getServer().getWorlds().forEach(world -> {
            String uuid = world.getUID().toString();
            worldconfig.set(uuid, world.getName());
        });
        try {
            worldconfig.save(new File(getDataFolder(), "worlds.yml"));
        } catch (IOException ex) {
        }

        config = new ConfigManager(this);
        config.load();
        
        language = new LanguageManager(this);
        language.load();
        
        if (config.isFeatureEnabled("mobwards")) {
            wards = new MobWardManager(this);
            wards.load();
            getServer().getPluginManager().registerEvents(new EntitySpawnListener(this), this);
            this.getCommand("mobward").setExecutor(new MobWardCommand(this));
        }
        logToConsole(Level.INFO, "CompleteMobControl Loaded Sucessfully!", false);
    }

    /**
     * Returns the loaded ConfigManager class.
     *
     * @return ConfigManager
     */
    public ConfigManager getPluginConfig() {
        return config;
    }
    
    /**
     * Returns the loaded LanguageManager class.
     *
     * @return LanguageManager
     */
    public LanguageManager getLanguage() {
        return language;
    }

    /**
     * Returns the loaded MobWardManager class.
     *
     * @return MobWardManager
     */
    public MobWardManager getWardManager() {
        return wards;
    }

    /**
     * Logs a message to the server console.
     * <br><br><b>Accepted Level Types:</b>
     * <br> SEVERE = Error Logging
     * <br> WARING = Warning Logging
     * <br> INFO = General Logging
     * <br> CONFIG = Initialization Logging
     * <br> ALL = Debug Logging
     *
     * @param level Log Level
     * @param message message to log
     * @param disable disable plugin
     */
    public void logToConsole(Level level, String message, Boolean disable) {
        switch (level.intValue()) {
            case 1000:
                getLogger().log(Level.SEVERE, "ERROR: {0}", message);
                break;
            case 900:
                getLogger().log(Level.WARNING, "WARN: {0}", message);
                break;
            case 700:
                getLogger().log(Level.WARNING, "INIT: {0}", message);
                break;
            case Integer.MIN_VALUE:
                if (config.debugEnabled()) {
                    getLogger().log(Level.INFO, "DEBUG: {0}", message);
                }
                break;
            default:
                getLogger().info(message);
        }
        if (disable) {
            getServer().getPluginManager().disablePlugin(this);
        }
    }

}

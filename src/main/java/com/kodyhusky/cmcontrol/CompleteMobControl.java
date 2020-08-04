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

import com.kodyhusky.cmcontrol.listeners.EntitySpawnListener;
import com.kodyhusky.cmcontrol.managers.MobWardManager;
import com.kodyhusky.cmcontrol.managers.ConfigManager;
import java.util.logging.Level;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main starting point and control for all plugin functions.
 * 
 * @author xkodyhuskyx
 */
public class CompleteMobControl extends JavaPlugin {

    private ConfigManager config;
    private MobWardManager wards;

    @Override
    public void onEnable() {
        logToConsole("Started CompleteMobControl Initialization!");
        config = new ConfigManager(this);
        wards = new MobWardManager(this);
        
        wards.load();
        
        getServer().getPluginManager().registerEvents(new EntitySpawnListener(this), this);
        
        logToConsole("CompleteMobControl Loaded Sucessfully!");
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
     * Returns the loaded MobWardManager class.
     * 
     * @return MobWardManager
     */
    public MobWardManager getWardManager() {
        return wards;
    }

    /**
     * Sends a message to the server console.
     * 
     * @param message Message to send to the console
     * @param debug Add debug prefix to message (debug mode must be enabled)
     */
    public void logToConsole(String message, Boolean debug) {
        if (debug && config.debugEnabled()) {
            getLogger().log(Level.INFO, "DEBUG >> {0}", message);
        } else {
            getLogger().info(message);
        }
    }

    /**
     * Sends a message to the server console.
     * 
     * @param message Message to send to the console
     */
    public void logToConsole(String message) {
        getLogger().info(message);
    }

}

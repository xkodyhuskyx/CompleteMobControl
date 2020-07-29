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
        
        wards.addDebugMobWard();
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

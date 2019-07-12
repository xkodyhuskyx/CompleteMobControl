package com.kodyhusky.cmc;

import java.util.logging.Level;
import org.bukkit.plugin.java.JavaPlugin;
import com.kodyhusky.cmc.listeners.EntitySpawnListener;

public class CompleteMobControl extends JavaPlugin {
    
    private ConfigManager config;
    private LanguageManager language;
    
    @Override
    public void onLoad() {
        config = new ConfigManager(this);
        language = new LanguageManager(this);
    }
    
    @Override
    public void onEnable() {
        if (config.isLoaded() && language.isLoaded()) {
            
            
            // NEED COMPLETED
        	getServer().getPluginManager().registerEvents(new EntitySpawnListener(this), this);
            
            
        } else {
            log(Level.SEVERE, "An error occured during while loading the plugin configuration. Plugin will be disabled.", null);
            getServer().getPluginManager().disablePlugin(this);
        }
    }
    
    public void debug(String message) {
        if (config.isDebugEnabled()) {
            getLogger().log(Level.FINE, "DEBUG: {0}", message);
        }
    }
    
    /**
     * Gets the initialized LanguageReader class.
     * 
     * @return LanguageManager
     */
    public LanguageManager getLanguageManager() {
        return language;
    }
    
    /**
     * Returns the initialized ConfigReader class.
     * 
     * @return ConfigManager
     */
    public ConfigManager getConfigManager() {
        return config;
    }
    
    /**
     * Sends a message to the server console.
     * <br><br>
     * Level Types Accepted:<br>
     * SEVERE = Error Reporting<br>
     * WARNING = General Warning<br>
     * INFO = Normal Logging<br>
     * FINE = Debugging
     * 
     * @param level type of message
     * @param message message to log
     * @param ex exception to report (optional/null)
     */
    public void log(Level level, String message, Exception ex) {
        String hr = "-------------------------------------------";
        // Error Logging
        if (level == Level.SEVERE) {
            getLogger().severe(">----------------- ERROR -----------------<");
            getLogger().log(Level.SEVERE, " {0}", message);
            getLogger().severe(hr);
        }
        // Warning Logging
        if (level == Level.WARNING) {
            getLogger().warning(">---------------- WARNING ----------------<");
            getLogger().log(Level.WARNING, " {0}", message);
            getLogger().warning(hr);
        }
        // General Logging
        if (level == Level.INFO) {
            getLogger().log(Level.INFO, " {0}", message);
        }
        // Debug Logging
        if (level == Level.FINE) {
            getLogger().log(Level.INFO, "DEBUG >> {0}", message);
        }
        // Exception Logging
        if (ex != null) {
            getLogger().log(level, "JAVA: {0}", ex.getMessage());
            getLogger().log(level, hr);
        }
    }
    
}

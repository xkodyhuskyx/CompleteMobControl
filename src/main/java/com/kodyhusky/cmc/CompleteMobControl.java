package com.kodyhusky.cmc;

import java.util.logging.Level;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CompleteMobControl extends JavaPlugin {
    
    private ConfigManager config;
    private LanguageManager language;
    
    @Override
    public void onLoad() {
        config = new ConfigManager(this);
        language = new LanguageManager(this, config.getLangName());
        
    }
    
    @Override
    public void onEnable() {
        if (config.isLoaded()) {
            
        } else {
            log(Level.SEVERE, "An error occured on load. Plugin will be disabled.", null);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] array) {
        return false;
    }
    
    public void debug(String message) {
        if (config.debugEnabled()) {
            getLogger().log(Level.FINE, "DEBUG: {0}", message);
        }
    }
    
    /**
     * Gets the initialized LanguageReader class.
     * 
     * @return LanguageReader
     */
    public LanguageManager getLangManager() {
        return language;
    }
    
    /**
     * Gets the initialized ConfigReader class.
     * 
     * @return ConfigReader
     */
    public ConfigManager getConfigManager() {
        return config;
    }
    
    /**
     * Logs a message to the server console.
     * 
     * @param level type of message
     * @param message message to log
     * @param ex exception to report (optional)
     */
    public void log(Level level, String message, Exception ex) {
        String hr = "-------------------------------------------";
        switch(level.intValue()) {
            case 1000:
                getLogger().severe(">----------------- ERROR -----------------<");
                getLogger().log(Level.SEVERE, " {0}", message);
                getLogger().severe(hr);
                if (ex != null) {
                    getLogger().log(Level.SEVERE, "JAVA: {0}", ex.getMessage());
                    getLogger().severe(hr);
                }
                break;
            case 900:
                getLogger().warning(">---------------- WARNING ----------------<");
                getLogger().log(Level.WARNING, " {0}", message);
                getLogger().warning(hr);
                if (ex != null) {
                    getLogger().log(Level.WARNING, "JAVA: {0}", ex.getMessage());
                    getLogger().warning(hr);
                }
                break;
            default:
                getLogger().info(message);
                break;
        }
    }
    
    @Override
    public void onDisable() {
        
    }
    
}

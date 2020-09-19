package com.kodyhusky.cmcontrol.util;

import com.kodyhusky.cmcontrol.CompleteMobControl;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

/**
 * Provides various configuration related functions.
 *
 * @author Jeffery Hancock
 * @version 1.0.0
 */
public class ConfigManager {

    private static final FileConfiguration configCurrent = new YamlConfiguration();
    private static final Logger logger;

    static {
        logger = CompleteMobControl.getPlugin(CompleteMobControl.class).getLogger();
        if (!(new File(CompleteMobControl.getPlugin(CompleteMobControl.class).getDataFolder(), "config.yml")).exists()) {
            CompleteMobControl.getPlugin(CompleteMobControl.class).saveDefaultConfig();
            logger.info(L10NManager.getString("config.createnew", false));
        }
    }

    private ConfigManager() {
    }

    /**
     * Loads the plugin configuration data from disk.
     *
     * @return reload successful
     */
    public static boolean load() {
        return reload();
    }

    /**
     * Reloads the plugin configuration data from disk.
     *
     * @return reload successful
     */
    public static boolean reload() {
        FileConfiguration configDefaults = new YamlConfiguration();
        FileConfiguration configCurrents = new YamlConfiguration();
        InputStream is = ConfigManager.class.getResourceAsStream("/config.yml");
        if (is != null) {
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            configDefaults = YamlConfiguration.loadConfiguration(isr);
            try {
                isr.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            configCurrents = CompleteMobControl.getPlugin(CompleteMobControl.class).getConfig();
            if (configCurrents.contains("version")) {
                if (configCurrents.getInt("version") != CompleteMobControl.getPlugin(CompleteMobControl.class)
                        .getSupportedConfigVersion()) {
                    logger.warning(L10NManager.getString("config.unsupported",
                            "version:" + CompleteMobControl.getPlugin(CompleteMobControl.class).getSupportedConfigVersion(), false));
                }
                configCurrent.addDefaults(configDefaults);
                configCurrent.addDefaults(configCurrents);
                logger.info(L10NManager.getString("config.loaded", "version:" + configCurrents.getInt("version"), false));
                return true;
            }
        }
        return false;
    }

    /**
     * Get the currently loaded plugin configuration data.
     *
     * @return configuration data
     */
    public static FileConfiguration getConfig() {
        return configCurrent;
    }
}

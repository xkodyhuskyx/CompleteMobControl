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

import com.kodyhusky.cmcontrol.commands.CommandMobWard;
import com.kodyhusky.cmcontrol.listeners.EntitySpawnListener;
import com.kodyhusky.cmcontrol.managers.MobWardManager;
import com.kodyhusky.cmcontrol.util.L10NManager;
import com.kodyhusky.cmcontrol.util.ConfigManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Level;

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
        if (L10NManager.load()) {

        }


        /* Worlds?
        FileConfiguration world5config = new YamlConfiguration();
        getServer().getWorlds().forEach(world -> {
            worldconfig.set(world.getUID().toString(), world.getName());
        });
        try {
            worldconfig.save(new File(getDataFolder(), "worlduuids.yml"));
        } catch (IOException ex) {}
         */

        List<String> features = ConfigManager.getConfig().getStringList("features");
        if (!(features.isEmpty())) {
            if (features.contains("MOBWARDS")) {

            }

        } else {
            getLogger().warning(L10NManager.getString("features.alldisabled", false));
        }


        
        if (config.isFeatureEnabled("mobwards")) {
            wards = new MobWardManager(this);
            wards.load();
            getServer().getPluginManager().registerEvents(new EntitySpawnListener(this), this);
            PluginCommand mwcmd = getCommand("mobward");
            if (mwcmd != null) {mwcmd.setExecutor(new CommandMobWard(this));}
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
     * Get the currently supported plugin configuration version.
     *
     * @return int version
     */
    public int getSupportedConfigVersion() {
        return 1;
    }
}

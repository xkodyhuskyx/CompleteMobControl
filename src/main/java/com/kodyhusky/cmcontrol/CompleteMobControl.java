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
import com.kodyhusky.cmcontrol.util.L10N;
import com.kodyhusky.cmcontrol.util.ConfigHandler;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main starting point and control for all plugin functions.
 *
 * @author xkodyhuskyx
 */
public class CompleteMobControl extends JavaPlugin {

    MobWardManager wards = null;

    @Override
    public void onEnable() {
        if (L10N.load()) {
            int fc = 0;
            if (ConfigHandler.getConfig().getBoolean("features.mobward")) {
                wards = new MobWardManager(this);
                wards.load();
                getServer().getPluginManager().registerEvents(new EntitySpawnListener(this), this);
                PluginCommand mwcmd = getCommand("mobward");
                if (mwcmd != null) mwcmd.setExecutor(new CommandMobWard(this));
                fc++;
            }
            if (fc == 0) {
                getLogger().warning(L10N.getString("features.alldisabled", false));
            }
            getLogger().warning(L10N.getString("plugin.loadsuccessful", false));
        }
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

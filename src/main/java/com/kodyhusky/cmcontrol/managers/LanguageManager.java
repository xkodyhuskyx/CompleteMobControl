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
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author xkodyhuskyx
 */
public class LanguageManager {

    private final CompleteMobControl plugin;
    private FileConfiguration language;

    public LanguageManager(CompleteMobControl plugin) {
        this.plugin = plugin;
    }

    public void load() {
        plugin.logToConsole(Level.CONFIG, "Reading Language File...", false);
        File langfile = new File(plugin.getDataFolder(), "language.yml");
        language = new YamlConfiguration();
        if (!langfile.exists()) {
            plugin.logToConsole(Level.INFO, "--> Creating Default Language File (language.yml).", false);
            plugin.saveResource("language.yml", false);
        }
        try {
            language.load(langfile);
        } catch (IOException | InvalidConfigurationException ex) {
            plugin.logToConsole(Level.SEVERE, "Unable To Read Language File (language.yml)! Disabling Plugin!", true);
            plugin.logToConsole(Level.SEVERE, "ISSUE: " + ex.getLocalizedMessage(), true);
        }
    }

    /**
     * Gets the unformatted language value for the given type. Returns default
     * value if not defined in language file.
     *
     * @param type string type
     * @return unformatted language string
     */
    public String getString(String type) {
        return language.getString(type, getDefault(type));
    }

    /**
     * Gets the unformatted language values for the given type. Returns default
     * values if not defined in language file.
     *
     * @param type string type
     * @return unformatted language strings
     */
    public String[] getMiltiString(String type) {
        return language.getString(type, getDefault(type)).split("\n");
    }

    /**
     * Gets the formatted language value for the given type. Returns default
     * value if not defined in language file.
     *
     * @param type string type
     * @return formatted language string
     */
    public String getFormattedString(String type) {
        return ChatColor.translateAlternateColorCodes('&', language.getString(type, getDefault(type)));
    }

    /**
     * Gets the formatted language values for the given type. Returns default
     * values if not defined in language file.
     *
     * @param type string type
     * @return formatted language strings
     */
    public String[] getFormattedMiltiString(String type) {
        return (ChatColor.translateAlternateColorCodes('&', language.getString(type, getDefault(type)))).split("\n");
    }

    /**
     * Returns the unformatted default language value for the given type. Will
     * log a message to the server console if no default value is found.
     *
     * @param type string type
     * @return unformatted default language string or type
     */
    private String getDefault(String type) {
        String defvalue = type;
        if ("mw-title".equals(type)) {
            defvalue = "Mob Ward";
        }
        if ("mw-active".equals(type)) {
            defvalue = "&aYES";
        }
        if ("mw-inactive".equals(type)) {
            defvalue = "&cNO";
        }
        if ("mw-list-title".equals(type)) {
            defvalue = "&7&l-----------------------------&r\n&a&l>>&r &6&m-&7&m]&b&m---&f [&e&l MOB WARD LIST &r&f] &b&m---&7&m]&6&m-&a&l <<&r\n&7&l-----------------------------&r";
        }
        if ("mw-list-footer".equals(type)) {
            defvalue = "&7&l-----------------------------&r";
        }
        if ("mw-list-value".equals(type)) {
            defvalue = "&fID &a{id} &7> &6L&7=&f[&b{LX}&f,&b{LY}&f,&b{LZ}&f] &6R&7=&b{R} &6A&7={A}&r";
        }
        if (defvalue.equals(type)) {
            plugin.logToConsole(Level.WARNING, "No Default Value For (" + type + ") Defined In LanguageManager Class! Please Create A GitHub Issue For This Warning!", true);
        }
        return defvalue;
    }

}

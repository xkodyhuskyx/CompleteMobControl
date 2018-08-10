package com.kodyhusky.cmc;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class LangMan {

    private CompleteMobControl plugin;
    private FileConfiguration config;
    private File file;

    public LangMan(CompleteMobControl plugin) {
        file = null;
        this.plugin = plugin;
        loadLangConfig();
    }

    public void loadLangConfig() {
        if (config == null) {
            file = new File(plugin.getDataFolder(), "lang.yml");
            if (!file.exists()) {
                plugin.saveResource("lang.yml", false);
            }
        }
        config = (FileConfiguration) YamlConfiguration.loadConfiguration(file);
    }

    public String get(String s) {
        return config.getString("language." + s);
    }
}

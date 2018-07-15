package net.highkingdom.cmc;

import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;

public final class LangMan
{
    private final CompleteMobControl plugin;
    private FileConfiguration config;
    private File file;
    
    public LangMan(final CompleteMobControl plugin) {
        this.file = null;
        this.plugin = plugin;
        this.loadLangConfig();
    }
    
    public void loadLangConfig() {
        if (this.config == null) {
            this.file = new File(this.plugin.getDataFolder(), "lang.yml");
            if (!this.file.exists()) {
                this.plugin.saveResource("lang.yml", false);
            }
        }
        this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.file);
    }
    
    public String get(final String s) {
        return this.config.getString("language." + s);
    }
}

package com.kodyhusky.cmcontrol.util;

import com.kodyhusky.cmcontrol.CompleteMobControl;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

/**
 * Provides various language related functions. Translations can be created by the community.
 *
 * @author Jeffery Hancock
 * @version 1.0.0
 */
public class L10N {

    private static YamlConfiguration langCurrent = null;
    private static YamlConfiguration langDefault = null;
    private static final Logger logger;

    static {
        logger = CompleteMobControl.getPlugin(CompleteMobControl.class).getLogger();
        try {
            Enumeration<JarEntry> jarFiles = (new JarFile(new File(
                    L10N.class.getProtectionDomain().getCodeSource().getLocation().toURI()))).entries();
            while (jarFiles.hasMoreElements()) {
                String candidate = jarFiles.nextElement().getName();
                if (candidate.startsWith("lang/")) {
                    candidate = candidate.replaceAll("lang/|\\.yml", "");
                    loadLanguage(candidate);
                }
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private L10N() {
    }

    /**
     * Loads the language configuration data from disk.
     *
     * @return reload successful
     */
    public static boolean load() {
        return reload();
    }

    /**
     * Reloads the language configuration data from disk.
     *
     * @return reload successful
     */
    public static boolean reload() {
        String langCode = CompleteMobControl.getPlugin(CompleteMobControl.class)
                .getConfig().getString("language", "en_US");
        assert langCode != null : "Language code is null!";
        langCurrent = loadLanguage(langCode);
        langDefault = loadLanguage("en_US");
        if (langCurrent == null && langDefault == null) {
            logger.severe("Unable to load any language data! Shutting down...");
            Bukkit.getPluginManager().disablePlugin(CompleteMobControl.getPlugin(CompleteMobControl.class));
            return false;
        }
        if (langCurrent == null) {
            langCurrent = langDefault;
            logger.warning("Unable to load language data (" + langCode + ")! Using Defaults...!");
        }
        if (langCode.equals("custom")) {
            logger.info("You are using a custom language file. Use with caution!");
            double percentage = langCurrent.getKeys(true).size() / (double) langDefault.getKeys(true).size();
            if (percentage < 1.0) {
                long pc = Math.round(percentage * 10_000);
                logger.info("Your language file is only " + (pc / 100) + "." + (pc % 100) +
                        "% complete. Missing strings will be loaded from the default language 'en_US'!");
            }
        }
        logger.info(getString("language-loaded", "langname:" + langCode, false));
        return true;
    }

    /**
     * Loads and returns the specified language configuration from disk.
     *
     * @param langCode Language code (ex. en_US)
     * @return YamlConfiguration if load is successful, otherwise null
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Nullable
    private static YamlConfiguration loadLanguage(@NotNull String langCode) {

        InputStream is = L10N.class.getResourceAsStream("/lang/" + langCode + ".yml");
        File langFile = new File(CompleteMobControl.getPlugin(CompleteMobControl.class)
                .getDataFolder(), "lang" + File.separator + langCode + ".yml");

        if (is != null) {
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            YamlConfiguration langDefaults = YamlConfiguration.loadConfiguration(isr);
            try {
                isr.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (!langFile.exists()) {
                    langFile.getParentFile().mkdirs();
                    langFile.createNewFile();
                }
                FileInputStream fis = new FileInputStream(langFile);
                isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                YamlConfiguration langConfig = YamlConfiguration.loadConfiguration(isr);
                langConfig.setDefaults(langDefaults);
                langConfig.options().copyDefaults(true);
                langConfig.save(langFile);
                isr.close();
                fis.close();
                return langConfig;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    /**
     * Gets a string from the language configuration file with arguments.<br/>
     * Note: Getting language data within args: lang:replacekey:path<br/>
     * Note: player:CONSOLE will be replaced with lang:player:console
     *
     * @param path  the Path at which the String is located
     * @param args  argument(s) to be replaced in string
     * @param color replace color codes with color values
     * @return String value from language file if exists, otherwise "" (empty string)
     */
    @NotNull
    public static String getString(@NotNull String path, @NotNull List<String> args, Boolean color) {
        String message = getLangString(path);
        if (!args.isEmpty()) {
            for (String arg : args) {
                String value = arg;
                if (arg.equalsIgnoreCase("player:CONSOLE")) {
                    value = "player:" + getLangString("console");
                }
                String[] values;
                if (arg.startsWith("lang:")) {
                    values = arg.split(":", 3);
                    message = message.replace("%" + values[1] + "%", getLangString(values[2]));
                } else {
                    values = value.split(":", 2);
                    message = message.replace("%" + values[0] + "%", values[1]);
                }
            }
        }
        return color ? ChatColor.translateAlternateColorCodes('&', message + "&r") : message;
    }

    /**
     * Gets a string from the language configuration file with arguments.<br/>
     * Note: Getting language data within args: lang:replacekey:path<br/>
     * Note: player:CONSOLE will be replaced with lang:player:console
     *
     * @param path  the Path at which the String is located
     * @param arg   argument to be replaced in string
     * @param color replace color codes with color values
     * @return String value from language file if exists, otherwise "" (empty string)
     */
    @NotNull
    public static String getString(@NotNull String path, @NotNull String arg, Boolean color) {
        return getString(path, Collections.singletonList(arg), color);
    }

    /**
     * Gets a string from the language configuration file.
     *
     * @param path  the Path at which the String is located
     * @param color replace color codes with color values
     * @return String value from language file if exists, otherwise "" (empty string)
     */
    @NotNull
    public static String getString(@NotNull String path, Boolean color) {
        return getString(path, new ArrayList<>(), color);
    }

    /**
     * Gets a string from the language configuration file.
     *
     * @param path the Path at which the String is located
     * @return String value from language file if exists, otherwise "" (empty string)
     */
    @NotNull
    private static String getLangString(@NotNull String path) {
        String value = langCurrent.getString(path, "String Not Defined");
        if (value == null || value.equals("")) {
            value = langDefault.getString(path, "String Not Defined");
        }
        return value != null ? value : "";
    }
}

package com.kodyhusky.cmc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class CompleteMobControl extends JavaPlugin {

    RepellerList repellers;
    RepellerBlockListener blockl;
    HashMap<String, Boolean> Toggler;
    ArrayList<World> worlds;
    PluginConfig config;
    Integer DLT;
    String pnames;
    String perm;
    CommandSender console;
    Boolean reload;
    PluginHelp help;
    BukkitTask rmode;
    LangMan lang;

    public CompleteMobControl() {
        Toggler = new HashMap<String, Boolean>();
        pnames = "CMC";
        perm = "completemc";
        reload = false;
        rmode = null;
    }

    public void onEnable() {
        console = getServer().getConsoleSender();
        DLT = getServer().getScheduler().scheduleSyncDelayedTask(this, new DelayLoadRunnable(this));
    }

    public void onDisable() {
        if (rmode != null) {
            rmode.cancel();
        }
        sM(console, "Thank you for using CompleteMobControl!", "norm");
        sM(console, "Plugin successfully disabled!", "norm");
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] array) {
        if (command.getName().equalsIgnoreCase("cmc")) {
            try {
                if (array[0].equalsIgnoreCase("reload") && array.length == 1) {
                    if (!commandSender.hasPermission(perm + ".reload")) {
                        sM(commandSender, "You do not have permission to perform this command!", "err");
                        return true;
                    }
                    config.reload();
                    rmode.cancel();
                    rmode = getServer().getScheduler().runTaskTimerAsynchronously(this, new EntityMoveListener(this), 0L, 80L);
                    sM(commandSender, "The plugin configuration has been successfully reloaded!", "norm");
                    return true;
                } else if (array[0].equalsIgnoreCase("version")) {
                    if (!commandSender.hasPermission(perm + ".reload")) {
                        sM(commandSender, "You do not have permission to perform this command!", "err");
                        return true;
                    }
                    sM(commandSender, "Currently running version " + getDescription().getVersion() + "!", "norm");
                    sM(commandSender, "http://dev.bukkit.org/server-mods/completemobcontrol/", "norm");
                    return true;
                } else {
                    if (array[0].equalsIgnoreCase("giants")) {
                        sM(commandSender, "Giant's Are not yet fully integrated in this version!", "norm");
                        sM(commandSender, "Please see: http://goo.gl/vljCK7", "norm");
                        return true;
                    }
                    if (array[0].equalsIgnoreCase("help")) {
                        if (!commandSender.hasPermission(perm + ".help")) {
                            sM(commandSender, "You do not have permission to perform this command!", "err");
                            return true;
                        }
                        try {
                            String s2 = array[1];
                            switch (s2) {
                            case "1": {
                                help.showPage(1, commandSender);
                                return true;
                            }
                            case "2": {
                                help.showPage(2, commandSender);
                                return true;
                            }
                            case "3": {
                                help.showPage(3, commandSender);
                                return true;
                            }
                            default: {
                                help.showPage(1, commandSender);
                                return true;
                            }
                            }
                        } catch (Exception ex) {
                            help.showPage(1, commandSender);
                            return true;
                        }
                    }
                    sM(commandSender, MessageFormat.format("{0}Please use {1}/cmc help 1{0} for command help!", ChatColor.GOLD, ChatColor.RED), "non");
                    return true;
                }
            } catch (Exception ex2) {
                sM(commandSender, MessageFormat.format("{0}Please use {1}/cmc help 1{0} for command help!", ChatColor.GOLD, ChatColor.RED), "non");
                return true;
            }
        }
        if (command.getName().equalsIgnoreCase("erepel")) {
            try {
                if (array[0].equalsIgnoreCase("create")) {
                    if ("CONSOLE".equals(commandSender.getName())) {
                        sM(commandSender, "This command can only be run in-game!", "err");
                        return true;
                    }
                    if (!commandSender.hasPermission(perm + ".erccreate")) {
                        sM(commandSender, "You do not have permission to perform this command!", "err");
                        return true;
                    }
                    try {
                        Material type = null;
                        String s3 = array[1];
                        switch (s3) {
                        case "small": {
                            type = config.getBlockType("small");
                            break;
                        }
                        case "medium": {
                            type = config.getBlockType("medium");
                            break;
                        }
                        case "large": {
                            type = config.getBlockType("large");
                            break;
                        }
                        case "extreme": {
                            type = config.getBlockType("extreme");
                            break;
                        }
                        }
                        if (type == null) {
                            sM(commandSender, "The repeller size you have entered is invalid!", "warn");
                            return true;
                        }
                        Player player = (Player) commandSender;
                        Location location = player.getLocation();
                        Block block = player.getWorld().getBlockAt(location);
                        Location location2 = player.getLocation();
                        location.setX(location.getX() + 1.0);
                        location.setZ(location.getZ() + 1.0);
                        player.teleport(location);
                        location2.getBlock().setType(type);
                        location2.setX(location2.getX() + 1.0);
                        location2.getBlock().setType(type);
                        location2.setX(location2.getX() - 2.0);
                        location2.getBlock().setType(type);
                        location2.setX(location2.getX() + 1.0);
                        location2.setZ(location2.getZ() + 1.0);
                        location2.getBlock().setType(type);
                        location2.setZ(location2.getZ() - 2.0);
                        location2.getBlock().setType(type);
                        location2.setZ(location2.getZ() + 1.0);
                        location2.setY(location2.getY() + 1.0);
                        location2.getBlock().setType(type);
                        location2.setY(location2.getY() + 1.0);
                        Block block2 = location2.getBlock();
                        block2.setType(type);
                        createRepeller(block2);
                        sM(commandSender, "You have created a " + array[1].toLowerCase() + " entity repeller!", "norm");
                        sM(console, "A " + array[1].toLowerCase() + " entity repeller was created by "
                                + commandSender.getName() + " at " + block.getX() + "," + block.getY() 
                                + "," + block.getZ() + ".", "warn");
                        return true;
                    } catch (Exception ex3) {
                        sM(commandSender, "Usage: /erepel create [size]", "err");
                        return true;
                    }
                }
                if (array[0].equalsIgnoreCase("list")) {
                    if (!commandSender.hasPermission(perm + ".erlist")) {
                        sM(commandSender, "You do not have permission to perform this command!", "err");
                        return true;
                    }
                    ArrayList<RepellerStructure> list = repellers.getList();
                    if (list.isEmpty()) {
                        sM(commandSender, "There are no active entity repellers!", "norm");
                    } else {
                        sM(commandSender, MessageFormat.format("{0}-- {1}Active Entity Repellers List {0}--",
                                ChatColor.YELLOW, ChatColor.GOLD), "non");
                        for (int i = 0; i < list.size(); ++i) {
                            RepellerStructure repellerStructure = list.get(i);
                            sM(commandSender, MessageFormat.format("{6}{0}: {7}{5} {8}- {7}{2},{3},{4} {8}- {7}{1}",
                                    i + 1, repellerStructure.getWorld().getName(), repellerStructure.getX(),
                                    repellerStructure.getY(), repellerStructure.getZ(),
                                    config.getStrength(repellerStructure.getWorld().getBlockAt(repellerStructure.getX(),
                                            repellerStructure.getY(), repellerStructure.getZ())).name().toLowerCase(),
                                    ChatColor.GREEN, ChatColor.GRAY, ChatColor.GOLD), "non");
                        }
                        sM(commandSender, MessageFormat.format("{0}End of entity repeller listing.", ChatColor.GOLD),
                                "non");
                    }
                    return true;
                } else if (array[0].equalsIgnoreCase("remove") && array.length == 1) {
                    if (!commandSender.hasPermission(perm + ".erremove")) {
                        sM(commandSender, "You do not have permission to perform this command!", "err");
                        return true;
                    }
                    Integer value;
                    try {
                        value = Integer.parseInt(array[1]);
                    } catch (NumberFormatException ex4) {
                        sM(commandSender, "The repeller number you have entered is invalid!", "err");
                        return true;
                    }
                    if (value <= 0 || value >= repellers.getList().size()) {
                        sM(commandSender, "The repeller number you have entered is invalid!", "err");
                        return true;
                    }
                    RepellerStructure repellerStructure2 = repellers.getList().get(value - 1);
                    String lowerCase = config
                            .getStrength(repellerStructure2.getWorld().getBlockAt(repellerStructure2.getX(),
                                    repellerStructure2.getY(), repellerStructure2.getZ()))
                            .name().toLowerCase();
                    try {
                        if (repellers.remove(value)) {
                            sM(commandSender, "You have sucessfully removed a " + lowerCase + " entity repeller!",
                                    "norm");
                            if (!"CONSOLE".equals(commandSender.getName())) {
                                sM(console,
                                        "A " + lowerCase + " entity repeller at " + repellerStructure2.getX() + ","
                                                + repellerStructure2.getY() + "," + repellerStructure2.getZ()
                                                + " was removed by " + commandSender.getName() + ".",
                                        "warn");
                            }
                            return true;
                        }
                    } catch (Exception ex5) {
                        sM(commandSender, "There was an error removing the specified entity repeller!", "err");
                        return true;
                    }
                    sM(commandSender, "There was an error removing the specified entity repeller!", "err");
                    return true;
                } else if (array[0].equalsIgnoreCase("removeall")) {
                    if (!commandSender.hasPermission(perm + ".erremoveall")) {
                        sM(commandSender, "You do not have permission to perform this command!", "err");
                        return true;
                    }
                    repellers.removeAll();
                    sM(commandSender, "You have sucessfully removed all entity repellers!", "norm");
                    if (!"CONSOLE".equals(commandSender.getName())) {
                        sM(console, "All entity repellers have been removed by " + commandSender.getName() + "!",
                                "warn");
                    }
                    return true;
                } else {
                    if (!array[0].equalsIgnoreCase("modify")) {
                        sM(commandSender, MessageFormat.format("{0}Please use {1}/cmc help 2{0} for command help!",
                                ChatColor.GOLD, ChatColor.RED), "non");
                        return true;
                    }
                    if (!commandSender.hasPermission(perm + ".ermodify")) {
                        sM(commandSender, "You do not have permission to perform this command!", "err");
                        return true;
                    }
                    try {
                        String s4 = null;
                        String s5 = array[1];
                        switch (s5) {
                        case "radius": {
                            s4 = "entity_repeller.radius.";
                            break;
                        }
                        case "material": {
                            s4 = "entity_repeller.material.";
                            break;
                        }
                        }
                        if (s4 == null) {
                            sM(commandSender, "The specified node could not be found!", "err");
                            return true;
                        }
                        if ("radius".equalsIgnoreCase(array[1]) || "material".equalsIgnoreCase(array[1])) {
                            String lowerCase2 = array[2].toLowerCase();
                            String s6 = null;
                            switch (lowerCase2) {
                            case "small": {
                                s6 = "small";
                                break;
                            }
                            case "medium": {
                                s6 = "medium";
                                break;
                            }
                            case "large": {
                                s6 = "large";
                                break;
                            }
                            case "extreme": {
                                s6 = "extreme";
                                break;
                            }
                            default: {
                                s6 = "null";
                                break;
                            }
                            }
                            if ("null".equals(s6)) {
                                sM(commandSender, "The specified size could not be found!", "err");
                            } else {
                                getConfig().set(s4 + array[2].toLowerCase(), array[3]);
                                saveConfig();
                                config.reload();
                                sM(commandSender, "Configuration value successfully changed!", "norm");
                            }
                            return true;
                        }
                    } catch (NumberFormatException ex6) {
                        sM(commandSender, "Usage: /erepel modify [material/radius] [size] [datavalue]", "err");
                        return true;
                    }
                    sM(commandSender, "Usage: /erepel modify [material/radius] [size] [datavalue]", "err");
                    return true;
                }
            } catch (Exception ex7) {
                sM(commandSender, MessageFormat.format("{0}Please use {1}/cmc help 2{0} for command help!",
                        ChatColor.GOLD, ChatColor.RED), "non");
                return true;
            }
        }
        if (command.getName().equalsIgnoreCase("ffield")) {
            try {
                if (!array[0].equalsIgnoreCase("toggle")) {
                    sM(commandSender, "An unspecified error has occurred!", "err");
                    return true;
                }
                if (!commandSender.hasPermission(perm + ".fftoggle")) {
                    sM(commandSender, "You do not have permission to perform this command!", "err");
                    return true;
                }
                if ("CONSOLE".equals(commandSender.getName())) {
                    sM(commandSender, "This command can not be run from the console!", "err");
                    return true;
                }
                if (Toggler.containsKey(commandSender.getName())) {
                    Toggler.remove(commandSender.getName());
                } else {
                    Toggler.put(commandSender.getName(), true);
                }
                sM(commandSender, MessageFormat.format("You have {0} forcefield building mode!",
                        Toggler.containsKey(commandSender.getName()) ? "entered" : "left"), "norm");
                return true;
            } catch (Exception ex8) {
                sM(commandSender, MessageFormat.format("{0}Please use {1}/cmc help 3{0} for command help!",
                        ChatColor.GOLD, ChatColor.RED), "non");
                return true;
            }
        }
        return false;
    }

    public void reloadRepellers() {
        worlds = (ArrayList<World>) getServer().getWorlds();
        repellers = new RepellerList("plugins/CompleteMobControl/", this);
    }

    public void loadPlugin() {
        getServer().getScheduler().cancelTask((int) DLT);
        (config = new PluginConfig(this)).checkConfig();
        lang = new LangMan(this);
        sM(console, "Loaded language file: " + getLang().get("lang-name"), "norm");
        getWorlds();
        repellers = new RepellerList("plugins/CompleteMobControl/", this);
        blockl = new RepellerBlockListener(this);
        getServer().getPluginManager().registerEvents(new EntitySpawnListener(this), this);
        getServer().getPluginManager().registerEvents(blockl, this);
        help = new PluginHelp(this);
        getServer().getPluginManager().registerEvents(new FFieldBlockListener(this), this);
        if (!config.getDevCode().contains("nolisten")) {
            rmode = getServer().getScheduler().runTaskTimerAsynchronously(this, new EntityMoveListener(this), 0L, 80L);
            getServer().getScheduler().runTaskTimerAsynchronously(this, new FFieldMoveListener(this), 0L, 50L);
        }
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(getLang().get("mob_rep_lore1"));
        lore.add(getLang().get("mob_rep_lore2"));
        lore.add(getLang().get("mob_rep_lore3"));
        Dye dye = new Dye();
        dye.setColor(DyeColor.RED);
        ItemStack itemStack = new ItemStack(Material.DEAD_BUSH);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(getLang().get("mob_rep"));
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(this, "mobrepellent"), itemStack);
        shapedRecipe.shape(new String[] { "rir", "bdb", "rir" });
        shapedRecipe.setIngredient('r', dye).setIngredient('i', Material.IRON_BLOCK).setIngredient('b', Material.BONE).setIngredient('d', Material.DEAD_BUSH);
        getServer().addRecipe(shapedRecipe);
        ItemStack itemStack2 = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta itemMeta2 = itemStack2.getItemMeta();
        itemMeta2.setDisplayName(getLang().get("mob_swo"));
        ArrayList<String> lore2 = new ArrayList<String>();
        lore2.add(getLang().get("mob_swo_lore1"));
        lore2.add(getLang().get("mob_swo_lore2"));
        lore2.add(getLang().get("mob_swo_lore3"));
        itemMeta2.setLore(lore2);
        itemStack2.setItemMeta(itemMeta2);
        ShapedRecipe shapedRecipe2 = new ShapedRecipe(new NamespacedKey(this, "repelling_sword"), itemStack2);
        shapedRecipe2.shape(new String[] { "rdr", "bdb", "rfr" });
        shapedRecipe2.setIngredient('r', dye).setIngredient('d', Material.DIAMOND_BLOCK).setIngredient('b', Material.BONE).setIngredient('f', Material.DEAD_BUSH);
        getServer().addRecipe(shapedRecipe2);
    }

    public RepellerList getCMClist() {
        return repellers;
    }

    public ArrayList<World> getWorlds() {
        return worlds = (ArrayList<World>) getServer().getWorlds();
    }

    public void createRepeller(Block block) {
        for (Block block2 : blockl.getAdjacentRepellerBlocks(block)) {
            if (!getCMClist().contains(block2)) {
                getCMClist().add(block2);
            }
        }
    }

    public static boolean isBaseOfRepeller(Block block, PluginConfig pluginConfig) {
        World world = block.getWorld();
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();
        return pluginConfig.getRadius(world.getBlockAt(x, y + 1, z)) != -1
                && pluginConfig.getRadius(world.getBlockAt(x, y + 2, z)) != -1
                && pluginConfig.getRadius(world.getBlockAt(x + 1, y, z)) != -1
                && pluginConfig.getRadius(world.getBlockAt(x - 1, y, z)) != -1
                && pluginConfig.getRadius(world.getBlockAt(x, y, z + 1)) != -1
                && pluginConfig.getRadius(world.getBlockAt(x, y, z - 1)) != -1;
    }

    public void sM(CommandSender commandSender, String format, String s) {
        if (!config.getDebugMode() && "deb".equals(s)) {
            return;
        }
        Object o = null;
        switch (s) {
        case "norm": {
            o = ChatColor.DARK_AQUA;
            break;
        }
        case "warn": {
            o = ChatColor.GOLD;
            break;
        }
        case "err": {
            o = ChatColor.RED;
            break;
        }
        case "deb": {
            o = ChatColor.LIGHT_PURPLE;
            break;
        }
        }
        String format2 = MessageFormat.format("{0}[{1}CMC{0}] ", ChatColor.DARK_GREEN, ChatColor.DARK_GRAY);
        if (!"non".equals(s)) {
            format = MessageFormat.format("{2}{0}{1}", o, format, ChatColor.WHITE);
        }
        if ("CONSOLE".equals(commandSender.getName())) {
            commandSender.sendMessage(format2 + format);
        } else {
            commandSender.sendMessage(format);
        }
    }

    public LangMan getLang() {
        return lang;
    }

    public static class Properties {

        public static String[] Load(String s) {
            if (Exist()) {
                ArrayList<String> list = new ArrayList<String>();
                try (BufferedReader bufferedReader = new BufferedReader(
                        new FileReader("plugins" + File.separator + "CompleteMobControl" + File.separator + s))) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        if (!line.startsWith("#") && !line.isEmpty()) {
                            list.add(line);
                        }
                    }
                } catch (IOException ex) {
                }
                return list.toArray(new String[list.size()]);
            }
            return null;
        }

        public static void Save(String s, String[] array) {
            if (Exist()) {
                try (BufferedWriter bufferedWriter = new BufferedWriter(
                        new FileWriter("plugins" + File.separator + "CompleteMobControl" + File.separator + s))) {
                    for (int length = array.length, i = 0; i < length; ++i) {
                        bufferedWriter.write(array[i] + System.getProperty("line.separator"));
                    }
                } catch (IOException ex) {
                }
            } else {
                File file = new File("plugins" + File.separator + "CompleteMobControl");
                File file2 = new File("plugins" + File.separator + "CompleteMobControl" + File.separator + s);
                try {
                    file.mkdir();
                    file2.createNewFile();
                    try (BufferedWriter bufferedWriter2 = new BufferedWriter(
                            new FileWriter("plugins" + File.separator + "CompleteMobControl" + File.separator + s))) {
                        for (int length2 = array.length, j = 0; j < length2; ++j) {
                            bufferedWriter2.write(array[j] + System.getProperty("line.separator"));
                        }
                    }
                } catch (IOException ex2) {
                }
            }
        }

        public static boolean Exist() {
            File file = new File("plugins" + File.separator + "CompleteMobControl");
            return file.exists() && file.isDirectory();
        }

        public static boolean Exist(String s) {
            File file = new File("plugins" + File.separator + "CompleteMobControl" + File.separator + s);
            return file.exists() && !file.isDirectory();
        }
    }
}

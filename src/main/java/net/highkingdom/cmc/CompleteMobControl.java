package net.highkingdom.cmc;

import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.Iterator;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.Recipe;
import org.bukkit.material.MaterialData;
import org.bukkit.inventory.ShapedRecipe;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;
import java.io.IOException;
import org.bukkit.event.Listener;
import org.bukkit.block.Block;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import java.text.MessageFormat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.plugin.Plugin;
import org.bukkit.command.CommandSender;
import org.bukkit.World;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.plugin.java.JavaPlugin;

public class CompleteMobControl extends JavaPlugin
{
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
    Integer rmode;
    Metrics metrics;
    LangMan lang;
    
    public CompleteMobControl() {
        this.Toggler = new HashMap<String, Boolean>();
        this.pnames = "CMC";
        this.perm = "completemc";
        this.reload = false;
        this.rmode = null;
    }
    
    public void onEnable() {
        this.console = (CommandSender)this.getServer().getConsoleSender();
        this.DLT = this.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)this, (Runnable)new DelayLoadRunnable(this));
    }
    
    public void onDisable() {
        this.getServer().getScheduler().cancelTask((int)this.rmode);
        this.sM(this.console, "Thank you for using CompleteMobControl!", "norm");
        this.sM(this.console, "Plugin successfully disabled!", "norm");
    }
    
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (command.getName().equalsIgnoreCase("cmc")) {
            try {
                if (array[0].equalsIgnoreCase("reload") && array.length == 1) {
                    if (!commandSender.hasPermission(this.perm + ".reload") && !this.isDev(commandSender)) {
                        this.sM(commandSender, "You do not have permission to perform this command!", "err");
                        return true;
                    }
                    this.config.reload();
                    this.getServer().getScheduler().cancelTask((int)this.rmode);
                    this.rmode = this.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this, (Runnable)new EntityMoveListener(this), 0L, 50L);
                    this.sM(commandSender, "The plugin configuration has been successfully reloaded!", "norm");
                    return true;
                }
                else if (array[0].equalsIgnoreCase("version")) {
                    if (!commandSender.hasPermission(this.perm + ".reload") && !this.isDev(commandSender)) {
                        this.sM(commandSender, "You do not have permission to perform this command!", "err");
                        return true;
                    }
                    this.sM(commandSender, "Currently running version " + this.getDescription().getVersion() + "!", "norm");
                    this.sM(commandSender, "http://dev.bukkit.org/server-mods/completemobcontrol/", "norm");
                    return true;
                }
                else {
                    if (array[0].equalsIgnoreCase("giants")) {
                        this.sM(commandSender, "Giant's Are not yet fully integrated in this version!", "norm");
                        this.sM(commandSender, "Please see: http://goo.gl/vljCK7", "norm");
                        return true;
                    }
                    if (array[0].equalsIgnoreCase("help")) {
                        if (!commandSender.hasPermission(this.perm + ".help") && !this.isDev(commandSender)) {
                            this.sM(commandSender, "You do not have permission to perform this command!", "err");
                            return true;
                        }
                        try {
                            final String s2 = array[1];
                            switch (s2) {
                                case "1": {
                                    this.help.showPage(1, commandSender);
                                    return true;
                                }
                                case "2": {
                                    this.help.showPage(2, commandSender);
                                    return true;
                                }
                                case "3": {
                                    this.help.showPage(3, commandSender);
                                    return true;
                                }
                                default: {
                                    this.help.showPage(1, commandSender);
                                    return true;
                                }
                            }
                        }
                        catch (Exception ex) {
                            this.help.showPage(1, commandSender);
                            return true;
                        }
                    }
                    this.sM(commandSender, MessageFormat.format("{0}Please use {1}/cmc help 1{0} for command help!", ChatColor.GOLD, ChatColor.RED), "non");
                    return true;
                }
            }
            catch (Exception ex2) {
                this.sM(commandSender, MessageFormat.format("{0}Please use {1}/cmc help 1{0} for command help!", ChatColor.GOLD, ChatColor.RED), "non");
                return true;
            }
        }
        if (command.getName().equalsIgnoreCase("erepel")) {
            try {
                if (array[0].equalsIgnoreCase("create")) {
                    if ("CONSOLE".equals(commandSender.getName())) {
                        this.sM(commandSender, "This command can only be run in-game!", "err");
                        return true;
                    }
                    if (!commandSender.hasPermission(this.perm + ".erccreate") && !this.isDev(commandSender)) {
                        this.sM(commandSender, "You do not have permission to perform this command!", "err");
                        return true;
                    }
                    try {
                        Material type = null;
                        final String s3 = array[1];
                        switch (s3) {
                            case "small": {
                                type = this.config.getBlockType("small");
                                break;
                            }
                            case "medium": {
                                type = this.config.getBlockType("medium");
                                break;
                            }
                            case "large": {
                                type = this.config.getBlockType("large");
                                break;
                            }
                            case "extreme": {
                                type = this.config.getBlockType("extreme");
                                break;
                            }
                        }
                        if (type == null) {
                            this.sM(commandSender, "The repeller size you have entered is invalid!", "warn");
                            return true;
                        }
                        final Player player = (Player)commandSender;
                        final Location location = player.getLocation();
                        final Block block = player.getWorld().getBlockAt(location);
                        final Location location2 = player.getLocation();
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
                        final Block block2 = location2.getBlock();
                        block2.setType(type);
                        this.createRepeller(block2);
                        this.sM(commandSender, "You have created a " + array[1].toLowerCase() + " entity repeller!", "norm");
                        this.sM(this.console, "A " + array[1].toLowerCase() + " entity repeller was created by " + commandSender.getName() + " at " + block.getX() + "," + block.getY() + "," + block.getZ() + ".", "warn");
                        return true;
                    }
                    catch (Exception ex3) {
                        this.sM(commandSender, "Usage: /erepel create [size]", "err");
                        return true;
                    }
                }
                if (array[0].equalsIgnoreCase("list")) {
                    if (!commandSender.hasPermission(this.perm + ".erlist") && !this.isDev(commandSender)) {
                        this.sM(commandSender, "You do not have permission to perform this command!", "err");
                        return true;
                    }
                    final ArrayList<RepellerStructure> list = this.repellers.getList();
                    if (list.isEmpty()) {
                        this.sM(commandSender, "There are no active entity repellers!", "norm");
                    }
                    else {
                        this.sM(commandSender, MessageFormat.format("{0}-- {1}Active Entity Repellers List {0}--", ChatColor.YELLOW, ChatColor.GOLD), "non");
                        for (int i = 0; i < list.size(); ++i) {
                            final RepellerStructure repellerStructure = list.get(i);
                            this.sM(commandSender, MessageFormat.format("{6}{0}: {7}{5} {8}- {7}{2},{3},{4} {8}- {7}{1}", i + 1, repellerStructure.getWorld().getName(), repellerStructure.getX(), repellerStructure.getY(), repellerStructure.getZ(), this.config.getStrength(repellerStructure.getWorld().getBlockAt(repellerStructure.getX(), repellerStructure.getY(), repellerStructure.getZ())).name().toLowerCase(), ChatColor.GREEN, ChatColor.GRAY, ChatColor.GOLD), "non");
                        }
                        this.sM(commandSender, MessageFormat.format("{0}End of entity repeller listing.", ChatColor.GOLD), "non");
                    }
                    return true;
                }
                else if (array[0].equalsIgnoreCase("remove") && array.length == 1) {
                    if (!commandSender.hasPermission(this.perm + ".erremove") && !this.isDev(commandSender)) {
                        this.sM(commandSender, "You do not have permission to perform this command!", "err");
                        return true;
                    }
                    Integer value;
                    try {
                        value = Integer.parseInt(array[1]);
                    }
                    catch (NumberFormatException ex4) {
                        this.sM(commandSender, "The repeller number you have entered is invalid!", "err");
                        return true;
                    }
                    if (value <= 0 || value >= this.repellers.getList().size()) {
                        this.sM(commandSender, "The repeller number you have entered is invalid!", "err");
                        return true;
                    }
                    final RepellerStructure repellerStructure2 = this.repellers.getList().get(value - 1);
                    final String lowerCase = this.config.getStrength(repellerStructure2.getWorld().getBlockAt(repellerStructure2.getX(), repellerStructure2.getY(), repellerStructure2.getZ())).name().toLowerCase();
                    try {
                        if (this.repellers.remove(value)) {
                            this.sM(commandSender, "You have sucessfully removed a " + lowerCase + " entity repeller!", "norm");
                            if (!"CONSOLE".equals(commandSender.getName())) {
                                this.sM(this.console, "A " + lowerCase + " entity repeller at " + repellerStructure2.getX() + "," + repellerStructure2.getY() + "," + repellerStructure2.getZ() + " was removed by " + commandSender.getName() + ".", "warn");
                            }
                            return true;
                        }
                    }
                    catch (Exception ex5) {
                        this.sM(commandSender, "There was an error removing the specified entity repeller!", "err");
                        return true;
                    }
                    this.sM(commandSender, "There was an error removing the specified entity repeller!", "err");
                    return true;
                }
                else if (array[0].equalsIgnoreCase("removeall")) {
                    if (!commandSender.hasPermission(this.perm + ".erremoveall") && !this.isDev(commandSender)) {
                        this.sM(commandSender, "You do not have permission to perform this command!", "err");
                        return true;
                    }
                    this.repellers.removeAll();
                    this.sM(commandSender, "You have sucessfully removed all entity repellers!", "norm");
                    if (!"CONSOLE".equals(commandSender.getName())) {
                        this.sM(this.console, "All entity repellers have been removed by " + commandSender.getName() + "!", "warn");
                    }
                    return true;
                }
                else {
                    if (!array[0].equalsIgnoreCase("modify")) {
                        this.sM(commandSender, MessageFormat.format("{0}Please use {1}/cmc help 2{0} for command help!", ChatColor.GOLD, ChatColor.RED), "non");
                        return true;
                    }
                    if (!commandSender.hasPermission(this.perm + ".ermodify") && !this.isDev(commandSender)) {
                        this.sM(commandSender, "You do not have permission to perform this command!", "err");
                        return true;
                    }
                    try {
                        String s4 = null;
                        final String s5 = array[1];
                        switch (s5) {
                            case "radius": {
                                s4 = "entity_repeller.radius.";
                                break;
                            }
                            case "blockid": {
                                s4 = "entity_repeller.blockid.";
                                break;
                            }
                        }
                        if (s4 == null) {
                            this.sM(commandSender, "The specified node could not be found!", "err");
                            return true;
                        }
                        if ("radius".equalsIgnoreCase(array[1]) || "blockid".equalsIgnoreCase(array[1])) {
                            final String lowerCase2 = array[2].toLowerCase();
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
                                this.sM(commandSender, "The specified size could not be found!", "err");
                            }
                            else {
                                this.getConfig().set(s4 + array[2].toLowerCase(), (Object)array[3]);
                                this.saveConfig();
                                this.config.reload();
                                this.sM(commandSender, "Configuration value successfully changed!", "norm");
                            }
                            return true;
                        }
                    }
                    catch (NumberFormatException ex6) {
                        this.sM(commandSender, "Usage: /erepel modify [blockid/radius] [size] [datavalue]", "err");
                        return true;
                    }
                    this.sM(commandSender, "Usage: /erepel modify [blockid/radius] [size] [datavalue]", "err");
                    return true;
                }
            }
            catch (Exception ex7) {
                this.sM(commandSender, MessageFormat.format("{0}Please use {1}/cmc help 2{0} for command help!", ChatColor.GOLD, ChatColor.RED), "non");
                return true;
            }
        }
        if (command.getName().equalsIgnoreCase("ffield")) {
            try {
                if (!array[0].equalsIgnoreCase("toggle")) {
                    this.sM(commandSender, "An unspecified error has occurred!", "err");
                    return true;
                }
                if (!commandSender.hasPermission(this.perm + ".fftoggle") && !this.isDev(commandSender)) {
                    this.sM(commandSender, "You do not have permission to perform this command!", "err");
                    return true;
                }
                if ("CONSOLE".equals(commandSender.getName())) {
                    this.sM(commandSender, "This command can not be run from the console!", "err");
                    return true;
                }
                if (this.Toggler.containsKey(commandSender.getName())) {
                    this.Toggler.remove(commandSender.getName());
                }
                else {
                    this.Toggler.put(commandSender.getName(), true);
                }
                this.sM(commandSender, MessageFormat.format("You have {0} forcefield building mode!", this.Toggler.containsKey(commandSender.getName()) ? "entered" : "left"), "norm");
                return true;
            }
            catch (Exception ex8) {
                this.sM(commandSender, MessageFormat.format("{0}Please use {1}/cmc help 3{0} for command help!", ChatColor.GOLD, ChatColor.RED), "non");
                return true;
            }
        }
        return false;
    }
    
    public void reloadRepellers() {
        this.worlds = (ArrayList<World>)this.getServer().getWorlds();
        this.repellers = new RepellerList("plugins/CompleteMobControl/", this);
    }
    
    public void loadPlugin() {
        this.getServer().getScheduler().cancelTask((int)this.DLT);
        (this.config = new PluginConfig(this)).checkConfig();
        this.lang = new LangMan(this);
        this.sM(this.console, "Loaded language file: " + this.getLang().get("lang-name"), "norm");
        this.getWorlds();
        this.repellers = new RepellerList("plugins/CompleteMobControl/", this);
        this.blockl = new RepellerBlockListener(this);
        this.getServer().getPluginManager().registerEvents((Listener)new EntitySpawnListener(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)this.blockl, (Plugin)this);
        this.help = new PluginHelp(this);
        this.rmode = this.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this, (Runnable)new EntityMoveListener(this), 0L, 75L);
        this.getServer().getPluginManager().registerEvents((Listener)new FFieldBlockListener(this), (Plugin)this);
        this.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this, (Runnable)new FFieldMoveListener(this), 0L, 50L);
        try {
            (this.metrics = new Metrics((Plugin)this)).start();
            this.sM(this.console, "Successfully integrated with Metrics!", "norm");
        }
        catch (IOException ex) {
            this.sM(this.console, "Error while loading plugin metrics!", "err");
            this.sM(this.console, ex.getLocalizedMessage(), "err");
        }
        final ArrayList<String> lore = new ArrayList<String>();
        lore.add(this.getLang().get("mob_rep_lore1"));
        lore.add(this.getLang().get("mob_rep_lore2"));
        lore.add(this.getLang().get("mob_rep_lore3"));
        final Dye dye = new Dye();
        dye.setData((byte)new Byte("1"));
        final ItemStack itemStack = new ItemStack(Material.DEAD_BUSH);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(this.getLang().get("mob_rep"));
        itemMeta.setLore((List)lore);
        itemStack.setItemMeta(itemMeta);
        final ShapedRecipe shapedRecipe = new ShapedRecipe(itemStack);
        shapedRecipe.shape(new String[] { "rir", "bdb", "rir" });
        shapedRecipe.setIngredient('r', (MaterialData)dye).setIngredient('i', Material.IRON_BLOCK).setIngredient('b', Material.BONE).setIngredient('d', Material.DEAD_BUSH);
        this.getServer().addRecipe((Recipe)shapedRecipe);
        final ItemStack itemStack2 = new ItemStack(Material.DIAMOND_SWORD);
        final ItemMeta itemMeta2 = itemStack2.getItemMeta();
        itemMeta2.setDisplayName(this.getLang().get("mob_swo"));
        final ArrayList<String> lore2 = new ArrayList<String>();
        lore2.add(this.getLang().get("mob_swo_lore1"));
        lore2.add(this.getLang().get("mob_swo_lore2"));
        lore2.add(this.getLang().get("mob_swo_lore3"));
        itemMeta2.setLore((List)lore2);
        itemStack2.setItemMeta(itemMeta2);
        final ShapedRecipe shapedRecipe2 = new ShapedRecipe(itemStack2);
        shapedRecipe2.shape(new String[] { "rdr", "bdb", "rfr" });
        shapedRecipe2.setIngredient('r', (MaterialData)dye).setIngredient('r', (MaterialData)dye).setIngredient('d', Material.DIAMOND_BLOCK).setIngredient('b', Material.BONE).setIngredient('f', Material.DEAD_BUSH);
        this.getServer().addRecipe((Recipe)shapedRecipe2);
    }
    
    public RepellerList getCMClist() {
        return this.repellers;
    }
    
    public ArrayList<World> getWorlds() {
        return this.worlds = (ArrayList<World>)this.getServer().getWorlds();
    }
    
    public void createRepeller(final Block block) {
        for (final Block block2 : this.blockl.getAdjacentRepellerBlocks(block)) {
            if (!this.getCMClist().contains(block2)) {
                this.getCMClist().add(block2);
            }
        }
    }
    
    public static boolean isBaseOfRepeller(final Block block, final PluginConfig pluginConfig) {
        final World world = block.getWorld();
        final int x = block.getX();
        final int y = block.getY();
        final int z = block.getZ();
        return pluginConfig.getRadius(world.getBlockAt(x, y + 1, z)) != -1 && pluginConfig.getRadius(world.getBlockAt(x, y + 2, z)) != -1 && pluginConfig.getRadius(world.getBlockAt(x + 1, y, z)) != -1 && pluginConfig.getRadius(world.getBlockAt(x - 1, y, z)) != -1 && pluginConfig.getRadius(world.getBlockAt(x, y, z + 1)) != -1 && pluginConfig.getRadius(world.getBlockAt(x, y, z - 1)) != -1;
    }
    
    public void sM(final CommandSender commandSender, String format, final String s) {
        if (!this.config.getDebugMode() && "deb".equals(s)) {
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
        final String format2 = MessageFormat.format("{0}[{1}CMC{0}] ", ChatColor.DARK_GREEN, ChatColor.DARK_GRAY);
        if (!"non".equals(s)) {
            format = MessageFormat.format("{2}{0}{1}", o, format, ChatColor.WHITE);
        }
        if ("CONSOLE".equals(commandSender.getName())) {
            commandSender.sendMessage(format2 + format);
        }
        else {
            commandSender.sendMessage(format);
        }
    }
    
    public boolean isDev(final CommandSender commandSender) {
        return this.getDescription().getAuthors().contains(commandSender.getName());
    }
    
    public LangMan getLang() {
        return this.lang;
    }
    
    public static class Properties
    {
        private static final String pluginName = "CompleteMobControl";
        private static final String pluginFolder = "plugins";
        
        public static String[] Load(final String s) {
            if (Exist()) {
                final ArrayList<String> list = new ArrayList<String>();
                try (final BufferedReader bufferedReader = new BufferedReader(new FileReader("plugins" + File.separator + "CompleteMobControl" + File.separator + s))) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        if (!line.startsWith("#") && !line.isEmpty()) {
                            list.add(line);
                        }
                    }
                }
                catch (IOException ex) {}
                return list.toArray(new String[list.size()]);
            }
            return null;
        }
        
        public static void Save(final String s, final String[] array) {
            if (Exist()) {
                try (final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("plugins" + File.separator + "CompleteMobControl" + File.separator + s))) {
                    for (int length = array.length, i = 0; i < length; ++i) {
                        bufferedWriter.write(array[i] + System.getProperty("line.separator"));
                    }
                }
                catch (IOException ex) {}
            }
            else {
                final File file = new File("plugins" + File.separator + "CompleteMobControl");
                final File file2 = new File("plugins" + File.separator + "CompleteMobControl" + File.separator + s);
                try {
                    file.mkdir();
                    file2.createNewFile();
                    try (final BufferedWriter bufferedWriter2 = new BufferedWriter(new FileWriter("plugins" + File.separator + "CompleteMobControl" + File.separator + s))) {
                        for (int length2 = array.length, j = 0; j < length2; ++j) {
                            bufferedWriter2.write(array[j] + System.getProperty("line.separator"));
                        }
                    }
                }
                catch (IOException ex2) {}
            }
        }
        
        public static boolean Exist() {
            final File file = new File("plugins" + File.separator + "CompleteMobControl");
            return file.exists() && file.isDirectory();
        }
        
        public static boolean Exist(final String s) {
            final File file = new File("plugins" + File.separator + "CompleteMobControl" + File.separator + s);
            return file.exists() && !file.isDirectory();
        }
    }
}

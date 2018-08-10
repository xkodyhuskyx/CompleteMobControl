package com.kodyhusky.cmc;

import java.io.File;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Wolf;

public class PluginConfig {

    CompleteMobControl plugin;
    FileConfiguration config;
    HashSet<EntityType> mobsToRepel;
    File cf;
    List<World> dworlds;

    public PluginConfig(CompleteMobControl plugin) {
        this.plugin = plugin;
        config = plugin.getConfig();
        load();
    }

    private void load() {
        mobsToRepel = new HashSet<EntityType>();
        for (String s : config.getStringList("entity_repeller.advanced.mobs_to_repel")) {
            EntityType fromName = EntityType.fromName(s);
            if (fromName != null) {
                mobsToRepel.add(fromName);
            } else {
                plugin.sM(plugin.console, "Unknown entity type " + s + " in mobs_to_repel.", "warn");
            }
        }
    }

    public void reload() {
        load();
        config = plugin.getConfig();
        plugin.reloadRepellers();
    }

    public void save() {
        plugin.saveConfig();
    }

    public boolean shouldRepelNeutralMobs() {
        return config.getBoolean("plugin.repel_neutral", false);
    }

    public boolean shouldRepelBelow() {
        return config.getBoolean("entity_repeller.advanced.check_below", true);
    }

    public int getFFid() {
        return config.getInt("force_field.blockid", 82);
    }

    public boolean getDebugMode() {
        return config.getBoolean("plugin.debug_mode", false);
    }

    public RepellerStrength getStrength(Block block) {
        return getStrength(block.getType());
    }

    public RepellerStrength getStrength(Material material) {
        if (material == getBlockType("small")) {
            return RepellerStrength.SMALL;
        }
        if (material == getBlockType("medium")) {
            return RepellerStrength.MEDIUM;
        }
        if (material == getBlockType("large")) {
            return RepellerStrength.LARGE;
        }
        if (material == getBlockType("extreme")) {
            return RepellerStrength.EXTREME;
        }
        return RepellerStrength.INVALID;
    }

    public int getRadius(RepellerStructure repellerStructure) {
        return getRadius(repellerStructure.getMaterial(), repellerStructure.getBlockData());
    }

    public int getRadius(Block block) {
        return getRadius(block.getType(), block.getData());
    }

    public int getRadius(Material material, int n) {
        if ((material == getBlockType("small") && getBlockDamage("small") == -1)
                || n == getBlockDamage("small")) {
            return config.getInt("entity_repeller.radius.small", 25);
        }
        if ((material == getBlockType("medium") && getBlockDamage("medium") == -1)
                || n == getBlockDamage("medium")) {
            return config.getInt("entity_repeller.radius.medium", 50);
        }
        if ((material == getBlockType("large") && getBlockDamage("large") == -1)
                || n == getBlockDamage("large")) {
            return config.getInt("entity_repeller.radius.large", 75);
        }
        if ((material == getBlockType("extreme") && getBlockDamage("extreme") == -1)
                || n == getBlockDamage("extreme")) {
            return config.getInt("entity_repeller.radius.extreme", 100);
        }
        return -1;
    }

    private int[] getItemType(String s, int n) {
        String[] split = config.getString(s, "").split("@");
        int[] array = { n, -1 };
        try {
            array[0] = Integer.parseInt(split[0]);
            if (split.length > 1) {
                array[1] = Integer.parseInt(split[1]);
            }
        } catch (NumberFormatException ex) {
            plugin.sM(plugin.console, "Error loading item type for " + s + ". Will default to " + n + ".",
                    "deb");
        }
        return array;
    }

    public Material getBlockType(String s) {
        int n2 = 0;
        switch (s) {
        case "small": {
            n2 = getItemType("entity_repeller.blockid.small", 42)[0];
            break;
        }
        case "medium": {
            n2 = getItemType("entity_repeller.blockid.medium", 41)[0];
            break;
        }
        case "large": {
            n2 = getItemType("entity_repeller.blockid.large", 57)[0];
            break;
        }
        case "extreme": {
            n2 = getItemType("entity_repeller.blockid.extreme", 133)[0];
            break;
        }
        default: {
            n2 = 57;
            break;
        }
        }
        Material material = Material.getMaterial(n2);
        switch (n2) {
        case 0:
        case 1:
        case 2:
        case 3:
        case 7:
        case 8:
        case 9:
        case 10:
        case 11:
        case 12:
        case 13:
        case 24:
        case 78:
        case 79:
        case 82:
        case 87:
        case 88:
        case 110:
        case 121: {
            material = Material.DIAMOND_BLOCK;
            break;
        }
        }
        if (material != null) {
            return material;
        }
        return Material.DIAMOND_BLOCK;
    }

    public int getBlockDamage(String s) {
        int n = -1;
        switch (s) {
        case "small": {
            n = getItemType("entity_repeller.blockid.small", 42)[1];
            break;
        }
        case "medium": {
            n = getItemType("entity_repeller.blockid.medium", 41)[1];
            break;
        }
        case "large": {
            n = getItemType("entity_repeller.blockid.large", 57)[1];
            break;
        }
        case "extreme": {
            n = getItemType("entity_repeller.blockid.extreme", 133)[1];
            break;
        }
        }
        return n;
    }

    public HashSet<EntityType> getMobsToRepel() {
        return mobsToRepel;
    }

    public void checkConfig() {
        if (!plugin.getConfig().contains("entity_repeller.radius.small")) {
            plugin.getConfig().set("entity_repeller.radius.small", (Object) 25);
        }
        if (!plugin.getConfig().contains("entity_repeller.radius.medium")) {
            plugin.getConfig().set("entity_repeller.radius.medium", (Object) 50);
        }
        if (!plugin.getConfig().contains("entity_repeller.radius.large")) {
            plugin.getConfig().set("entity_repeller.radius.large", (Object) 75);
        }
        if (!plugin.getConfig().contains("entity_repeller.radius.extreme")) {
            plugin.getConfig().set("entity_repeller.radius.extreme", (Object) 100);
        }
        if (!plugin.getConfig().contains("entity_repeller.blockid.small")) {
            plugin.getConfig().set("entity_repeller.blockid.small", (Object) 42);
        }
        if (!plugin.getConfig().contains("entity_repeller.blockid.medium")) {
            plugin.getConfig().set("entity_repeller.blockid.medium", (Object) 41);
        }
        if (!plugin.getConfig().contains("entity_repeller.blockid.large")) {
            plugin.getConfig().set("entity_repeller.blockid.large", (Object) 57);
        }
        if (!plugin.getConfig().contains("entity_repeller.blockid.extreme")) {
            plugin.getConfig().set("entity_repeller.blockid.extreme", (Object) 133);
        }
        if (!plugin.getConfig().contains("entity_repeller.advanced.ignore_below")) {
            plugin.getConfig().set("entity_repeller.advanced.check_below", (Object) true);
        }
        if (!plugin.getConfig().contains("entity_repeller.advanced.mobs_to_repel")) {
            plugin.getConfig().createSection("entity_repeller.advanced.mobs_to_repel");
        }
        if (!plugin.getConfig().contains("force_field.blockid")) {
            plugin.getConfig().set("force_field.blockid", (Object) 82);
        }
        if (!plugin.getConfig().contains("plugin.debug_mode")) {
            plugin.getConfig().set("plugin.debug_mode", (Object) false);
        }
        if (!plugin.getConfig().contains("plugin.repel_neutral")) {
            plugin.getConfig().set("plugin.repel_neutral", (Object) false);
        }
        plugin.saveConfig();
    }

    public boolean isNeutralEntity(Entity entity) {
        return entity.getType() == EntityType.COW || entity.getType() == EntityType.SHEEP
                || entity.getType() == EntityType.WOLF || entity.getType() == EntityType.VILLAGER
                || entity.getType() == EntityType.OCELOT || entity.getType() == EntityType.IRON_GOLEM
                || entity.getType() == EntityType.SQUID || entity.getType() == EntityType.PIG
                || entity.getType() == EntityType.SNOWMAN || entity.getType() == EntityType.MUSHROOM_COW
                || entity.getType() == EntityType.RABBIT || entity.getType() == EntityType.CHICKEN
                || entity.getType() == EntityType.HORSE;
    }

    public boolean isInvalidEntity(Entity entity) {
        return entity.getType() == EntityType.ARMOR_STAND || entity.getType() == EntityType.ARROW
                || entity.getType() == EntityType.DROPPED_ITEM || entity.getType() == EntityType.BOAT
                || entity.getType() == EntityType.EGG || entity.getType() == EntityType.ENDER_PEARL
                || entity.getType() == EntityType.EXPERIENCE_ORB || entity.getType() == EntityType.FALLING_BLOCK
                || entity.getType() == EntityType.FIREBALL || entity.getType() == EntityType.FIREWORK
                || entity.getType() == EntityType.LEASH_HITCH || entity.getType() == EntityType.LIGHTNING
                || entity.getType() == EntityType.MINECART || entity.getType() == EntityType.MINECART_CHEST
                || entity.getType() == EntityType.MINECART_COMMAND || entity.getType() == EntityType.MINECART_FURNACE
                || entity.getType() == EntityType.MINECART_HOPPER || entity.getType() == EntityType.MINECART_MOB_SPAWNER
                || entity.getType() == EntityType.MINECART_TNT || entity.getType() == EntityType.PAINTING
                || entity.getType() == EntityType.PLAYER || entity.getType() == EntityType.PRIMED_TNT
                || entity.getType() == EntityType.SMALL_FIREBALL || entity.getType() == EntityType.SPLASH_POTION
                || entity.getType() == EntityType.SNOWBALL || entity.getType() == EntityType.THROWN_EXP_BOTTLE
                || entity.getType() == EntityType.UNKNOWN || entity.getType() == EntityType.WEATHER
                || entity.getType() == EntityType.FISHING_HOOK || entity.getType() == EntityType.ITEM_FRAME;
    }

    public boolean isEntityTame(Entity entity) {
        return (entity.getType() == EntityType.WOLF && ((Wolf) entity).isTamed())
                || (entity.getType() == EntityType.OCELOT && ((Ocelot) entity).isTamed())
                || (entity.getType() == EntityType.HORSE && ((Horse) entity).isTamed());
    }
}

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
        for (String name : config.getStringList("entity_repeller.advanced.mobs_to_repel")) {
            try {
                EntityType type = EntityType.valueOf(name.toUpperCase());
                mobsToRepel.add(type);
            } catch (Exception e) {
                plugin.sM(plugin.console, "Unknown entity type " + name + " in mobs_to_repel.", "warn");
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
    
    // Untested 1.13 Replacement for int getFFid()
    public Material getFFId() {
        try {
            Material block = Material.valueOf(config.getString("force_field.material").toUpperCase());
            return block;
        } catch (Exception e) {
            return null;
        }
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
        return getRadius(repellerStructure.getMaterial());
    }

    public int getRadius(Block block) {
        return getRadius(block.getType());
    }

    public int getRadius(Material material) {
        if (material == getBlockType("small")) {
            return config.getInt("entity_repeller.radius.small", 25);
        }
        if (material == getBlockType("medium")) {
            return config.getInt("entity_repeller.radius.medium", 50);
        }
        if (material == getBlockType("large")) {
            return config.getInt("entity_repeller.radius.large", 75);
        }
        if (material == getBlockType("extreme")) {
            return config.getInt("entity_repeller.radius.extreme", 100);
        }
        return -1;
    }

    private Material getItemType(String path, Material defaultmat) {
        try {
            String name = config.getString("path");
            Material mat = Material.getMaterial(name.toUpperCase());
            return mat;
        } catch (Exception e) {
            plugin.sM(plugin.console, "Error loading item type for " + path + ". Will default to " + defaultmat.name() + ".", "deb");
            return defaultmat;
        }
    }

    public Material getBlockType(String name) {
        Material type;
        switch (name) {
            case "small": {
                type = getItemType("entity_repeller.material.small", Material.IRON_BLOCK);
                break;
            }
            case "medium": {
                type = getItemType("entity_repeller.material.medium", Material.GOLD_BLOCK);
                break;
            }
            case "large": {
                type = getItemType("entity_repeller.material.large", Material.DIAMOND_BLOCK);
                break;
            }
            case "extreme": {
                type = getItemType("entity_repeller.material.extreme", Material.EMERALD_BLOCK);
                break;
            }
            default: {
                type = Material.DIAMOND_BLOCK;
                break;
            }
        }
        return type;
    }

    // Entire Function Needs To Be Rewritten
    public Material getBlockDamage(String type) {
        Material mat = null;
        switch (type) {
            case "small": {
                mat = getItemType("entity_repeller.blockid.small", Material.IRON_BLOCK);
                break;
            }
            case "medium": {
                mat = getItemType("entity_repeller.blockid.medium", Material.GOLD_BLOCK);
                break;
            }
            case "large": {
                mat = getItemType("entity_repeller.blockid.large", Material.DIAMOND_BLOCK);
                break;
            }
            case "extreme": {
                mat = getItemType("entity_repeller.blockid.extreme", Material.EMERALD_BLOCK);
                break;
            }
        }
        return mat;
    }

    public HashSet<EntityType> getMobsToRepel() {
        return mobsToRepel;
    }

    public void checkConfig() {
        if (!plugin.getConfig().contains("entity_repeller.radius.small")) {
            plugin.getConfig().set("entity_repeller.radius.small", 25);
        }
        if (!plugin.getConfig().contains("entity_repeller.radius.medium")) {
            plugin.getConfig().set("entity_repeller.radius.medium", 50);
        }
        if (!plugin.getConfig().contains("entity_repeller.radius.large")) {
            plugin.getConfig().set("entity_repeller.radius.large", 75);
        }
        if (!plugin.getConfig().contains("entity_repeller.radius.extreme")) {
            plugin.getConfig().set("entity_repeller.radius.extreme", 100);
        }
        if (!plugin.getConfig().contains("entity_repeller.material.small")) {
            plugin.getConfig().set("entity_repeller.material.small", "IRON_BLOCK");
        }
        if (!plugin.getConfig().contains("entity_repeller.material.medium")) {
            plugin.getConfig().set("entity_repeller.material.medium", "GOLD_BLOCK");
        }
        if (!plugin.getConfig().contains("entity_repeller.material.large")) {
            plugin.getConfig().set("entity_repeller.material.large", "DIAMOND_BLOCK");
        }
        if (!plugin.getConfig().contains("entity_repeller.material.extreme")) {
            plugin.getConfig().set("entity_repeller.material.extreme", "EMERALD_BLOCK");
        }
        if (!plugin.getConfig().contains("entity_repeller.advanced.ignore_below")) {
            plugin.getConfig().set("entity_repeller.advanced.check_below", true);
        }
        if (!plugin.getConfig().contains("entity_repeller.advanced.mobs_to_repel")) {
            plugin.getConfig().createSection("entity_repeller.advanced.mobs_to_repel");
        }
        if (!plugin.getConfig().contains("force_field.material")) {
            plugin.getConfig().set("force_field.material", "CLAY");
        }
        if (!plugin.getConfig().contains("plugin.debug_mode")) {
            plugin.getConfig().set("plugin.debug_mode", false);
        }
        if (!plugin.getConfig().contains("plugin.repel_neutral")) {
            plugin.getConfig().set("plugin.repel_neutral", false);
        }
        plugin.saveConfig();
    }

    // Add 1.13 Neutral Entities
    public boolean isNeutralEntity(Entity entity) {
        return entity.getType() == EntityType.COW || entity.getType() == EntityType.SHEEP
                || entity.getType() == EntityType.WOLF || entity.getType() == EntityType.VILLAGER
                || entity.getType() == EntityType.OCELOT || entity.getType() == EntityType.IRON_GOLEM
                || entity.getType() == EntityType.SQUID || entity.getType() == EntityType.PIG
                || entity.getType() == EntityType.SNOWMAN || entity.getType() == EntityType.MUSHROOM_COW
                || entity.getType() == EntityType.RABBIT || entity.getType() == EntityType.CHICKEN
                || entity.getType() == EntityType.HORSE || entity.getType() == EntityType.DOLPHIN;
    }

    // Check For New 1.13 Invalid Entities
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

    // Check For New 1.13 Tamable Entities
    public boolean isEntityTame(Entity entity) {
        return (entity.getType() == EntityType.WOLF && ((Wolf) entity).isTamed())
                || (entity.getType() == EntityType.OCELOT && ((Ocelot) entity).isTamed())
                || (entity.getType() == EntityType.HORSE && ((Horse) entity).isTamed());
    }
}

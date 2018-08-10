package com.kodyhusky.cmc;

import java.io.File;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Mule;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.ZombieHorse;

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

    public String getDevCode() {
        if (config.contains("devcode")) {
            return config.getString("devcode");
        }
        return "";
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
            plugin.sM(plugin.console,
                    "Error loading item type for " + path + ". Will default to " + defaultmat.name() + ".", "deb");
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
        if (!plugin.getConfig().contains("entity.neutral")) {
            String[] entities = { "BAT", "CHICKEN", "COD", "COW", "DONKEY", "HORSE", "MUSHROOM_COW", "MULE", "OCELOT",
                    "PARROT", "PIG", "PUFFERFISH", "RABBIT", "SHEEP", "SKELETON_HORSE", "SALMON", "SQUID", "TURTLE",
                    "TROPICAL_FISH", "VILLAGER", "DOLPHIN", "IRON_GOLEM", "LLAMA", "POLAR_BEAR", "WOLF", "SNOWMAN",
                    "ZOMBIE_HORSE", "AGENT", "NPC" };
            plugin.getConfig().set("entity.neutral", entities);
        }
        plugin.saveConfig();
    }

    public boolean isNeutralEntity(Entity entity) {
        List<String> neutral = config.getStringList("entity.neutral");
        if (neutral.contains(entity.getName())) {
            return true;
        }
        return false;
    }

    // Check For New 1.13 Invalid Entities
    public boolean isInvalidEntity(Entity entity) {
        if (!(entity instanceof Creature)) {
            return true;
        }
        return false;
    }

    // Check For New 1.13 Tamable Entities
    public boolean isEntityTame(Entity entity) {
        return (entity.getType() == EntityType.DONKEY && ((Donkey) entity).isTamed()
                || entity.getType() == EntityType.HORSE && ((Horse) entity).isTamed()
                || entity.getType() == EntityType.LLAMA && ((Llama) entity).isTamed()
                || entity.getType() == EntityType.MULE && ((Mule) entity).isTamed()
                || entity.getType() == EntityType.OCELOT && ((Ocelot) entity).isTamed()
                || entity.getType() == EntityType.PARROT && ((Parrot) entity).isTamed()
                || entity.getType() == EntityType.SKELETON_HORSE && ((SkeletonHorse) entity).isTamed()
                || entity.getType() == EntityType.WOLF && ((Wolf) entity).isTamed()
                || entity.getType() == EntityType.ZOMBIE_HORSE && ((ZombieHorse) entity).isTamed());
    }
}

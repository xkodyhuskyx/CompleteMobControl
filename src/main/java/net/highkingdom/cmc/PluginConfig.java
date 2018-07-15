package net.highkingdom.cmc;

import org.bukkit.entity.Horse;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Entity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import java.util.Iterator;
import org.bukkit.World;
import java.util.List;
import java.io.File;
import org.bukkit.entity.EntityType;
import java.util.HashSet;
import org.bukkit.configuration.file.FileConfiguration;

public class PluginConfig
{
    CompleteMobControl plugin;
    FileConfiguration config;
    HashSet<EntityType> mobsToRepel;
    File cf;
    List<World> dworlds;
    
    public PluginConfig(final CompleteMobControl plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.load();
    }
    
    private void load() {
        this.mobsToRepel = new HashSet<EntityType>();
        for (final String s : this.config.getStringList("entity_repeller.advanced.mobs_to_repel")) {
            final EntityType fromName = EntityType.fromName(s);
            if (fromName != null) {
                this.mobsToRepel.add(fromName);
            }
            else {
                this.plugin.sM(this.plugin.console, "Unknown entity type " + s + " in mobs_to_repel.", "warn");
            }
        }
    }
    
    public void reload() {
        this.load();
        this.config = this.plugin.getConfig();
        this.plugin.reloadRepellers();
    }
    
    public void save() {
        this.plugin.saveConfig();
    }
    
    public boolean shouldRepelNeutralMobs() {
        return this.config.getBoolean("plugin.repel_neutral", false);
    }
    
    public boolean shouldRepelBelow() {
        return this.config.getBoolean("entity_repeller.advanced.check_below", true);
    }
    
    public int getFFid() {
        return this.config.getInt("force_field.blockid", 82);
    }
    
    public boolean getDebugMode() {
        return this.config.getBoolean("plugin.debug_mode", false);
    }
    
    public RepellerStrength getStrength(final Block block) {
        return this.getStrength(block.getType());
    }
    
    public RepellerStrength getStrength(final Material material) {
        if (material == this.getBlockType("small")) {
            return RepellerStrength.SMALL;
        }
        if (material == this.getBlockType("medium")) {
            return RepellerStrength.MEDIUM;
        }
        if (material == this.getBlockType("large")) {
            return RepellerStrength.LARGE;
        }
        if (material == this.getBlockType("extreme")) {
            return RepellerStrength.EXTREME;
        }
        return RepellerStrength.INVALID;
    }
    
    public int getRadius(final RepellerStructure repellerStructure) {
        return this.getRadius(repellerStructure.getMaterial(), repellerStructure.getBlockData());
    }
    
    public int getRadius(final Block block) {
        return this.getRadius(block.getType(), block.getData());
    }
    
    public int getRadius(final Material material, final int n) {
        if ((material == this.getBlockType("small") && this.getBlockDamage("small") == -1) || n == this.getBlockDamage("small")) {
            return this.config.getInt("entity_repeller.radius.small", 25);
        }
        if ((material == this.getBlockType("medium") && this.getBlockDamage("medium") == -1) || n == this.getBlockDamage("medium")) {
            return this.config.getInt("entity_repeller.radius.medium", 50);
        }
        if ((material == this.getBlockType("large") && this.getBlockDamage("large") == -1) || n == this.getBlockDamage("large")) {
            return this.config.getInt("entity_repeller.radius.large", 75);
        }
        if ((material == this.getBlockType("extreme") && this.getBlockDamage("extreme") == -1) || n == this.getBlockDamage("extreme")) {
            return this.config.getInt("entity_repeller.radius.extreme", 100);
        }
        return -1;
    }
    
    private int[] getItemType(final String s, final int n) {
        final String[] split = this.config.getString(s, "").split("@");
        final int[] array = { n, -1 };
        try {
            array[0] = Integer.parseInt(split[0]);
            if (split.length > 1) {
                array[1] = Integer.parseInt(split[1]);
            }
        }
        catch (NumberFormatException ex) {
            this.plugin.sM(this.plugin.console, "Error loading item type for " + s + ". Will default to " + n + ".", "deb");
        }
        return array;
    }
    
    public Material getBlockType(final String s) {
        int n2 = 0;
        switch (s) {
            case "small": {
                n2 = this.getItemType("entity_repeller.blockid.small", 42)[0];
                break;
            }
            case "medium": {
                n2 = this.getItemType("entity_repeller.blockid.medium", 41)[0];
                break;
            }
            case "large": {
                n2 = this.getItemType("entity_repeller.blockid.large", 57)[0];
                break;
            }
            case "extreme": {
                n2 = this.getItemType("entity_repeller.blockid.extreme", 133)[0];
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
    
    public int getBlockDamage(final String s) {
        int n = -1;
        switch (s) {
            case "small": {
                n = this.getItemType("entity_repeller.blockid.small", 42)[1];
                break;
            }
            case "medium": {
                n = this.getItemType("entity_repeller.blockid.medium", 41)[1];
                break;
            }
            case "large": {
                n = this.getItemType("entity_repeller.blockid.large", 57)[1];
                break;
            }
            case "extreme": {
                n = this.getItemType("entity_repeller.blockid.extreme", 133)[1];
                break;
            }
        }
        return n;
    }
    
    public HashSet<EntityType> getMobsToRepel() {
        return this.mobsToRepel;
    }
    
    public void checkConfig() {
        if (!this.plugin.getConfig().contains("entity_repeller.radius.small")) {
            this.plugin.getConfig().set("entity_repeller.radius.small", (Object)25);
        }
        if (!this.plugin.getConfig().contains("entity_repeller.radius.medium")) {
            this.plugin.getConfig().set("entity_repeller.radius.medium", (Object)50);
        }
        if (!this.plugin.getConfig().contains("entity_repeller.radius.large")) {
            this.plugin.getConfig().set("entity_repeller.radius.large", (Object)75);
        }
        if (!this.plugin.getConfig().contains("entity_repeller.radius.extreme")) {
            this.plugin.getConfig().set("entity_repeller.radius.extreme", (Object)100);
        }
        if (!this.plugin.getConfig().contains("entity_repeller.blockid.small")) {
            this.plugin.getConfig().set("entity_repeller.blockid.small", (Object)42);
        }
        if (!this.plugin.getConfig().contains("entity_repeller.blockid.medium")) {
            this.plugin.getConfig().set("entity_repeller.blockid.medium", (Object)41);
        }
        if (!this.plugin.getConfig().contains("entity_repeller.blockid.large")) {
            this.plugin.getConfig().set("entity_repeller.blockid.large", (Object)57);
        }
        if (!this.plugin.getConfig().contains("entity_repeller.blockid.extreme")) {
            this.plugin.getConfig().set("entity_repeller.blockid.extreme", (Object)133);
        }
        if (!this.plugin.getConfig().contains("entity_repeller.advanced.ignore_below")) {
            this.plugin.getConfig().set("entity_repeller.advanced.check_below", (Object)true);
        }
        if (!this.plugin.getConfig().contains("entity_repeller.advanced.mobs_to_repel")) {
            this.plugin.getConfig().createSection("entity_repeller.advanced.mobs_to_repel");
        }
        if (!this.plugin.getConfig().contains("force_field.blockid")) {
            this.plugin.getConfig().set("force_field.blockid", (Object)82);
        }
        if (!this.plugin.getConfig().contains("plugin.debug_mode")) {
            this.plugin.getConfig().set("plugin.debug_mode", (Object)false);
        }
        if (!this.plugin.getConfig().contains("plugin.repel_neutral")) {
            this.plugin.getConfig().set("plugin.repel_neutral", (Object)false);
        }
        this.plugin.saveConfig();
    }
    
    public boolean isNeutralEntity(final Entity entity) {
        return entity.getType() == EntityType.COW || entity.getType() == EntityType.SHEEP || entity.getType() == EntityType.WOLF || entity.getType() == EntityType.VILLAGER || entity.getType() == EntityType.OCELOT || entity.getType() == EntityType.IRON_GOLEM || entity.getType() == EntityType.SQUID || entity.getType() == EntityType.PIG || entity.getType() == EntityType.SNOWMAN || entity.getType() == EntityType.MUSHROOM_COW || entity.getType() == EntityType.RABBIT || entity.getType() == EntityType.CHICKEN || entity.getType() == EntityType.HORSE;
    }
    
    public boolean isInvalidEntity(final Entity entity) {
        return entity.getType() == EntityType.ARMOR_STAND || entity.getType() == EntityType.ARROW || entity.getType() == EntityType.DROPPED_ITEM || entity.getType() == EntityType.BOAT || entity.getType() == EntityType.EGG || entity.getType() == EntityType.ENDER_PEARL || entity.getType() == EntityType.EXPERIENCE_ORB || entity.getType() == EntityType.FALLING_BLOCK || entity.getType() == EntityType.FIREBALL || entity.getType() == EntityType.FIREWORK || entity.getType() == EntityType.LEASH_HITCH || entity.getType() == EntityType.LIGHTNING || entity.getType() == EntityType.MINECART || entity.getType() == EntityType.MINECART_CHEST || entity.getType() == EntityType.MINECART_COMMAND || entity.getType() == EntityType.MINECART_FURNACE || entity.getType() == EntityType.MINECART_HOPPER || entity.getType() == EntityType.MINECART_MOB_SPAWNER || entity.getType() == EntityType.MINECART_TNT || entity.getType() == EntityType.PAINTING || entity.getType() == EntityType.PLAYER || entity.getType() == EntityType.PRIMED_TNT || entity.getType() == EntityType.SMALL_FIREBALL || entity.getType() == EntityType.SPLASH_POTION || entity.getType() == EntityType.SNOWBALL || entity.getType() == EntityType.THROWN_EXP_BOTTLE || entity.getType() == EntityType.UNKNOWN || entity.getType() == EntityType.WEATHER || entity.getType() == EntityType.FISHING_HOOK || entity.getType() == EntityType.ITEM_FRAME;
    }
    
    public boolean isEntityTame(final Entity entity) {
        return (entity.getType() == EntityType.WOLF && ((Wolf)entity).isTamed()) || (entity.getType() == EntityType.OCELOT && ((Ocelot)entity).isTamed()) || (entity.getType() == EntityType.HORSE && ((Horse)entity).isTamed());
    }
}

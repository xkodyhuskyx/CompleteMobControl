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
import com.kodyhusky.cmcontrol.objects.MobWard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import com.kodyhusky.cmcontrol.objects.MobWardType;
import com.kodyhusky.cmcontrol.util.ConfigHandler;
import com.kodyhusky.cmcontrol.util.L10N;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 *
 * @author xkodyhuskyx
 */
public class MobWardManager {

    private final CompleteMobControl plugin;

    private HashMap<Location, UUID> redstones = new HashMap<>();
    private HashMap<Location, UUID> locations = new HashMap<>();
    private HashMap<UUID, MobWard> names = new HashMap<>();
    private HashMap<String, MobWardType> types = new HashMap<>();
    private int radiusType = 0;

    public MobWardManager(CompleteMobControl plugin) {
        this.plugin = plugin;
    }


    public boolean load() {
        return reload();
    }

    public boolean reload() {
        // Get Radius Type From Config
        String rType = ConfigHandler.getString("mobward.radius_type", "SPHERE");
        radiusType = rType.equalsIgnoreCase("SPHERE") ? 0 : 1;
        if (!rType.matches("^(?i)(sphere|cube)$"))
            plugin.getLogger().warning(L10N.getString("mobward.invradiustype", false));
        if (loadMobWardTypes()) {

        }
        return false;
    }


    public boolean loadMobWardTypes() {
        ConfigurationSection wardconfig = plugin.getConfig().getConfigurationSection("mobward.types");
        if (!(wardconfig == null)) {
            if (!wardconfig.getKeys(false).isEmpty()) {
                wardconfig.getKeys(false).forEach(type -> {
                    if (type.matches("^[A-Za-z0-9]*$")) {
                        String mat = wardconfig.getString(type + ".material", "is-invalid");
                        Material material = Material.getMaterial(mat != null ? mat : "is-invalid");
                        int radius = wardconfig.getInt(type + ".radius", 1000000000);
                        if (material != null && radius != 1000000000) {
                            types.put(type.toLowerCase(), new MobWardType(type, material, radius));
                        } else {
                            plugin.getLogger().warning(L10N.getString("mobward.invalidconfig", false));
                        }
                    } else {
                        plugin.getLogger().warning(L10N.getString("mobward.invalidname","type:" + type, false));
                    }
                });
                if (types.isEmpty()) {
                    plugin.getLogger().warning(L10N.getString("mobward.invalidconfig", false));
                    plugin.getServer().getPluginManager().disablePlugin(plugin);
                    return false;
                }
                return true;
            }
        }
        plugin.getLogger().warning(L10N.getString("mobward.invalidconfig", false));
        plugin.getServer().getPluginManager().disablePlugin(plugin);
        return false;
    }




















    /*
    private final CompleteMobControl plugin;

    private final HashMap<UUID, MobWard> allwards = new HashMap<>();
    private final HashMap<UUID, Pair<Location, Integer>> activewards = new HashMap<>();
    private final HashMap<Material, Pair<String, Integer>> materials = new HashMap<>();
    private final HashMap<Location, UUID> redstones = new HashMap<>();
    private int radiustype;


    public void load() {
        radiustype = plugin.getPluginConfig().getMobWardRadiusType();
        ConfigurationSection wardconfig = plugin.getPluginConfig().getMobWardTypesConfig();
        if (wardconfig != null) {
            if (!wardconfig.getKeys(false).isEmpty()) {
                wardconfig.getKeys(false).forEach(type -> {
                    String wardmat = wardconfig.getString(type + ".material", "is-invalid");
                    Material material = Material.getMaterial(wardmat != null ? wardmat : "is-invalid");
                    int radius = wardconfig.getInt(type + ".radius", 1000000000);
                    if (radius != 1000000000) {
                        if (material != null) {
                            materials.put(material, new Pair<>(type.toUpperCase(), radius));
                        } else {
                            plugin.logToConsole(Level.WARNING, "MobWard Type (" + type + ") Contains Invalid Material In (config.yml). Skipping...", false);
                        }
                    } else {
                        plugin.logToConsole(Level.WARNING, "MobWard Type (" + type + ") Contains Invalid Radius In (config.yml). Skipping...", false);
                    }
                });
            }
        }
        if (materials.isEmpty()) {
            plugin.logToConsole(Level.WARNING, "No MobWard Sizes Are Defined In (config.yml). Loading Defaults...", false);
            materials.put(Material.IRON_BLOCK, new Pair<>("MINI", 25));
            materials.put(Material.GOLD_BLOCK, new Pair<>("SMALL", 50));
            materials.put(Material.DIAMOND_BLOCK, new Pair<>("MEDIUM", 75));
            materials.put(Material.EMERALD_BLOCK, new Pair<>("LARGE", 100));
        }


        ========================================  GOT HERE WITH RELOAD FUNCTION  ===============================


        File[] files = (new File(plugin.getDataFolder(), "mobwards")).listFiles((File dir, String name) -> name.toLowerCase().endsWith(".yml"));
        for (File file : files) {
            FileConfiguration warddata = new YamlConfiguration();
            try {
                warddata.load(file);
                MobWard ward = loadMobWard(warddata, file.getName());
                if (ward != null) {
                    allwards.put(ward.getUUID(), ward);
                    if (ward.isActive()) {
                        activewards.put(ward.getUUID(), new Pair<>(ward.getLocation(), materials.get(ward.getMaterial()).getValue()));
                    }
                    if (ward.getRedstoneLocation() != null) {
                        redstones.put(ward.getRedstoneLocation(), ward.getUUID());
                    }
                    plugin.logToConsole(Level.ALL, "Loaded New Repeller ID (" + ward.getUUID() + ") At Location (" + ward.getLocation().toString() + ")", false);
                }
            } catch (IOException | InvalidConfigurationException ex) {
                plugin.logToConsole(Level.WARNING, "Unable to load MobWard file (" + file.getName() + ")! Skipping...", false);
                plugin.logToConsole(Level.INFO, "ISSUE: " + ex.getLocalizedMessage(), Boolean.FALSE);
            }
        }
        plugin.logToConsole(Level.WARNING, "Sucessfully Loaded (" + allwards.size() + ") MobWards!", false);
    }


    public MobWard loadMobWard(FileConfiguration config, String filename) {
        String uuidregex = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}$";
        String sc = config.getString("uuid", "is-invalid");
        UUID uuid = sc.matches(uuidregex) ? UUID.fromString(sc) : null;
        Material mc = Material.getMaterial(config.getString("material", "is-invalid"));
        Material material = mc != null ? mc : null;
        sc = config.getString("location.world", "is-invalid");
        World wc = sc.matches(uuidregex) ? plugin.getServer().getWorld(UUID.fromString(sc)) : null;
        Double xc = config.getDouble("location.x", 1000000000.00);
        Double yc = config.getDouble("location.y", 1000000000.00);
        Double zc = config.getDouble("location.z", 1000000000.00);
        Location location = (wc != null && xc != 1000000000.00 && yc != 1000000000.00 && zc != 1000000000.00) ? new Location(wc, xc, yc, zc) : null;
        Location redstone = null;
        if (config.contains("powerblock")) {
            xc = config.getDouble("powerblock.x", 1000000000.00);
            yc = config.getDouble("powerblock.y", 1000000000.00);
            zc = config.getDouble("powerblock.z", 1000000000.00);
            redstone = (xc != 1000000000.00 && yc != 1000000000.00 && zc != 1000000000.00) ? new Location(wc, xc, yc, zc) : null;
            if (redstone == null) {
                plugin.logToConsole(Level.WARNING, "Found invalid coordinates for MobWard redstone (" + filename + ")! Skipping...", false);
            }
        }
        sc = config.getString("owner.uuid", "is-invalid");
        Pair<UUID, String> owner = sc.matches(uuidregex) ? new Pair<>(UUID.fromString(sc), config.getString("owner.name", "Unknown")) : null;
        HashMap<UUID, Pair<String, List<String>>> managers = new HashMap<>();
        if (config.contains("managers")) {
            config.getConfigurationSection("managers").getKeys(false).forEach(key -> {
                if (key.matches(uuidregex)) {
                    List<String> mm = new ArrayList<>();
                    config.getStringList("managers." + key + ".modes").forEach(managermode -> {
                        mm.add(managermode);
                    });
                    managers.put(UUID.fromString(key), new Pair<>(config.getString("managers." + key + ".name", "Unknown"), mm));
                } else {
                    plugin.logToConsole(Level.WARNING, "Found invalid UUID for MobWard manager (" + filename + ")! Skipping...", false);
                }
            });
        }
        sc = config.getString("check_level", "is-invalid");
        int checkint = 0;
        switch (sc) {
            case "BELOW":
                checkint = -1;
                break;
            case "DEFAULT":
                checkint = 0;
                break;
            case "ABOVE":
                checkint = 1;
                break;
            default:
                plugin.logToConsole(Level.WARNING, "Check level for MobWard (" + filename + ") is invalid! Loading defaults...", false);
                checkint = 0;
        }
        sc = config.getString("mode", "is-invalid");
        int modeint = ("BLACKLIST".equalsIgnoreCase(sc) || "WHITELIST".equalsIgnoreCase(sc) ? ("BLACKLIST".equalsIgnoreCase(sc) ? 0 : 1) : 0);
        List<String> entities = config.getStringList("entities");
        if (config.contains("entities")) {
            if (entities.isEmpty()) {
                plugin.logToConsole(Level.WARNING, "Entity list for MobWard (" + filename + ") is empty! MobWard will do nothing!", false);
            }
        } else {
            plugin.logToConsole(Level.WARNING, "Entity list for MobWard (" + filename + ") is empty! MobWard will do nothing!", false);
        }
        Boolean active = config.getBoolean("active", false);
        if (!config.contains("active")) {
            plugin.logToConsole(Level.WARNING, "Active boolean not found for MobWard (" + filename + ")! Defaulting to disabled...", false);
        }
        if (uuid == null || material == null || location == null || owner == null) {
            plugin.logToConsole(Level.SEVERE, "Found invalid MobWard Configuration File (" + filename + ")! Skipping...", false);
            return null;
        }
        return new MobWard(uuid, active, material, location, redstone, owner, managers, checkint, modeint, entities);
    }

    public List<MobWard> getAllMobWards(Player player) {
        List<MobWard> wards = new ArrayList<>();
        if (!allwards.isEmpty()) {
            allwards.forEach((uuid, ward) -> {
                if (player.getUniqueId().toString().equalsIgnoreCase(ward.getOwner().getKey().toString())) {
                    wards.add(ward);
                }
            });
        }
        return wards;
    }


    public void setWardActive(UUID uuid, Boolean active) {
        MobWard ward = allwards.get(uuid);
        ward.setActive(active);
        allwards.replace(uuid, ward);
        if (active) {
            if (!activewards.containsKey(uuid)) {
                activewards.put(uuid, new Pair<>(ward.getLocation(), materials.get(ward.getMaterial()).getValue()));
            }
        } else {
            if (activewards.containsKey(uuid)) {
                activewards.remove(uuid);
            }
        }
    }


    private boolean isInRadius(Location entity, Location ward, Integer radius) {
        if (radiustype == 0) {
            double x1 = entity.getX();
            double y1 = entity.getY();
            double z1 = entity.getZ();
            double x2 = ward.getX();
            double y2 = ward.getY();
            double z2 = ward.getZ();
            return x1 >= x2 - radius && x1 <= x2 + radius && y1 >= y2 - radius && y1 <= y2 + radius && z1 >= z2 - radius && z1 <= z2 + radius;
        }
        return (entity.distanceSquared(ward) <= radius * radius);
    }


    public boolean isSpawnBlocked(Entity entity) {
        for (Map.Entry<UUID, Pair<Location, Integer>> entry : activewards.entrySet()) {
            if (isInRadius(entity.getLocation(), entry.getValue().getKey(), entry.getValue().getValue())) {
                MobWard ward = allwards.get(entry.getKey());
                double y1 = entity.getLocation().getY();
                double y2 = ward.getLocation().getY();
                int cl = ward.getCheckLevel();
                if (cl == 0 || cl == 1 && y1 >= y2 || cl == -1 && y1 < y2) {
                    if (ward.getMode() == 0 && matchInEntityList(ward.getEntityList(), entity.getType().name().toUpperCase())
                            || ward.getMode() == 1 && !this.matchInEntityList(ward.getEntityList(), entity.getType().name().toUpperCase())) {
                        plugin.logToConsole(Level.ALL, "[BLOCKED] " + entity.getType().name().toUpperCase() + " {" + entity.getLocation().toString() + "} BY WARD ID {" + ward.getUUID() + "}", false);
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public int getMaterialRadius(Material material) {
        return materials.get(material).getValue();
    }


    public boolean matchInEntityList(List<String> list, String name) {
        List<String> allentities = new ArrayList<>();
        List<String> toremove = new ArrayList<>();
        list.forEach(entry -> {
            Boolean remove = entry.startsWith("-");
            String clean = entry.replace("-", "");
            if (plugin.getPluginConfig().getEntityGroups().contains(clean)) {
                if (remove) {
                    plugin.getPluginConfig().getEntityGroupEntities(clean).forEach(entity -> {
                        toremove.add(entity);
                    });
                } else {
                    plugin.getPluginConfig().getEntityGroupEntities(clean).forEach(entity -> {
                        allentities.add(entity);
                    });
                }
            } else {
                if (remove) {
                    toremove.add(clean);
                } else {
                    allentities.add(clean);
                }
            }
        });
        allentities.removeAll(toremove);
        return allentities.contains(name);
    }

    public boolean isValidMobWard(Location location) {
        if (location.subtract(0, 1, 0).getBlock().getType().equals(Material.REDSTONE_BLOCK)) {
            plugin.getServer().broadcastMessage("Redstone Valid");
            if (location.add(1, 0, 1).getBlock().getType().equals(Material.LAPIS_BLOCK)
                    && location.subtract(2, 0, 2).getBlock().getType().equals(Material.LAPIS_BLOCK)
                    && location.add(2, 0, 0).getBlock().getType().equals(Material.LAPIS_BLOCK)
                    && location.add(-2, 0, 2).getBlock().getType().equals(Material.LAPIS_BLOCK)) {
                plugin.getServer().broadcastMessage("1 1 Lapis");
                return true;
            }
        }
        return false;
    }
    */
}

/*

I I   I I
I L X L I
  X R X
I L X L I
I I   I I

 */

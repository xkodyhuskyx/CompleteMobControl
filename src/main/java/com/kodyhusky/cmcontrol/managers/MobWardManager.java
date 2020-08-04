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
import com.kodyhusky.cmcontrol.util.Pair;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;

/**
 *
 * @author xkodyhuskyx
 */
public class MobWardManager {

    private final CompleteMobControl plugin;

    public MobWardManager(CompleteMobControl plugin) {
        this.plugin = plugin;
    }

    private final HashMap<UUID, MobWard> allwards = new HashMap<>();
    private final HashMap<UUID, Pair<Location, Integer>> activewards = new HashMap<>();
    private HashMap<Material, Pair<String,Integer>> materials = new HashMap<>();
    private final HashMap<Location, UUID> redstones = new HashMap<>();
    private int radiustype;

    public void load() {
        plugin.logToConsole("Loading MobWard material types...");
        materials = plugin.getPluginConfig().getMobWardTypes();
        plugin.logToConsole("Loading MobWard radius type...");
        radiustype = plugin.getPluginConfig().getMobWardRadiusType();
        plugin.logToConsole("Loading MobWard configuration files...");
        File warddir = new File(plugin.getDataFolder(), "mobwards");
        File[] files = warddir.listFiles((File dir, String name) -> name.toLowerCase().endsWith(".yml"));
        for (File file : files) {
            FileConfiguration wardconfig = new YamlConfiguration();
            try {
                File wfile = new File(plugin.getDataFolder(), "mobwards/" + file.getName());
                wardconfig.load(wfile);
                MobWard ward = loadMobWard(wardconfig, file.getName());
                if (ward == null) {throw new InvalidConfigurationException();}
                UUID warduuid = ward.getUUID();
                allwards.put(warduuid, ward);
                if (ward.isActive()) {
                    activewards.put(warduuid, new Pair(ward.getLocation(), materials.get(ward.getMaterial()).getValue()));
                }
                if (ward.getRedstoneLocation() != null) {
                    redstones.put(ward.getRedstoneLocation(), warduuid);
                }
            } catch (IOException | InvalidConfigurationException ex) {
                plugin.logToConsole("ERROR: Unable to load MobWard file (" + file.getName() + ")! Not Loading!");
                plugin.logToConsole(ex.getMessage());
            }
        }
    }

    /**
     * Returns a MobWard object based on the given configuration data.
     *
     * @param config MobWard YAML data
     * @param filename Configuration file name
     * @return MobWard - MobWard or null if error occurs
     */
    @SuppressWarnings("null")
    public MobWard loadMobWard(FileConfiguration config, String filename) {
        String uuidregex = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}$";
        String error;
        UUID uuid;
        Boolean active = config.getBoolean("active", false);
        Material material;
        Location location;
        Location redstone = null;
        Pair<UUID, String> owner;
        HashMap<UUID, Pair<String, List<String>>> managers = new HashMap<>();
        String checklevel = config.getString("check_level", "is-invalid");
        String mode = config.getString("mode", "is-invalid");
        List<String> entities = config.getStringList("entities");
        String uuidcheck = config.getString("uuid", "is-invalid");
        if (uuidcheck.matches(uuidregex)) {
            uuid = UUID.fromString(uuidcheck);
            Material matcheck = Material.getMaterial(config.getString("material", "is-invalid"));
            if (matcheck != null) {
                material = matcheck;
                String worldcheck = config.getString("location.world", "is-invalid");
                if (worldcheck.matches(uuidregex)) {
                    World world = plugin.getServer().getWorld(UUID.fromString(worldcheck));
                    if (world != null) {
                        Double xc = config.getDouble("location.x", 1000000000.00);
                        Double yc = config.getDouble("location.y", 1000000000.00);
                        Double zc = config.getDouble("location.z", 1000000000.00);
                        if (xc != 1000000000.00 && yc != 1000000000.00 && zc != 1000000000.00) {
                            location = new Location(world, xc, yc, zc);
                            if (config.contains("powerblock")) {
                                xc = config.getDouble("powerblock.x", 1000000000.00);
                                yc = config.getDouble("powerblock.y", 1000000000.00);
                                zc = config.getDouble("powerblock.z", 1000000000.00);
                                if (xc != 1000000000.00 && yc != 1000000000.00 && zc != 1000000000.00) {
                                    redstone = new Location(world, xc, yc, zc);
                                } else {
                                    plugin.logToConsole("WARN: Found invalid coordinates for MobWard redstone (" + filename + ")! Skipping...");
                                }
                            }
                            String owneruuid = config.getString("owner.uuid", "is-invalid");
                            String ownername = config.getString("owner.name", "Unknown");
                            if (owneruuid.matches(uuidregex)) {
                                owner = new Pair(UUID.fromString(owneruuid), ownername);
                                if (config.contains("managers")) {
                                    config.getConfigurationSection("managers").getKeys(false).forEach(key -> {
                                        if (key.matches(uuidregex)) {
                                            List<String> managermodes = new ArrayList<>();
                                            config.getStringList("managers." + key + ".modes").forEach(managermode -> {
                                                if ("USE".equals(managermode) || "MODIFY".equals(managermode) || "DESTROY".equals(managermode)) {
                                                    managermodes.add(managermode);
                                                } else {
                                                    plugin.logToConsole("WARN: Found invalid manager mode (" + mode + ") for MobWard (" + filename + ")! Skipping...");
                                                }
                                            });
                                            managers.put(UUID.fromString(key), new Pair(config.getString("managers." + key + ".name", "Unknown"), managermodes));
                                        } else {
                                            plugin.logToConsole("WARN: Found invalid UUID for MobWard manager (" + filename + ")! Skipping...");
                                        }
                                    });
                                }
                                if (!"is-invalid".equals(checklevel)) {
                                    int checkint = 0;
                                    switch (checklevel) {
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
                                            plugin.logToConsole("WARN: Check level for MobWard (" + filename + ") is invalid! Loading defaults...");
                                            checkint = 0;
                                            break;
                                    }
                                    if ("BLACKLIST".equals(mode) || "WHITELIST".equals(mode)) {
                                        int modeint = "BLACKLIST".equals(mode) ? 0 : 1;
                                        if (config.contains("entities")) {
                                            if (entities.isEmpty()) {
                                                plugin.logToConsole("WARN: Entity list for MobWard (" + filename + ") is empty! MobWard will do nothing!");
                                            }
                                        } else {
                                            plugin.logToConsole("WARN: Entity list for MobWard (" + filename + ") is empty! MobWard will do nothing!");
                                        }
                                        if (!config.contains("active")) {
                                            plugin.logToConsole("WARN: Active boolean not found for MobWard (" + filename + ")! Defaulting to disabled...");
                                        }
                                        return new MobWard(uuid, active, material, location, redstone, owner, managers, checkint, modeint, entities);
                                    } else {
                                        error = "mode";
                                    }
                                } else {
                                    error = "check level";
                                }
                            } else {
                                error = "owner uuid";
                            }
                        } else {
                            error = "world coordinates";
                        }
                    } else {
                        error = "world UUID";
                    }
                } else {
                    error = "world UUID";
                }
            } else {
                error = "material";
            }
        } else {
            error = "UUID";
        }
        plugin.logToConsole("ERROR: Found invalid " + error + " for MobWard (" + filename + ")!");
        return null;
    }

    /**
     * Sets the active status of a MobWard.
     *
     * @param uuid MobWard UUID
     * @param active is active
     */
    public void setWardActive(UUID uuid, Boolean active) {
        MobWard ward = allwards.get(uuid);
        ward.setActive(active);
        allwards.replace(uuid, ward);
        if (active) {
            if (!activewards.containsKey(uuid)) {
                activewards.put(uuid, new Pair(ward.getLocation(), materials.get(ward.getMaterial())));
            }
        } else {
            if (activewards.containsKey(uuid)) {
                activewards.remove(uuid);
            }
        }
    }

    /**
     * Checks if a Entity is within the radius of a MobWard.
     *
     * @param entity Entity location
     * @param ward Ward Location
     * @param radius Radius
     * @return Boolean
     */
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

    /**
     * Checks if an entity spawn event should be cancelled.
     *
     * @param entity Spawning entity
     * @return Boolean
     */
    public boolean isSpawnBlocked(Entity entity) {
        for (Map.Entry<UUID, Pair<Location, Integer>> entry : activewards.entrySet()) {
            Location wardlocation = entry.getValue().getKey();
            int radius = (entry.getValue()).getValue();
            if (isInRadius(entity.getLocation(), wardlocation, radius)) {
                MobWard ward = allwards.get(entry.getKey());
                double y1 = entity.getLocation().getY();
                double y2 = ward.getLocation().getY();
                if (ward.getCheckLevel() == 0 || ward.getCheckLevel() == 1 && y1 >= y2 || ward.getCheckLevel() == -1 && y1 < y2) {
                    if (ward.getMode() == 0) {
                        if (this.matchInEntityList(ward.getEntityList(), entity.getType().name().toUpperCase())) {
                            plugin.logToConsole("[BLOCKED] " + entity.getType().name().toUpperCase() + " {" + entity.getLocation().toString() + "} BY WARD ID {" + ward.getUUID() + "}", true);
                            return true;
                        }
                    } else {
                        if (!this.matchInEntityList(ward.getEntityList(), entity.getType().name().toUpperCase())) {
                            plugin.logToConsole("[BLOCKED] " + entity.getType().name().toUpperCase() + " {" + entity.getLocation().toString() + "} BY WARD ID {" + ward.getUUID() + "}", true);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Gets the radius value for the material.
     *
     * @param material Material
     * @return Integer
     */
    public int getMaterialRadius(Material material) {
        return materials.get(material).getValue();
    }

    /**
     * Checks the given entity list for the given entity name. This does check
     * groups and takes into account negated items.
     *
     * @param list MobWard entity list
     * @param name Spawning entity name
     * @return Boolean is listed
     */
    public boolean matchInEntityList(List<String> list, String name) {
        List<String> allentities = new ArrayList<>();
        List<String> toremove = new ArrayList<>();
        HashMap<String, List<String>> egroups = plugin.getPluginConfig().getEntityGroups();
        list.forEach(entry -> {
            if (entry.contains("-")) {
                String entryr = entry.replace("-", "");
                if (egroups.containsKey(entryr)) {
                    toremove.addAll(egroups.get(entryr));
                } else {
                    toremove.add(entryr);
                }
            } else {
                if (egroups.containsKey(entry)) {
                    allentities.addAll(egroups.get(entry));
                } else {
                    toremove.add(entry);
                }
            }
        });
        allentities.removeAll(toremove);
        return allentities.contains(name);
    }
}

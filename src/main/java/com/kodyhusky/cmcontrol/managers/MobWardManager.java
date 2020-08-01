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

    private HashMap<UUID, MobWard> allwards;
    private HashMap<UUID, Pair<Location, Integer>> activewards;
    private HashMap<Material, Integer> materials;
    private HashMap<Location, UUID> powerblocks;
    private HashMap<String, String> entitytypes;
    private int radiustype;

    public boolean load() {
        
        
        
        
        plugin.logToConsole("Loading MobWard configuration files...");
        File warddir = new File(plugin.getDataFolder(), "wards");
        File[] files = warddir.listFiles((File dir, String name) -> name.toLowerCase().endsWith(".yml"));
        for (File file : files) {
            FileConfiguration wardconfig = new YamlConfiguration();
                try {
                    wardconfig.load(file);
                    UUID uuid = UUID.fromString(wardconfig.getString("uuid"));
                    Material material = Material.getMaterial(wardconfig.getString("material"));
                    World world = plugin.getServer().getWorld(UUID.fromString(wardconfig.getString("location.world")));
                    Location location = new Location(world, wardconfig.getInt("location.x"), wardconfig.getInt("location.y"), wardconfig.getInt("location.z"));
                    Location powerblock = new Location(world, wardconfig.getInt("powerblock.x"), wardconfig.getInt("powerblock.y"), wardconfig.getInt("powerblock.z"));
                    UUID owner = UUID.fromString(wardconfig.getString("owner"));
                    HashMap<UUID, List<String>> managers = new HashMap<>();
                    int checklevel = wardconfig.getInt("checklevel");
                    List<String> denytypes = new ArrayList<>();
                    int checkmode = wardconfig.getInt("checkmode");
                    List<String> customlist = new ArrayList<>();
                    boolean active = wardconfig.getBoolean("active");
                    
                    MobWard ward = new MobWard(uuid,active,material,location,powerblock,owner,managers,checklevel,denytypes,checkmode,customlist);
                    allwards.put(uuid, ward);
                    if (active) {activewards.put(uuid, new Pair(location, materials.get(ward.getMaterial())));} // Must Load Materials First!!!!
                    
                } catch (IOException | InvalidConfigurationException ex) {
                    plugin.logToConsole("WARN: Unable to load MobWard file (" + file.getName() + ")! Skipping...");
                }
            
        }
        
        
        
        
        
        
        allwards = new HashMap<>();
        activewards = new HashMap<>();
        materials = new HashMap<>();
        powerblocks = new HashMap<>();
        entitytypes = new HashMap<>();
        return false;
    }
    
    /**
     * Sets the active status of a MobWard.
     * @param uuid MobWard UUID
     * @param active is active
     */
    public void setWardActive(UUID uuid, Boolean active) {
        MobWard ward = allwards.get(uuid);
        ward.setActive(active);
        allwards.replace(uuid, ward);
        if (active) {
            if (!activewards.containsKey(uuid)) {
                activewards.put(uuid, new Pair(ward.getLocation(),materials.get(ward.getMaterial())));
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
            int radius = entry.getValue().getValue();
            if (isInRadius(entity.getLocation(), wardlocation, radius)) {
                MobWard ward = allwards.get(entry.getKey());
                double y1 = entity.getLocation().getY();
                double y2 = ward.getLocation().getY();
                if (ward.getCheckLevel() == 0 || ward.getCheckLevel() == 1 && y1 >= y2 || ward.getCheckLevel() == -1 && y1 < y2) {
                    if (ward.getCheckMode() != 0) {
                        return ward.isEntityDenied(entity.getName().toUpperCase());
                    } else {
                        return ward.getEntityTypeDenied(entitytypes.get(entity.getName().toUpperCase()));
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
        return materials.get(material);
    }
}

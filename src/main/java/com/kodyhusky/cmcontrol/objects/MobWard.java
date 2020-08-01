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
package com.kodyhusky.cmcontrol.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 * An object containing all data for a MobWard structure.
 * <br><br>Note: The UUID, Material, and Location cannot be changed after
 * initialization.
 *
 * @author xkodyhuskyx
 * @version 3.0.0
 */
public class MobWard {

    private final UUID uuid;
    private boolean active;
    private final Material material;
    private final Location location;
    private Location redstone;
    private UUID owner;
    private final HashMap<UUID, List<String>> managers;
    private int checklevel;
    private List<String> denytypes;
    private int checkmode;
    private final List<String> customlist;

    /**
     * Used to create a MobWard object loaded from the plugin configuration
     * files.
     * <br><br>Note: The UUID, Material, and Location cannot be changed after
     * initialization.
     *
     * @param uuid Unique Identifier
     * @param active is active
     * @param material
     * @param location
     * @param redstone
     * @param owner Owners UUID
     * @param managers List of managers UUID's
     * @param checklevel
     * @param denytypes
     * @param checkmode
     * @param customlist Custom entity list
     */
    public MobWard(UUID uuid, Boolean active, Material material, Location location,
            Location redstone, UUID owner, HashMap<UUID, List<String>> managers,
            int checklevel, List<String> denytypes, int checkmode, List<String> customlist) {
        this.uuid = uuid;
        this.active = active;
        this.material = material;
        this.location = location;
        this.redstone = redstone;
        this.owner = owner;
        this.managers = managers;
        this.checklevel = checklevel;
        this.denytypes = denytypes;
        this.checkmode = checkmode;
        this.customlist = customlist;
    }

    /**
     * Used to create a new MobWard object.<br><br>Note: The Material, and
     * Location cannot be changed after initialization.
     *
     * @param active
     * @param material
     * @param location
     * @param owner Owners UUID
     * @param checklevel
     * @param denytypes
     */
    public MobWard(Boolean active, Material material, Location location, UUID owner, int checklevel, List<String> denytypes) {
        this.uuid = UUID.randomUUID();
        this.active = active;
        this.material = material;
        this.location = location;
        this.redstone = null;
        this.owner = owner;
        this.managers = new HashMap<>();
        this.checklevel = checklevel;
        this.denytypes = denytypes;
        this.checkmode = 0;
        this.customlist = new ArrayList<>();
    }

    /**
     * Get the UUID of the MobWard.
     *
     * @return UUID
     */
    public UUID getUUID() {
        return this.uuid;
    }

    /**
     * Get the MobWard structure material. This is used to calculate the size of
     * the check radius.
     *
     * @return Material
     */
    public Material getMaterial() {
        return this.material;
    }

    /**
     * Get the location of the MobWard. This is used as the center block of the
     * check area.
     *
     * @return Location
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * Sets the power block location that will be checked for a redstone signal.
     *
     * @param redstone block location.
     */
    public void setRedstoneLocation(Location redstone) {
        this.redstone = redstone;
    }

    /**
     * Get the block location to be checked for a redstone signal.
     *
     * @return Location
     */
    public Location getRedstoneLocation() {
        return this.redstone;
    }

    /**
     * Get the MobWards owner UUID.
     *
     * @return UUID
     */
    public UUID getOwnerUUID() {
        return this.uuid;
    }

    /**
     * Set the MobWards owner UUID.
     *
     * @param owner - Owners UUID
     */
    public void setOwnerUUID(UUID owner) {
        this.owner = owner;
    }

    /**
     * Get a UUID List of all registered managers.
     * <br>Will return an empty List if no managers are registered.
     *
     * @return List
     */
    public List<UUID> getManagers() {
        if (!managers.isEmpty()) {
            return (List<UUID>) this.managers.keySet();
        }
        return new ArrayList<>();
    }

    /**
     * Get a List of manager options allowed to the specified user.
     * <br>Will return an empty List if no options are allowed.
     *
     * @param user User to get options for
     * @return List
     */
    public List<String> getUserOptions(UUID user) {
        if (managers.containsKey(user)) {
            return managers.get(user);
        }
        return new ArrayList<>();
    }

    /**
     * Changes the options for a MobWard manager. Can also be used to add or
     * remove a manager (0 add, -1 remove).
     * <br><br>Options: ALL (A), USE (U), MODIFY (M), DESTROY(D)
     *
     * @param manager Managers UUID
     * @param option Option to modify
     * @param allow Allow option
     */
    public void setManagerOption(UUID manager, String option, boolean allow) {
        if (manager == owner) {
            return;
        }
        List<String> optionlist = new ArrayList<>();
        if ("A".equals(option)) {
            if (allow) {
                optionlist = Arrays.asList("U", "M", "D");
            }
        } else {
            if (managers.containsKey(manager)) {
                optionlist = managers.get(manager);
                if (allow && !optionlist.contains(option)) {
                    optionlist.add(option);
                }
                if (!allow && optionlist.contains(option)) {
                    optionlist.remove(option);
                }
            } else {
                if (allow) {
                    optionlist.add(option);
                }
            }
        }
        if (managers.containsKey(manager)) {
            if (!optionlist.isEmpty()) {
                managers.replace(manager, optionlist);
            } else {
                managers.remove(manager);
            }
        } else {
            if (!optionlist.isEmpty()) {
                managers.put(manager, optionlist);
            }
        }
    }

    /**
     * Get the MobWard check level.
     * <br><br>Levels: BELOW (-1), ALL (0), ABOVE (1)
     *
     * @return int
     */
    public int getCheckLevel() {
        return this.checklevel;
    }

    /**
     * Set the MobWard check level.
     * <br><br>Levels: BELOW (-1), ALL (0), ABOVE (1)
     *
     * @param checklevel Level
     */
    public void setCheckLevel(int checklevel) {
        this.checklevel = checklevel;
    }

    /**
     * Get if an entity type is denied.
     * <br><br>Types: PASSIVE (P), NEUTRAL (N), HOSTILE (H), BOSS (B)
     *
     * @param type Type to check
     * @return Boolean
     */
    public Boolean getEntityTypeDenied(String type) {
        return this.denytypes.contains(type);
    }

    /**
     * Allow or deny an entity type.
     * <br><br>Types: ALL (A), PASSIVE (P), NEUTRAL (N), HOSTILE (H), BOSS (B)
     *
     * @param type Entity type
     * @param denied Is denied
     */
    public void setEntityTypeDenied(String type, boolean denied) {
        List<String> deniedtypes = new ArrayList<>();
        if ("A".equals(type)) {
            if (denied) {
                deniedtypes.addAll(Arrays.asList("P", "N", "H", "B"));
            }
        } else {
            deniedtypes.addAll(denytypes);
            if (deniedtypes.contains(type) && !denied) {
                deniedtypes.remove(type);
            }
        }
        if (!deniedtypes.isEmpty()) {
            this.denytypes = deniedtypes;
        } else {
            denytypes = new ArrayList<>();
        }
    }

    /**
     * Get the MobWard check mode.
     * <br><br>Levels: BLACKLIST (-1), NORMAL (0), WHITELIST (1)
     *
     * @return int
     */
    public int getCheckMode() {
        return this.checkmode;
    }

    /**
     * Set the MobWard check mode.
     * <br><br>Levels: BLACKLIST (-1), NORMAL (0), WHITELIST (1)
     *
     * @param checkmode mode
     */
    public void setCheckMode(int checkmode) {
        this.checkmode = checkmode;
    }

    /**
     * Check is an entity spawn is denied when blacklist or whitelist mode is
     * enabled.
     * <br><br>Note: Only us this if getCheckMode() returns -1 or 1!
     *
     * @param name Entity name (ALL UPPERCASE)
     * @return Boolean
     */
    public Boolean isEntityDenied(String name) {
        if (checkmode == -1 && customlist.contains(name)) {
            return true;
        }
        return checkmode == 1 && !customlist.contains(name);
    }

    /**
     * Adds an entity to the black/whitelist.
     *
     * @param name Entity name (ALL UPPERCASE)
     */
    public void addCustomEntity(String name) {
        if (!customlist.contains(name)) {
            customlist.add(name);
        }
    }

    /**
     * Removes an entity from the black/whitelist.
     *
     * @param name Entity name (ALL UPPERCASE)
     */
    public void removeCustomEntity(String name) {
        if (customlist.contains(name)) {
            customlist.remove(name);
        }
    }

    /**
     * Gets if the MobWard is currently active.
     *
     * @return Boolean
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the MobWards active status..
     *
     * @param active is active
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

}

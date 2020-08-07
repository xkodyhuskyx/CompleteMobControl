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

import com.kodyhusky.cmcontrol.util.Pair;
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
@SuppressWarnings({"unchecked", "rawtypes", "ReturnOfCollectionOrArrayField"})
public class MobWard {

    private final UUID uuid;
    private boolean active;
    private final Material material;
    private final Location location;
    private Location redstone;
    private Pair<UUID, String> owner;
    private final HashMap<UUID, Pair<String, List<String>>> managers;
    private int checklevel;
    private int mode;
    private final List<String> entities;

    /**
     * Used to create a MobWard object loaded from the plugin configuration
     * files.
     * <br><br>Note: The UUID, Material, and Location cannot be changed after
     * initialization.
     *
     * @param uuid Unique Identifier
     * @param active is active
     * @param material .
     * @param location .
     * @param redstone .
     * @param owner Owner
     * @param managers List of managers UUID's
     * @param checklevel .
     * @param mode blacklist (0) or whitelist (1)
     * @param entities Entities to check
     */
    public MobWard(UUID uuid, Boolean active, Material material, Location location,
            Location redstone, Pair<UUID, String> owner, HashMap<UUID, Pair<String, List<String>>> managers,
            int checklevel, int mode, List<String> entities) {
        this.uuid = uuid;
        this.active = active;
        this.material = material;
        this.location = location;
        this.redstone = redstone;
        this.owner = owner;
        this.managers = managers;
        this.checklevel = checklevel;
        this.mode = mode;
        this.entities = entities;
    }

    /**
     * Used to create a new MobWard object.<br><br>Note: The Material, and
     * Location cannot be changed after initialization.
     *
     * @param active .
     * @param material .
     * @param location .
     * @param owner Owner .
     * @param checklevel .
     * @param mode .
     * @param entities .
     */
    public MobWard(Boolean active, Material material, Location location, Pair<UUID, String> owner, int checklevel, int mode, List<String> entities) {
        this.uuid = UUID.randomUUID();
        this.active = active;
        this.material = material;
        this.location = location;
        this.redstone = null;
        this.owner = owner;
        this.managers = new HashMap<>();
        this.checklevel = checklevel;
        this.mode = mode;
        this.entities = new ArrayList<>();
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
     * Get the MobWards owner.
     *
     * @return Pair uuid and username pair
     */
    public Pair<UUID, String> getOwner() {
        return this.owner;
    }

    /**
     * Set the MobWards owner.
     *
     * @param uuid - Owners UUID
     * @param name - Owners username
     */
    public void setOwner(UUID uuid, String name) {
        this.owner = new Pair(uuid, name);
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
            return managers.get(user).getValue();
        }
        return new ArrayList<>();
    }

    /**
     * Gets a managers stored username.
     *
     * @param user Users UUID
     * @return String - Users username
     */
    public String getManagerName(UUID user) {
        if (managers.containsKey(user)) {
            return managers.get(user).getKey();
        }
        return "Unknown";
    }

    /**
     * Changes the options for a MobWard manager.Can also be used to add or
     * remove a manager (0 add, -1 remove).<br><br>Options: ALL, USE, MODIFY,
     * DESTROY
     *
     * @param manager Managers UUID
     * @param name Managers username
     * @param option Option to modify
     * @param allow Allow option
     */
    public void setManagerOption(UUID manager, String name, String option, boolean allow) {
        if (manager == owner.getKey()) {
            return;
        }
        List<String> optionlist = new ArrayList<>();
        if ("ALL".equals(option)) {
            if (allow) {
                optionlist = Arrays.asList("USE", "MODIFY", "DESTROY");
            }
        } else {
            if (managers.containsKey(manager)) {
                optionlist = managers.get(manager).getValue();
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
                managers.replace(manager, new Pair(name, optionlist));
            } else {
                managers.remove(manager);
            }
        } else {
            if (!optionlist.isEmpty()) {
                managers.put(manager, new Pair(name, optionlist));
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
     * Get the MobWard check mode.
     * <br><br>Levels: BLACKLIST (0), WHITELIST (1)
     *
     * @return int
     */
    public int getMode() {
        return this.mode;
    }

    /**
     * Set the MobWard check mode.
     * <br><br>Levels: BLACKLIST (0), WHITELIST (1)
     *
     * @param mode blacklist (0) or whitelist (1)
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * Returns the list of entities monitored by this MobWard.
     *
     * @return List - list of entities
     */
    public List<String> getEntityList() {
        return entities;
    }

    /**
     * Adds a group or entity to the black/whitelist.
     *
     * @param type entity or group name (ALL UPPERCASE)
     */
    public void addEntity(String type) {
        if (!entities.contains(type) && !type.contains("!")) {
            entities.add(type);
            entities.remove("!" + type);
        }
        if (!entities.contains(type) && type.contains("!")) {
            entities.add(type);
            entities.remove(type.replace("!", ""));
        }
    }

    /**
     * Removes a group or entity from the black/whitelist.
     *
     * @param type entity or group name (ALL UPPERCASE)
     */
    public void removeEntity(String type) {
        entities.remove(type);
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

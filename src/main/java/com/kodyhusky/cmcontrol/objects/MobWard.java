package com.kodyhusky.cmcontrol.objects;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;


/**
 * An object containing all data for a MobWard structure.
 * <br><br>Note: The UUID, Material, and Location cannot be changed after
 * initialization.
 *
 * @author xkodyhuskyx
 * @version 1.0.0
 */
public class MobWard {

    private String id;
    private boolean active;
    private final String type;
    private final Location location;
    private Location redstone;
    private String owner;
    private HashMap<String, List<String>> users;
    private int modifier;
    private int mode;
    private List<String> entities;

    /**
     * Constructs a new MobWard object.
     *
     * @param id repeller id (name:uuid)
     * @param active is active
     * @param type MobWard type
     * @param location base location
     * @param redstone redstone input location
     * @param owner owning player
     * @param users managing players
     * @param modifier repel below, all, or above base
     * @param mode blacklist or whitelist
     * @param entities blacklist or whitelist entities list
     */
    public MobWard(String id, boolean active, String type, Location location,
                   Location redstone, String owner, HashMap<String, List<String>> users, int modifier, int mode, List<String> entities) {
        this.id = id;
        this.active = active;
        this.type = type;
        this.location = location;
        this.redstone = redstone;
        this.owner = owner;
        this.users = users;
        this.modifier = modifier;
        this.mode = mode;
        this.entities = entities;
    }

    /**
     * Returns the name of the MobWard.
     *
     * @return name if set or ""
     */
    public String getName() {
        if (id.contains("|")) return id.split("\\|")[0];
        return "";
    }

    /**
     * Sets the name of the MobWard.
     *
     * @param name name to be set
     */
    public void setName(String name) {
        if (id.contains("|")) {
            id = (name + "|" + id.split("\\|")[1]);
        } else {
            id = (name + "|" + id);
        }
    }

    /**
     * Returns the UUID of the MobWard.
     *
     * @return universally unique identifier (UUIDv4)
     */
    public UUID getUUID() {
        if (id.contains("|")) return UUID.fromString(id.split("\\|")[0]);
        return UUID.fromString(id);
    }

    /**
     * Returns the active status of the MobWard.
     *
     * @return active status
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active status of the MobWard.
     *
     * @param active is active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Returns the type of the MobWard.
     *
     * @return MobWard type
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the base location of the MobWard.
     *
     * @return base location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Returns the redstone input location of the MobWard.
     *
     * @return redstone input location
     */
    public Location getRedstoneLocation() {
        return location;
    }

    /**
     * Sets the redstone input location of the MobWard.
     *
     * @param location redstone input location
     */
    public void setRedstoneLocation(Location location) {
        this.redstone = location;
    }

    /**
     * Returns the owner of the MobWard.
     *
     * @return owners UUID or last known username if offline mode enabled
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets the owner of the MobWard.
     *
     * @param owner owners UUID or last known username if offline mode enabled
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Returns the user list of the MobWard with user options.
     *
     * @return users with options
     */
    public HashMap<String, List<String>> getUsersWithOptions() {
        return users;
    }

    /**
     * Sets the user list of the MobWard with user options.<br><br>
     *
     * Note: Remember to getUsersWithOptions before setUsersWithOptions when adding/removing!
     *
     * @param users users with options
     */
    public void setUsersWithOptions(HashMap<String, List<String>> users) {
        this.users = users;
    }

    /**
     * Returns the modifier value of the MobWard.<br>
     * Values: <i>-1 = BELOW</i> | <i>0 = ALL</i> | <i>1 = ABOVE</i>
     *
     * @return modifier value
     */
    public int getModifier() {
        return modifier;
    }

    /**
     * Set the modifier value of the MobWard.<br>
     * Values: <i>-1 = BELOW</i> | <i>0 = ALL</i> | <i>1 = ABOVE</i>
     *
     * @param modifier modifier value
     */
    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    /**
     * Returns the mode of the MobWard.<br>
     * Values: <i>0 = BLACKLIST</i> | <i>1 = WHITELIST</i>
     *
     * @return blacklist or whitelist
     */
    public int getMode() {
        return mode;
    }

    /**
     * Sets the mode of the MobWard.<br>
     * Values: <i>0 = BLACKLIST</i> | <i>1 = WHITELIST</i>
     *
     * @param mode blacklist or whitelist
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * Returns a list of entities/groups monitored by the MobWard.<br><br>
     * Note: Groups are prefixed with an @ sign<br>
     * Note: Negated values are defined with a - sign
     *
     * @return entities/groups list
     */
    public List<String> getEntities() {
        return entities;
    }

    /**
     * Sets the list of entities/groups monitored by the MobWard.<br><br>
     *
     * Note: Groups are prefixed with an @ sign<br>
     * Note: Negated values are defined with a - sign<br><br>
     *
     * Note: Remember to getEntities before setEntities when adding/removing!
     *
     * @param entities entities/groups list
     */
    public void setEntities(List<String> entities) {
        this.entities = entities;
    }
}

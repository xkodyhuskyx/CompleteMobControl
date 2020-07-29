/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kodyhusky.cmcontrol.objects;

import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * An object containing all data for a given MobWard.
 * <br>Note1: Base Location, UUID, and Type cannot be
 * changed once the object is constructed.
 *
 * @author xkodyhuskyx
 * @version 3.0.0
 */
public class MobWard {
    
    private String name;
    private UUID uuid;
    private String type;
    private Location baseblock;
    private Location powerblock;
    private UUID owner;
    private ArrayList<UUID> managers;
    private ArrayList<String> options;
    private ArrayList<String> customlist;
    private boolean debug;

    /**
     * Creates an object containing all data for a given MobWard.
     * <br>Note1: Base Location, UUID, and Type cannot be
     * changed once the object is constructed.
     * @param name Given Name
     * @param uuid Unique Identifier
     * @param type Material
     * @param baseblock Base Location
     * @param powerblock Power block Location
     * @param owner Owners UUID
     * @param managers List of managers UUID's
     * @param options Options
     * @param customlist Custom entity list
     * @param debug Debug mode
     */
    public MobWard(String name, UUID uuid, String type, Location baseblock,
            Location powerblock, UUID owner, ArrayList<UUID> managers,
            ArrayList<String> options, ArrayList<String> customlist, boolean debug) {
        this.name = name;
        this.uuid = UUID.randomUUID();
        this.type = type;
        this.baseblock = baseblock;
        this.powerblock = powerblock;
        this.owner = owner;
        this.managers = managers;
        this.options = options;
        this.debug = debug;
    }
    
    /**
     * Get the current blacklist / whitelist mode.<br>
     * <br>0 - Default (Don't Use White/Blacklist)
     * <br>1 - Blacklist (Deny Specific Mobs)
     * <br>2 - Whitelist (Allow Specific Mobs)
     * @return int - list type
     */
    public int entityListType() {
        if (options.contains("BLACKLIST")) {return 1;}
        if (options.contains("WHITELIST")) {return 2;}
        return 0;
    }
    
    /**
     * Gets if the specified option has been set.
     * @param option Option to check
     * @return boolean - has option
     */
    public boolean hasOption(String option) {
        return this.options.contains(option);
    }
    
    /**
     * Get if the specified entity spawn should be allowed.
     * <br>!!Do not use with entity list type 0 (Default)!!
     * @param entity entity trying to spawn
     * @return Boolean - spawn allowed
     */
    public boolean isEntityAllowed(Entity entity) {
        if (options.contains("BLACKLIST")) {
            return !customlist.contains(entity.getType().toString().toUpperCase());
        }
        if (options.contains("WHITELIST")) {
            return customlist.contains(entity.getType().toString().toUpperCase());
        }
        return true;
    }
    
    /**
     * Assign a custom entity list.
     * <br>Ensure all values are in uppercase!
     * <br>Assign empty ArrayList to clear.
     * @param customlist list of entity names
     */
    public void setCustomEntityList(ArrayList<String> customlist) {
        this.customlist = new ArrayList<>();
        if (!customlist.isEmpty()) {this.customlist.addAll(customlist);}
    }
    
    /**
     * For development purposes only! May spam the console and global chat!
     * @return Boolean - debug mode enabled
     */
    public boolean isDebugEnabled() {
        return this.debug;
    }
    
    /**
     * For development purposes only! May spam the console and global chat!
     * @param debug debug mode
     */
    public void setDebugMode(Boolean debug) {
        this.debug = debug;
    }
    
    
    
    
    
    
    
    
}

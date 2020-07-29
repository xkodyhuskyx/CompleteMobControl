/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kodyhusky.cmcontrol.objects;

import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;

/**
 *
 * @author Kody
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
    private Boolean debug;

    public MobWard(String name, UUID uuid, String type, Location baseblock,
            Location powerblock, UUID owner, ArrayList<UUID> managers,
            ArrayList<String> options, Boolean debug) {
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
    
    public Boolean isSpawnAllowed(Entity entity) {
        return false;
    }
    
    
    
    
    
    
}

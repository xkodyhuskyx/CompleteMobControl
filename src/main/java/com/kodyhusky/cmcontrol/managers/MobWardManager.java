/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kodyhusky.cmcontrol.managers;

import com.kodyhusky.cmcontrol.CompleteMobControl;
import com.kodyhusky.cmcontrol.objects.MobWard;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

/**
 *
 * @author Kody
 */
public class MobWardManager {
    
    private CompleteMobControl plugin;
    
    private HashMap<Location,UUID> activewards;
    private HashMap<UUID,HashMap<Rectangle2D,MobWard>> mobwards;
    
    public MobWardManager(CompleteMobControl plugin) {
        this.plugin = plugin;
    }
    
    public MobWard isBlocked(Location location, EntityType entity) {
        UUID world = location.getWorld().getUID();
        
        // Get All Wards On Specific World
        // Check Each For X and Z values
        // Check Y Value
        // Check EntityType
        
        
    }
    
    
}

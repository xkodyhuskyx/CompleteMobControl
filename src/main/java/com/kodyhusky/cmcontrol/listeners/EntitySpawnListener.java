/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kodyhusky.cmcontrol.listeners;

import com.kodyhusky.cmcontrol.CompleteMobControl;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

/**
 *
 * @author Kody
 */
public class EntitySpawnListener implements Listener {
    
    CompleteMobControl plugin;
    
    public EntitySpawnListener(CompleteMobControl plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntitySpawn(EntitySpawnEvent event) {
        
        World world = plugin.getServer().getWorld("world");
        Location location = new Location(world,0,64,0);
        Double radius = 10.00;
        
        if (event.getEntityType() == EntityType.ZOMBIE) {
            plugin.getServer().broadcastMessage("Entity Spawned: " + event.getLocation().getX() + " " + event.getLocation().getZ());
            if (event.getLocation().distanceSquared(location) <= radius * radius) {
                plugin.getServer().broadcastMessage("Entity In Radius: " + event.getLocation().getX() + " " + event.getLocation().getZ());
            }
        }
    }
}

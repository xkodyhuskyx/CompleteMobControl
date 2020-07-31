/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kodyhusky.cmcontrol.listeners;

import com.kodyhusky.cmcontrol.CompleteMobControl;
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
        
        if (plugin.getWardManager().isSpawnBlocked(event.getEntity())) {
            event.setCancelled(true);
        }
    }
}

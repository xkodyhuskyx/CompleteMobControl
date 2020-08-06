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
package com.kodyhusky.cmcontrol.listeners;

import com.kodyhusky.cmcontrol.CompleteMobControl;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

/**
 * Listens for entity events.
 *
 * @author Kody
 */
public class EntitySpawnListener implements Listener {

    private final CompleteMobControl plugin;

    public EntitySpawnListener(CompleteMobControl plugin) {
        this.plugin = plugin;
    }

    /**
     * Called when an entity spawns.
     *
     * @param event Entity spawn event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (plugin.getPluginConfig().isFeatureEnabled("mobwards")) {
            if (plugin.getWardManager().isSpawnBlocked(event.getEntity())) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onFireStart(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getType() == Material.CAMPFIRE) {
            plugin.getServer().broadcastMessage("Fire Placed: " + event.getBlockPlaced().getLocation().toString());
            if (plugin.getWardManager().isValidMobWard(event.getBlockPlaced().getLocation())) {
                
            }
        }
    }
    
    
    
}

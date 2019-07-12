package com.kodyhusky.cmc.listeners;

import org.bukkit.entity.Creature;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import com.kodyhusky.cmc.CompleteMobControl;

public class EntitySpawnListener implements Listener {
	
	private CompleteMobControl plugin;
	
	public EntitySpawnListener(CompleteMobControl plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityTarget(EntityTargetEvent entityTargetEvent) {
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
    public void onEntitySpawn(CreatureSpawnEvent event) {
		if (plugin.getConfigManager().isFeatureEnabled("repeller-structures")) {
			if (event.getEntity() instanceof Creature) {
				/*EntityRepeller repeller = plugin.getRepellerManager().getRepellerByLocation(event.getLocation());*/
				
			}
		}
	}
}

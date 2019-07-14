package com.kodyhusky.cmc.listeners;

import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import com.kodyhusky.cmc.CompleteMobControl;
import com.kodyhusky.cmc.objects.EntityRepeller;

public class RepellerSpawnListener implements Listener {
	
	private CompleteMobControl plugin;
	
	public RepellerSpawnListener(CompleteMobControl plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityTarget(EntityTargetEvent entityTargetEvent) {
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
    public void onEntitySpawn2(CreatureSpawnEvent event) {
		if (event.getEntity() instanceof Creature && plugin.getRepellerManager()
				.getMonitoredEntities().contains(event.getEntityType())) {
			EntityRepeller repeller = plugin.getRepellerManager().getRepellerByLocation(event.getLocation());
			if (repeller.getEntitiesToRemove().contains(event.getEntityType())) {
				event.setCancelled(true);
		}}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntitySpawn(CreatureSpawnEvent event) {
		if (event.getEntity() instanceof LivingEntity) {
			
		}
		
		if event.getEntity().isT
	}
	
}

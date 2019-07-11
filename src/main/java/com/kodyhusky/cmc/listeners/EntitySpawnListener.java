package com.kodyhusky.cmc.listeners;

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
		
	}
}

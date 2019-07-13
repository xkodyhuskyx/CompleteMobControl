package com.kodyhusky.cmc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import com.kodyhusky.cmc.objects.EntityRepeller;

public class RepellerManager {
	
	private CompleteMobControl plugin;
	
	private HashMap<Location,EntityRepeller> activeRepellers;
	
	
	public RepellerManager(CompleteMobControl plugin) {
		this.plugin = plugin;
	}
	
	
	public EntityRepeller getRepellerByLocation(Location location) {
		
		
		activeRepellers.forEach((key,value) -> {
			
		});
		
		return null;
	}
	
	
	
	public EntityRepeller getRepellerByLocation2(Location location) {
		
		
		
		
		for (Location repellerLocation : enabledRepellerLocations) {
			if (repellerLocation.getWorld().getUID().equals(location.getWorld().getUID()) && 
					repellerLocation.getX() - repellerLocation.getYaw() < location.getX() &&
					repellerLocation.getX() + repellerLocation.getYaw() > location.getX() && 
					repellerLocation.getY() - repellerLocation.getYaw() < location.getY() && 
					repellerLocation.getY() + repellerLocation.getYaw() > location.getY() && 
					repellerLocation.getZ() - repellerLocation.getYaw() < location.getZ() && 
					repellerLocation.getZ() + repellerLocation.getYaw() > location.getZ()) {
				EntityRepeller repeller = loadedRepellers.get(repellerLocation.getPitch());
				if ((location.getY() < repellerLocation.getY() && repeller.repelsBelow()) && 
						(location.getY() >= repellerLocation.getY() && repeller.repelsAbove())) {
					return repeller;
				}}}
		return null;
	}

	public List<EntityType> getMonitoredEntities() {
		return enabledEntities;
	}

}

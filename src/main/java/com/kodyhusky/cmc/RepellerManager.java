package com.kodyhusky.cmc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Tameable;

import com.kodyhusky.cmc.objects.EntityRepeller;

public class RepellerManager {
	
	private CompleteMobControl plugin;
	private HashMap<UUID,EntityRepeller> activeRepellers;
	private List<String> removableEntities;
	
	
	public RepellerManager(CompleteMobControl plugin) {
		this.plugin = plugin;
	}
	
	
	public EntityRepeller getRepellerByLocation(Location location) {
		for(Map.Entry<UUID, EntityRepeller> entry : activeRepellers.entrySet()) {
			
			// Get Values From HashMap
			EntityRepeller repeller = entry.getValue();
			Location repellerLocation = repeller.getLocation();
			short radius = getMaterialRadius(repeller.getType());
			double x = repellerLocation.getX();
			double y = repellerLocation.getY();
			double z = repellerLocation.getZ();
			double testX = location.getX();
			double testY = location.getY();
			double testZ = location.getZ();
			
			// Check AreaMode Values
			switch(repeller.getAreaMode()) {
				case -1:
					if (testY >= y) {return null;}
				case 1:
					if (testY < y) {return null;}
			}
			
			// Check For General Location Match
			if (repellerLocation.getWorld().getUID().equals(location.getWorld().getUID()) && 
					x - radius < testX && x + radius > testX && 
					y - radius < testY && y + radius > testY && 
					z - radius < testZ && z + radius > testZ) {
				return repeller;
			}
		};
		return null;
	}
	
	
	
	
	
	
	
	
	
	public boolean isUntouchableEntity(LivingEntity entity) {
		
		// Check List Of Available Mobs
		if (!removableEntities.contains(entity.getType().name().toUpperCase())) {
			return true;
		}
		// Check If Entity Has Custom Name
		if (entity.getCustomName() != null && !removableEntities.contains("CUSTOM_NAMED")) {
			return true;
		}
		// Check For Tamed Entities
        if (entity instanceof Tameable) {
            if (((Tameable) entity).isTamed() && !removableEntities.contains("TAMED")) {
                return true;
            }
        }
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	private short getMaterialRadius(Material type) {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<EntityType> getMonitoredEntities() {
		// TODO
		return null;
	}

}

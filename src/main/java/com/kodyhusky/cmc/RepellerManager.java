package com.kodyhusky.cmc;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;

public class RepellerManager {
	
	private CompleteMobControl plugin;
	
	/*private HashMap<Double,EntityRepeller> loadedRepellers;*/
	
	// Pitch = Repeller ID | Yaw = Radius
	private List<Location> enabledRepellerLocations;
	
	public RepellerManager(CompleteMobControl plugin) {
		this.plugin = plugin;
	}
	
	/*public EntityRepeller getRepellerByLocation(Location location) {
		
		
		for (Location repellerLocation : enabledRepellerLocations) {
			double x = repellerLocation.getX() + repellerLocation.getYaw();
		}
		
		
		
		
		
		
		return null;
		double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        World world = location.getWorld();
        for (RepellerStructure repellerStructure : list) {
            int radius = plugin.config.getRadius(repellerStructure);
            if (!plugin.config.shouldRepelBelow() && repellerStructure.getY() > y) {
                return false;
            }
            if (repellerStructure.getX() - radius < x && repellerStructure.getX() + radius > x
                    && repellerStructure.getY() - radius < y && repellerStructure.getY() + radius > y
                    && repellerStructure.getZ() - radius < z && repellerStructure.getZ() + radius > z
                    && repellerStructure.getWorld().getUID().equals(world.getUID())) {
                return true;
            }
        }
        return false;
	}*/

}

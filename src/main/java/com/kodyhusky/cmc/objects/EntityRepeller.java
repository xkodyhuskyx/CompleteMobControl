package com.kodyhusky.cmc.objects;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

public class EntityRepeller {
    
    public EntityRepeller(FileConfiguration repellerConfig) {
        
    }
    
    private boolean repelsAbove = true;
    private boolean repelsBelow = true;
    private List<EntityType> repelledEntities;
    
    
    public boolean repelsAbove() {
    	return repelsAbove;
    }
    
    public boolean repelsBelow() {
    	return repelsBelow;
    }

	public List<EntityType> getEntitiesToRemove() {
		return repelledEntities;
	}
    
}

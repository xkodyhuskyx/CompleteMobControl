/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kodyhusky.cmcontrol.objects;

import java.util.ArrayList;
import org.bukkit.entity.EntityType;

/**
 *
 * @author Kody
 */
public class MobWard {
    
    private ArrayList<String> flags;
    private int size = 0;
    
    public boolean isFlagged(String flag) {
        return flags.contains(flag);
    }
    
    public int getSize() {
        return size;
    }
    
    
    public boolean isSpawnAllowed(EntityType type) {
        
        return false; 
    }
    
}

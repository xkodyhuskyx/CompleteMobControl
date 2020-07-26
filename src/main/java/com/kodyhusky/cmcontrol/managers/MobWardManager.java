/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kodyhusky.cmcontrol.managers;

import com.kodyhusky.cmcontrol.CompleteMobControl;
import com.kodyhusky.cmcontrol.objects.MobWard;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

/**
 *
 * @author Kody
 */
public class MobWardManager {

    private CompleteMobControl plugin;

    private HashMap<UUID, HashMap<Location, MobWard>> mobwards;

    public MobWardManager(CompleteMobControl plugin) {
        this.plugin = plugin;
    }

    public MobWard getMobWardRepelled(Location loc) {
        UUID world = loc.getWorld().getUID();
        if (mobwards.containsKey(world)) {
            for (Map.Entry entry : mobwards.get(world).entrySet()) {
                Location mwloc = (Location) entry.getKey();
                MobWard mward = (MobWard) entry.getValue();
                
                
                int size = mward.getSize();
                
                
                
                if (mward.isFlagged("CHECK_ABOVE") && loc.getY() >= mwloc.getY() ||
                        mward.isFlagged("CHECK_BELOW") && loc.getY() < mwloc.getY()) {
                    return mward;
                }
            }
        }
        return null;
    }

    public Boolean isSpawnAllowed(Location location, EntityType entity) {

        UUID world = location.getWorld().getUID();

        // Get All Wards On Specific World
        // Check Each For X and Z values
        // Check Y Value
        // Check EntityType
        
        return false;
    }

}

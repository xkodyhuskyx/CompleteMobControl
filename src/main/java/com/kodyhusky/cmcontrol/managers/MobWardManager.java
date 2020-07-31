package com.kodyhusky.cmcontrol.managers;

import com.kodyhusky.cmcontrol.CompleteMobControl;
import com.kodyhusky.cmcontrol.objects.MobWard;
import com.kodyhusky.cmcontrol.utils.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

/**
 *
 * @author xkodyhuskyx
 */
public class MobWardManager {

    private final CompleteMobControl plugin;
    
    private HashMap<UUID, MobWard> mobwards;
    private HashMap<UUID,Pair<Location,Integer>> activemw;

    public MobWardManager(CompleteMobControl plugin) {
        mobwards = new HashMap<>();
        activemw = new HashMap<>();
        this.plugin = plugin;
    }
    
    // =========================================================================
    // TEMPORARY TESTING FUNCTION (world,0,80,0) default
    // =========================================================================
    public void addDebugMobWard() {
        UUID uuid = UUID.randomUUID();
        UUID puuid = UUID.randomUUID();
        World world = plugin.getServer().getWorld("world");
        Location noloc = new Location(world,0,64,0);
        ArrayList<String> options = new ArrayList(Arrays.asList("SEARCH_BELOW","SEARCH_ABOVE"));
        MobWard debugward = new MobWard("debug",uuid,"AIR",noloc,noloc,puuid,
                (new ArrayList<>()),options,(new ArrayList<>()));
        mobwards.put(uuid, debugward);
        @SuppressWarnings("unchecked")
        Pair<Location,Integer> pair = (new Pair(noloc, 16));
        activemw.put(uuid, pair);
    }
    // =========================================================================
    
    public boolean isEntityAllowed(Entity entity) {
        
        
        
        
        
        
        if (options.contains("BLACKLIST")) {
            return !customlist.contains(entity.getType().toString().toUpperCase());
        }
        if (options.contains("WHITELIST")) {
            return customlist.contains(entity.getType().toString().toUpperCase());
        }
        return true;
    }

    public boolean isSpawnBlocked(Entity entity) {
        
        int rtype = 1;
        
        // Cube Type
        if (rtype == 0) {
        }
        // Sphere Radius Type
        if (rtype == 1) {
            for (Map.Entry<UUID,Pair<Location,Integer>> entry : activemw.entrySet()) {
                Location mwloc = entry.getValue().getKey();
                int radius = entry.getValue().getValue();
                if (entity.getLocation().distanceSquared(mwloc) <= radius * radius) {
                    plugin.getServer().broadcastMessage("ENTITY IN RADIUS: " + entity.getName()); // DEBUG
                    MobWard ward = mobwards.get(entry.getKey());
                    if ((ward.hasOption("SEARCH_BELOW") && entity.getLocation().getY() < mwloc.getY()) ||
                            (ward.hasOption("SEARCH_ABOVE") && entity.getLocation().getY() >= mwloc.getY())) {
                        if (ward.hasOption("BLACKLIST") || ward.hasOption("WHITELIST")) {
                            return !ward.isEntityAllowed(entity);
                        } else {
                            plugin.getServer().broadcastMessage("ENTITY SPAWN BLOCKED: " + entity.getName()); // DEBUG
                            return !isEntityAllowed(entity);
                        }
                    }
                }
            }
        }
        return false;
    }
}

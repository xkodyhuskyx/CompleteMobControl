package com.kodyhusky.cmcontrol.managers;

import com.kodyhusky.cmcontrol.CompleteMobControl;
import com.kodyhusky.cmcontrol.objects.MobWard;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javafx.util.Pair;
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
        this.plugin = plugin;
    }
    
    public void addDebugMobWard() {
        mobwards = new HashMap<>();
        activemw = new HashMap<>();
        UUID newuuid = UUID.randomUUID();
        World world = plugin.getServer().getWorld("world");
        MobWard debugward = new MobWard(
                "debug",newuuid,"",
                (new Location(world,0,64,0)),
                (new Location(world,0,64,0)),
                UUID.randomUUID(),
                null,null,true);
        mobwards.put(newuuid, debugward);
        @SuppressWarnings("unchecked")
        Pair<Location,Integer> pair = (new Pair(new Location(world,0,64,0), 10));
        activemw.put(newuuid, pair);
    }

    /**
     * Checks if an entity is blocked by an active MobWard.
     * 
     * @param entity A Entity to check
     * @return Boolean If the Entity spawn is allowed.
     */
    @SuppressWarnings("unchecked")
    public boolean isMWSpawnAllowed(Entity entity) {
        for (Map.Entry<UUID,Pair<Location,Integer>> entry : activemw.entrySet()) {
            UUID mward = entry.getKey();
            Location mwloc = entry.getValue().getKey();
            Integer radius = entry.getValue().getValue();
            if (entity.getLocation().distanceSquared(mwloc) <= radius * radius) {
                MobWard mwards = mobwards.get(mward);
                return mwards.isSpawnAllowed(entity);
            }
        }
        return false;
    }
}

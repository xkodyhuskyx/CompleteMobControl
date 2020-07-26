package com.kodyhusky.cmcontrol.managers;

import com.kodyhusky.cmcontrol.CompleteMobControl;
import com.kodyhusky.cmcontrol.objects.MobWard;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;

/**
 *
 * @author xkodyhuskyx
 */
public class MobWardManager {

    private CompleteMobControl plugin;

    private HashMap<UUID, HashMap<Location, MobWard>> activemobwards;
    private HashMap<UUID, HashMap<Location, MobWard>> mobwards;

    public MobWardManager(CompleteMobControl plugin) {
        this.plugin = plugin;
    }

    /**
     * Checks if a location is being controlled by an active MobWard.
     * Returns the MobWard controlling the location or null.
     * 
     * @param cl A Location to check
     * @return MobWard The MobWard controlling the given Location
     */
    public MobWard getActiveMobWard(Location cl) {
        UUID w = cl.getWorld().getUID();
        if (activemobwards.containsKey(w)) {
            for (Map.Entry entry : activemobwards.get(w).entrySet()) {
                Location ml = (Location) entry.getKey();
                MobWard mw = (MobWard) entry.getValue();
                int d = mw.getSize();
                double x1 = cl.getX(); double z1 = cl.getZ();
                double x2 = ml.getX(); double z2 = ml.getZ();
                if (x1 > x2 - d && x1 < x2 + d && z1 > z2 - d && z1 < z2 + d) {
                    if (mw.isFlagged("CHECK_ABOVE") && cl.getY() >= ml.getY() ||
                            mw.isFlagged("CHECK_BELOW") && cl.getY() < ml.getY()) {
                        return mw;
                    }
                }
            }
        }
        return null;
    }
}

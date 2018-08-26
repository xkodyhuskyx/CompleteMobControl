package com.kodyhusky.cmc;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class EntityMoveListener implements Runnable {
    
    CompleteMobControl plugin;
    
    public EntityMoveListener(CompleteMobControl plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void run() {
        for (World world : plugin.getServer().getWorlds()) {
            if (plugin.config.getEnabledWorlds().contains(world.getName())) {
                for (LivingEntity entity : world.getLivingEntities()) {
                    if (!(entity instanceof Player) && !plugin.config.isInvalidEntity((Entity)entity) && 
                        !entity.isLeashed() && plugin.config.isEntityTame((Entity)entity) && 
                        !(plugin.config.isNeutralEntity((Entity)entity) && !plugin.config.shouldRepelNeutralMobs()) && 
                        !(!plugin.config.getMobsToRepel().isEmpty() && !plugin.config.getMobsToRepel().contains(entity.getType()))) {
                        if (plugin.getCMClist().isRepelled(entity.getLocation())) {
                            if (entity.getFireTicks() <= 0 || entity.getHealth() > 1.0) {
                                entity.setHealth(1.0);
                                entity.setFireTicks(30000);
                            }
                            if (plugin.config.getDebugMode()) {
                                plugin.sM(plugin.console, entity.getType().name() + " " + plugin.getLang().get("entity_rep_killed_by") + " " + plugin.getCMClist().getRepelledBaseId(entity.getLocation()), "deb");
                            }
                        }
                    }
                }
            }
        }
    }
}

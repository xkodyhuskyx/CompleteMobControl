package com.kodyhusky.cmc.old;

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
                    if (!(entity instanceof Player) && !plugin.config.isInvalidEntity(entity) && !entity.isLeashed() && !plugin.config.isEntityTame(entity)) {
                        if (!plugin.config.getMobsToRepel().isEmpty() && plugin.config.getMobsToRepel().contains(entity.getType())) {
                            killMob(entity);
                        } else {
                            if (plugin.config.isNeutralEntity((Entity)entity) && plugin.config.shouldRepelNeutralMobs()) {
                                killMob(entity);
                            } else {
                                if (!plugin.config.isNeutralEntity(entity)) {
                                    killMob(entity);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void killMob(LivingEntity entity) {
        if (plugin.getCMClist().isRepelled(entity.getLocation())) {
            if (plugin.config.getRemoveType().equals("FIRE")) {
                if (entity.getFireTicks() <= 0 || entity.getHealth() > 1.0) {
                    entity.setHealth(1.0);
                    entity.setFireTicks(30000);
                }
            } else {
                entity.remove();
            }
            if (plugin.config.getDebugMode()) {
                plugin.sM(plugin.console, entity.getType().name() + " " + plugin.getLang().get("entity_rep_killed_by") + " " + plugin.getCMClist().getRepelledBaseId(entity.getLocation()), "deb");
            }
        }
    }
}

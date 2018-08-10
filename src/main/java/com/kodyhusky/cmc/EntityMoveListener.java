package com.kodyhusky.cmc;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class EntityMoveListener implements Runnable {
    
    CompleteMobControl plugin;
    
    public EntityMoveListener(CompleteMobControl plugin) {
        this.plugin = plugin;
        run();
    }
    
    @Override
    public void run() {
        Iterator<World> iterator = plugin.getWorlds().iterator();
        while (iterator.hasNext()) {
            for (LivingEntity livingEntity : iterator.next().getLivingEntities()) {
                Integer n = 1;
                if (plugin.config.isNeutralEntity((Entity)livingEntity) && !plugin.config.shouldRepelNeutralMobs()) {
                    n = 0;
                }
                if (plugin.config.isInvalidEntity((Entity)livingEntity)) {
                    n = 0;
                }
                if (n == 1) {
                    if (livingEntity.isLeashed()) {
                        n = 0;
                    }
                    if (plugin.config.isEntityTame((Entity)livingEntity)) {
                        n = 0;
                    }
                    if (n != 1 || livingEntity.getType() == EntityType.PLAYER) {
                        continue;
                    }
                    HashSet<EntityType> mobsToRepel = plugin.config.getMobsToRepel();
                    if (!mobsToRepel.isEmpty() && !mobsToRepel.contains(livingEntity.getType())) {
                        n = 0;
                    }
                    if (n != 1 || !plugin.getCMClist().isRepelled(livingEntity.getLocation())) {
                        continue;
                    }
                    if (livingEntity.getFireTicks() <= 0 || livingEntity.getHealth() > 1.0) {
                        livingEntity.setHealth(1.0);
                        livingEntity.setFireTicks(30000);
                    }
                    if (!plugin.config.getDebugMode()) {
                        continue;
                    }
                    plugin.sM(plugin.console, livingEntity.getType().name() + " " + plugin.getLang().get("entity_rep_killed_by") + " " + plugin.getCMClist().getRepelledBaseId(livingEntity.getLocation()), "deb");
                }
            }
        }
    }
}

package net.highkingdom.cmc;

import java.util.HashSet;
import java.util.Iterator;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.World;

public class EntityMoveListener implements Runnable
{
    CompleteMobControl plugin;
    
    public EntityMoveListener(final CompleteMobControl plugin) {
        this.plugin = plugin;
        this.run();
    }
    
    @Override
    public final void run() {
        final Iterator<World> iterator = this.plugin.getWorlds().iterator();
        while (iterator.hasNext()) {
            for (final LivingEntity livingEntity : iterator.next().getLivingEntities()) {
                Integer n = 1;
                if (this.plugin.config.isNeutralEntity((Entity)livingEntity) && !this.plugin.config.shouldRepelNeutralMobs()) {
                    n = 0;
                }
                if (this.plugin.config.isInvalidEntity((Entity)livingEntity)) {
                    n = 0;
                }
                if (n == 1) {
                    if (livingEntity.isLeashed()) {
                        n = 0;
                    }
                    if (this.plugin.config.isEntityTame((Entity)livingEntity)) {
                        n = 0;
                    }
                    if (n != 1 || livingEntity.getType() == EntityType.PLAYER) {
                        continue;
                    }
                    final HashSet<EntityType> mobsToRepel = this.plugin.config.getMobsToRepel();
                    if (!mobsToRepel.isEmpty() && !mobsToRepel.contains(livingEntity.getType())) {
                        n = 0;
                    }
                    if (n != 1 || !this.plugin.getCMClist().isRepelled(livingEntity.getLocation())) {
                        continue;
                    }
                    if (livingEntity.getFireTicks() <= 0 || livingEntity.getHealth() > 1.0) {
                        livingEntity.setHealth(1.0);
                        livingEntity.setFireTicks(30000);
                    }
                    if (!this.plugin.config.getDebugMode()) {
                        continue;
                    }
                    this.plugin.sM(this.plugin.console, livingEntity.getType().name() + " " + this.plugin.getLang().get("entity_rep_killed_by") + " " + this.plugin.getCMClist().getRepelledBaseId(livingEntity.getLocation()), "deb");
                }
            }
        }
    }
}

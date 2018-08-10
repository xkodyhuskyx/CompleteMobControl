package com.kodyhusky.cmc;

import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class FFieldMoveListener implements Runnable {

    CompleteMobControl plugin;

    public FFieldMoveListener(CompleteMobControl plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Iterator<World> iterator = plugin.getWorlds().iterator();
        while (iterator.hasNext()) {
            for (LivingEntity livingEntity : iterator.next().getLivingEntities()) {
                try {
                    if (livingEntity instanceof Player) {
                        continue;
                    }
                    if (plugin.config.isNeutralEntity((Entity) livingEntity)
                            && !plugin.config.shouldRepelNeutralMobs()) {
                        continue;
                    }
                    if (plugin.config.isInvalidEntity((Entity) livingEntity)) {
                        continue;
                    }
                    if (plugin.config.isEntityTame((Entity) livingEntity)) {
                        continue;
                    }
                    Location location = livingEntity.getLocation();
                    Block block = null;
                    for (int i = location.getBlockY(); i >= 0; --i) {
                        block = livingEntity.getWorld().getBlockAt(location.getBlockX(), i - 1, location.getBlockZ());
                        if (block.getTypeId() == plugin.config.getFFid()) {
                            break;
                        }
                        if (i >= 0) {
                            for (int j = location.getBlockY(); j <= 256; ++j) {
                                block = livingEntity.getWorld().getBlockAt(location.getBlockX(), j + 1,
                                        location.getBlockZ());
                                if (block.getTypeId() == plugin.config.getFFid()) {
                                    break;
                                }
                            }
                        }
                    }
                    if (block.getTypeId() != plugin.config.getFFid() || block.getBlockPower() <= 0
                            || (livingEntity.getFireTicks() > 0 && livingEntity.getHealth() <= 1.0)) {
                        continue;
                    }
                    livingEntity.setHealth(1.0);
                    livingEntity.setFireTicks(30000);
                } catch (Exception ex) {
                }
            }
        }
    }
}

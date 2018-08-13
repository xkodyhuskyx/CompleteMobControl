package com.kodyhusky.cmc;

import java.util.HashSet;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class EntitySpawnListener implements Listener {

    private CompleteMobControl plugin;

    public EntitySpawnListener(CompleteMobControl plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityTarget(EntityTargetEvent entityTargetEvent) {
        if (entityTargetEvent.getTarget() instanceof Player) {
            try {
                Player player = (Player) entityTargetEvent.getTarget();
                if (player.getInventory().getItemInMainHand().getItemMeta().getLore()
                        .contains(plugin.getLang().get("mob_rep_lore1"))
                        || player.getInventory().getItemInMainHand().getItemMeta().getLore()
                                .contains(plugin.getLang().get("mob_swo_lore1"))
                        || player.getInventory().getItemInOffHand().getItemMeta().getLore()
                                .contains(plugin.getLang().get("mob_rep_lore1"))
                        || player.getInventory().getItemInOffHand().getItemMeta().getLore()
                                .contains(plugin.getLang().get("mob_swo_lore1"))) {
                    entityTargetEvent.setCancelled(true);
                }
            } catch (Exception ex) {
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCraft(PrepareItemCraftEvent prepareItemCraftEvent) {
        if (prepareItemCraftEvent.getRecipe().getResult().hasItemMeta()) {
            String replace = prepareItemCraftEvent.getRecipe().getResult().getItemMeta().getDisplayName().toLowerCase()
                    .replace(" ", "");
            if ((prepareItemCraftEvent.getRecipe().getResult().getItemMeta().getDisplayName().toLowerCase()
                    .equals(plugin.getLang().get("mob_rep").toLowerCase())
                    || prepareItemCraftEvent.getRecipe().getResult().getItemMeta().getDisplayName().toLowerCase()
                            .equals(plugin.getLang().get("mob_swo").toLowerCase()))
                    && !prepareItemCraftEvent.getView().getPlayer().hasPermission("completemc.craft." + replace)) {
                prepareItemCraftEvent.getInventory().setResult((ItemStack) null);
                plugin.sM((CommandSender) prepareItemCraftEvent.getView().getPlayer(),
                        plugin.getLang().get("no_craft_perm") + " " + prepareItemCraftEvent.getRecipe().getResult()
                                .getItemMeta().getDisplayName().toLowerCase() + "!",
                        "err");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntitySpawn(CreatureSpawnEvent event) {
        if (event.getEntity() instanceof Creature) {
            LivingEntity entity = event.getEntity();
            if ((plugin.config.isNeutralEntity(entity) && !plugin.config.shouldRepelNeutralMobs())
                    || plugin.config.isInvalidEntity(entity) || plugin.config.isEntityTame(entity)
                    || (!plugin.config.getMobsToRepel().isEmpty()
                            && !plugin.config.getMobsToRepel().contains(event.getEntityType()))) {
            } else {
                if (plugin.getCMClist().isRepelled(event.getLocation())) {
                    if (plugin.config.getDebugMode()) {
                        plugin.sM(plugin.console,
                                event.getEntityType().name() + " " + plugin.getLang().get("entity_rep_killed_by") + " "
                                        + plugin.getCMClist().getRepelledBaseId(event.getLocation()),
                                "deb");
                    }
                    event.setCancelled(true);
                }
            }
        }
    }
}

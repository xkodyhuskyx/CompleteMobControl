package net.highkingdom.cmc;

import org.bukkit.entity.EntityType;
import java.util.HashSet;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.Listener;

public class EntitySpawnListener implements Listener
{
    private final CompleteMobControl plugin;
    
    public EntitySpawnListener(final CompleteMobControl plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityTarget(final EntityTargetEvent entityTargetEvent) {
        if (entityTargetEvent.getTarget() instanceof Player) {
            try {
                final Player player = (Player)entityTargetEvent.getTarget();
                if (player.getItemInHand().getItemMeta().getLore().contains(this.plugin.getLang().get("mob_rep_lore1"))) {
                    entityTargetEvent.setCancelled(true);
                }
                if (player.getItemInHand().getItemMeta().getLore().contains(this.plugin.getLang().get("mob_swo_lore1"))) {
                    entityTargetEvent.setCancelled(true);
                }
            }
            catch (Exception ex) {}
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCraft(final PrepareItemCraftEvent prepareItemCraftEvent) {
        if (prepareItemCraftEvent.getRecipe().getResult().hasItemMeta()) {
            final String replace = prepareItemCraftEvent.getRecipe().getResult().getItemMeta().getDisplayName().toLowerCase().replace(" ", "");
            if ((prepareItemCraftEvent.getRecipe().getResult().getItemMeta().getDisplayName().toLowerCase().equals(this.plugin.getLang().get("mob_rep").toLowerCase()) || prepareItemCraftEvent.getRecipe().getResult().getItemMeta().getDisplayName().toLowerCase().equals(this.plugin.getLang().get("mob_swo").toLowerCase())) && !prepareItemCraftEvent.getView().getPlayer().hasPermission("completemc.craft." + replace)) {
                prepareItemCraftEvent.getInventory().setResult((ItemStack)null);
                this.plugin.sM((CommandSender)prepareItemCraftEvent.getView().getPlayer(), this.plugin.getLang().get("no_craft_perm") + " " + prepareItemCraftEvent.getRecipe().getResult().getItemMeta().getDisplayName().toLowerCase() + "!", "err");
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntitySpawn(final CreatureSpawnEvent creatureSpawnEvent) {
        final LivingEntity entity = creatureSpawnEvent.getEntity();
        if (this.plugin.config.isNeutralEntity((Entity)entity) && !this.plugin.config.shouldRepelNeutralMobs()) {
            return;
        }
        if (this.plugin.config.isInvalidEntity((Entity)entity)) {
            return;
        }
        if (this.plugin.config.isEntityTame((Entity)entity)) {
            return;
        }
        final HashSet<EntityType> mobsToRepel = this.plugin.config.getMobsToRepel();
        if (!mobsToRepel.isEmpty() && !mobsToRepel.contains(creatureSpawnEvent.getEntityType())) {
            return;
        }
        if (this.plugin.getCMClist().isRepelled(creatureSpawnEvent.getLocation())) {
            if (this.plugin.config.getDebugMode()) {
                this.plugin.sM(this.plugin.console, creatureSpawnEvent.getEntityType().name() + " " + this.plugin.getLang().get("entity_rep_killed_by") + " " + this.plugin.getCMClist().getRepelledBaseId(creatureSpawnEvent.getLocation()), "deb");
            }
            creatureSpawnEvent.setCancelled(true);
        }
    }
}

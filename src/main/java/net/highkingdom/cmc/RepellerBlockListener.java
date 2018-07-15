package net.highkingdom.cmc;

import java.util.ArrayList;
import org.bukkit.World;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import java.util.Iterator;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.Listener;

public class RepellerBlockListener implements Listener
{
    private final CompleteMobControl plugin;
    
    public RepellerBlockListener(final CompleteMobControl plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityExplode(final EntityExplodeEvent entityExplodeEvent) {
        if (!entityExplodeEvent.isCancelled()) {
            for (final Block block : entityExplodeEvent.blockList()) {
                if (this.plugin.config.getRadius(block) != -1 && this.getAdjacentRepellerBlocks(block).size() > 0 && this.removeBrokenRepellers(block)) {
                    final Block block2 = this.getAdjacentRepellerBlocks(block).get(0);
                    this.plugin.sM(this.plugin.console, "A " + this.plugin.config.getStrength(block).toString() + " entity repeller was destroyed by " + entityExplodeEvent.getEntity().getName() + " at " + block2.getX() + "," + block2.getY() + "," + block2.getZ() + ".", "warn");
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlaceEvent(final BlockPlaceEvent blockPlaceEvent) {
        final Player player = blockPlaceEvent.getPlayer();
        final Block block = blockPlaceEvent.getBlock();
        if (this.plugin.config.getRadius(block) != -1) {
            for (final Block block2 : this.getAdjacentRepellerBlocks(block)) {
                if (!this.plugin.getCMClist().contains(block2)) {
                    if (!player.hasPermission(this.plugin.perm + ".ercreate") && !this.plugin.isDev((CommandSender)blockPlaceEvent.getPlayer())) {
                        continue;
                    }
                    this.plugin.getCMClist().add(block2);
                    final String string = this.plugin.config.getStrength(block2).toString();
                    this.plugin.sM((CommandSender)blockPlaceEvent.getPlayer(), this.plugin.getLang().get("rep_const_pl"), "norm");
                    this.plugin.sM(this.plugin.console, "A " + string + " entity repeller was constructed by " + blockPlaceEvent.getPlayer().getName() + " at " + block2.getX() + "," + block2.getY() + "," + block2.getZ() + ".", "warn");
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreakEvent(final BlockBreakEvent blockBreakEvent) {
        final Player player = blockBreakEvent.getPlayer();
        final Block block = blockBreakEvent.getBlock();
        if (this.plugin.config.getRadius(block) != -1 && this.getAdjacentRepellerBlocks(block).size() > 0) {
            if (!player.hasPermission(this.plugin.perm + ".erdestroy") && !this.plugin.isDev((CommandSender)blockBreakEvent.getPlayer())) {
                final Iterator<Block> iterator = this.getAdjacentRepellerBlocks(block).iterator();
                while (iterator.hasNext()) {
                    if (this.plugin.getCMClist().contains(iterator.next())) {
                        this.plugin.sM((CommandSender)player, this.plugin.getLang().get("rep_destr_deny"), "err");
                        blockBreakEvent.setCancelled(true);
                    }
                }
                return;
            }
            if (this.removeBrokenRepellers(block)) {
                final String string = this.plugin.config.getStrength(block).toString();
                final Block block2 = this.getAdjacentRepellerBlocks(block).get(0);
                this.plugin.sM((CommandSender)player, this.plugin.getLang().get("rep_destr_pl"), "norm");
                this.plugin.sM(this.plugin.console, "A " + string + " entity repeller was destroyed by " + blockBreakEvent.getPlayer().getName() + " at " + block2.getX() + "," + block2.getY() + "," + block2.getZ() + ".", "warn");
            }
        }
    }
    
    private boolean removeBrokenRepellers(final Block block) {
        final World world = block.getWorld();
        final RepellerList cmClist = this.plugin.getCMClist();
        final int x = block.getX();
        final int y = block.getY();
        final int z = block.getZ();
        Boolean b = false;
        if (cmClist.contains(world.getBlockAt(x + 1, y, z)) && cmClist.remove(world.getBlockAt(x + 1, y, z))) {
            b = true;
        }
        if (cmClist.contains(world.getBlockAt(x - 1, y, z)) && cmClist.remove(world.getBlockAt(x - 1, y, z))) {
            b = true;
        }
        if (cmClist.contains(world.getBlockAt(x, y - 1, z)) && cmClist.remove(world.getBlockAt(x, y - 1, z))) {
            b = true;
        }
        if (cmClist.contains(world.getBlockAt(x, y - 2, z)) && cmClist.remove(world.getBlockAt(x, y - 2, z))) {
            b = true;
        }
        if (cmClist.contains(world.getBlockAt(x, y, z + 1)) && cmClist.remove(world.getBlockAt(x, y, z + 1))) {
            b = true;
        }
        if (cmClist.contains(world.getBlockAt(x, y, z - 1)) && cmClist.remove(world.getBlockAt(x, y, z - 1))) {
            b = true;
        }
        if (cmClist.contains(block) && cmClist.remove(block)) {
            b = true;
        }
        return b;
    }
    
    public ArrayList<Block> getAdjacentRepellerBlocks(final Block block) {
        final ArrayList<Block> list = new ArrayList<Block>();
        this.addToSet(block, list);
        return this.getMatchingPatternBlocks(list);
    }
    
    private void addToSet(final Block block, final ArrayList<Block> list) {
        final World world = block.getWorld();
        list.add(block);
        final int x = block.getX();
        final int y = block.getY();
        final int z = block.getZ();
        if (this.plugin.config.getRadius(world.getBlockAt(x + 1, y, z)) != -1 && !list.contains(world.getBlockAt(x + 1, y, z))) {
            this.addToSet(world.getBlockAt(x + 1, y, z), list);
        }
        if (this.plugin.config.getRadius(world.getBlockAt(x - 1, y, z)) != -1 && !list.contains(world.getBlockAt(x - 1, y, z))) {
            this.addToSet(world.getBlockAt(x - 1, y, z), list);
        }
        if (this.plugin.config.getRadius(world.getBlockAt(x, y + 1, z)) != -1 && !list.contains(world.getBlockAt(x, y + 1, z))) {
            this.addToSet(world.getBlockAt(x, y + 1, z), list);
        }
        if (this.plugin.config.getRadius(world.getBlockAt(x, y - 1, z)) != -1 && !list.contains(world.getBlockAt(x, y - 1, z))) {
            this.addToSet(world.getBlockAt(x, y - 1, z), list);
        }
        if (this.plugin.config.getRadius(world.getBlockAt(x, y, z + 1)) != -1 && !list.contains(world.getBlockAt(x, y, z + 1))) {
            this.addToSet(world.getBlockAt(x, y, z + 1), list);
        }
        if (this.plugin.config.getRadius(world.getBlockAt(x, y, z - 1)) != -1 && !list.contains(world.getBlockAt(x, y, z - 1))) {
            this.addToSet(world.getBlockAt(x, y, z - 1), list);
        }
    }
    
    private ArrayList<Block> getMatchingPatternBlocks(final ArrayList<Block> list) {
        final ArrayList<Block> list2 = new ArrayList<Block>();
        for (final Block block : list) {
            if (CompleteMobControl.isBaseOfRepeller(block, this.plugin.config)) {
                list2.add(block);
            }
        }
        return list2;
    }
}

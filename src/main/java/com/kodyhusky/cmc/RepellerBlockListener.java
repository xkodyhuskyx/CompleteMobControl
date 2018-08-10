package com.kodyhusky.cmc;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class RepellerBlockListener implements Listener {

    private CompleteMobControl plugin;

    public RepellerBlockListener(CompleteMobControl plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityExplode(EntityExplodeEvent entityExplodeEvent) {
        if (!entityExplodeEvent.isCancelled()) {
            for (Block block : entityExplodeEvent.blockList()) {
                if (plugin.config.getRadius(block) != -1 && getAdjacentRepellerBlocks(block).size() > 0
                        && removeBrokenRepellers(block)) {
                    Block block2 = getAdjacentRepellerBlocks(block).get(0);
                    plugin.sM(plugin.console,
                            "A " + plugin.config.getStrength(block).toString() + " entity repeller was destroyed by "
                                    + entityExplodeEvent.getEntity().getName() + " at " + block2.getX() + ","
                                    + block2.getY() + "," + block2.getZ() + ".",
                            "warn");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlaceEvent(BlockPlaceEvent blockPlaceEvent) {
        Player player = blockPlaceEvent.getPlayer();
        Block block = blockPlaceEvent.getBlock();
        if (plugin.config.getRadius(block) != -1) {
            for (Block block2 : getAdjacentRepellerBlocks(block)) {
                if (!plugin.getCMClist().contains(block2)) {
                    if (!player.hasPermission(plugin.perm + ".ercreate")) {
                        break;
                    }
                    plugin.getCMClist().add(block2);
                    String string = plugin.config.getStrength(block2).toString();
                    plugin.sM((CommandSender) blockPlaceEvent.getPlayer(), plugin.getLang().get("rep_const_pl"),
                            "norm");
                    plugin.sM(plugin.console,
                            "A " + string + " entity repeller was constructed by "
                                    + blockPlaceEvent.getPlayer().getName() + " at " + block2.getX() + ","
                                    + block2.getY() + "," + block2.getZ() + ".",
                            "warn");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreakEvent(BlockBreakEvent blockBreakEvent) {
        Player player = blockBreakEvent.getPlayer();
        Block block = blockBreakEvent.getBlock();
        if (plugin.config.getRadius(block) != -1 && getAdjacentRepellerBlocks(block).size() > 0) {
            if (!player.hasPermission(plugin.perm + ".erdestroy")) {
                Iterator<Block> iterator = getAdjacentRepellerBlocks(block).iterator();
                while (iterator.hasNext()) {
                    if (plugin.getCMClist().contains(iterator.next())) {
                        plugin.sM((CommandSender) player, plugin.getLang().get("rep_destr_deny"), "err");
                        blockBreakEvent.setCancelled(true);
                    }
                }
                return;
            }
            if (removeBrokenRepellers(block)) {
                String string = plugin.config.getStrength(block).toString();
                Block block2 = getAdjacentRepellerBlocks(block).get(0);
                plugin.sM((CommandSender) player, plugin.getLang().get("rep_destr_pl"), "norm");
                plugin.sM(plugin.console,
                        "A " + string + " entity repeller was destroyed by " + blockBreakEvent.getPlayer().getName()
                                + " at " + block2.getX() + "," + block2.getY() + "," + block2.getZ() + ".",
                        "warn");
            }
        }
    }

    private boolean removeBrokenRepellers(Block block) {
        World world = block.getWorld();
        RepellerList cmClist = plugin.getCMClist();
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();
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

    public ArrayList<Block> getAdjacentRepellerBlocks(Block block) {
        ArrayList<Block> list = new ArrayList<Block>();
        addToSet(block, list);
        return getMatchingPatternBlocks(list);
    }

    private void addToSet(Block block, ArrayList<Block> list) {
        World world = block.getWorld();
        list.add(block);
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();
        if (plugin.config.getRadius(world.getBlockAt(x + 1, y, z)) != -1
                && !list.contains(world.getBlockAt(x + 1, y, z))) {
            addToSet(world.getBlockAt(x + 1, y, z), list);
        }
        if (plugin.config.getRadius(world.getBlockAt(x - 1, y, z)) != -1
                && !list.contains(world.getBlockAt(x - 1, y, z))) {
            addToSet(world.getBlockAt(x - 1, y, z), list);
        }
        if (plugin.config.getRadius(world.getBlockAt(x, y + 1, z)) != -1
                && !list.contains(world.getBlockAt(x, y + 1, z))) {
            addToSet(world.getBlockAt(x, y + 1, z), list);
        }
        if (plugin.config.getRadius(world.getBlockAt(x, y - 1, z)) != -1
                && !list.contains(world.getBlockAt(x, y - 1, z))) {
            addToSet(world.getBlockAt(x, y - 1, z), list);
        }
        if (plugin.config.getRadius(world.getBlockAt(x, y, z + 1)) != -1
                && !list.contains(world.getBlockAt(x, y, z + 1))) {
            addToSet(world.getBlockAt(x, y, z + 1), list);
        }
        if (plugin.config.getRadius(world.getBlockAt(x, y, z - 1)) != -1
                && !list.contains(world.getBlockAt(x, y, z - 1))) {
            addToSet(world.getBlockAt(x, y, z - 1), list);
        }
    }

    private ArrayList<Block> getMatchingPatternBlocks(ArrayList<Block> list) {
        ArrayList<Block> list2 = new ArrayList<Block>();
        for (Block block : list) {
            if (CompleteMobControl.isBaseOfRepeller(block, plugin.config)) {
                list2.add(block);
            }
        }
        return list2;
    }
}

package com.kodyhusky.cmc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

public class FFieldBlockListener implements Listener {
    
    public static CompleteMobControl plugin;
    List<String> blocks;

    public FFieldBlockListener(CompleteMobControl plugin) {
        blocks = Blocks.load("ffblocks.yml");
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent blockPlaceEvent) {
        Player player = blockPlaceEvent.getPlayer();
        Block block = blockPlaceEvent.getBlock();
        String string = block.getWorld().getName() + "," + block.getX() + "," + block.getY() + "," + block.getZ()
                + ",";
        if (block.getTypeId() == plugin.config.getFFid()
                && plugin.Toggler.containsKey(player.getName())) {
            blocks = Blocks.load("ffblocks.yml");
            if (!blocks.contains(string)) {
                blocks.add(string);
                plugin.sM((CommandSender) player,
                        plugin.getLang().get("ff_block_placed"), "norm");
                Blocks.save("ffblocks.yml", blocks);
            }
            if (!blockPlaceEvent.isCancelled()) {
                player.playEffect(player.getLocation(), Effect.CLICK1, 200);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent blockBreakEvent) {
        Player player = blockBreakEvent.getPlayer();
        Block block = blockBreakEvent.getBlock();
        String string = block.getWorld().getName() + "," + block.getX() + "," + block.getY() + "," + block.getZ()
                + ",";
        if (blockBreakEvent.isCancelled()) {
            return;
        }
        if (block.getTypeId() == plugin.config.getFFid()
                && CompleteMobControl.Properties.Exist("ffblocks.yml")
                && !plugin.isDev((CommandSender) blockBreakEvent.getPlayer())) {
            blocks = Blocks.load("ffblocks.yml");
            if (!blockBreakEvent.getPlayer().hasPermission("completemc.ffremove")) {
                plugin.sM((CommandSender) player,
                        plugin.getLang().get("ff_destroy_blocked"), "err");
                blockBreakEvent.setCancelled(true);
                return;
            }
            if (blocks.contains(string)) {
                block.getWorld().dropItem(block.getLocation(),
                        new ItemStack(plugin.config.getFFid(), 1));
                block.setTypeId(0);
                blocks.remove(string);
                Blocks.save("ffblocks.yml", blocks);
            }
            player.playEffect(player.getLocation(), Effect.CLICK2, 200);
            blockBreakEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent entityExplodeEvent) {
        if (!entityExplodeEvent.isCancelled()) {
            for (Block block : entityExplodeEvent.blockList()) {
                if (block.getTypeId() == plugin.config.getFFid()
                        && CompleteMobControl.Properties.Exist("ffblocks.yml")) {
                    blocks = Blocks.load("ffblocks.yml");
                    String string = block.getWorld().getName() + "," + block.getX() + "," + block.getY() + ","
                            + block.getZ() + ",";
                    if (!blocks.contains(string)) {
                        continue;
                    }
                    block.getWorld().dropItem(block.getLocation(),
                            new ItemStack(plugin.config.getFFid(), 1));
                    block.setTypeId(0);
                    blocks.remove(string);
                    Blocks.save("ffblocks.yml", blocks);
                }
            }
        }
    }

    public static Block[] getBlocksAroundLocation(Location location, int n, boolean b) {
        int blockX = location.getBlockX();
        int blockY = location.getBlockY();
        int blockZ = location.getBlockZ();
        ArrayList<Block> list = new ArrayList<Block>();
        for (int i = blockX - n; i <= blockX + n; ++i) {
            for (int j = b ? 0 : (blockY - n); j <= (b ? (blockY + n) : blockY); ++j) {
                for (int k = blockZ - n; k <= blockZ + n; ++k) {
                    list.add(location.getWorld().getBlockAt(i, j, k));
                }
            }
        }
        return list.toArray(new Block[list.size()]);
    }

    public static Block getBlockAroundLocation(Location location, int n, int n2) {
        for (Block block : getBlocksAroundLocation(location, n, true)) {
            if (block.getTypeId() == n2) {
                return block;
            }
        }
        return null;
    }

    public static class Blocks {
        public static List<String> load(String s) {
            ArrayList<String> list = new ArrayList<String>();
            if (CompleteMobControl.Properties.Exist(s)) {
                for (String s2 : CompleteMobControl.Properties.Load(s)) {
                    try {
                        String s3 = null;
                        int int1 = 0;
                        int int2 = 0;
                        int int3 = 0;
                        try {
                            s3 = s2.split(",")[0];
                            int1 = Integer.parseInt(s2.split(",")[1]);
                            int2 = Integer.parseInt(s2.split(",")[2]);
                            int3 = Integer.parseInt(s2.split(",")[3]);
                        } catch (NumberFormatException ex) {
                        }
                        World world = plugin.getServer().getWorld(s3);
                        if (world != null && world.getBlockTypeIdAt(int1, int2,
                                int3) == plugin.config.getFFid()) {
                            list.add(s2);
                        }
                    } catch (Exception ex2) {
                    }
                }
            }
            return list;
        }

        public static void save(String s, List<String> list) {
            ArrayList<String> list2 = new ArrayList<String>();
            for (String s2 : list) {
                try {
                    String s3 = null;
                    int int1 = 0;
                    int int2 = 0;
                    int int3 = 0;
                    try {
                        s3 = s2.split(",")[0];
                        int1 = Integer.parseInt(s2.split(",")[1]);
                        int2 = Integer.parseInt(s2.split(",")[2]);
                        int3 = Integer.parseInt(s2.split(",")[3]);
                    } catch (NumberFormatException ex) {
                    }
                    World world = plugin.getServer().getWorld(s3);
                    if (world == null || world.getBlockTypeIdAt(int1, int2, int3) != plugin.config
                            .getFFid()) {
                        continue;
                    }
                    list2.add(s2);
                } catch (Exception ex2) {
                }
            }
            CompleteMobControl.Properties.Save(s, list2.toArray(new String[list2.size()]));
        }
    }
}

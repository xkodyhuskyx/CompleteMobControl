package net.highkingdom.cmc;

import org.bukkit.World;
import java.util.ArrayList;
import org.bukkit.Location;
import java.util.Iterator;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.Effect;
import org.bukkit.command.CommandSender;
import org.bukkit.event.block.BlockPlaceEvent;
import java.util.List;
import org.bukkit.event.Listener;

public class FFieldBlockListener implements Listener
{
    public static CompleteMobControl plugin;
    List<String> blocks;
    
    public FFieldBlockListener(final CompleteMobControl plugin) {
        this.blocks = Blocks.load("ffblocks.yml");
        FFieldBlockListener.plugin = plugin;
    }
    
    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent blockPlaceEvent) {
        final Player player = blockPlaceEvent.getPlayer();
        final Block block = blockPlaceEvent.getBlock();
        final String string = block.getWorld().getName() + "," + block.getX() + "," + block.getY() + "," + block.getZ() + ",";
        if (block.getTypeId() == FFieldBlockListener.plugin.config.getFFid() && FFieldBlockListener.plugin.Toggler.containsKey(player.getName())) {
            this.blocks = Blocks.load("ffblocks.yml");
            if (!this.blocks.contains(string)) {
                this.blocks.add(string);
                FFieldBlockListener.plugin.sM((CommandSender)player, FFieldBlockListener.plugin.getLang().get("ff_block_placed"), "norm");
                Blocks.save("ffblocks.yml", this.blocks);
            }
            if (!blockPlaceEvent.isCancelled()) {
                player.playEffect(player.getLocation(), Effect.CLICK1, 200);
            }
        }
    }
    
    @EventHandler
    public void onBlockBreak(final BlockBreakEvent blockBreakEvent) {
        final Player player = blockBreakEvent.getPlayer();
        final Block block = blockBreakEvent.getBlock();
        final String string = block.getWorld().getName() + "," + block.getX() + "," + block.getY() + "," + block.getZ() + ",";
        if (blockBreakEvent.isCancelled()) {
            return;
        }
        if (block.getTypeId() == FFieldBlockListener.plugin.config.getFFid() && CompleteMobControl.Properties.Exist("ffblocks.yml") && !FFieldBlockListener.plugin.isDev((CommandSender)blockBreakEvent.getPlayer())) {
            this.blocks = Blocks.load("ffblocks.yml");
            if (!blockBreakEvent.getPlayer().hasPermission("completemc.ffremove")) {
                FFieldBlockListener.plugin.sM((CommandSender)player, FFieldBlockListener.plugin.getLang().get("ff_destroy_blocked"), "err");
                blockBreakEvent.setCancelled(true);
                return;
            }
            if (this.blocks.contains(string)) {
                block.getWorld().dropItem(block.getLocation(), new ItemStack(FFieldBlockListener.plugin.config.getFFid(), 1));
                block.setTypeId(0);
                this.blocks.remove(string);
                Blocks.save("ffblocks.yml", this.blocks);
            }
            player.playEffect(player.getLocation(), Effect.CLICK2, 200);
            blockBreakEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onEntityExplode(final EntityExplodeEvent entityExplodeEvent) {
        if (!entityExplodeEvent.isCancelled()) {
            for (final Block block : entityExplodeEvent.blockList()) {
                if (block.getTypeId() == FFieldBlockListener.plugin.config.getFFid() && CompleteMobControl.Properties.Exist("ffblocks.yml")) {
                    this.blocks = Blocks.load("ffblocks.yml");
                    final String string = block.getWorld().getName() + "," + block.getX() + "," + block.getY() + "," + block.getZ() + ",";
                    if (!this.blocks.contains(string)) {
                        continue;
                    }
                    block.getWorld().dropItem(block.getLocation(), new ItemStack(FFieldBlockListener.plugin.config.getFFid(), 1));
                    block.setTypeId(0);
                    this.blocks.remove(string);
                    Blocks.save("ffblocks.yml", this.blocks);
                }
            }
        }
    }
    
    public static Block[] getBlocksAroundLocation(final Location location, final int n, final boolean b) {
        final int blockX = location.getBlockX();
        final int blockY = location.getBlockY();
        final int blockZ = location.getBlockZ();
        final ArrayList<Block> list = new ArrayList<Block>();
        for (int i = blockX - n; i <= blockX + n; ++i) {
            for (int j = b ? 0 : (blockY - n); j <= (b ? (blockY + n) : blockY); ++j) {
                for (int k = blockZ - n; k <= blockZ + n; ++k) {
                    list.add(location.getWorld().getBlockAt(i, j, k));
                }
            }
        }
        return list.toArray(new Block[list.size()]);
    }
    
    public static Block getBlockAroundLocation(final Location location, final int n, final int n2) {
        for (final Block block : getBlocksAroundLocation(location, n, true)) {
            if (block.getTypeId() == n2) {
                return block;
            }
        }
        return null;
    }
    
    public static class Blocks
    {
        public static List<String> load(final String s) {
            final ArrayList<String> list = new ArrayList<String>();
            if (CompleteMobControl.Properties.Exist(s)) {
                for (final String s2 : CompleteMobControl.Properties.Load(s)) {
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
                        }
                        catch (NumberFormatException ex) {}
                        final World world = FFieldBlockListener.plugin.getServer().getWorld(s3);
                        if (world != null && world.getBlockTypeIdAt(int1, int2, int3) == FFieldBlockListener.plugin.config.getFFid()) {
                            list.add(s2);
                        }
                    }
                    catch (Exception ex2) {}
                }
            }
            return list;
        }
        
        public static void save(final String s, final List<String> list) {
            final ArrayList<String> list2 = new ArrayList<String>();
            for (final String s2 : list) {
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
                    }
                    catch (NumberFormatException ex) {}
                    final World world = FFieldBlockListener.plugin.getServer().getWorld(s3);
                    if (world == null || world.getBlockTypeIdAt(int1, int2, int3) != FFieldBlockListener.plugin.config.getFFid()) {
                        continue;
                    }
                    list2.add(s2);
                }
                catch (Exception ex2) {}
            }
            CompleteMobControl.Properties.Save(s, list2.toArray(new String[list2.size()]));
        }
    }
}

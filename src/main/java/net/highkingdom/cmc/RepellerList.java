package net.highkingdom.cmc;

import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.bukkit.plugin.Plugin;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import org.bukkit.Material;
import org.bukkit.block.Block;
import java.util.Iterator;
import org.bukkit.World;
import org.bukkit.Location;
import java.util.ArrayList;
import java.io.File;

public class RepellerList
{
    private final File file;
    public ArrayList<RepellerStructure> list;
    private final CompleteMobControl plugin;
    
    public RepellerList(final String s, final CompleteMobControl plugin) {
        this.plugin = plugin;
        this.file = new File(s + "entityrepellers.list");
        this.list = new ArrayList<RepellerStructure>();
        this.load();
    }
    
    public int getRepelledBaseId(final Location location) {
        final double x = location.getX();
        final double y = location.getY();
        final double z = location.getZ();
        final World world = location.getWorld();
        for (int i = 0; i < this.list.size(); ++i) {
            final RepellerStructure repellerStructure = this.list.get(i);
            final int radius = this.plugin.config.getRadius(repellerStructure);
            if (repellerStructure.getX() - radius < x && repellerStructure.getX() + radius > x && repellerStructure.getY() - radius < y && repellerStructure.getY() + radius > y && repellerStructure.getZ() - radius < z && repellerStructure.getZ() + radius > z && repellerStructure.getWorld().getUID().equals(world.getUID())) {
                return i + 1;
            }
        }
        return -1;
    }
    
    public boolean isRepelled(final Location location) {
        final double x = location.getX();
        final double y = location.getY();
        final double z = location.getZ();
        final World world = location.getWorld();
        for (final RepellerStructure repellerStructure : this.list) {
            final int radius = this.plugin.config.getRadius(repellerStructure);
            if (!this.plugin.config.shouldRepelBelow() && repellerStructure.getY() > y) {
                return false;
            }
            if (repellerStructure.getX() - radius < x && repellerStructure.getX() + radius > x && repellerStructure.getY() - radius < y && repellerStructure.getY() + radius > y && repellerStructure.getZ() - radius < z && repellerStructure.getZ() + radius > z && repellerStructure.getWorld().getUID().equals(world.getUID())) {
                return true;
            }
        }
        return false;
    }
    
    public void add(final Block block) {
        this.list.add(new RepellerStructure(block));
        this.save();
    }
    
    public void add(final int n, final int n2, final int n3, final World world, final Material material, final int n4) {
        this.list.add(new RepellerStructure(n, n2, n3, world, material, n4));
        this.save();
    }
    
    public boolean remove(final Block block) {
        final int positionOfRepeller = this.getPositionOfRepeller(block);
        if (positionOfRepeller > -1) {
            this.list.remove(positionOfRepeller);
            this.save();
            return true;
        }
        return false;
    }
    
    public boolean remove(final int n) {
        if (n > 0 && n <= this.list.size()) {
            this.list.remove(n - 1);
            this.save();
            return true;
        }
        return false;
    }
    
    public void removeAll() {
        this.list.clear();
        this.save();
    }
    
    public Block getCenterBlock(final Block block) {
        if (this.plugin.getCMClist().contains(block.getLocation().getBlock())) {
            return block;
        }
        final Location location = block.getLocation();
        location.setX(location.getX() + 1.0);
        if (this.plugin.getCMClist().contains(location.getBlock())) {
            return location.getBlock();
        }
        location.setX(location.getX() - 1.0);
        location.setX(location.getX() - 1.0);
        if (this.plugin.getCMClist().contains(location.getBlock())) {
            return location.getBlock();
        }
        location.setX(location.getX() + 1.0);
        location.setZ((double)(block.getZ() + 1));
        if (this.plugin.getCMClist().contains(location.getBlock())) {
            return location.getBlock();
        }
        location.setZ(location.getZ() - 1.0);
        location.setZ((double)(block.getZ() - 1));
        if (this.plugin.getCMClist().contains(location.getBlock())) {
            return location.getBlock();
        }
        location.setZ(location.getZ() + 1.0);
        location.setY(location.getY() - 1.0);
        if (this.plugin.getCMClist().contains(location.getBlock())) {
            return location.getBlock();
        }
        location.setY(location.getY() + 1.0);
        location.setY(location.getY() - 2.0);
        if (this.plugin.getCMClist().contains(location.getBlock())) {
            return location.getBlock();
        }
        return null;
    }
    
    private int getPositionOfRepeller(final Block block) {
        for (int i = 0; i < this.list.size(); ++i) {
            final RepellerStructure repellerStructure = this.list.get(i);
            if (repellerStructure.getX() == block.getX() && repellerStructure.getY() == block.getY() && repellerStructure.getZ() == block.getZ() && repellerStructure.getWorld().getUID().toString().equals(block.getWorld().getUID().toString())) {
                return i;
            }
        }
        return -1;
    }
    
    public ArrayList<RepellerStructure> getList() {
        return this.list;
    }
    
    public boolean contains(final Block block) {
        return this.getPositionOfRepeller(block) > -1;
    }
    
    private void load() {
        if (this.file.exists()) {
            try (final FileInputStream fileInputStream = new FileInputStream(this.file);
                 final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream))) {
                try {
                    int n = 0;
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        ++n;
                        if (!"".equals(line.trim())) {
                            final String[] split = line.split("\\,");
                            try {
                                final int int1 = Integer.parseInt(split[0]);
                                final int int2 = Integer.parseInt(split[1]);
                                final int int3 = Integer.parseInt(split[2]);
                                String s = null;
                                final ArrayList<World> worlds = this.plugin.getWorlds();
                                boolean b = false;
                                this.plugin.sM(this.plugin.console, "Searching for entity repeller number " + n + " at " + int1 + "," + int2 + "," + int3, "deb");
                                if (split.length >= 4) {
                                    s = split[3];
                                    this.plugin.sM(this.plugin.console, "Entity repeller number " + n + " has wUID " + s, "deb");
                                }
                                for (final World world : worlds) {
                                    this.plugin.sM(this.plugin.console, "Block matching entity repeller in world is type " + world.getBlockAt(int1, int2, int3).getType().toString(), "deb");
                                    if ((s != null && world.getUID().toString().equals(s)) || CompleteMobControl.isBaseOfRepeller(world.getBlockAt(int1, int2, int3), this.plugin.config)) {
                                        if (!CompleteMobControl.isBaseOfRepeller(world.getBlockAt(int1, int2, int3), this.plugin.config)) {
                                            this.plugin.sM(this.plugin.console, "Entity repeller at " + int1 + ", " + int2 + ", " + int3 + " in world " + world.getName() + " may be invalid.", "warn");
                                        }
                                        if (!this.contains(world.getBlockAt(int1, int2, int3))) {
                                            this.plugin.sM(this.plugin.console, "Found entity repeller number " + n + " in world " + world.getName() + ".", "deb");
                                            this.list.add(new RepellerStructure(world.getBlockAt(int1, int2, int3)));
                                            b = true;
                                            break;
                                        }
                                        continue;
                                    }
                                }
                                if (b) {
                                    continue;
                                }
                                this.plugin.sM(this.plugin.console, "No entity repeller was found that matches entry on line " + n + ". Ignoring entry.", "warn");
                            }
                            catch (NumberFormatException ex4) {
                                this.plugin.sM(this.plugin.console, "Malformed repeller entry on line " + n + ". Ignoring entry.", "warn");
                            }
                        }
                    }
                }
                catch (IOException ex) {
                    this.plugin.sM(this.plugin.console, "Error reading from repellers.list. Aborting load.", "err");
                    this.plugin.sM(this.plugin.console, ex.getLocalizedMessage(), "err");
                    this.plugin.getServer().getPluginManager().disablePlugin((Plugin)this.plugin);
                    return;
                }
            }
            catch (FileNotFoundException ex2) {
                this.plugin.sM(this.plugin.console, "Unable to read from configuration file!", "err");
                this.plugin.sM(this.plugin.console, ex2.getLocalizedMessage(), "err");
            }
            catch (IOException ex3) {
                this.plugin.sM(this.plugin.console, ex3.getLocalizedMessage(), "err");
            }
        }
        else {
            this.plugin.sM(this.plugin.console, "No storage file was found. Creating a new one for you.", "norm");
        }
        if (!this.plugin.reload) {
            this.plugin.sM(this.plugin.console, "The plugin has been successfully enabled! :)", "norm");
        }
        else {
            this.plugin.reload = false;
        }
        this.save();
    }
    
    private void save() {
        try {
            if (this.file.exists()) {
                this.file.delete();
            }
            this.file.createNewFile();
            try (final FileWriter fileWriter = new FileWriter(this.file)) {
                final Iterator<RepellerStructure> iterator = this.list.iterator();
                while (iterator.hasNext()) {
                    fileWriter.write(iterator.next().toString() + "\n");
                }
            }
        }
        catch (IOException ex) {
            this.plugin.sM(this.plugin.console, ex.getLocalizedMessage(), "err");
        }
    }
}

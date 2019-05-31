package com.kodyhusky.cmc.old;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

public class RepellerList {

    private File file;
    public ArrayList<RepellerStructure> list;
    private CompleteMobControl plugin;

    public RepellerList(String s, CompleteMobControl plugin) {
        this.plugin = plugin;
        file = new File(s + "entityrepellers.list");
        list = new ArrayList<RepellerStructure>();
        load();
    }

    public int getRepelledBaseId(Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        World world = location.getWorld();
        for (int i = 0; i < list.size(); ++i) {
            RepellerStructure repellerStructure = list.get(i);
            int radius = plugin.config.getRadius(repellerStructure);
            if (repellerStructure.getX() - radius < x && repellerStructure.getX() + radius > x
                    && repellerStructure.getY() - radius < y && repellerStructure.getY() + radius > y
                    && repellerStructure.getZ() - radius < z && repellerStructure.getZ() + radius > z
                    && repellerStructure.getWorld().getUID().equals(world.getUID())) {
                return i + 1;
            }
        }
        return -1;
    }

    public boolean isRepelled(Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        World world = location.getWorld();
        for (RepellerStructure repellerStructure : list) {
            int radius = plugin.config.getRadius(repellerStructure);
            if (!plugin.config.shouldRepelBelow() && repellerStructure.getY() > y) {
                return false;
            }
            if (repellerStructure.getX() - radius < x && repellerStructure.getX() + radius > x
                    && repellerStructure.getY() - radius < y && repellerStructure.getY() + radius > y
                    && repellerStructure.getZ() - radius < z && repellerStructure.getZ() + radius > z
                    && repellerStructure.getWorld().getUID().equals(world.getUID())) {
                return true;
            }
        }
        return false;
    }

    public void add(Block block) {
        list.add(new RepellerStructure(block));
        save();
    }

    public void add(int n, int n2, int n3, World world, Material material, int n4) {
        list.add(new RepellerStructure(n, n2, n3, world, material, n4));
        save();
    }

    public boolean remove(Block block) {
        int positionOfRepeller = getPositionOfRepeller(block);
        if (positionOfRepeller > -1) {
            list.remove(positionOfRepeller);
            save();
            return true;
        }
        return false;
    }

    public boolean remove(int n) {
        if (n > 0 && n <= list.size()) {
            list.remove(n - 1);
            save();
            return true;
        }
        return false;
    }

    public void removeAll() {
        list.clear();
        save();
    }

    public Block getCenterBlock(Block block) {
        if (plugin.getCMClist().contains(block.getLocation().getBlock())) {
            return block;
        }
        Location location = block.getLocation();
        location.setX(location.getX() + 1.0);
        if (plugin.getCMClist().contains(location.getBlock())) {
            return location.getBlock();
        }
        location.setX(location.getX() - 1.0);
        location.setX(location.getX() - 1.0);
        if (plugin.getCMClist().contains(location.getBlock())) {
            return location.getBlock();
        }
        location.setX(location.getX() + 1.0);
        location.setZ((double) (block.getZ() + 1));
        if (plugin.getCMClist().contains(location.getBlock())) {
            return location.getBlock();
        }
        location.setZ(location.getZ() - 1.0);
        location.setZ((double) (block.getZ() - 1));
        if (plugin.getCMClist().contains(location.getBlock())) {
            return location.getBlock();
        }
        location.setZ(location.getZ() + 1.0);
        location.setY(location.getY() - 1.0);
        if (plugin.getCMClist().contains(location.getBlock())) {
            return location.getBlock();
        }
        location.setY(location.getY() + 1.0);
        location.setY(location.getY() - 2.0);
        if (plugin.getCMClist().contains(location.getBlock())) {
            return location.getBlock();
        }
        return null;
    }

    private int getPositionOfRepeller(Block block) {
        for (int i = 0; i < list.size(); ++i) {
            RepellerStructure repellerStructure = list.get(i);
            if (repellerStructure.getX() == block.getX() && repellerStructure.getY() == block.getY()
                    && repellerStructure.getZ() == block.getZ()
                    && repellerStructure.getWorld().getUID().toString().equals(block.getWorld().getUID().toString())) {
                return i;
            }
        }
        return -1;
    }

    public ArrayList<RepellerStructure> getList() {
        return list;
    }

    public boolean contains(Block block) {
        return getPositionOfRepeller(block) > -1;
    }

    private void load() {
        if (file.exists()) {
            try (FileInputStream fileInputStream = new FileInputStream(file);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream))) {
                try {
                    int n = 0;
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        ++n;
                        if (!"".equals(line.trim())) {
                            String[] split = line.split("\\,");
                            try {
                                int int1 = Integer.parseInt(split[0]);
                                int int2 = Integer.parseInt(split[1]);
                                int int3 = Integer.parseInt(split[2]);
                                String s = null;
                                ArrayList<World> worlds = plugin.getWorlds();
                                boolean b = false;
                                plugin.sM(plugin.console, "Searching for entity repeller number " + n + " at " + int1
                                        + "," + int2 + "," + int3, "deb");
                                if (split.length >= 4) {
                                    s = split[3];
                                    plugin.sM(plugin.console, "Entity repeller number " + n + " has wUID " + s, "deb");
                                }
                                for (World world : worlds) {
                                    plugin.sM(plugin.console, "Block matching entity repeller in world is type "
                                            + world.getBlockAt(int1, int2, int3).getType().toString(), "deb");
                                    if ((s != null && world.getUID().toString().equals(s)) || CompleteMobControl
                                            .isBaseOfRepeller(world.getBlockAt(int1, int2, int3), plugin.config)) {
                                        if (!CompleteMobControl.isBaseOfRepeller(world.getBlockAt(int1, int2, int3),
                                                plugin.config)) {
                                            plugin.sM(plugin.console,
                                                    "Entity repeller at " + int1 + ", " + int2 + ", " + int3
                                                            + " in world " + world.getName() + " may be invalid.",
                                                    "warn");
                                        }
                                        if (!contains(world.getBlockAt(int1, int2, int3))) {
                                            plugin.sM(plugin.console, "Found entity repeller number " + n + " in world "
                                                    + world.getName() + ".", "deb");
                                            list.add(new RepellerStructure(world.getBlockAt(int1, int2, int3)));
                                            b = true;
                                            break;
                                        }
                                        continue;
                                    }
                                }
                                if (b) {
                                    continue;
                                }
                                plugin.sM(plugin.console, "No entity repeller was found that matches entry on line " + n
                                        + ". Ignoring entry.", "warn");
                            } catch (NumberFormatException ex4) {
                                plugin.sM(plugin.console, "Malformed repeller entry on line " + n + ". Ignoring entry.",
                                        "warn");
                            }
                        }
                    }
                } catch (IOException ex) {
                    plugin.sM(plugin.console, "Error reading from repellers.list. Aborting load.", "err");
                    plugin.sM(plugin.console, ex.getLocalizedMessage(), "err");
                    plugin.getServer().getPluginManager().disablePlugin((Plugin) plugin);
                    return;
                }
            } catch (FileNotFoundException ex2) {
                plugin.sM(plugin.console, "Unable to read from configuration file!", "err");
                plugin.sM(plugin.console, ex2.getLocalizedMessage(), "err");
            } catch (IOException ex3) {
                plugin.sM(plugin.console, ex3.getLocalizedMessage(), "err");
            }
        } else {
            plugin.sM(plugin.console, "No storage file was found. Creating a new one for you.", "norm");
        }
        if (!plugin.reload) {
            plugin.sM(plugin.console, "The plugin has been successfully enabled! :)", "norm");
        } else {
            plugin.reload = false;
        }
        save();
    }

    private void save() {
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            try (FileWriter fileWriter = new FileWriter(file)) {
                Iterator<RepellerStructure> iterator = list.iterator();
                while (iterator.hasNext()) {
                    fileWriter.write(iterator.next().toString() + "\n");
                }
            }
        } catch (IOException ex) {
            plugin.sM(plugin.console, ex.getLocalizedMessage(), "err");
        }
    }
}

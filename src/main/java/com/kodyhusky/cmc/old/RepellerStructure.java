package com.kodyhusky.cmc.old;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class RepellerStructure {

    private int x;
    private int y;
    private int z;
    private Material material;
    private World world;
    private int data;

    @SuppressWarnings("deprecation")
	public RepellerStructure(Block block) {
        this(block.getX(), block.getY(), block.getZ(), block.getWorld(), block.getType(), block.getData());
    }

    public RepellerStructure(int x, int y, int z, World world, Material material, int data) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.material = material;
        this.data = data;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Material getMaterial() {
        return material;
    }

    public World getWorld() {
        return world;
    }

    public int getBlockData() {
        return data;
    }

    @Override
    public String toString() {
        return x + "," + y + "," + z + "," + world.getUID();
    }
}

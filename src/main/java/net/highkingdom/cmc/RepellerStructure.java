package net.highkingdom.cmc;

import org.bukkit.block.Block;
import org.bukkit.World;
import org.bukkit.Material;

public class RepellerStructure
{
    private int x;
    private int y;
    private int z;
    private Material material;
    private World world;
    private int data;
    
    public RepellerStructure(final Block block) {
        this(block.getX(), block.getY(), block.getZ(), block.getWorld(), block.getType(), block.getData());
    }
    
    public RepellerStructure(final int x, final int y, final int z, final World world, final Material material, final int data) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.material = material;
        this.data = data;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getZ() {
        return this.z;
    }
    
    public Material getMaterial() {
        return this.material;
    }
    
    public World getWorld() {
        return this.world;
    }
    
    public int getBlockData() {
        return this.data;
    }
    
    @Override
    public String toString() {
        return this.x + "," + this.y + "," + this.z + "," + this.world.getUID();
    }
}

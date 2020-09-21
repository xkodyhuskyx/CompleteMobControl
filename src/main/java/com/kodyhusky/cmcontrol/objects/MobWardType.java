package com.kodyhusky.cmcontrol.objects;

import org.bukkit.Material;

public class MobWardType {

    private final String name;
    private final Material material;
    private final int radius;

    public MobWardType(String name, Material material, int radius) {
        this.name = name.toLowerCase();
        this.material = material;
        this.radius = radius;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public int getRadius() {
        return this.radius;
    }
}

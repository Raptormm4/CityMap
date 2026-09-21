package com.raptormm4.cityMap;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

// See https://github.com/jwkerr/Quarters for cuboid code used for reference in developing this.
public class Cuboid {

    private final Location cornerOne, cornerTwo;
    private final World world;
    private final Block cornerBlockOne, cornerBlockTwo;
    private final BoundingBox bounding;
    private final int minX, minZ, maxX, maxZ, length, width, area;
    private int id;
    private Set<Integer> existingCuboidIds;

    public Cuboid(@NotNull Location cornerOne, @NotNull Location cornerTwo) {
        this.cornerOne = cornerOne;
        this.cornerTwo = cornerTwo;
        this.world = cornerOne.getWorld();
        this.cornerBlockOne = world.getBlockAt(cornerOne);
        this.cornerBlockTwo = world.getBlockAt(cornerTwo);
        this.bounding = BoundingBox.of(cornerBlockOne, cornerBlockTwo);
        this.minX = Math.min(cornerOne.getBlockX(), cornerTwo.getBlockX());
        this.minZ = Math.min(cornerOne.getBlockZ(), cornerTwo.getBlockZ());
        this.maxX = Math.max(cornerOne.getBlockX(), cornerTwo.getBlockX());
        this.maxZ = Math.max(cornerOne.getBlockZ(), cornerTwo.getBlockZ());
        this.length = Math.abs(cornerOne.getBlockX() - cornerTwo.getBlockX());
        this.width = Math.abs(cornerOne.getBlockZ() - cornerTwo.getBlockZ());
        this.area = length * width;

        do {
            int min = 100000000;
            int max = 999999999;
            this.id = ThreadLocalRandom.current().nextInt(min, max + 1);
        } while (existingCuboidIds.contains(this.id));
        existingCuboidIds.add(this.id);
    }

    public Cuboid(int id, Location cornerOne, Location cornerTwo) {
        this.id = id;
        this.cornerOne = cornerOne;
        this.cornerTwo = cornerTwo;
        this.world = cornerOne.getWorld();
        this.cornerBlockOne = world.getBlockAt(cornerOne);
        this.cornerBlockTwo = world.getBlockAt(cornerTwo);
        this.bounding = BoundingBox.of(cornerBlockOne, cornerBlockTwo);
        this.minX = Math.min(cornerOne.getBlockX(), cornerTwo.getBlockX());
        this.minZ = Math.min(cornerOne.getBlockZ(), cornerTwo.getBlockZ());
        this.maxX = Math.max(cornerOne.getBlockX(), cornerTwo.getBlockX());
        this.maxZ = Math.max(cornerOne.getBlockZ(), cornerTwo.getBlockZ());
        this.length = Math.abs(cornerOne.getBlockX() - cornerTwo.getBlockX());
        this.width = Math.abs(cornerOne.getBlockZ() - cornerTwo.getBlockZ());
        this.area = length * width;
    }

    // Check valid
    public boolean intersects(Cuboid other) {
        return minX < other.maxX
                && maxX > other.minX
                && minZ < other.maxZ
                && maxZ > other.minZ;
    }

    // Check pos
    public boolean isInside(Location location) {
        double x = location.x();
        double z = location.z();

        return x >= minX && x <= maxX
                && z >= minZ && z <= maxZ;
    }

    // Retrieve functions
    public Location getCornerOne() {
        return cornerOne;
    }
    public Location getCornerTwo() {
        return cornerTwo;
    }
    public World getWorld() {
        return world;
    }
    public Block getCornerBlockOne() {
        return cornerBlockOne;
    }
    public Block getCornerBlockTwo() {
        return cornerBlockTwo;
    }
    public BoundingBox getBounding() {
        return bounding;
    }
    public int getMinX() {
        return minX;
    }
    public int getMaxX() {
        return maxX;
    }
    public int getMinZ() {
        return minZ;
    }
    public int getMaxZ() {
        return maxZ;
    }
    public int getLength() {
        return length;
    }
    public int getWidth() {
        return width;
    }
    public int getCuboidArea() {
        return area;
    }
    public int getCuboidId() {
        return id;
    }

}

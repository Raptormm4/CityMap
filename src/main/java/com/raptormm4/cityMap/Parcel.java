package com.raptormm4.cityMap;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Parcel {

    private UUID creator;
    private long tEff = System.currentTimeMillis();
    private List<Cuboid> cuboids;
    private int id;
    private String owner;
    private Float price;
    private String zoning;
    private boolean forSale;
    private int area;
    private List<UUID> trusted;

    private Set<Integer> existingParcelIds;

    // Creation and action functions
    public Parcel(UUID creator, Cuboid cuboid, String zoning) {
        this.creator = creator;
        this.cuboids.add(cuboid);
        this.area = cuboid.getCuboidArea();
        this.zoning = zoning;

        // Generate id
        do {
            int min = 1000000;
            int max = 9999999;
            this.id = ThreadLocalRandom.current().nextInt(min, max + 1);
        } while (existingParcelIds.contains(this.id));
        existingParcelIds.add(this.id);
    }
    public Parcel(UUID creator, long tEff, List<Cuboid> cuboids, int id, String owner, String zoning, int area, List<UUID> trusted) {
        this.creator = creator;
        this.tEff = tEff;
        this.cuboids = cuboids;
        this.id = id;
        this.owner = owner;
        this.zoning = zoning;
        this.area = area;
        this.trusted = trusted;
    }

    public void changeOwner(String owner) {
        this.owner = owner;
    }
    public void changeZoning(String zoning) {
        this.zoning = zoning;
    }
    public void addCuboid(Cuboid cuboid) {
        this.cuboids.add(cuboid);
    }
    public void addTrusted(UUID uuid) {
        this.trusted.add(uuid);
    }

    // Get info functions
    public UUID getCreator() {
        return creator;
    }
    public long getTimeCreated() {
        return tEff;
    }
    public List<Cuboid> getCuboids() {
        return cuboids;
    }
    public int getId() {
        return id;
    }
    public String getOwner() {
        if (owner == null) {
            return "NO OWNER";
        } else {
            return owner;
        }
    }
    public String getZoning() {
        return zoning;
    }
    public int getArea() {
        return area;
    }
    public @Nullable List<UUID> getTrusted() {
        return trusted;
    }

}

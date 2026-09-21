package com.raptormm4.cityMap;

import com.raptormm4.cityMap.commands.*;
import com.raptormm4.cityMap.commands.PlatCommand;
import com.raptormm4.cityMap.commands.ParcelCommand;
import com.raptormm4.cityMap.listeners.ClaimProtectionListener;
import com.raptormm4.cityMap.listeners.SelectionListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;

import java.util.*;

public final class CityMap extends JavaPlugin {

    private static CityMap instance;
    private SelectionListener selectionListener;
    public final List<Cuboid> allCuboids = new ArrayList<>();
    public final List<Parcel> allParcels = new ArrayList<>();

    @Override
    public void onEnable() {
        System.out.println("CityMap is up and running. Go Gators!");

        getCommand("plat").setExecutor(new PlatCommand(this, selectionListener));
        getCommand("parcel").setExecutor(new ParcelCommand(this, selectionListener));
        getCommand("selection").setExecutor(new SelectionCommand(this));

        getServer().getPluginManager().registerEvents(new ClaimProtectionListener(this), this);

        this.saveDefaultConfig();
        if (this.getConfig().contains("data")) {
            this.restoreParcels();
        }

        instance = this;
    }

    @Override
    public void onDisable() {
        this.saveParcels();
        System.out.println("CityMap has shut down. Please tell me we weren't DOGEd");
    }

    public static CityMap getInstance() {
        return instance;
    }

    // Saves
    public void saveParcels() {
        for (Parcel parcel : allParcels) {
            String path = "parcels." + parcel.getId();
            // Parcel info
            getConfig().set(path + ".creator-uuid", parcel.getCreator().toString());
            getConfig().set(path + ".timeEffective", parcel.getTimeCreated());
            getConfig().set(path + ".owner", parcel.getOwner());
            getConfig().set(path + ".zoning", parcel.getZoning());
            getConfig().set(path + ".area", parcel.getArea());
            // Cuboid info
            for (Cuboid c : parcel.getCuboids()) {
                String pathC = ".cuboids." + c.getCuboidId();
                getConfig().set(path + pathC + ".cornerOne", c.getCornerOne());
                getConfig().set(path + pathC + ".cornerTwo", c.getCornerTwo());
            }
        }
        for (Cuboid cuboid : allCuboids) {
            String path = "cuboids." + cuboid.getCuboidId();
            getConfig().set(path + ".cornerOne", cuboid.getCornerOne());
            getConfig().set(path + ".cornerTwo", cuboid.getCornerTwo());
        }
        saveConfig();
    }

    public void restoreParcels() {
        ConfigurationSection parcelsSection = getConfig().getConfigurationSection("parcels");
        ConfigurationSection cuboidsSection = getConfig().getConfigurationSection("cuboids");

        if (parcelsSection == null) {
            return;
        }
        allParcels.clear();

        // Restore parcels
        for (String keyStr : parcelsSection.getKeys(false)) {
            try {
                int id = Integer.parseInt(keyStr);
                ConfigurationSection path = parcelsSection.getConfigurationSection(keyStr);
                if (path == null) continue;

                UUID creator = UUID.fromString(path.getString("creator-uuid"));
                long tEff = path.getLong("timeEffective");
                String owner = path.getString("owner");
                String zoning = path.getString("zoning");
                int area = path.getInt("area");

                List<Cuboid> parcelCuboids = new ArrayList<>();
                ConfigurationSection parcelCuboidSection = path.getConfigurationSection("cuboids");
                if (parcelCuboidSection != null) {
                    for (String cuboidKeyStr : cuboidsSection.getKeys(false)) {
                        try {
                            int cuboidID = Integer.parseInt(cuboidKeyStr);
                            Location cornerOne = parcelCuboidSection.getLocation(cuboidKeyStr + ".cornerOne");
                            Location cornerTwo = parcelCuboidSection.getLocation(cuboidKeyStr + ".cornerTwo");
                            Cuboid cuboid = new Cuboid(cuboidID, cornerOne, cornerTwo);
                            parcelCuboids.add(cuboid);
                        } catch (NumberFormatException e) {
                            getLogger().warning("Failed to parse parcel cuboid ID: " + cuboidKeyStr + " in parcel ID: " + keyStr);
                        }
                    }
                }

                Parcel parcel = new Parcel(creator, tEff, parcelCuboids, id, owner, zoning, area);
                allParcels.add(parcel);
            } catch (IllegalArgumentException e) {
                getLogger().warning("Failed to restore Parcel ID: " + keyStr);
            }
        }

        // Restore cuboids
        for (String keyStr : cuboidsSection.getKeys(false)) {
            try {
                int cuboidId = Integer.parseInt(keyStr);
                ConfigurationSection path = cuboidsSection.getConfigurationSection(keyStr);
                if (path == null) continue;

                Location cornerOne = path.getLocation("cornerOne");
                Location cornerTwo = path.getLocation("cornerTwo");

                Cuboid cuboid = new Cuboid(cuboidId, cornerOne, cornerTwo);
                allCuboids.add(cuboid);
            } catch (IllegalArgumentException e) {
                getLogger().warning("Failed to restore Cuboid: " + keyStr);
            }
        }
    }
}

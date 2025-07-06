package com.raptormm4.cityMap;

import com.raptormm4.cityMap.commands.ClaimCommand;
import com.raptormm4.cityMap.commands.PlotCommand;
import com.raptormm4.cityMap.commands.UnclaimCommand;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class CityMap extends JavaPlugin {

    private HashMap<String, UUID> plotOwnership;
    private HashMap<String, String> plotZoning;

    Set<String> chunkIdSet;
    List<String> chunkIdList;

    @Override
    public void onEnable() {
        this.plotOwnership = new HashMap<>();
        this.plotZoning = new HashMap<>();
        this.chunkIdSet = plotOwnership.keySet();
        this.chunkIdList = new ArrayList<>(chunkIdSet);

        System.out.println("CityMap is up and running. Go Gators!");

        getCommand("claim").setExecutor(new ClaimCommand(this));
        getCommand("unclaim").setExecutor(new UnclaimCommand(this));
        getCommand("plot").setExecutor(new PlotCommand(this));

        getServer().getPluginManager().registerEvents(new ClaimProtectionListener(this), this);

        this.saveDefaultConfig();
        if (this.getConfig().contains("data")) {
            this.restorePlots();
        }
    }

    @Override
    public void onDisable() {
        this.savePlots();
        System.out.println("CityMap has shut down. Please tell me we weren't DOGEd");
    }

    public void addChunk(String chunk, UUID owner, String zoning) {
        plotOwnership.put(chunk, owner);
        plotZoning.put(chunk, zoning);
    }

    public void removeChunk(String chunk, UUID owner, String zoning) {
        plotOwnership.remove(chunk, owner);
        plotZoning.remove(chunk, zoning);
    }

    public boolean isChunk(String chunk) {
        return plotOwnership.containsKey(chunk);
    }

    public boolean isPlotReal(String chunk) {
        System.out.println(chunkIdList);
        return chunkIdList.contains(chunk);
    }

    public UUID getOwner(String chunk) {
        return plotOwnership.get(chunk);
    }

    public String getOwnerName(String chunk) {
        UUID uuid = plotOwnership.get(chunk);
        return Bukkit.getPlayer(uuid).getName();
    }

    public String getZoning(String chunk) {
        return plotZoning.get(chunk);
    }

    public void changeZoning(String chunk, String zoning) {
        plotZoning.replace(chunk, zoning);
    }

    public void savePlots() {
        for (Map.Entry<String, UUID> entry : plotOwnership.entrySet()) {
            this.getConfig().set("uuidData." + entry.getKey(), entry.toString());
        }
        for (Map.Entry<String, String> entry : plotZoning.entrySet()) {
            this.getConfig().set("zoningData." + entry.getKey(), entry.getValue());
        }
        this.saveConfig();
    }

    public void restorePlots() {
        ConfigurationSection uuidSection = getConfig().getConfigurationSection("uuidData");
        ConfigurationSection zoningSection = getConfig().getConfigurationSection("zoningData");
        if (uuidSection != null) {
            for (String key : uuidSection.getKeys(false)) {
                String plotId = uuidSection.getString(key);
                UUID owner = UUID.fromString(key);
                plotOwnership.put(plotId, owner);
            }
        }
        if (zoningSection != null) {
            for (String key : zoningSection.getKeys(false)) {
                String zoning = zoningSection.getString(key);
                plotZoning.put(key, zoning);
            }
        }

    }
}

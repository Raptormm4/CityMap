package com.raptormm4.cityMap;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.raptormm4.cityMap.commands.ClaimCommand;
import com.raptormm4.cityMap.commands.PlotCommand;
import com.raptormm4.cityMap.commands.UnclaimCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.codehaus.plexus.util.IOUtil;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

public final class CityMap extends JavaPlugin {

    private HashMap<String, ChunkBundle> chunks;

    class ChunkBundle {
        UUID uuid;
        String zoning;

        public ChunkBundle(UUID uuid, String zoning) {
            this.uuid = uuid;
            this.zoning = zoning;
        }

        public UUID getUuid() {
            return uuid;
        }

        public String getZoning() {
            return zoning;
        }
    }

    Set<String> chunkIdSet = chunks.keySet();
    List<String> chunkIdList = new ArrayList<>(chunkIdSet);

    @Override
    public void onEnable() {
        this.chunks = new HashMap<>();
        System.out.println("CityMap is up and running. Go Gators!");

        getCommand("claim").setExecutor(new ClaimCommand(this));
        getCommand("unclaim").setExecutor(new UnclaimCommand(this));
        getCommand("plot").setExecutor(new PlotCommand(this));

        getServer().getPluginManager().registerEvents(new ClaimProtectionListener(this), this);
    }

    @Override
    public void onDisable() {
        System.out.println("CityMap has shut down. Please tell me we weren't DOGEd");
    }

    public void addChunk(String chunk, UUID owner, String zoning) {
        chunks.put(chunk, new ChunkBundle(owner, zoning));
    }

    public void removeChunk(String chunk, UUID owner, String zoning) {
        chunks.remove(chunk, new ChunkBundle(owner, zoning));
    }

    public boolean isChunk(String chunk) {
        return chunks.containsKey(chunk);
    }

    public boolean isPlotReal(String chunk) {
        return chunkIdList.contains(chunk);
    }

    public UUID getOwner(String chunk) {
        ChunkBundle chunkBundle = chunks.get(chunk);
        return chunkBundle.uuid;
    }

    public String getOwnerName(String chunk) {
        ChunkBundle chunkBundle = chunks.get(chunk);
        UUID uuid = chunkBundle.uuid;
        return Bukkit.getPlayer(uuid).getName();
    }

    public String getZoning(String chunk) {
        ChunkBundle chunkBundle = chunks.get(chunk);
        return chunkBundle.zoning;
    }

    public void changeZoning(String chunk, String zoning) {
        ChunkBundle chunkBundle = chunks.get(chunk);
        chunkBundle.zoning = zoning;
    }
}

package com.raptormm4.cityMap.commands;

import com.raptormm4.cityMap.CityMap;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PlotCommand implements CommandExecutor, TabExecutor {

    private final CityMap plugin;

    public PlotCommand(CityMap cityMap) {
        this.plugin = cityMap;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            Chunk chunk = player.getLocation().getChunk();

            String chunkID = chunk.getX() + "." + chunk.getZ();

            if (args.length == 0) {
                if (plugin.isChunk(chunkID)) {
                    String zoning = plugin.getZoning(chunkID);
                    String ownerName = plugin.getOwnerName(chunkID);

                    sender.sendMessage(ChatColor.AQUA + "Chunk ID: " + chunkID);
                    sender.sendMessage(ChatColor.AQUA + "Owner: " + ownerName);
                    sender.sendMessage(ChatColor.AQUA + "Zoning: " + zoning);
                } else {
                    sender.sendMessage(ChatColor.RED + "This chunk has not yet been claimed!");
                }
            }
            if (args.length == 1 && !(args[0].equals("set"))) {
                boolean isPlotReal = plugin.isChunk(args[0]);
                if (isPlotReal == true) {
                    if (player.isOp()) {
                        String zoning = plugin.getZoning(args[0]);
                        String ownerName = plugin.getOwnerName(args[0]);
                        UUID owner = plugin.getOwner(args[0]);

                        sender.sendMessage(ChatColor.AQUA + "Chunk ID: " + args[0]);
                        sender.sendMessage(ChatColor.AQUA + "Owner: " + ownerName);
                        sender.sendMessage(ChatColor.AQUA + "Zoning: " + zoning);
                        sender.sendMessage(ChatColor.AQUA + "UUID: " + owner);
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "This plot does not exist");
                }
            }
            if (args.length == 3 && args[0].equals("set") && args[1].equals("zoning") && args[2].equals("agricultural")) {
                if (plugin.isChunk(chunkID)) {
                    String zoning = "Agricultural";
                    plugin.changeZoning(chunkID, zoning);
                    sender.sendMessage(ChatColor.GREEN + "This plot is now zoned Agricultural (A)");
                } else {
                    sender.sendMessage(ChatColor.RED + "This chunk has not yet been claimed!");
                }
            }
            if (args.length == 3 && args[0].equals("set") && args[1].equals("zoning") && args[2].equals("residential")) {
                if (plugin.isChunk(chunkID)) {
                    String zoning = "Residential";
                    plugin.changeZoning(chunkID, zoning);
                    sender.sendMessage(ChatColor.GREEN + "This plot is now zoned Residential (R-1)");
                } else {
                    sender.sendMessage(ChatColor.RED + "This chunk has not yet been claimed!");
                }
            }
            if (args.length == 3 && args[0].equals("set") && args[1].equals("zoning") && args[2].equals("commercial")) {
                if (plugin.isChunk(chunkID)) {
                    String zoning = "Commercial";
                    plugin.changeZoning(chunkID, zoning);
                    sender.sendMessage(ChatColor.GREEN + "This plot is now zoned Commercial (C-1)");
                } else {
                    sender.sendMessage(ChatColor.RED + "This chunk has not yet been claimed!");
                }
            }
            if (args.length == 3 && args[0].equals("set") && args[1].equals("zoning") && args[2].equals("industrial")) {
                if (plugin.isChunk(chunkID)) {
                    String zoning = "Industrial";
                    plugin.changeZoning(chunkID, zoning);
                    sender.sendMessage(ChatColor.GREEN + "This plot is now zoned Industrial (LM-I)");
                } else {
                    sender.sendMessage(ChatColor.RED + "This chunk has not yet been claimed!");
                }
            }

        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        System.out.println("Args size: " + args.length);

        if (args.length == 1) {
            return Arrays.asList("set");
        }
        if (args.length == 2) {
            return Arrays.asList("zoning");
        }
        if (args.length == 3) {
            return Arrays.asList("residential", "commercial", "industrial", "agricultural");
        }

        return new ArrayList<>();
    }

}

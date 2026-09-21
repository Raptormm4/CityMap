package com.raptormm4.cityMap.commands;

import com.raptormm4.cityMap.CityMap;
import com.raptormm4.cityMap.Cuboid;
import com.raptormm4.cityMap.Parcel;
import com.raptormm4.cityMap.listeners.SelectionListener;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
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

public class ParcelCommand implements CommandExecutor, TabExecutor {

    private final CityMap plugin;
    private final SelectionListener selectionListener;

    public ParcelCommand(CityMap cityMap, SelectionListener selectionListener) {
        this.plugin = cityMap;
        this.selectionListener = selectionListener;
    }

    // Command arguments
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            Location pLoc = player.getLocation();
            Cuboid cuboid = getCuboidAt(pLoc);
            Parcel parcel = getParcelAt(cuboid);

            // Basic parcel info
            if (args.length == 0) {
                if (parcel != null) {
                    sender.sendMessage(ChatColor.AQUA + "Parcel ID: " + parcel.getId());
                    sender.sendMessage(ChatColor.AQUA + "Owner: " + parcel.getOwner());
                    sender.sendMessage(ChatColor.AQUA + "Zoning: " + parcel.getZoning());
                    sender.sendMessage(ChatColor.AQUA + "Area: " + parcel.getArea());
                } else {
                    sender.sendMessage(ChatColor.RED + "This location has not been platted!");
                }
            }
            if (args.length == 1 && !(args[0].equals("set"))) {
                sender.sendMessage(ChatColor.RED + "ERROR! Must clarify what you are setting.");
            }

            // Zoning changes
            if (args.length == 2 && args[0].equals("set") && args[1].equals("zoning")) {
                if (parcel != null) {
                    sender.sendMessage(ChatColor.AQUA + "Current zoning: " + parcel.getZoning());
                } else {
                    sender.sendMessage(ChatColor.RED + "This location has not been platted!");
                }
            }
            if (args.length == 3 && args[0].equals("set") && args[1].equals("zoning") && args[2].equals("agricultural")) {
                if (parcel != null) {
                    String zoning = "Agricultural";
                    parcel.changeZoning(zoning);
                    sender.sendMessage(ChatColor.GREEN + "This parcel is now zoned Agricultural (A)");
                } else {
                    sender.sendMessage(ChatColor.RED + "This location has not been platted!");
                }
            }
            if (args.length == 3 && args[0].equals("set") && args[1].equals("zoning") && args[2].equals("residential")) {
                if (parcel != null) {
                    String zoning = "Residential";
                    parcel.changeZoning(zoning);
                    sender.sendMessage(ChatColor.GREEN + "This parcel is now zoned Residential (R)");
                } else {
                    sender.sendMessage(ChatColor.RED + "This location has not been platted!");
                }
            }
            if (args.length == 3 && args[0].equals("set") && args[1].equals("zoning") && args[2].equals("commercial")) {
                if (parcel != null) {
                    String zoning = "Commercial";
                    parcel.changeZoning(zoning);
                    sender.sendMessage(ChatColor.GREEN + "This parcel is now zoned Commercial (C)");
                } else {
                    sender.sendMessage(ChatColor.RED + "This location has not been platted!");
                }
            }
            if (args.length == 3 && args[0].equals("set") && args[1].equals("zoning") && args[2].equals("industrial")) {
                if (parcel != null) {
                    String zoning = "Industrial";
                    parcel.changeZoning(zoning);
                    sender.sendMessage(ChatColor.GREEN + "This parcel is now zoned Agricultural (I)");
                } else {
                    sender.sendMessage(ChatColor.RED + "This location has not been platted!");
                }
            }

            // Ownership changes
            if (args.length == 2 && args[0].equals("set") && args[1].equals("owner")) {
                if (parcel != null) {
                    sender.sendMessage(ChatColor.AQUA + "Current owner: " + parcel.getOwner());
                } else {
                    sender.sendMessage(ChatColor.RED + "This location has not been platted!");
                }
            }
            if (args.length == 3 && args[0].equals("set") && args[1].equals("owner") && args[2].equals("agricultural")) {
                if (parcel != null) {
                    String zoning = "Agricultural";
                    parcel.changeZoning(zoning);
                    sender.sendMessage(ChatColor.GREEN + "This parcel is now zoned Agricultural (A)");
                } else {
                    sender.sendMessage(ChatColor.RED + "This location has not been platted!");
                }
            }

            // Add selection
            if (args.length == 2 && args[0].equals("add") && args[1].equals("selection")) {
                if (parcel != null) {
                    if (selectionListener.hasSelection.containsKey(uuid)) {
                        Cuboid c = selectionListener.currentCuboid.get(uuid);
                        parcel.addCuboid(cuboid);

                        // Clear selection data
                        selectionListener.hasSelection.remove(uuid);
                        selectionListener.currentCuboid.remove(uuid);
                        selectionListener.firstSelection.remove(uuid);
                        selectionListener.secondSelection.remove(uuid);

                        // Provide output to player
                        sender.sendMessage(net.md_5.bungee.api.ChatColor.GREEN + "You have added a selection to parcel " + parcel.getId());
                    }
                }
            }
        }
        return true;
    }

    // Tab autocomplete
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        System.out.println("Args size: " + args.length);

        if (args.length == 1) {
            return Arrays.asList("set", "add");
        }
        if (args.length == 2) {
            return Arrays.asList("zoning", "selection");
        }
        if (args.length == 3) {
            return Arrays.asList("residential", "commercial", "industrial", "agricultural");
        }

        return new ArrayList<>();
    }

    // Check which cuboid and parcel you're within
    public Cuboid getCuboidAt(Location location) {
        for (Cuboid cuboid : plugin.allCuboids) {
            if (cuboid.isInside(location)) {
                return cuboid;
            }
        }
        return null;
    }
    public Parcel getParcelAt(Cuboid cuboidInside) {
        for (Parcel parcel : plugin.allParcels) {
            List<Cuboid> cuboidsSearch = parcel.getCuboids();
            for (Cuboid c : cuboidsSearch) {
                if (c != cuboidInside) {
                    continue;
                }
                else {
                    return parcel;
                }
            }
        }
        return null;
    }

}

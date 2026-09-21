package com.raptormm4.cityMap.commands;

import com.raptormm4.cityMap.CityMap;
import com.raptormm4.cityMap.Cuboid;
import com.raptormm4.cityMap.Parcel;
import com.raptormm4.cityMap.listeners.SelectionListener;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlatCommand implements CommandExecutor {
    private final CityMap plugin;
    private final SelectionListener selectionListener;

    public PlatCommand(CityMap cityMap, SelectionListener selectionListener) {
        this.plugin = cityMap;
        this.selectionListener = selectionListener;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            UUID uuid = p.getUniqueId();
            Cuboid cuboid = selectionListener.currentCuboid.get(uuid);
            String zoning = "Agricultural";

            if (selectionListener.hasSelection.containsKey(uuid)) {
                // Create new parcel
                Parcel newParcel = new Parcel(uuid, cuboid, zoning);
                plugin.allParcels.add(newParcel);

                // Clear selection data
                selectionListener.hasSelection.remove(uuid);
                selectionListener.currentCuboid.remove(uuid);
                selectionListener.firstSelection.remove(uuid);
                selectionListener.secondSelection.remove(uuid);

                // Provide output to player
                sender.sendMessage(ChatColor.GREEN + "You have created parcel " + newParcel.getId() + "!");
            }
        }

        return true;
    }
}

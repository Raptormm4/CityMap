package com.raptormm4.cityMap.commands;

import com.raptormm4.cityMap.CityMap;
import com.raptormm4.cityMap.Cuboid;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SelectionCommand implements CommandExecutor, TabExecutor {

    private final CityMap plugin;
    public HashMap<UUID, Boolean> ClaimToggled = new HashMap<>();

    public SelectionCommand(CityMap cityMap) {
        this.plugin = cityMap;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            boolean isToggled = ClaimToggled.containsKey(uuid);

            if (args.length == 1 && args[0].equals("toggle") && !isToggled) {
                ClaimToggled.put(uuid, true);
                sender.sendMessage(ChatColor.GREEN + "Parcel selection has been enabled. Please use a golden shovel to continue");
            } else {
                ClaimToggled.remove(uuid);
                sender.sendMessage(ChatColor.RED + "Parcel selection has been disabled.");
            }
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        return List.of();
    }
}

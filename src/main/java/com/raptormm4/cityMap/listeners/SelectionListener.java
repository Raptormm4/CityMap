package com.raptormm4.cityMap.listeners;

import com.raptormm4.cityMap.CityMap;
import com.raptormm4.cityMap.Cuboid;
import com.raptormm4.cityMap.Parcel;
import com.raptormm4.cityMap.commands.SelectionCommand;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SelectionListener implements Listener {

    private final CityMap plugin;
    private final SelectionCommand selectionCommand;
    public HashMap<UUID, Location> firstSelection = new HashMap<>();
    public HashMap<UUID, Location> secondSelection = new HashMap<>();
    public HashMap<UUID, Cuboid> hasSelection = new HashMap<>();
    public HashMap<UUID, Cuboid> currentCuboid = new HashMap<>();

    public SelectionListener(CityMap plugin, SelectionCommand selectionCommand) {
        this.plugin = plugin;
        this.selectionCommand = selectionCommand;
    }

    // Ensures that when a player relogs selection is disabled
    @EventHandler
    public void OnLogout(PlayerLoginEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        if (selectionCommand.ClaimToggled.containsKey(uuid)) {
            selectionCommand.ClaimToggled.remove(uuid);
        }
        if (firstSelection.containsKey(uuid)) {
            firstSelection.remove(uuid);
        }
        if (secondSelection.containsKey(uuid)) {
            secondSelection.remove(uuid);
        }
    }

    @EventHandler
    public void OnInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        if (selectionCommand.ClaimToggled.containsKey(uuid)) {
            if (p.getInventory().getItemInMainHand().getType() == Material.GOLDEN_SHOVEL
                    && event.getClickedBlock() != null) {
                if (event.getAction().isLeftClick()) {
                    event.setCancelled(true);
                    if (event.getClickedBlock().getWorld().getEnvironment() == World.Environment.NETHER ||
                            event.getClickedBlock().getWorld().getEnvironment() == World.Environment.THE_END) {
                        p.sendMessage(ChatColor.RED + "ERROR! Must be in overworld!");
                    } else {
                        firstSelection.put(uuid, event.getClickedBlock().getLocation());
                        p.sendMessage(ChatColor.AQUA + "First block selected: " + event.getClickedBlock().getLocation());
                    }
                } else if (event.getAction().isRightClick()) {
                    event.setCancelled(true);
                    if (event.getClickedBlock().getWorld().getEnvironment() == World.Environment.NETHER ||
                            event.getClickedBlock().getWorld().getEnvironment() == World.Environment.THE_END) {
                        p.sendMessage(ChatColor.RED + "ERROR! Must be in overworld!");
                    } else {
                        secondSelection.put(uuid, event.getClickedBlock().getLocation());
                        p.sendMessage(ChatColor.AQUA + "Second block selected: " + event.getClickedBlock().getLocation());
                    }
                }

                if (firstSelection.containsKey(uuid) && secondSelection.containsKey(uuid)) {
                    Location location1 = firstSelection.get(uuid);
                    Location location2 = secondSelection.get(uuid);
                    Cuboid newCuboid = new Cuboid(location1, location2);
                    for (Cuboid existingCuboid : plugin.allCuboids) {
                        if (newCuboid.intersects(existingCuboid)) {
                            p.sendMessage(ChatColor.RED + "This selection overlaps an existing selection.");
                            return;
                        }
                    }
                    plugin.allCuboids.add(newCuboid);
                    p.sendMessage(ChatColor.GREEN + "Selection successful.");
                    hasSelection.put(uuid, newCuboid);
                    currentCuboid.put(uuid, newCuboid);
                }
            }
        }
    }

}

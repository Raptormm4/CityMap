package com.raptormm4.cityMap.listeners;

import com.raptormm4.cityMap.CityMap;
import com.raptormm4.cityMap.Cuboid;
import com.raptormm4.cityMap.Parcel;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public class ClaimProtectionListener implements Listener {

    private final CityMap plugin;

    public ClaimProtectionListener(CityMap plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void OnInteract(PlayerInteractEvent event ) {
        if (event.getClickedBlock() != null) {
            Player player = event.getPlayer();
            Location pLoc = player.getLocation();
            Cuboid cuboid = getCuboidAt(pLoc);
            Parcel parcel = getParcelAt(cuboid);

            if (parcel != null) {
                if (parcel.getOwner() == player.getUniqueId().toString()) {
                    if (!player.isOp()) {
                        event.setCancelled(true);
                        player.sendMessage("You do not own this parcel!");
                    }
                }
            }
        }
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

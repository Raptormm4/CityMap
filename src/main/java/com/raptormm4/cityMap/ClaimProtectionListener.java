package com.raptormm4.cityMap;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ClaimProtectionListener implements Listener {

    private final CityMap plugin;

    public ClaimProtectionListener(CityMap plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void OnInteract(PlayerInteractEvent event ) {
        if (event.getClickedBlock() != null) {
            Chunk chunk = event.getClickedBlock().getChunk();
            String chunkID = chunk.getX() + "." + chunk.getZ();

            if (plugin.isChunk(chunkID)) {
                Player player = event.getPlayer();

                if (!plugin.getOwner(chunkID).equals(player.getUniqueId())) {
                    if (!player.isOp()) {
                        event.setCancelled(true);
                        player.sendMessage("You do not own this plot!");
                    }
                }
            }
        }
    }

}

package com.raptormm4.cityMap.commands;

import com.raptormm4.cityMap.CityMap;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ClaimCommand implements CommandExecutor {
    private final CityMap plugin;

    public ClaimCommand(CityMap cityMap) {
        this.plugin = cityMap;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Chunk chunk = player.getLocation().getChunk();
            String zoning = "Agricultural";

            String chunkID = chunk.getX() + "." + chunk.getZ();

            if (plugin.isChunk(chunkID)) {
                sender.sendMessage(ChatColor.RED + "This chunk has already been claimed");
            } else {
                plugin.addChunk(chunkID, player.getUniqueId(), zoning);
                sender.sendMessage(ChatColor.GREEN + "You have claimed plot " + chunkID + "!");
            }
        }

        return true;
    }
}

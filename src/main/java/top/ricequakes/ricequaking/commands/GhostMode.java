package top.ricequakes.ricequaking.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.ricequakes.ricequaking.Ricequaking;
import top.ricequakes.ricequaking.Game;

import java.util.HashMap;

public class GhostMode implements CommandExecutor {
    public final Ricequaking plugin;

    public GhostMode(Ricequaking plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("g") && sender instanceof Player) {
            Player player = (Player) sender;
            HashMap<String, Location> hashMap = Game.ghostModeBackLocations;
            if (player.getGameMode() == GameMode.SURVIVAL) {
                hashMap.put(player.getName(), player.getLocation());
                player.setGameMode(GameMode.SPECTATOR);
                Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + " 灵魂出窍了!");
            } else if (player.getGameMode() == GameMode.SPECTATOR && hashMap.containsKey(player.getName())) {
                player.setGameMode(GameMode.SURVIVAL);
                player.teleport(hashMap.get(player.getName()));
                hashMap.remove(player.getName());
                Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + " 回到了躯壳之中");
            }
        }
        return true;
    }
}

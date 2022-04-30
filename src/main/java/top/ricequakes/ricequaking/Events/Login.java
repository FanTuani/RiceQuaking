package top.ricequakes.ricequaking.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import top.ricequakes.ricequaking.Ricequaking;

public class Login implements Listener {
    public final Ricequaking plugin;

    public Login(Ricequaking plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void PlayerLoginWelcome(PlayerJoinEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.getName().equalsIgnoreCase(event.getPlayer().getName())) {
                player.sendTitle(ChatColor.GREEN + event.getPlayer().getName(), "Joined!", 5, 50, 10);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 0);
            } else {
                player.sendTitle(ChatColor.YELLOW + "Welcome!", event.getPlayer().getName(), 5, 40, 10);
            }
        }
    }
}

package top.ricequakes.ricequaking.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import top.ricequakes.ricequaking.RiceQuaking;

public class Login implements Listener {
    public final RiceQuaking plugin;

    public Login(RiceQuaking plugin) {
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
                StringBuilder subTitle = new StringBuilder();
                if (plugin.getServer().getOnlinePlayers().size() == 1) {
                    subTitle.append(player.getName());
                } else {
                    subTitle.append("Online Players: ");
                    for (Player p : plugin.getServer().getOnlinePlayers()) {
                        subTitle.append(p.getName()).append(" ");
                    }
                }
                player.sendTitle(ChatColor.YELLOW + "Welcome!", subTitle.toString(), 5, 60, 10);
            }
        }
    }
}

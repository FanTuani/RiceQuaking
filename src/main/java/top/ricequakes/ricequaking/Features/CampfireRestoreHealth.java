package top.ricequakes.ricequaking.Features;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import top.ricequakes.ricequaking.RiceQuaking;

public class CampfireRestoreHealth implements Listener {
    public final RiceQuaking plugin;

    public CampfireRestoreHealth(RiceQuaking plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerRightClickCampfire(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            if (event.getClickedBlock().getType() == Material.CAMPFIRE) {
                Player player = event.getPlayer();

                player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player.getHealth() >= 20 && player.getFoodLevel() >= 30) {
                            cancel();
                        } else {
                            if (player.getHealth() < 20)
                                player.setHealth(player.getHealth() + 0.5);
                            if (player.getFoodLevel() < 30)
                                player.setFoodLevel(player.getFoodLevel() + 1);
                            String subTitle = "Curing... HP: " + player.getHealth() + " FL: " + player.getFoodLevel();
                            player.sendTitle("", ChatColor.GREEN + subTitle, 1, 20, 5);
                        }
                    }
                }.runTaskTimer(plugin, 1, 4);
            }
        }
    }
}

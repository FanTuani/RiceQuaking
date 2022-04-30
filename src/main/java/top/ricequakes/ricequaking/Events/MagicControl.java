package top.ricequakes.ricequaking.Events;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import top.ricequakes.ricequaking.Ricequaking;
import top.ricequakes.ricequaking.StaticValues;

import java.util.HashMap;

public class MagicControl implements Listener {
    public final Ricequaking plugin;

    public MagicControl(Ricequaking plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerRightClickEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if (event.getRightClicked().getType() == EntityType.VILLAGER
                && player.getInventory().getItemInMainHand().getType() == Material.WOODEN_PICKAXE) {
            Villager villager = (Villager) event.getRightClicked();
            HashMap<Player, Villager> hashMap = StaticValues.magicControllingPair;

            if (!hashMap.containsKey(player)) {
                hashMap.put(player, villager);
                player.sendMessage(ChatColor.YELLOW + "Picked!");
                new BukkitRunnable() {
                    int cnt = 0;

                    @Override
                    public void run() {
                        if (++cnt == 12000 || !hashMap.containsKey(player)) {
                            hashMap.remove(player);
                            player.sendMessage(ChatColor.YELLOW + "Released!");
                            cancel();
                        }
                        Vector vector = player.getLocation().getDirection();
                        Location teleLocation = player.getLocation().add(vector.multiply(3));
                        villager.teleport(teleLocation);
                    }
                }.runTaskTimer(plugin, 0, 1);
            } else {
                hashMap.remove(player);
            }
        }
    }

    @EventHandler
    public void onVillagerDamage(EntityDamageEvent event) {
        if (event.getEntity().getType() == EntityType.VILLAGER) {
            if (StaticValues.magicControllingPair.containsValue(event.getEntity()))
                event.setCancelled(true);
        }
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL
                && event.getEntity().getType() == EntityType.VILLAGER) {
            event.setCancelled(true);
        }
    }
}

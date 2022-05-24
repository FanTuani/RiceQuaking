package top.ricequakes.ricequaking.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import top.ricequakes.ricequaking.Ricequaking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VillagersTransporter implements Listener {
    public final Ricequaking plugin;
    private static HashMap<Player, List<Villager>> transVi = new HashMap<>();
    private static HashMap<Player, Villager> controlVi = new HashMap<>();

    public VillagersTransporter(Ricequaking plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerRightClickEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if (event.getRightClicked().getType() == EntityType.VILLAGER //木镐右键村民
                && player.getInventory().getItemInMainHand().getType() == Material.WOODEN_PICKAXE) {
            Villager villager = (Villager) event.getRightClicked();
            if (!controlVi.containsKey(player)) {
                controlVi.put(player, villager);
                player.sendMessage(ChatColor.YELLOW + "Picked!");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!controlVi.containsKey(player)) {
                            controlVi.remove(player);
                            player.sendMessage(ChatColor.YELLOW + "Released!");
                            cancel();
                        } else {
                            Vector vector = player.getLocation().getDirection();
                            Location teleLocation = player.getLocation().add(vector.multiply(3));
                            villager.teleport(teleLocation);
                            villager.setFallDistance(0);
                        }
                    }
                }.runTaskTimer(plugin, 0, 1);
            } else {
                controlVi.remove(player);
            }
        } else if (event.getRightClicked().getType() == EntityType.VILLAGER //石稿右键村民
                && player.getInventory().getItemInMainHand().getType() == Material.STONE_PICKAXE) {
            Villager villager = (Villager) event.getRightClicked();

            if (!transVi.containsKey(player)) { //hashmap中无此玩家 将玩家加入hashmap
                List<Villager> list = new ArrayList();
                list.add(villager);
                transVi.put(player, list);
                new BukkitRunnable() {
                    @Override
                    public void run() { //控制村民位置
                        if (transVi.get(player).size() == 0) {
                            for (Villager vi : transVi.get(player)) {
                                vi.teleport(player.getLocation());
                            }
                            transVi.remove(player);
                            player.sendMessage(ChatColor.YELLOW + "Released!");
                            cancel();
                        } else {
                            for (Villager vi : transVi.get(player)) {
                                Location teleLocation = player.getLocation();
                                teleLocation.setY(teleLocation.getY() + 3);
                                vi.teleport(teleLocation);
                                vi.setFallDistance(0);
                            }
                        }
                    }
                }.runTaskTimer(plugin, 0, 1);
            } else { // 有此玩家
                if (transVi.get(player).contains(villager)) { //右键了正在被控制的村民 则释放
                    transVi.get(player).clear();
                } else { // 否则把该村民列入hashmap
                    transVi.get(player).add(villager);
                }
            }
            player.sendMessage(ChatColor.YELLOW + "Picked!");
        }
    }

    @EventHandler
    public void onStartTransform(InventoryOpenEvent event) {
        if (event.getInventory().getType() == InventoryType.MERCHANT) {
            if ((event.getPlayer().getInventory().getItemInMainHand().getType() == Material.WOODEN_PICKAXE)
                    || event.getPlayer().getInventory().getItemInMainHand().getType() == Material.STONE_PICKAXE) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onVillagerDamage(EntityDamageEvent event) { // transforming 村民保护
        if (event.getEntity().getType() == EntityType.VILLAGER) {
            for (Player player : transVi.keySet()) {
                if (transVi.get(player).contains(event.getEntity())) {
                    event.setCancelled(true);
                }
            }
            for (Player player : controlVi.keySet()) {
                if (controlVi.get(player) == event.getEntity()) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
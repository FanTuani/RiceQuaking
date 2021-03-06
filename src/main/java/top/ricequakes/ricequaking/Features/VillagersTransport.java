package top.ricequakes.ricequaking.Features;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import top.ricequakes.ricequaking.RiceQuaking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VillagersTransport implements Listener {
    public final RiceQuaking plugin;
    private static HashMap<Player, List<Villager>> transVi = new HashMap<>();
    private static HashMap<Player, Villager> controlVi = new HashMap<>();

    public VillagersTransport(RiceQuaking plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.WOODEN_SWORD) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Player player = event.getPlayer();

                Location location = player.getEyeLocation();
                Vector direc = player.getLocation().getDirection();
                location.add(direc);

                RayTraceResult result = player.getWorld().rayTraceEntities(location, direc, 200);
                if (result != null && result.getHitEntity() != null) {
                    if (result.getHitEntity().getType() == EntityType.VILLAGER) {
                        Villager villager = (Villager) result.getHitEntity();
                        villager.teleport(player.getLocation());
                        player.sendMessage(ChatColor.YELLOW + "??????");
                    } else {
                        player.sendMessage(ChatColor.GRAY + "???????????????????????????");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRightClickEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if (event.getRightClicked().getType() == EntityType.VILLAGER //??????????????????
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
        } else if (event.getRightClicked().getType() == EntityType.VILLAGER //??????????????????
                && player.getInventory().getItemInMainHand().getType() == Material.STONE_PICKAXE) {
            Villager villager = (Villager) event.getRightClicked();

            if (!transVi.containsKey(player)) { //hashmap??????????????? ???????????????hashmap
                List<Villager> list = new ArrayList();
                list.add(villager);
                transVi.put(player, list);
                player.sendMessage(ChatColor.YELLOW + "Picked! ???????????? " + transVi.get(player).size() + " ?????????");
                new BukkitRunnable() {
                    @Override
                    public void run() { //??????????????????
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
            } else { // ????????????
                if (transVi.get(player).contains(villager)) { //????????????????????????????????? ?????????
                    transVi.get(player).clear();
                } else { // ????????????????????????hashmap
                    transVi.get(player).add(villager);
                    player.sendMessage(ChatColor.YELLOW + "Picked! ???????????? " + transVi.get(player).size() + " ?????????");
                }
            }
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
    public void onVillagerDamage(EntityDamageEvent event) { // transforming ????????????
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

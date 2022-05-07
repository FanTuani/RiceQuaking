package top.ricequakes.ricequaking.Gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.ricequakes.ricequaking.Ricequaking;
import top.ricequakes.ricequaking.StaticValues;

import java.util.List;

public class RecordsGui {
    public static final Inventory inv;

    static {
        inv = Bukkit.createInventory(null, 54, "Records");

        ItemStack quitItem = new ItemStack(Material.BARRIER, 1);
        ItemMeta quitMeta = quitItem.getItemMeta();
        quitMeta.setDisplayName("quit");
        quitItem.setItemMeta(quitMeta);
        inv.setItem(53, quitItem);

        ItemStack fireItem = new ItemStack(Material.FIRE_CHARGE, 1);
        ItemMeta fireMeta = fireItem.getItemMeta();
        fireMeta.setDisplayName("借个火");
        fireItem.setItemMeta(fireMeta);
        inv.setItem(45, fireItem);
    }

    public static void open(Player player) {
        int i = 0;
        for (String name : StaticValues.getPerfectList()) {
            ItemStack mapItem = new ItemStack(Material.MAP, 1);
            ItemMeta mapMata = mapItem.getItemMeta();
            mapMata.setDisplayName(name);
            mapItem.setItemMeta(mapMata);
            inv.setItem(i++, mapItem);
        }

        player.openInventory(inv);
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onClick(InventoryClickEvent event) {
                event.setCancelled(true);
                if (event.getCurrentItem() != null) {
                    switch (event.getCurrentItem().getItemMeta().getDisplayName()) {
                        case "quit": {
                            break;
                        }
                        case "借个火": {
                            if (player.getInventory().contains(Material.FIRE_CHARGE)
                                    || player.getInventory().contains(Material.FLINT_AND_STEEL)) {
                                player.damage(0.1);
                                player.sendMessage(ChatColor.RED + "你特么该是缺火？");
                            } else {
                                player.getInventory().addItem(new ItemStack(Material.FIRE_CHARGE, 1));
                                player.sendMessage(ChatColor.GRAY + "在你背包里啦=-= 如果它没有满的话");
                            }
                            break;
                        }
                    }
                    if (event.getCurrentItem().getType() == Material.MAP) {
                        FileConfiguration config = Ricequaking.instance.getConfig();
                        String name = event.getCurrentItem().getItemMeta().getDisplayName();
                        List list = config.getList(name);
                        double x = new Double(list.get(0).toString());
                        double y = new Double(list.get(1).toString());
                        double z = new Double(list.get(2).toString());
                        if (name.contains("地狱：") || name.contains("地狱:")) {
                            player.teleport(new Location(Ricequaking.instance.getServer().getWorld("world_nether"), x, y, z));
                        } else if (name.contains("末地：") || name.contains("末地:")) {
                            player.teleport(new Location(Ricequaking.instance.getServer().getWorld("world_the_end"), x, y, z));
                        } else {
                            player.teleport(new Location(Ricequaking.instance.getServer().getWorld("world"), x, y, z));
                        }
                        player.sendMessage(ChatColor.GOLD + "Teleported to: " + name);
                    }
                    player.closeInventory();
                }
            }

            @EventHandler
            public void onDrag(InventoryDragEvent event) {
                event.setCancelled(true);
                player.closeInventory();
            }

            @EventHandler
            public void onClose(InventoryCloseEvent event) {
                HandlerList.unregisterAll(this);
            }
        }, Ricequaking.instance);
    }
}

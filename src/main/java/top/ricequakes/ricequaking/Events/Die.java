package top.ricequakes.ricequaking.Events;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import top.ricequakes.ricequaking.RiceQuaking;

import java.util.List;

public class Die implements Listener {
    public final RiceQuaking plugin;

    public Die(RiceQuaking plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerDie(PlayerDeathEvent event) {
        Location location = event.getEntity().getLocation();
        Bukkit.broadcastMessage(ChatColor.RED + event.getEntity().getName() + "的死亡坐标为：" +
                Math.round(location.getX()) + " " + Math.round(location.getY()) + " " + Math.round(location.getZ()));

        PlayerInventory inventory = event.getEntity().getInventory();
        ItemStack item = inventory.getItemInOffHand();
        if (item.getType() == Material.CLOCK && item.getEnchantmentLevel(Enchantment.DURABILITY) == 10) {
            item.setAmount(item.getAmount() - 1);
            event.setKeepInventory(true);
            event.setKeepLevel(true);
            List<ItemStack> drops = event.getDrops();
            drops.clear();
            event.getEntity().playSound(event.getEntity().getLocation(), Sound.ITEM_TOTEM_USE, 1, 0);
            Bukkit.broadcastMessage(ChatColor.GREEN + "他使用了读盘器，保住了身上的物品！");
        }
    }
}

package top.ricequakes.ricequaking.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import top.ricequakes.ricequaking.Ricequaking;

import java.util.List;

public class FastExchange2EndChest implements CommandExecutor, TabExecutor {
    public final Ricequaking plugin;

    public FastExchange2EndChest(Ricequaking plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("e") && sender instanceof Player) {
            Player player = ((Player) sender);
            PlayerInventory inv = player.getInventory();
            Inventory chest = player.getEnderChest();
            int count = 0;
            ItemStack[] armors = inv.getArmorContents();
            for (int i = 0; i < 4; i++) {
                if (armors[i] != null && chest.firstEmpty() != -1) {
                    chest.addItem(armors[i]);
                    armors[i] = null;
                    count++;
                }
            } inv.setArmorContents(armors);
            for (int i = 0; i < 9; i++) {
                if (inv.getItem(i) != null && chest.firstEmpty() != -1) {
                    chest.addItem(inv.getItem(i));
                    inv.remove(inv.getItem(i));
                    count++;
                }
            }
            player.sendMessage(ChatColor.YELLOW + "成功将 " + count + " 个物品转移至末影箱！");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}

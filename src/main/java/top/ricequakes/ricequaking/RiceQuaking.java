package top.ricequakes.ricequaking;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import top.ricequakes.ricequaking.Events.Die;
import top.ricequakes.ricequaking.Events.Login;
import top.ricequakes.ricequaking.Features.CampfireRestoreHealth;
import top.ricequakes.ricequaking.Features.VillagersTransport;
import top.ricequakes.ricequaking.Commands.FastExchange2EndChest;
import top.ricequakes.ricequaking.Commands.GhostMode;
import top.ricequakes.ricequaking.Commands.Records;
import top.ricequakes.ricequaking.Gui.RecordsGui;

public class RiceQuaking extends JavaPlugin {
    public static RiceQuaking instance;

    @Override
    public void onEnable() {
        instance = this;
        for (int i = 1; i <= 5; i++)
            getLogger().info("RiceQuaking was loaded Successfully !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        new Login(this);
        new Die(this);
        new VillagersTransport(this);
        new CampfireRestoreHealth(this);
        new RecordsGui(this);
        getCommand("sl").setExecutor(new Records(this));
        getCommand("record").setExecutor(new Records(this));
        getCommand("r").setExecutor(new Records(this));
        getCommand("g").setExecutor(new GhostMode(this));
        getCommand("e").setExecutor(new FastExchange2EndChest(this));
        getCommand("rm").setExecutor(new Records(this));
        saveDefaultConfig();
        enableRecipes();
    }

    @Override
    public void onDisable() {
        Bukkit.broadcastMessage(ChatColor.YELLOW + "[RiceQuaking] Reloading plugins...");
    }

    private void enableRecipes() {
        recipeKeepInvClock();
        recipeRottenToBoneMeal();
    }

    private void recipeKeepInvClock() {
        ItemStack item = new ItemStack(Material.CLOCK);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 10);  //??????
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("?????????");
        item.setItemMeta(itemMeta);
        NamespacedKey namespacedKey = new NamespacedKey(this, "InvKeeper");
        ShapedRecipe recipe = new ShapedRecipe(namespacedKey, item);
        recipe.shape(" O ", "OTO", "BBB");
        recipe.setIngredient('O', Material.OBSIDIAN);
        recipe.setIngredient('T', Material.GHAST_TEAR);
        recipe.setIngredient('B', Material.BLAZE_ROD);
        Bukkit.addRecipe(recipe);
    }

    private void recipeRottenToBoneMeal() {
        ItemStack item = new ItemStack(Material.BONE_MEAL);
        NamespacedKey namespacedKey = new NamespacedKey(this, "BoneMeal");
        ShapedRecipe recipe = new ShapedRecipe(namespacedKey, item);
        recipe.shape("RRR", "RRR", "RRR");
        recipe.setIngredient('R', Material.ROTTEN_FLESH);
        Bukkit.addRecipe(recipe);
    }
}
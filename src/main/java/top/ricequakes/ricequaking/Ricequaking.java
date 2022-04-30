package top.ricequakes.ricequaking;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import top.ricequakes.ricequaking.Events.Die;
import top.ricequakes.ricequaking.Events.Login;
import top.ricequakes.ricequaking.Events.MagicControl;
import top.ricequakes.ricequaking.commands.FastExchange2EndChest;
import top.ricequakes.ricequaking.commands.GhostMode;
import top.ricequakes.ricequaking.commands.Records;

public class Ricequaking extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Loaded Successfully!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        new Login(this);
        new Die(this);
        new MagicControl(this);
        getCommand("sl").setExecutor(new Records(this));
        getCommand("record").setExecutor(new Records(this));
        getCommand("r").setExecutor(new Records(this));
        getCommand("g").setExecutor(new GhostMode(this));
        getCommand("e").setExecutor(new FastExchange2EndChest(this));
        getCommand("test").setExecutor(new Records(this));
        saveDefaultConfig();
        enableRecipes();
    }

    @Override
    public void onDisable() {
    }

    private void enableRecipes() {
        recipeKeepInvClock();
        recipeRottenToBoneMeal();
    }

    private void recipeKeepInvClock() {
        ItemStack item = new ItemStack(Material.CLOCK);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 10);  //给物品附魔
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("读盘器");
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
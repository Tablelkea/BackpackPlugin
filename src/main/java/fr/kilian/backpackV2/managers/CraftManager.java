package fr.kilian.backpackV2.managers;

import fr.kilian.backpackV2.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

import java.net.MalformedURLException;

public class CraftManager {

    public NamespacedKey backpackCraft1 = Main.getInstance().registerNBT("backpack-lvl1");
    public NamespacedKey backpackCraft2 = Main.getInstance().registerNBT( "backpack-lvl2");
    public NamespacedKey backpackCraft3 = Main.getInstance().registerNBT( "backpack-lvl3");

    public NamespacedKey craftRuneCraftKey = Main.getInstance().registerNBT( "craft-rune-craft");
    public NamespacedKey enderRuneCraftKey =Main.getInstance().registerNBT("ender-rune-craft");

    public void initBackpackCraft() throws MalformedURLException {
        ItemManager itemManager = Main.getInstance().getItemManager();
        ShapedRecipe recipe_level1 = new ShapedRecipe(backpackCraft1, itemManager.backpackItem(1));
        recipe_level1.shape("ABA","DCD","ABA");
        recipe_level1.setIngredient('A', Material.LEATHER);
        recipe_level1.setIngredient('B', Material.STRING);
        recipe_level1.setIngredient('C', Material.CHEST);
        recipe_level1.setIngredient('D', Material.IRON_INGOT);
        Bukkit.getServer().addRecipe(recipe_level1);

        ShapedRecipe recipe_level2 = new ShapedRecipe(backpackCraft2, itemManager.backpackItem(2));
        recipe_level2.shape("ABA","DCD","ABA");
        recipe_level2.setIngredient('A', Material.IRON_INGOT);
        recipe_level2.setIngredient('B', Material.BLAZE_ROD);
        recipe_level2.setIngredient('C', Material.PLAYER_HEAD);
        recipe_level2.setIngredient('D', Material.GOLD_INGOT);
        Bukkit.getServer().addRecipe(recipe_level2);

        ShapedRecipe recipe_level3 = new ShapedRecipe(backpackCraft3, itemManager.backpackItem(3));
        recipe_level3.shape("ABA","DCD","ABA");
        recipe_level3.setIngredient('A', Material.GOLD_INGOT);
        recipe_level3.setIngredient('B', Material.ENDER_EYE);
        recipe_level3.setIngredient('C', Material.PLAYER_HEAD);
        recipe_level3.setIngredient('D', Material.DIAMOND);
        Bukkit.getServer().addRecipe(recipe_level3);
    }

    public void initRunesCraft() {
        ItemManager itemManager = Main.getInstance().getItemManager();
        CraftManager craftManager = Main.getInstance().getCraftManager();

        ShapedRecipe craftRune = new ShapedRecipe(craftManager.craftRuneCraftKey, itemManager.craftUpgradeItem());
        craftRune.shape("ABA", "BCB", "ABA");
        craftRune.setIngredient('A', Material.IRON_INGOT);
        craftRune.setIngredient('B', Material.BLAZE_ROD);
        craftRune.setIngredient('C', Material.CRAFTING_TABLE);
        Bukkit.getServer().addRecipe(craftRune);

        ShapedRecipe enderRune = new ShapedRecipe(craftManager.enderRuneCraftKey, itemManager.enderChestUpgradeItem());
        enderRune.shape("ABA", "BCB", "ABA");
        enderRune.setIngredient('A', Material.GOLD_INGOT);
        enderRune.setIngredient('B', Material.ENDER_EYE);
        enderRune.setIngredient('C', Material.ENDER_CHEST);
        Bukkit.getServer().addRecipe(enderRune);
    }

    public void initForgeCraft() {

        ForgeManager craftManager = Main.getInstance().getForgeManager();
        ItemManager itemManager = Main.getInstance().getItemManager();

        ShapedRecipe recipe = new ShapedRecipe(craftManager.forgeCraftKey, itemManager.forgeItem());
        recipe.shape("ABA", "BCB", "ABA");
        recipe.setIngredient('A', Material.IRON_INGOT);
        recipe.setIngredient('B', Material.BLAZE_ROD);
        recipe.setIngredient('C', Material.SMITHING_TABLE);
        Bukkit.getServer().addRecipe(recipe);
    }

}

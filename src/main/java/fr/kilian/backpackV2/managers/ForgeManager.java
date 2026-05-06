package fr.kilian.backpackV2.managers;

import fr.kilian.backpackV2.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class ForgeManager {

    public NamespacedKey forgeIdKey = Main.getInstance().registerNBT("forge-block");
    public NamespacedKey forgeCraftKey = Main.getInstance().registerNBT("forge-craft");

    public ItemStack forgeItem() {
        ItemStack forge = new ItemStack(Material.SMITHING_TABLE);
        ItemMeta forgeMeta = forge.getItemMeta();
        forgeMeta.displayName(Component.text("§6§l✦ §e§lForge de Sac §6§l✦"));
        forgeMeta.lore(List.of(
                Component.text("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                Component.text("§7Placez ce bloc pour accéder"),
                Component.text("§7à la §fforge§7 et appliquer"),
                Component.text("§7des §frunes§7 sur vos sacs."),
                Component.text("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                Component.text("§e§l» §6Placez pour activer")
        ));
        forgeMeta.setRarity(ItemRarity.EPIC);
        forgeMeta.getPersistentDataContainer().set(forgeIdKey, PersistentDataType.BOOLEAN, true);
        forge.setItemMeta(forgeMeta);
        return forge;
    }

    public boolean isForge(ItemStack item) {
        if(item == null || item.getItemMeta() == null) return false;
        return item.getItemMeta().getPersistentDataContainer().has(forgeIdKey, PersistentDataType.BOOLEAN);
    }

    public boolean isForgeBlock(Block block) {
        if(block == null || block.getType() != Material.SMITHING_TABLE) return false;
        return block.getChunk().getPersistentDataContainer().has(
                new NamespacedKey(Main.getInstance(), "forge-" + block.getX() + "-" + block.getY() + "-" + block.getZ()),
                PersistentDataType.BOOLEAN
        );
    }

    public void markForgeBlock(@NonNull Block block) {
        block.getChunk().getPersistentDataContainer().set(
                new NamespacedKey(Main.getInstance(), "forge-" + block.getX() + "-" + block.getY() + "-" + block.getZ()),
                PersistentDataType.BOOLEAN,
                true
        );
    }

    public void unmarkForgeBlock(@NonNull Block block) {
        block.getChunk().getPersistentDataContainer().remove(
                new NamespacedKey(Main.getInstance(), "forge-" + block.getX() + "-" + block.getY() + "-" + block.getZ())
        );
    }

    public ItemStack forgeSlotSacItem() {
        ItemStack slot = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta slotMeta = slot.getItemMeta();
        slotMeta.displayName(Component.text("§7Placez votre §fsac à dos"));
        slot.setItemMeta(slotMeta);
        return slot;
    }

    public ItemStack forgeSlotRuneItem() {
        ItemStack slot = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta slotMeta = slot.getItemMeta();
        slotMeta.displayName(Component.text("§7Placez votre §frune"));
        slot.setItemMeta(slotMeta);
        return slot;
    }

    public ItemStack forgeResultLockedItem() {
        ItemStack locked = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta lockedMeta = locked.getItemMeta();
        lockedMeta.displayName(Component.text("§cPlacez un sac et une rune"));
        locked.setItemMeta(lockedMeta);
        return locked;
    }

    public ItemStack forgeConfirmItem() {
        ItemStack confirm = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.displayName(Component.text("§a§l✦ Appliquer la rune §a§l✦"));
        confirmMeta.lore(List.of(
                Component.text("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                Component.text("§7Cliquez pour appliquer"),
                Component.text("§7la rune sur le sac."),
                Component.text("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                Component.text("§a§l» §2Cliquez pour confirmer")
        ));
        confirm.setItemMeta(confirmMeta);
        return confirm;
    }

    public ItemStack voidItem() {
        ItemStack voidit = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta voiditm = voidit.getItemMeta();
        voiditm.setHideTooltip(true);
        voidit.setItemMeta(voiditm);
        return voidit;
    }

    public void initForgeCraft() {
        ShapedRecipe recipe = new ShapedRecipe(forgeCraftKey, forgeItem());
        recipe.shape("ABA", "BCB", "ABA");
        recipe.setIngredient('A', Material.IRON_INGOT);
        recipe.setIngredient('B', Material.BLAZE_ROD);
        recipe.setIngredient('C', Material.SMITHING_TABLE);
        Bukkit.getServer().addRecipe(recipe);
    }

    public NamespacedKey craftRuneCraftKey = Main.getInstance().registerNBT( "craft-rune-craft");
    public NamespacedKey enderRuneCraftKey =Main.getInstance().registerNBT("ender-rune-craft");

    public void initRunesCraft() {
        ShapedRecipe craftRune = new ShapedRecipe(craftRuneCraftKey, Main.getInstance().getBackpackManager().craftUpgradeItem());
        craftRune.shape("ABA", "BCB", "ABA");
        craftRune.setIngredient('A', Material.IRON_INGOT);
        craftRune.setIngredient('B', Material.BLAZE_ROD);
        craftRune.setIngredient('C', Material.CRAFTING_TABLE);
        Bukkit.getServer().addRecipe(craftRune);

        ShapedRecipe enderRune = new ShapedRecipe(enderRuneCraftKey, Main.getInstance().getBackpackManager().enderChestUpgradeItem());
        enderRune.shape("ABA", "BCB", "ABA");
        enderRune.setIngredient('A', Material.GOLD_INGOT);
        enderRune.setIngredient('B', Material.ENDER_EYE);
        enderRune.setIngredient('C', Material.ENDER_CHEST);
        Bukkit.getServer().addRecipe(enderRune);
    }
}
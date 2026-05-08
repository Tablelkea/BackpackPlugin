package fr.kilian.backpack.managers;

import fr.kilian.backpack.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NonNull;

public class ForgeManager {

    public NamespacedKey forgeIdKey = Main.getInstance().registerNBT("forge-block");
    public NamespacedKey forgeCraftKey = Main.getInstance().registerNBT("forge-craft");

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

}
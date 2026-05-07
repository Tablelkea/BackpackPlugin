package fr.kilian.backpackV2.managers;

import fr.kilian.backpackV2.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ItemManager {

    public NamespacedKey backpackIdKey = Main.getInstance().registerNBT("backpack-id");
    public NamespacedKey backpackLevelKey = Main.getInstance().registerNBT("backpack-level");

    public List<ItemStack> runes = new ArrayList<>();

    private static final String SEP = "§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬";

    // ───── Builder générique ─────

    public ItemStack buildItem(Material material, String name, ItemRarity rarity, String... loreLines) {
        ItemStack item = new ItemStack(material);
        item.editMeta(meta -> {
            meta.displayName(Component.text(name));
            meta.setRarity(rarity);
            meta.setMaxStackSize(1);
            meta.lore(Arrays.stream(loreLines).map(Component::text).toList());
        });
        return item;
    }

    public ItemStack buildGuiItem(Material material, String name, String... loreLines) {
        ItemStack item = new ItemStack(material);
        item.editMeta(meta -> {
            meta.displayName(Component.text(name));
            if (loreLines.length > 0)
                meta.lore(Arrays.stream(loreLines).map(Component::text).toList());
        });
        return item;
    }

    // ───── Runes ─────

    public ItemStack craftUpgradeItem() {
        ItemStack buildItem = buildItem(Material.NAUTILUS_SHELL, "§e§l✦ §6§lRune de Craft §e§l✦", ItemRarity.EPIC,
                SEP,
                "§7Appliquez cette rune sur votre",
                "§fsac à dos §7pour débloquer",
                "§fl'accès à une table de craft.",
                SEP,
                "§e§l» §6Clic droit sur le sac pour appliquer"
        );
        runes.add(buildItem);
        return buildItem;
    }

    public ItemStack enderChestUpgradeItem() {
        ItemStack enderchestItem = buildItem(Material.ENDER_EYE, "§5§l✦ §d§lRune d'Ender §5§l✦", ItemRarity.EPIC,
                SEP,
                "§7Appliquez cette rune sur votre",
                "§fsac à dos §7pour débloquer",
                "§fl'accès à votre EnderChest.",
                SEP,
                "§5§l» §dClic droit sur le sac pour appliquer"
        );
        runes.add(enderchestItem);
        return enderchestItem;
    }

    public ItemStack soulUpgradeItem() {
        ItemStack soulItem = buildItem(Material.TOTEM_OF_UNDYING, "§6§l✦ §e§lRune d'Âme §6§l✦", ItemRarity.EPIC,
                SEP,
                "§7Appliquez cette rune sur votre",
                "§fsac à dos §7pour le protéger",
                "§fcontre la mort.",
                SEP,
                "§6§l» §eClic droit sur le sac pour appliquer"
        );
        runes.add(soulItem);
        return soulItem;
    }

    // ───── Sac à dos ─────

    public ItemStack backpackItem(int level){
        String[] textures = {
                "http://textures.minecraft.net/texture/cc1b2f592cfc8d372dcf5fd44eed69dddc64601d7846d72619f70511d8043a89",
                "http://textures.minecraft.net/texture/8e4ebefefa8cb3c5860ac8412659e123ba154d4b7096c3b123c0d1fca63c9799",
                "http://textures.minecraft.net/texture/d4675158c0767ee508c52d52426cef3a2c2b29b7e487c92953a3823f561d06af"
        };

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (meta == null) return null;

        BackpackManager backpackManager = Main.getInstance().getBackpackManager();
        meta.setPlayerProfile(backpackManager.createPlayerProfile(textures[level - 1], "backpack-level" + level));

        meta.displayName(Component.text("§6§l✦ Sac à Dos - Niveau " + level + " §6§l✦"));
        meta.lore(Arrays.stream(new String[]{
                SEP,
                "§7Accédez à votre §fsac à dos",
                "§7personnel depuis §fn'importe où.",
                SEP,
                "§6§l» §eCliquez pour ouvrir"
        }).map(Component::text).toList());
        meta.setRarity(ItemRarity.EPIC);
        meta.setMaxStackSize(1);
        meta.getPersistentDataContainer().set(backpackIdKey, PersistentDataType.STRING, UUID.randomUUID().toString());
        meta.getPersistentDataContainer().set(backpackLevelKey, PersistentDataType.INTEGER, level);

        item.setItemMeta(meta);
        return item;
    }

    // ───── Icônes sac ─────

    public ItemStack workbenchIcon() {
        return buildItem(Material.CRAFTING_TABLE, "§6§l✦ §e§lTable de Craft §6§l✦", ItemRarity.COMMON,
                SEP,
                "§7Accédez à un §fcrafteur complet",
                "§7depuis §fn'importe où§7 dans le monde.",
                SEP,
                "§e§l» §eCliquez pour ouvrir"
        );
    }

    public ItemStack enderChestIcon() {
        return buildItem(Material.ENDER_CHEST, "§5§l✦ §d§lEnder Chest §5§l✦", ItemRarity.COMMON,
                SEP,
                "§7Accédez à votre §fstockage personnel",
                "§7depuis §fn'importe où§7 dans le monde.",
                SEP,
                "§5§l» §dCliquez pour ouvrir"
        );
    }

    // ───── Items GUI Forge ─────

    public ItemStack voidItem() {
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        item.editMeta(meta -> meta.setHideTooltip(true));
        return item;
    }

    public ItemStack forgeSlotSacItem() {
        return buildGuiItem(Material.GRAY_STAINED_GLASS_PANE, "§7Placez votre §fsac à dos");
    }

    public ItemStack forgeSlotRuneItem() {
        return buildGuiItem(Material.GRAY_STAINED_GLASS_PANE, "§7Placez votre §frune");
    }

    public ItemStack forgeResultLockedItem() {
        return buildGuiItem(Material.RED_STAINED_GLASS_PANE, "§cPlacez un sac et une rune");
    }

    public ItemStack forgeConfirmItem() {
        return buildGuiItem(Material.LIME_STAINED_GLASS_PANE, "§a§l✦ Appliquer la rune §a§l✦",
                SEP,
                "§7Cliquez pour appliquer",
                "§7la rune sur le sac.",
                SEP,
                "§a§l» §2Cliquez pour confirmer"
        );
    }

    // ───── Forge ─────

    public ItemStack forgeItem() {
        ForgeManager forgeManager = Main.getInstance().getForgeManager();

        ItemStack item = buildItem(Material.SMITHING_TABLE, "§6§l✦ §e§lForge de Sac §6§l✦", ItemRarity.EPIC,
                SEP,
                "§7Placez ce bloc pour accéder",
                "§7à la §fforge§7 et appliquer",
                "§7des §frunes§7 sur vos sacs.",
                SEP,
                "§e§l» §6Placez pour activer"
        );
        item.editMeta(meta ->
                meta.getPersistentDataContainer().set(forgeManager.forgeIdKey, PersistentDataType.BOOLEAN, true)
        );
        return item;
    }

    public boolean isRune(ItemStack item) {
        if (item == null || item.getType().isAir()) return false;
        for(ItemStack rune : runes){
            if(rune.isSimilar(item)){
                return true;
            }
        }
        return false;
    }


}
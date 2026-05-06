package fr.kilian.backpackV2.managers;

import fr.kilian.backpackV2.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;

public class ItemManager {

    public NamespacedKey backpackIdKey = Main.getInstance().registerNBT("backpack-id");
    public NamespacedKey backpackLevelKey = Main.getInstance().registerNBT("backpack-level");

    public ItemStack craftUpgradeItem(){
        ItemStack upgrade = new ItemStack(Material.NAUTILUS_SHELL);
        ItemMeta upgradeMeta = upgrade.getItemMeta();
        upgradeMeta.displayName(Component.text("§e§l✦ §6§lRune de Craft §e§l✦"));
        upgradeMeta.lore(List.of(
                Component.text("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                Component.text("§7Appliquez cette rune sur votre"),
                Component.text("§fsac à dos §7pour débloquer"),
                Component.text("§fl'accès à une table de craft."),
                Component.text("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                Component.text("§e§l» §6Clic droit sur le sac pour appliquer")
        ));
        upgradeMeta.setRarity(ItemRarity.EPIC);
        upgradeMeta.setMaxStackSize(1);
        upgrade.setItemMeta(upgradeMeta);
        return upgrade;
    }

    public ItemStack enderChestUpgradeItem(){
        ItemStack upgrade = new ItemStack(Material.ENDER_EYE);
        ItemMeta upgradeMeta = upgrade.getItemMeta();
        upgradeMeta.displayName(Component.text("§5§l✦ §d§lRune d'Ender §5§l✦"));
        upgradeMeta.lore(List.of(
                Component.text("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                Component.text("§7Appliquez cette rune sur votre"),
                Component.text("§fsac à dos §7pour débloquer"),
                Component.text("§fl'accès à votre EnderChest."),
                Component.text("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                Component.text("§5§l» §dClic droit sur le sac pour appliquer")
        ));
        upgradeMeta.setRarity(ItemRarity.EPIC);
        upgradeMeta.setMaxStackSize(1);
        upgrade.setItemMeta(upgradeMeta);
        return upgrade;
    }

    public ItemStack backpackItem(int level) throws MalformedURLException {
        ItemStack it = new ItemStack(Material.PLAYER_HEAD);
        if(it.getItemMeta() == null) return null;
        SkullMeta itm = (SkullMeta) it.getItemMeta();
        BackpackManager backpackManager = Main.getInstance().getBackpackManager();

        if(level == 1) {
            itm.setOwnerProfile(backpackManager.createPlayerProfile("http://textures.minecraft.net/texture/cc1b2f592cfc8d372dcf5fd44eed69dddc64601d7846d72619f70511d8043a89", "backpack-level1"));
        } else if(level == 2)
            itm.setOwnerProfile(backpackManager.createPlayerProfile("http://textures.minecraft.net/texture/8e4ebefefa8cb3c5860ac8412659e123ba154d4b7096c3b123c0d1fca63c9799", "backpack-level2"));
        else
            itm.setOwnerProfile(backpackManager.createPlayerProfile("http://textures.minecraft.net/texture/d4675158c0767ee508c52d52426cef3a2c2b29b7e487c92953a3823f561d06af", "backpack-level3"));

        itm.displayName(Component.text("§6§l✦ Sac à Dos - Niveau " + level + " §6§l✦"));
        itm.lore(List.of(
                Component.text("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                Component.text("§7Accédez à votre §fsac à dos"),
                Component.text("§7personnel depuis §fn'importe où."),
                Component.text("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                Component.text("§6§l» §eCliquez pour ouvrir")
        ));
        itm.setRarity(ItemRarity.EPIC);
        itm.setMaxStackSize(1);

        itm.getPersistentDataContainer().set(backpackIdKey, PersistentDataType.STRING, UUID.randomUUID().toString());
        itm.getPersistentDataContainer().set(backpackLevelKey, PersistentDataType.INTEGER, level);

        it.setItemMeta(itm);
        return it;
    }

    public ItemStack workbenchIcon(){
        ItemStack workbench = new ItemStack(Material.CRAFTING_TABLE);
        ItemMeta workbenchMeta = workbench.getItemMeta();
        workbenchMeta.displayName(Component.text("§6§l✦ §e§lTable de Craft §6§l✦"));
        workbenchMeta.lore(List.of(
                Component.text("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                Component.text("§7Accédez à un §fcrafteur complet"),
                Component.text("§7depuis §fn'importe où§7 dans le monde."),
                Component.text("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                Component.text("§e§l» §eCliquez pour ouvrir")
        ));
        workbench.setItemMeta(workbenchMeta);
        return workbench;
    }

    public ItemStack enderChestIcon(){
        ItemStack enderChest = new ItemStack(Material.ENDER_CHEST);
        ItemMeta enderChestMeta = enderChest.getItemMeta();
        enderChestMeta.displayName(Component.text("§5§l✦ §d§lEnder Chest §5§l✦"));
        enderChestMeta.lore(List.of(
                Component.text("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                Component.text("§7Accédez à votre §fstockage personnel"),
                Component.text("§7depuis §fn'importe où§7 dans le monde."),
                Component.text("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                Component.text("§5§l» §dCliquez pour ouvrir")
        ));
        enderChest.setItemMeta(enderChestMeta);
        return enderChest;
    }

    public ItemStack voidItem(){
        ItemStack voidit = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta voiditm = voidit.getItemMeta();
        voiditm.setHideTooltip(true);
        voidit.setItemMeta(voiditm);
        return voidit;
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

    public ItemStack forgeItem() {

        ForgeManager forgeManager = Main.getInstance().getForgeManager();

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
        forgeMeta.getPersistentDataContainer().set(forgeManager.forgeIdKey, PersistentDataType.BOOLEAN, true);
        forge.setItemMeta(forgeMeta);
        return forge;
    }

}

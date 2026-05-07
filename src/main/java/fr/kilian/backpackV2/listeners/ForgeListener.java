package fr.kilian.backpackV2.listeners;

import fr.kilian.backpackV2.Main;
import fr.kilian.backpackV2.managers.BackpackManager;
import fr.kilian.backpackV2.managers.ForgeManager;
import fr.kilian.backpackV2.managers.ItemManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;

public class ForgeListener implements Listener {

    private static final int SLOT_SAC = 11;
    private static final int SLOT_RUNE = 15;
    private static final int SLOT_CONFIRM = 13;

    @EventHandler
    public void onPlace(@NonNull BlockPlaceEvent e) {
        ForgeManager forge = Main.getInstance().getForgeManager();
        if(forge.isForge(e.getItemInHand())) {
            forge.markForgeBlock(e.getBlockPlaced());
            e.getPlayer().sendMessage("§6§lForge placée avec succès !");
        }
    }

    @EventHandler
    public void onBreak(@NonNull BlockBreakEvent e) {
        ItemManager itemManager = Main.getInstance().getItemManager();
        ForgeManager forgeManager = Main.getInstance().getForgeManager();
        if(forgeManager.isForgeBlock(e.getBlock())) {
            forgeManager.unmarkForgeBlock(e.getBlock());
            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), itemManager.forgeItem());
            e.setDropItems(false);
        }
    }

    @EventHandler
    public void onInteract(@NonNull PlayerInteractEvent e) {
        ForgeManager forgeManager = Main.getInstance().getForgeManager();
        ItemManager itemManager = Main.getInstance().getItemManager();

        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(e.getClickedBlock() == null) return;
        if(!forgeManager.isForgeBlock(e.getClickedBlock())) return;

        e.setCancelled(true);
        Player player = e.getPlayer();

        Inventory forgeGUI = Bukkit.createInventory(null, 27, Component.text("§6§l✦ Forge de Sac ✦"));

        for(int i = 0; i < 27; i++) forgeGUI.setItem(i, itemManager.voidItem());

        forgeGUI.setItem(SLOT_SAC, itemManager.forgeSlotSacItem());
        forgeGUI.setItem(SLOT_RUNE, itemManager.forgeSlotRuneItem());
        forgeGUI.setItem(SLOT_CONFIRM, itemManager.forgeResultLockedItem());

        player.openInventory(forgeGUI);
    }

    @EventHandler
    public void onGuiClick(@NonNull InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return;

        if (!e.getView().title().equals(Component.text("§6§l✦ Forge de Sac ✦"))) return;

        BackpackManager backpackManager = Main.getInstance().getBackpackManager();
        ItemManager itemManager = Main.getInstance().getItemManager();
        Inventory gui = e.getInventory();
        ItemStack currentItem = e.getCurrentItem();
        int slot = e.getSlot();

        e.setCancelled(true);

        if (currentItem == null || currentItem.getType().isAir()) return;

        boolean clickedInPlayerInv = e.getClickedInventory() == player.getInventory();
        boolean clickedInGui = e.getClickedInventory() == gui;

        if (clickedInPlayerInv) {

            if (backpackManager.isBackpack(currentItem)) {
                ItemStack previousSac = gui.getItem(SLOT_SAC);

                if (previousSac != null && backpackManager.isBackpack(previousSac)) {
                    player.getInventory().addItem(previousSac);
                }

                player.getInventory().setItem(slot, null);
                gui.setItem(SLOT_SAC, currentItem);

            } else if (itemManager.isRune(currentItem)) {

                ItemStack previousRune = gui.getItem(SLOT_RUNE);

                if (previousRune != null && !previousRune.isSimilar(itemManager.forgeSlotRuneItem())) {
                    player.getInventory().addItem(previousRune);
                }

                player.getInventory().setItem(slot, null);
                gui.setItem(SLOT_RUNE, currentItem);
            }
        }

        if (clickedInGui) {

            if (slot == SLOT_SAC && backpackManager.isBackpack(currentItem)) {
                player.getInventory().addItem(currentItem);
                gui.setItem(SLOT_SAC, itemManager.forgeSlotSacItem());

            } else if (slot == SLOT_RUNE && itemManager.isRune(currentItem)) {
                player.getInventory().addItem(currentItem);
                gui.setItem(SLOT_RUNE, itemManager.forgeSlotRuneItem());

            } else if (slot == SLOT_CONFIRM) {
                handleConfirm(player, gui, itemManager, backpackManager);
                return;
            }
        }

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            ItemStack sacItem = gui.getItem(SLOT_SAC);
            ItemStack runeItem = gui.getItem(SLOT_RUNE);

            boolean sacOk = backpackManager.isBackpack(sacItem);
            boolean runeOk = runeItem != null && itemManager.isRune(currentItem);

            gui.setItem(SLOT_CONFIRM, sacOk && runeOk
                    ? itemManager.forgeConfirmItem()
                    : itemManager.forgeResultLockedItem());
        }, 1L);
    }

    private void handleConfirm(Player player, @NonNull Inventory gui, ItemManager itemManager, @NonNull BackpackManager backpackManager) {
        ItemStack sacItem = gui.getItem(SLOT_SAC);
        ItemStack runeItem = gui.getItem(SLOT_RUNE);

        if (!backpackManager.isBackpack(sacItem) || runeItem == null) return;

        if (runeItem.isSimilar(itemManager.craftUpgradeItem())) {
            if (backpackManager.hasCraftUnlock(sacItem, player)) return;
            backpackManager.applyCraftRune(sacItem, player);

        } else if (runeItem.isSimilar(itemManager.enderChestUpgradeItem())) {
            if (backpackManager.hasEnderUnlock(sacItem, player)) return;
            backpackManager.applyEnderRune(sacItem, player);

        } else if (runeItem.isSimilar(itemManager.soulUpgradeItem())) {
            if (backpackManager.hasSoulUnlock(sacItem, player)) return;
            backpackManager.applySoulRune(sacItem, player);
        }else {
            return;
        }

        if(sacItem == null) return;

        player.getInventory().addItem(sacItem);
        gui.setItem(SLOT_SAC, itemManager.forgeSlotSacItem());
        gui.setItem(SLOT_RUNE, itemManager.forgeSlotRuneItem());
        gui.setItem(SLOT_CONFIRM, itemManager.forgeResultLockedItem());
    }

    @EventHandler
    public void onForgeClose(@NonNull InventoryCloseEvent e) {
        if (!e.getView().title().equals(Component.text("§6§l✦ Forge de Sac ✦"))) return;

        Inventory gui = e.getInventory();
        Player player = (Player) e.getPlayer();

        ItemStack sacItem = gui.getItem(SLOT_SAC);
        ItemStack runeItem = gui.getItem(SLOT_RUNE);

        BackpackManager backpackManager = Main.getInstance().getBackpackManager();
        ItemManager itemManager = Main.getInstance().getItemManager();
        if(sacItem != null && backpackManager.isBackpack(sacItem)) {
            player.getWorld().dropItemNaturally(player.getLocation(), sacItem);
            gui.setItem(SLOT_SAC, null);
        }

        if(runeItem != null && !runeItem.getType().isAir() && itemManager.isRune(runeItem)) {
            player.getWorld().dropItemNaturally(player.getLocation(), runeItem);
            gui.setItem(SLOT_RUNE, null);
        }
    }
}
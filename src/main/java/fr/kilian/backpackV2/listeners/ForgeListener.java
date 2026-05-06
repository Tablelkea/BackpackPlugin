package fr.kilian.backpackV2.listeners;

import fr.kilian.backpackV2.Main;
import fr.kilian.backpackV2.managers.BackpackManager;
import fr.kilian.backpackV2.managers.ForgeManager;
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
        ForgeManager forge = Main.getInstance().getForgeManager();
        if(forge.isForgeBlock(e.getBlock())) {
            forge.unmarkForgeBlock(e.getBlock());
            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), forge.forgeItem());
            e.setDropItems(false);
        }
    }

    @EventHandler
    public void onInteract(@NonNull PlayerInteractEvent e) {
        ForgeManager forge = Main.getInstance().getForgeManager();
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(e.getClickedBlock() == null) return;
        if(!forge.isForgeBlock(e.getClickedBlock())) return;

        e.setCancelled(true);
        Player player = e.getPlayer();

        Inventory gui = Bukkit.createInventory(null, 27, "§6§l✦ Forge de Sac ✦");

        for(int i = 0; i < 27; i++) gui.setItem(i, forge.voidItem());

        gui.setItem(SLOT_SAC, forge.forgeSlotSacItem());
        gui.setItem(SLOT_RUNE, forge.forgeSlotRuneItem());
        gui.setItem(SLOT_CONFIRM, forge.forgeResultLockedItem());

        player.openInventory(gui);
    }

    @EventHandler
    public void onGuiClick(@NonNull InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof Player player)) return;
        if(!e.getView().getTitle().equals("§6§l✦ Forge de Sac ✦")) return;

        ForgeManager forge = Main.getInstance().getForgeManager();
        BackpackManager manager = Main.getInstance().getBackpackManager();
        Inventory gui = e.getInventory();
        ItemStack currentItem = e.getCurrentItem();

        int slot = e.getSlot();

        if(manager.isBackpack(currentItem) && e.getClickedInventory() == player.getInventory()){
            if (currentItem != null) {
                player.getInventory().remove(currentItem);
            }
            gui.setItem(SLOT_SAC, currentItem);
        }

        if((currentItem.isSimilar(manager.craftUpgradeItem()) || currentItem.isSimilar(manager.enderChestUpgradeItem()) )&& e.getClickedInventory() == player.getInventory()){
            player.getInventory().remove(currentItem);
            gui.setItem(SLOT_RUNE, currentItem);
        }

        if(slot != SLOT_SAC && slot != SLOT_RUNE) {
            e.setCancelled(true);
        }

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            ItemStack sacItem = gui.getItem(SLOT_SAC);
            ItemStack runeItem = gui.getItem(SLOT_RUNE);

            if(manager.isBackpack(sacItem) && (
                    runeItem != null && (
                            runeItem.isSimilar(manager.craftUpgradeItem()) ||
                                    runeItem.isSimilar(manager.enderChestUpgradeItem())
                    )
            )) {
                gui.setItem(SLOT_CONFIRM, forge.forgeConfirmItem());
            } else {
                gui.setItem(SLOT_CONFIRM, forge.forgeResultLockedItem());
            }
        }, 1L);

        if(slot == SLOT_CONFIRM) {
            ItemStack sacItem = gui.getItem(SLOT_SAC);
            ItemStack runeItem = gui.getItem(SLOT_RUNE);

            if(!manager.isBackpack(sacItem) || runeItem == null) return;

            if(runeItem.isSimilar(manager.craftUpgradeItem())) {
                if(manager.hasCraftUnlock(sacItem)){
                    player.sendMessage("§cCe sac possède déjà la §e§lRune de Craft§c !");
                    return;
                }
                manager.applyCraftRune(sacItem);
                gui.setItem(SLOT_RUNE, forge.forgeSlotRuneItem());
                gui.setItem(SLOT_SAC, forge.forgeSlotSacItem());
                assert sacItem != null;
                player.getInventory().addItem(sacItem);
                player.sendMessage("§a§lRune de Craft appliquée avec succès !");

            } else if(runeItem.isSimilar(manager.enderChestUpgradeItem())) {
                if(manager.hasEnderUnlock(sacItem)){
                    player.sendMessage("§cCe sac possède déjà la §5§lRune d'Ender§c !");
                    return;
                }
                manager.applyEnderRune(sacItem);
                gui.setItem(SLOT_RUNE, forge.forgeSlotRuneItem());
                gui.setItem(SLOT_SAC, forge.forgeSlotSacItem());
                player.getInventory().addItem(sacItem);
                player.sendMessage("§5§lRune d'Ender appliquée avec succès !");
            }

            gui.setItem(SLOT_CONFIRM, forge.forgeResultLockedItem());
        }
    }

    @EventHandler
    public void onForgeClose(@NonNull InventoryCloseEvent e) {
        if(!e.getView().getTitle().equals("§6§l✦ Forge de Sac ✦")) return;

        Inventory gui = e.getInventory();
        Player player = (Player) e.getPlayer();

        ItemStack sacItem = gui.getItem(SLOT_SAC);
        ItemStack runeItem = gui.getItem(SLOT_RUNE);

        BackpackManager backpackManager = Main.getInstance().getBackpackManager();
        if(sacItem != null && backpackManager.isBackpack(sacItem)) {
            player.getWorld().dropItemNaturally(player.getLocation(), sacItem);
            gui.setItem(SLOT_SAC, null);
        }

        if(runeItem != null && !runeItem.getType().isAir() && (
                runeItem.isSimilar(backpackManager.craftUpgradeItem()) ||
                        runeItem.isSimilar(backpackManager.enderChestUpgradeItem())
        )) {
            player.getWorld().dropItemNaturally(player.getLocation(), runeItem);
            gui.setItem(SLOT_RUNE, null);
        }
    }
}
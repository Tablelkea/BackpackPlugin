package fr.kilian.backpackV2.listeners;

import fr.kilian.backpackV2.Main;
import fr.kilian.backpackV2.managers.BackpackManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PlayerGuiEvent implements Listener {

    @EventHandler
    public void playerGUI(@NonNull InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof Player player)) return;
        Inventory gui = e.getClickedInventory();
        BackpackManager manager = Main.getInstance().getBackpackManager();
        int slot = e.getSlot();

        if(gui == null || gui == player.getInventory()) {
            manager.sendDebugMessage(player, gui != null ? gui.toString() : "null gui");
            return;
        }

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if(!manager.isBackpack(itemInHand)) return;

        Inventory backpack = manager.getPlayerBackpack(itemInHand);
        if(!gui.equals(backpack)) return;

        int size = backpack.getSize();

        for(int i = size - 9; i < size; i++){
            if(slot == i){
                e.setCancelled(true);
                manager.sendDebugMessage(player, "Slot verrouillé (PlayerGuiEvent)");
            }
        }

        if(slot == size - 7){
            if(manager.hasEnderUnlock(itemInHand)){
                player.openInventory(player.getEnderChest());
            } else {
                player.sendMessage("§cVous n'avez pas la §5§lRune d'Ender§c sur ce sac !");
            }
        }

        if(slot == size - 3){
            if(manager.hasCraftUnlock(itemInHand)){
                player.openWorkbench(player.getLocation(), true);
            } else {
                player.sendMessage("§cVous n'avez pas la §e§lRune de Craft§c sur ce sac !");
            }
        }
    }
}
package fr.kilian.backpackV2.listeners;

import fr.kilian.backpackV2.Main;
import fr.kilian.backpackV2.managers.BackpackManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PlayerInteractionEvent implements Listener {

    @EventHandler
    public void playerInteract(@NonNull PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack currentItem = player.getInventory().getItemInMainHand();
        BackpackManager manager = Main.getInstance().getBackpackManager();

        if(manager.isBackpack(currentItem)) {
            e.setCancelled(true);
            manager.openBackpack(currentItem, player.getUniqueId());
        } else {
            manager.sendDebugMessage(player, "Wrong item (PlayerInteractionEvent)");
        }
    }
}
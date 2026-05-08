package fr.kilian.backpack.listeners;

import fr.kilian.backpack.Main;
import fr.kilian.backpack.managers.BackpackManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onInteract(@NonNull PlayerInteractEvent e) {
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

    @EventHandler
    public void playerJoin(@NonNull PlayerJoinEvent e) {
        Main.getInstance().getCraftManager().getAllRecipeKeys
                .forEach(e.getPlayer()::discoverRecipe);
    }
}
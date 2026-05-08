package fr.kilian.backpack.listeners;

import fr.kilian.backpack.Main;
import fr.kilian.backpack.managers.BackpackManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;

public class BackpackDeathListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(@NonNull PlayerDeathEvent e) {
        Player player = e.getEntity();
        BackpackManager backpackManager = Main.getInstance().getBackpackManager();

        // On cherche un sac avec la rune d'âme dans l'inventaire du joueur
        for (ItemStack item : player.getInventory().getContents()) {
            if (backpackManager.isBackpack(item) && backpackManager.hasSoulUnlock(item)) {
                e.getDrops().remove(item); // retire le sac des drops de mort
                // On le remet après respawn
                if(item == null) return;

                ItemStack sac = item.clone();
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                    Player online = Bukkit.getPlayer(player.getUniqueId());
                    if (online != null) {
                        online.getInventory().addItem(sac);
                    }
                }, 1L);
                break; // un seul sac protégé à la fois
            }
        }
    }

}

package fr.kilian.backpackV2.listeners;

import fr.kilian.backpackV2.Main;
import fr.kilian.backpackV2.managers.BackpackManager;
import fr.kilian.backpackV2.managers.ForgeManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PlayerWorldInteraction implements Listener {

    @EventHandler
    public void playerJoin(@NonNull PlayerJoinEvent e){
        Player player = e.getPlayer();

        BackpackManager backpackManager = Main.getInstance().getBackpackManager();
        player.discoverRecipe(backpackManager.backpackCraft1);
        player.discoverRecipe(backpackManager.backpackCraft2);
        player.discoverRecipe(backpackManager.backpackCraft3);
        ForgeManager forgeManager = Main.getInstance().getForgeManager();
        player.discoverRecipe(forgeManager.forgeCraftKey);
        player.discoverRecipe(forgeManager.craftRuneCraftKey);
        player.discoverRecipe(forgeManager.enderRuneCraftKey);
    }

}

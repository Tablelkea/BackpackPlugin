package fr.kilian.backpackV2;

import fr.kilian.backpackV2.managers.BackpackManager;
import fr.kilian.backpackV2.managers.ForgeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.jspecify.annotations.NonNull;

import java.net.MalformedURLException;

public class DebugCommand implements CommandExecutor {

    public static boolean debugMode = false;

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command cmd, @NonNull String label, String[] args) {

        if(sender instanceof Player player){

            if(!debugMode){
                debugMode = true;
                player.sendMessage("§aDebug Mode Activé!");
                try {
                    BackpackManager backpackManager = Main.getInstance().getBackpackManager();
                    ForgeManager forgeManager = Main.getInstance().getForgeManager();
                    PlayerInventory playerInventory = player.getInventory();

                    playerInventory.addItem(backpackManager.backpackItem(1));
                    playerInventory.addItem(backpackManager.backpackItem(2));
                    playerInventory.addItem(backpackManager.backpackItem(3));
                    playerInventory.addItem(forgeManager.forgeItem());
                    playerInventory.addItem(backpackManager.craftUpgradeItem());
                    playerInventory.addItem(backpackManager.enderChestUpgradeItem());

                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }else{
                debugMode = false;
                player.sendMessage("§cDebug Mode Désactivé!");
            }

        }

        return true;
    }
}

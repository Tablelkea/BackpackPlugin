package fr.kilian.backpackV2;

import fr.kilian.backpackV2.managers.BackpackManager;
import fr.kilian.backpackV2.managers.ForgeManager;
import fr.kilian.backpackV2.managers.ItemManager;
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
                    ItemManager itemManager = Main.getInstance().getItemManager();

                    PlayerInventory playerInventory = player.getInventory();

                    playerInventory.addItem(itemManager.backpackItem(1));
                    playerInventory.addItem(itemManager.backpackItem(2));
                    playerInventory.addItem(itemManager.backpackItem(3));
                    playerInventory.addItem(itemManager.forgeItem());
                    playerInventory.addItem(itemManager.craftUpgradeItem());
                    playerInventory.addItem(itemManager.enderChestUpgradeItem());

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

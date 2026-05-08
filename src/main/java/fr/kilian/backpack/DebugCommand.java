package fr.kilian.backpack;

import fr.kilian.backpack.managers.ItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.function.Function;

public class DebugCommand implements CommandExecutor {

    public static boolean debugMode = false;

    private static final List<Function<ItemManager, ItemStack>> DEBUG_ITEMS = List.of(
            m -> {
                try { return m.backpackItem(1); } catch (Exception e) { throw new RuntimeException(e); }
            },
            m -> {
                try { return m.backpackItem(2); } catch (Exception e) { throw new RuntimeException(e); }
            },
            m -> {
                try { return m.backpackItem(3); } catch (Exception e) { throw new RuntimeException(e); }
            },
            m -> {
                try { return m.forgeItem(); } catch (Exception e) { throw new RuntimeException(e); }
            },
            ItemManager::craftUpgradeItem,
            ItemManager::enderChestUpgradeItem,
            ItemManager::soulUpgradeItem
    );

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command cmd, @NonNull String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        debugMode = !debugMode;

        if (debugMode) {
            player.sendMessage("§aDebug Mode Activé!");
            ItemManager itemManager = Main.getInstance().getItemManager();
            DEBUG_ITEMS.stream()
                    .map(fn -> fn.apply(itemManager))
                    .forEach(player.getInventory()::addItem);
        } else {
            player.sendMessage("§cDebug Mode Désactivé!");
        }

        return true;
    }
}

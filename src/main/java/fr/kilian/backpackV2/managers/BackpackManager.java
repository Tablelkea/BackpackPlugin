package fr.kilian.backpackV2.managers;

import fr.kilian.backpackV2.DebugCommand;
import fr.kilian.backpackV2.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BackpackManager implements Listener {

    private final HashMap<String, Inventory> backpacks_level1 = new HashMap<>();
    private final HashMap<String, Inventory> backpacks_level2 = new HashMap<>();
    private final HashMap<String, Inventory> backpacks_level3 = new HashMap<>();



    public NamespacedKey craftUpgrade = Main.getInstance().registerNBT( "craft-upgrade");
    public NamespacedKey enderchestUpgrade =Main.getInstance().registerNBT( "enderchest-upgrade");

    public @NonNull PlayerProfile createPlayerProfile(String url, String name) throws MalformedURLException {
        PlayerProfile profile = Bukkit.createPlayerProfile(name);
        profile.getTextures().setSkin(URI.create(url).toURL());
        return profile;
    }

    public void applyCraftRune(ItemStack backpack) {
        if(!isBackpack(backpack)) return;
        backpack.editMeta(meta -> {
            meta.getPersistentDataContainer().set(craftUpgrade, PersistentDataType.BOOLEAN, true);
            List<Component> lore = meta.lore();

            if(!meta.getPersistentDataContainer().has(enderchestUpgrade)){
                lore.add(Component.text(" "));
                lore.add(Component.text("§3Runes équipées: "));
            }

            lore.add(Component.text("§a    - Rune de Craft"));
            meta.lore(lore);

        });
    }

    public boolean hasCraftUnlock(ItemStack backpack) {
        if(backpack == null || backpack.getItemMeta() == null) return false;
        Boolean value = backpack.getItemMeta().getPersistentDataContainer().get(craftUpgrade, PersistentDataType.BOOLEAN);
        return value != null && value;
    }

    public void applyEnderRune(ItemStack backpack) {
        if(!isBackpack(backpack)) return;
        backpack.editMeta(meta -> {
            meta.getPersistentDataContainer().set(enderchestUpgrade, PersistentDataType.BOOLEAN, true);
            List<Component> lore = meta.lore();

            if(!meta.getPersistentDataContainer().has(craftUpgrade)){
                lore.add(Component.text(" "));
                lore.add(Component.text("§3§lRunes équipées: "));
            }

            lore.add(Component.text("§a    - Rune d'ender"));
            meta.lore(lore);
        });
    }

    public boolean hasEnderUnlock(ItemStack backpack) {
        if(backpack == null || backpack.getItemMeta() == null) return false;
        Boolean value = backpack.getItemMeta().getPersistentDataContainer().get(enderchestUpgrade, PersistentDataType.BOOLEAN);
        return value != null && value;
    }

    public String getBackpackId(ItemStack item) {
        if(item == null || item.getItemMeta() == null) return null;
        return item.getItemMeta().getPersistentDataContainer().get(Main.getInstance().getItemManager().backpackIdKey, PersistentDataType.STRING);
    }

    public int getBackpackLevel(ItemStack item) {
        if(item == null || item.getItemMeta() == null) return -1;
        Integer level = item.getItemMeta().getPersistentDataContainer().get(Main.getInstance().getItemManager().backpackLevelKey, PersistentDataType.INTEGER);
        return level != null ? level : -1;
    }

    public boolean isBackpack(ItemStack item) {
        return getBackpackId(item) != null;
    }

    public Inventory getPlayerBackpack(ItemStack itemStack) {
        String backpackId = getBackpackId(itemStack);
        if(backpackId == null) return null;

        int level = getBackpackLevel(itemStack);

        switch(level) {
            case 1 -> {
                backpacks_level1.computeIfAbsent(backpackId, id ->
                        Bukkit.createInventory(null, 36, Component.text("§8§lSac a dos"))
                );
                return backpacks_level1.get(backpackId);
            }
            case 2 -> {
                backpacks_level2.computeIfAbsent(backpackId, id ->
                        Bukkit.createInventory(null, 45, Component.text("§8§lSac a dos"))
                );
                return backpacks_level2.get(backpackId);
            }
            default -> {
                backpacks_level3.computeIfAbsent(backpackId, id ->
                        Bukkit.createInventory(null, 54, Component.text("§8§lSac a dos"))
                );
                return backpacks_level3.get(backpackId);
            }
        }
    }

    public void openBackpack(ItemStack itemStack, UUID playerUUID) {
        Player player = Bukkit.getPlayer(playerUUID);
        ItemManager itemManager = Main.getInstance().getItemManager();
        if(player == null || !player.isOnline()) return;

        Inventory backpack = getPlayerBackpack(itemStack);
        if(backpack == null) return;

        int size = backpack.getSize();

        // Remplissage de la dernière ligne
        for(int i = size - 9; i < size; i++){
            backpack.setItem(i, itemManager.voidItem());
        }
        if(hasCraftUnlock(itemStack)){
            backpack.setItem(size - 3, itemManager.workbenchIcon());
        } else {
            backpack.setItem(size - 3, itemManager.voidItem());
        }

        if(hasEnderUnlock(itemStack)){
            backpack.setItem(size - 7, itemManager.enderChestIcon());
        } else {
            backpack.setItem(size - 7, itemManager.voidItem());
        }

        player.openInventory(backpack);
    }

    @EventHandler
    public void onCraft(@org.jspecify.annotations.NonNull CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack result = event.getRecipe().getResult();
        int resultLevel = getBackpackLevel(result);

        if(resultLevel == 2 || resultLevel == 3){
            ItemStack centerItem = event.getInventory().getMatrix()[4];
            int requiredLevel = resultLevel - 1;

            if(getBackpackLevel(centerItem) != requiredLevel){
                event.setCancelled(true);
                player.sendMessage("§cVous devez utiliser un §lSac à Dos Niveau " + requiredLevel + "§c pour crafter le niveau " + resultLevel + " !");
            }
        }
    }

    public void sendDebugMessage(Player player, String message){
        if(DebugCommand.debugMode)
            player.sendMessage("§3§lDEBUG§7: §6" + message);
    }
}
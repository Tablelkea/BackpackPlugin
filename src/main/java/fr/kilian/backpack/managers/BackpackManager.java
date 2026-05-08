package fr.kilian.backpack.managers;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import fr.kilian.backpack.DebugCommand;
import fr.kilian.backpack.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class BackpackManager implements Listener {

    public NamespacedKey craftUpgrade = Main.getInstance().registerNBT( "craft-upgrade");
    public NamespacedKey enderchestUpgrade =Main.getInstance().registerNBT( "enderchest-upgrade");
    public NamespacedKey soulUpgrage = Main.getInstance().registerNBT("soul-upgrade");
    public NamespacedKey runeHeaderKey = Main.getInstance().registerNBT("rune-header");

    public PlayerProfile createPlayerProfile(String textureUrl, String name) {
        PlayerProfile profile =
                Bukkit.createProfile(UUID.randomUUID(), name);

        String json = "{\"textures\":{\"SKIN\":{\"url\":\"" + textureUrl + "\"}}}";
        String base64 = Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));

        profile.setProperty(new ProfileProperty("textures", base64));
        return profile;
    }

    private void addRuneLore(@org.jspecify.annotations.NonNull ItemMeta meta, String runeLine) {
        List<Component> lore = meta.lore();
        if (lore == null) lore = new ArrayList<>();

        // Ajoute le header une seule fois
        if (!meta.getPersistentDataContainer().has(runeHeaderKey)) {
            meta.getPersistentDataContainer().set(runeHeaderKey, PersistentDataType.BOOLEAN, true);
            lore.add(Component.text(" "));
            lore.add(Component.text("§3§lRunes équipées :"));
        }

        lore.add(Component.text("§a    - " + runeLine));
        meta.lore(lore);
    }

    public void applyCraftRune(ItemStack backpack, Player player) {
        if (!isBackpack(backpack)) return;
        backpack.editMeta(meta -> {
            meta.getPersistentDataContainer().set(craftUpgrade, PersistentDataType.BOOLEAN, true);
            addRuneLore(meta, "Rune de Craft");
        });
        player.sendMessage("§a§lRune de Craft appliquée avec succès !");
    }

    public boolean hasCraftUnlock(ItemStack backpack, Player player) {
        if(backpack == null || backpack.getItemMeta() == null) return false;
        PersistentDataContainer pdc = backpack.getItemMeta().getPersistentDataContainer();
        boolean value = pdc.has(craftUpgrade, PersistentDataType.BOOLEAN);
        if(value) player.sendMessage("§cCe sac possède déjà la §e§lRune de Craft§c !");
        return value;
    }

    public boolean hasCraftUnlock(ItemStack backpack) {
        if(backpack == null || backpack.getItemMeta() == null) return false;
        PersistentDataContainer pdc = backpack.getItemMeta().getPersistentDataContainer();
        return pdc.has(craftUpgrade, PersistentDataType.BOOLEAN);
    }

    public void applyEnderRune(ItemStack backpack, Player player) {
        if (!isBackpack(backpack)) return;
        backpack.editMeta(meta -> {
            meta.getPersistentDataContainer().set(enderchestUpgrade, PersistentDataType.BOOLEAN, true);
            addRuneLore(meta, "Rune d'Ender");
        });
        player.sendMessage("§5§lRune d'Ender appliquée avec succès !");
    }

    public boolean hasEnderUnlock(ItemStack backpack, Player player) {
        if(backpack == null || backpack.getItemMeta() == null) return false;
        PersistentDataContainer pdc = backpack.getItemMeta().getPersistentDataContainer();
        boolean value = pdc.has(enderchestUpgrade, PersistentDataType.BOOLEAN);
        if (value) player.sendMessage("§cCe sac possède déjà la §5§lRune d'Ender§c !");

        return value;
    }

    public boolean hasEnderUnlock(ItemStack backpack) {
        if(backpack == null || backpack.getItemMeta() == null) return false;
        PersistentDataContainer pdc = backpack.getItemMeta().getPersistentDataContainer();

        return pdc.has(enderchestUpgrade, PersistentDataType.BOOLEAN);
    }

    public void applySoulRune(ItemStack backpack, Player player) {
        if (!isBackpack(backpack)) return;
        backpack.editMeta(meta -> {
            meta.getPersistentDataContainer().set(soulUpgrage, PersistentDataType.BOOLEAN, true);
            addRuneLore(meta, "Rune d'Âme");
        });
        player.sendMessage("§6§lRune d'Âme appliquée avec succès !");
    }

    public boolean hasSoulUnlock(ItemStack backpack, Player player) {
        if (backpack == null || !backpack.hasItemMeta()) return false;
        PersistentDataContainer pdc = backpack.getItemMeta().getPersistentDataContainer();
        boolean value = pdc.has(soulUpgrage, PersistentDataType.BOOLEAN);
        if(value) player.sendMessage("§cCe sac possède déjà la §e§lRune d'Âme§c !");
        return value;
    }

    public boolean hasSoulUnlock(ItemStack backpack) {
        if (backpack == null || !backpack.hasItemMeta()) return false;
        PersistentDataContainer pdc = backpack.getItemMeta().getPersistentDataContainer();
        return pdc.has(soulUpgrage, PersistentDataType.BOOLEAN);
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

    private final HashMap<String, Inventory> backpacks = new HashMap<>();

    public Inventory getPlayerBackpack(ItemStack itemStack) {
        String backpackId = getBackpackId(itemStack);
        if (backpackId == null) return null;

        int size = switch (getBackpackLevel(itemStack)) {
            case 1 -> 36;
            case 2 -> 45;
            default -> 54;
        };

        return backpacks.computeIfAbsent(backpackId,
                id -> Bukkit.createInventory(null, size, Component.text("§8§lSac a dos"))
        );
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
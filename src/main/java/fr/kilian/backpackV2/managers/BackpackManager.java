package fr.kilian.backpackV2.managers;

import fr.kilian.backpackV2.DebugCommand;
import fr.kilian.backpackV2.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
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

    public NamespacedKey backpackIdKey = Main.getInstance().registerNBT("backpack-id");

    public NamespacedKey backpackLevelKey = Main.getInstance().registerNBT("backpack-level");
    public NamespacedKey backpackCraft1 = Main.getInstance().registerNBT("backpack-lvl1");
    public NamespacedKey backpackCraft2 = Main.getInstance().registerNBT( "backpack-lvl2");
    public NamespacedKey backpackCraft3 = Main.getInstance().registerNBT( "backpack-lvl3");

    public NamespacedKey craftUpgrade = Main.getInstance().registerNBT( "craft-upgrade");
    public NamespacedKey enderchestUpgrade =Main.getInstance().registerNBT( "enderchest-upgrade");

    public ItemStack craftUpgradeItem(){
        ItemStack upgrade = new ItemStack(Material.NAUTILUS_SHELL);
        ItemMeta upgradeMeta = upgrade.getItemMeta();
        upgradeMeta.displayName(Component.text("§e§l✦ §6§lRune de Craft §e§l✦"));
        upgradeMeta.lore(List.of(
                Component.text("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                Component.text("§7Appliquez cette rune sur votre"),
                Component.text("§fsac à dos §7pour débloquer"),
                Component.text("§fl'accès à une table de craft."),
                Component.text("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                Component.text("§e§l» §6Clic droit sur le sac pour appliquer")
        ));
        upgradeMeta.setRarity(ItemRarity.EPIC);
        upgrade.setItemMeta(upgradeMeta);
        return upgrade;
    }

    public ItemStack enderChestUpgradeItem(){
        ItemStack upgrade = new ItemStack(Material.ENDER_EYE);
        ItemMeta upgradeMeta = upgrade.getItemMeta();
        upgradeMeta.displayName(Component.text("§5§l✦ §d§lRune d'Ender §5§l✦"));
        upgradeMeta.lore(List.of(
                Component.text("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                Component.text("§7Appliquez cette rune sur votre"),
                Component.text("§fsac à dos §7pour débloquer"),
                Component.text("§fl'accès à votre EnderChest."),
                Component.text("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                Component.text("§5§l» §dClic droit sur le sac pour appliquer")
        ));
        upgradeMeta.setRarity(ItemRarity.EPIC);
        upgrade.setItemMeta(upgradeMeta);
        return upgrade;
    }

    public ItemStack backpackItem(int level) throws MalformedURLException {
        ItemStack it = new ItemStack(Material.PLAYER_HEAD);
        if(it.getItemMeta() == null) return null;
        SkullMeta itm = (SkullMeta) it.getItemMeta();

        if(level == 1)
            itm.setOwnerProfile(createPlayerProfile("http://textures.minecraft.net/texture/cc1b2f592cfc8d372dcf5fd44eed69dddc64601d7846d72619f70511d8043a89", "backpack-level1"));
        else if(level == 2)
            itm.setOwnerProfile(createPlayerProfile("http://textures.minecraft.net/texture/8e4ebefefa8cb3c5860ac8412659e123ba154d4b7096c3b123c0d1fca63c9799", "backpack-level2"));
        else
            itm.setOwnerProfile(createPlayerProfile("http://textures.minecraft.net/texture/d4675158c0767ee508c52d52426cef3a2c2b29b7e487c92953a3823f561d06af", "backpack-level3"));

        itm.displayName(Component.text("§6§l✦ Sac à Dos - Niveau " + level + " §6§l✦"));
        itm.lore(List.of(
                Component.text("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                Component.text("§7Accédez à votre §fsac à dos"),
                Component.text("§7personnel depuis §fn'importe où."),
                Component.text("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                Component.text("§6§l» §eCliquez pour ouvrir")
        ));
        itm.setRarity(ItemRarity.EPIC);
        itm.setMaxStackSize(1);

        itm.getPersistentDataContainer().set(backpackIdKey, PersistentDataType.STRING, UUID.randomUUID().toString());
        itm.getPersistentDataContainer().set(backpackLevelKey, PersistentDataType.INTEGER, level);

        it.setItemMeta(itm);
        return it;
    }

    public @NonNull PlayerProfile createPlayerProfile(String url, String name) throws MalformedURLException {
        PlayerProfile profile = Bukkit.createPlayerProfile(name);
        profile.getTextures().setSkin(URI.create(url).toURL());
        return profile;
    }

    public void applyCraftRune(ItemStack backpack) {
        if(!isBackpack(backpack)) return;
        backpack.editMeta(meta -> meta.getPersistentDataContainer().set(craftUpgrade, PersistentDataType.BOOLEAN, true));
    }

    public boolean hasCraftUnlock(ItemStack backpack) {
        if(backpack == null || backpack.getItemMeta() == null) return false;
        Boolean value = backpack.getItemMeta().getPersistentDataContainer().get(craftUpgrade, PersistentDataType.BOOLEAN);
        return value != null && value;
    }


    public void applyEnderRune(ItemStack backpack) {
        if(!isBackpack(backpack)) return;
        backpack.editMeta(meta -> meta.getPersistentDataContainer().set(enderchestUpgrade, PersistentDataType.BOOLEAN, true));
    }

    public boolean hasEnderUnlock(ItemStack backpack) {
        if(backpack == null || backpack.getItemMeta() == null) return false;
        Boolean value = backpack.getItemMeta().getPersistentDataContainer().get(enderchestUpgrade, PersistentDataType.BOOLEAN);
        return value != null && value;
    }

    public String getBackpackId(ItemStack item) {
        if(item == null || item.getItemMeta() == null) return null;
        return item.getItemMeta().getPersistentDataContainer().get(backpackIdKey, PersistentDataType.STRING);
    }

    public int getBackpackLevel(ItemStack item) {
        if(item == null || item.getItemMeta() == null) return -1;
        Integer level = item.getItemMeta().getPersistentDataContainer().get(backpackLevelKey, PersistentDataType.INTEGER);
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

    public ItemStack workbenchIcon(){
        ItemStack workbench = new ItemStack(Material.CRAFTING_TABLE);
        ItemMeta workbenchMeta = workbench.getItemMeta();
        workbenchMeta.displayName(Component.text("§6§l✦ §e§lTable de Craft §6§l✦"));
        workbenchMeta.lore(List.of(
                Component.text("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                Component.text("§7Accédez à un §fcrafteur complet"),
                Component.text("§7depuis §fn'importe où§7 dans le monde."),
                Component.text("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                Component.text("§e§l» §eCliquez pour ouvrir")
        ));
        workbench.setItemMeta(workbenchMeta);
        return workbench;
    }

    public ItemStack enderChestIcon(){
        ItemStack enderChest = new ItemStack(Material.ENDER_CHEST);
        ItemMeta enderChestMeta = enderChest.getItemMeta();
        enderChestMeta.displayName(Component.text("§5§l✦ §d§lEnder Chest §5§l✦"));
        enderChestMeta.lore(List.of(
                Component.text("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                Component.text("§7Accédez à votre §fstockage personnel"),
                Component.text("§7depuis §fn'importe où§7 dans le monde."),
                Component.text("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                Component.text("§5§l» §dCliquez pour ouvrir")
        ));
        enderChest.setItemMeta(enderChestMeta);
        return enderChest;
    }

    public ItemStack voidItem(){
        ItemStack voidit = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta voiditm = voidit.getItemMeta();
        voiditm.setHideTooltip(true);
        voidit.setItemMeta(voiditm);
        return voidit;
    }

    public void openBackpack(ItemStack itemStack, UUID playerUUID) {
        Player player = Bukkit.getPlayer(playerUUID);
        if(player == null || !player.isOnline()) return;

        Inventory backpack = getPlayerBackpack(itemStack);
        if(backpack == null) return;

        int size = backpack.getSize();

        // Remplissage de la dernière ligne
        for(int i = size - 9; i < size; i++){
            backpack.setItem(i, voidItem());
        }
        if(hasCraftUnlock(itemStack)){
            backpack.setItem(size - 3, workbenchIcon());
        } else {
            backpack.setItem(size - 3, voidItem());
        }

        if(hasEnderUnlock(itemStack)){
            backpack.setItem(size - 7, enderChestIcon());
        } else {
            backpack.setItem(size - 7, voidItem());
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

    public void initBackpackCraft() throws MalformedURLException {
        ShapedRecipe recipe_level1 = new ShapedRecipe(backpackCraft1, backpackItem(1));
        recipe_level1.shape("ABA","DCD","ABA");
        recipe_level1.setIngredient('A', Material.LEATHER);
        recipe_level1.setIngredient('B', Material.STRING);
        recipe_level1.setIngredient('C', Material.CHEST);
        recipe_level1.setIngredient('D', Material.IRON_INGOT);
        Bukkit.getServer().addRecipe(recipe_level1);

        ShapedRecipe recipe_level2 = new ShapedRecipe(backpackCraft2, backpackItem(2));
        recipe_level2.shape("ABA","DCD","ABA");
        recipe_level2.setIngredient('A', Material.IRON_INGOT);
        recipe_level2.setIngredient('B', Material.BLAZE_ROD);
        recipe_level2.setIngredient('C', Material.PLAYER_HEAD);
        recipe_level2.setIngredient('D', Material.GOLD_INGOT);
        Bukkit.getServer().addRecipe(recipe_level2);

        ShapedRecipe recipe_level3 = new ShapedRecipe(backpackCraft3, backpackItem(3));
        recipe_level3.shape("ABA","DCD","ABA");
        recipe_level3.setIngredient('A', Material.GOLD_INGOT);
        recipe_level3.setIngredient('B', Material.ENDER_EYE);
        recipe_level3.setIngredient('C', Material.PLAYER_HEAD);
        recipe_level3.setIngredient('D', Material.DIAMOND);
        Bukkit.getServer().addRecipe(recipe_level3);
    }

    public void sendDebugMessage(Player player, String message){
        if(DebugCommand.debugMode)
            player.sendMessage("§3§lDEBUG§7: §6" + message);
    }
}
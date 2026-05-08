package fr.kilian.backpack;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class NBTCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command cmd, @NonNull String label, String[] args) {

        if(sender instanceof Player player){
            ItemStack item = player.getInventory().getItemInMainHand();

            if(item.getType().isAir()){
                player.sendMessage("§cTenez un item en main !");
                return true;
            }

            ItemMeta meta = item.getItemMeta();
            if(meta == null){
                player.sendMessage("§cCet item n'a pas de métadonnées !");
                return true;
            }
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            if(args.length == 0){
                Set<NamespacedKey> keys = pdc.getKeys();

                int size = Math.max(9, (int) Math.ceil(keys.size() / 9.0) * 9);
                size = Math.min(size, 54);
                Inventory gui = Bukkit.createInventory(null, size, Component.text("§8§lNBT: §7" + item.getType().name()));

                if(keys.isEmpty()){
                    ItemStack empty = new ItemStack(Material.BARRIER);
                    ItemMeta emptyMeta = empty.getItemMeta();
                    emptyMeta.displayName(Component.text("§cAucun NBT trouvé"));
                    empty.setItemMeta(emptyMeta);
                    gui.setItem(0, empty);
                    player.openInventory(gui);
                    return true;
                }

                for(NamespacedKey key : keys){
                    ItemStack nbtItem = new ItemStack(Material.PAPER);
                    ItemMeta nbtMeta = nbtItem.getItemMeta();

                    String value = "§cInconnu";
                    if(pdc.has(key, PersistentDataType.STRING)){
                        value = "§a" + pdc.get(key, PersistentDataType.STRING);
                    } else if(pdc.has(key, PersistentDataType.INTEGER)){
                        value = "§b" + pdc.get(key, PersistentDataType.INTEGER);
                    } else if(pdc.has(key, PersistentDataType.BOOLEAN)){
                        value = "§e" + pdc.get(key, PersistentDataType.BOOLEAN);
                    } else if(pdc.has(key, PersistentDataType.DOUBLE)){
                        value = "§d" + pdc.get(key, PersistentDataType.DOUBLE);
                    } else if(pdc.has(key, PersistentDataType.FLOAT)){
                        value = "§d" + pdc.get(key, PersistentDataType.FLOAT);
                    } else if(pdc.has(key, PersistentDataType.LONG)){
                        value = "§b" + pdc.get(key, PersistentDataType.LONG);
                    } else if(pdc.has(key, PersistentDataType.BYTE)){
                        value = "§b" + pdc.get(key, PersistentDataType.BYTE);
                    }

                    nbtMeta.displayName(Component.text("§6§l" + key.getKey()));
                    nbtMeta.lore(List.of(
                            Component.text("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                            Component.text("§7Namespace §f» §7" + key.getNamespace()),
                            Component.text("§7Clé      §f» §7" + key.getKey()),
                            Component.text("§7Valeur   §f» " + value),
                            Component.text("§8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬")
                    ));
                    nbtItem.setItemMeta(nbtMeta);
                    gui.addItem(nbtItem);
                }

                player.openInventory(gui);
            }else {
                HashMap<String, NamespacedKey> nbtList = Main.nbtList;
                if(args[0].equalsIgnoreCase("add")){
                    String key = args[1];

                    if(nbtList.containsKey(key)){
                        pdc.set(nbtList.get(key), PersistentDataType.BOOLEAN, true);
                        item.setItemMeta(meta);
                        player.sendMessage("§aNBT \"" + key + "\" ajouté avec succès !");
                    } else {
                        player.sendMessage("§cClé NBT \"" + key + "\" introuvable.");
                    }

                }else if(args[0].equalsIgnoreCase("remove")){
                    String key = args[1];

                    if(pdc.has(nbtList.get(key))){
                        pdc.remove(nbtList.get(key));
                        item.setItemMeta(meta);
                        player.sendMessage("§eNBT \"" + key + "\" supprimé.");
                    } else {
                        player.sendMessage("§cCe NBT n'est pas présent sur l'objet.");
                    }
                }
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender, @NonNull Command cmd, @NonNull String label, String @NonNull [] args) {

        if(args.length == 1){
            return List.of("add", "remove");
        }

        else if(args.length == 2){

            return new ArrayList<>(Main.nbtList.keySet());
        }

        return List.of();
    }
}

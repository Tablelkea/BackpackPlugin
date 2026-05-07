package fr.kilian.backpackV2;

import fr.kilian.backpackV2.listeners.*;
import fr.kilian.backpackV2.managers.BackpackManager;
import fr.kilian.backpackV2.managers.CraftManager;
import fr.kilian.backpackV2.managers.ForgeManager;
import fr.kilian.backpackV2.managers.ItemManager;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.net.MalformedURLException;
import java.util.HashMap;

public final class Main extends JavaPlugin {

    public static HashMap<String, NamespacedKey> nbtList = new HashMap<>();

    public static Main instance;
    private BackpackManager backpackManager;
    private ForgeManager forgeManager;
    private CraftManager craftManager;
    private ItemManager itemManager;

    @Override
    public void onEnable() {
        instance = this;
        backpackManager = new BackpackManager();
        forgeManager = new ForgeManager();
        craftManager = new CraftManager();
        itemManager = new ItemManager();

        registerCommand("debug", new DebugCommand());
        registerCommand("nbt", new NBTCommand());

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new BackpackListener(), this);
        pluginManager.registerEvents(new PlayerInteractListener(), this);
        pluginManager.registerEvents(new BackpackManager(), this);
        pluginManager.registerEvents(new ForgeListener(), this);
        pluginManager.registerEvents(new BackpackDeathListener(), this);
        pluginManager.registerEvents(new MobsDeathListener(), this);

        try {
            craftManager.initBackpackCraft();
            craftManager.initForgeCraft();
            craftManager.initRunesCraft();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onDisable() {
    }

    public static Main getInstance(){return instance;}

    public BackpackManager getBackpackManager() {return backpackManager;}

    public ForgeManager getForgeManager() {return forgeManager;}

    public CraftManager getCraftManager() {
        return craftManager;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public @NonNull NamespacedKey registerNBT(String key){
        NamespacedKey namespacedKey = new NamespacedKey(this, key);
        nbtList.put(key, namespacedKey);

        return namespacedKey;
    }

    public void registerCommand(String command, CommandExecutor commandExecutor){
        PluginCommand cmd = Main.getInstance().getCommand(command);
        if(cmd == null) return;

        cmd.setExecutor(commandExecutor);
    }
}

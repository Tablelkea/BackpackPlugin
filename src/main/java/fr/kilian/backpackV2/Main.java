package fr.kilian.backpackV2;

import fr.kilian.backpackV2.listeners.ForgeListener;
import fr.kilian.backpackV2.listeners.PlayerGuiEvent;
import fr.kilian.backpackV2.listeners.PlayerInteractionEvent;
import fr.kilian.backpackV2.listeners.PlayerWorldInteraction;
import fr.kilian.backpackV2.managers.BackpackManager;
import fr.kilian.backpackV2.managers.ForgeManager;
import org.bukkit.NamespacedKey;
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

    @Override
    public void onEnable() {
        instance = this;
        backpackManager = new BackpackManager();
        forgeManager = new ForgeManager();

        getCommand("debug").setExecutor(new DebugCommand());
        getCommand("nbt").setExecutor(new NBTCommand());

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerGuiEvent(), this);
        pluginManager.registerEvents(new PlayerInteractionEvent(), this);
        pluginManager.registerEvents(new PlayerWorldInteraction(), this);
        pluginManager.registerEvents(new BackpackManager(), this);
        pluginManager.registerEvents(new ForgeListener(), this);

        try {
            backpackManager.initBackpackCraft();
            forgeManager.initForgeCraft();
            forgeManager.initRunesCraft();
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

    public @NonNull NamespacedKey registerNBT(String key){
        NamespacedKey namespacedKey = new NamespacedKey(this, key);
        nbtList.put(key, namespacedKey);

        return namespacedKey;
    }
}

package fr.kilian.backpackV2.listeners;

import fr.kilian.backpackV2.Main;
import fr.kilian.backpackV2.managers.ItemManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class MobsDeathListener implements Listener {

    @EventHandler
    public void onDeath(@NonNull EntityDeathEvent event){

        List<EntityType> HOSTILE_MOBS = List.of(
                EntityType.BLAZE,
                EntityType.BOGGED,
                EntityType.BREEZE,
                EntityType.CAVE_SPIDER,
                EntityType.CREEPER,
                EntityType.DROWNED,
                EntityType.ELDER_GUARDIAN,
                EntityType.ENDERMAN,
                EntityType.ENDERMITE,
                EntityType.EVOKER,
                EntityType.GHAST,
                EntityType.GUARDIAN,
                EntityType.HOGLIN,
                EntityType.HUSK,
                EntityType.ILLUSIONER,
                EntityType.MAGMA_CUBE,
                EntityType.PHANTOM,
                EntityType.PIGLIN,
                EntityType.PIGLIN_BRUTE,
                EntityType.PILLAGER,
                EntityType.RAVAGER,
                EntityType.SHULKER,
                EntityType.SILVERFISH,
                EntityType.SKELETON,
                EntityType.SLIME,
                EntityType.SPIDER,
                EntityType.STRAY,
                EntityType.VEX,
                EntityType.VINDICATOR,
                EntityType.WARDEN,
                EntityType.WITCH,
                EntityType.WITHER,
                EntityType.WITHER_SKELETON,
                EntityType.ZOGLIN,
                EntityType.ZOMBIE,
                EntityType.ZOMBIE_VILLAGER,
                EntityType.ZOMBIFIED_PIGLIN
        );

        Entity entity = event.getEntity();

        if(HOSTILE_MOBS.contains(entity.getType())){

            ItemManager itemManager = Main.getInstance().getItemManager();

            if (Math.random() <= 0.01) entity.getWorld().dropItemNaturally(entity.getLocation(), itemManager.backpackItem(1));
            if (Math.random() <= 0.01) entity.getWorld().dropItemNaturally(entity.getLocation(), itemManager.craftUpgradeItem());
            if (Math.random() <= 0.01) entity.getWorld().dropItemNaturally(entity.getLocation(), itemManager.enderChestUpgradeItem());
            if (Math.random() <= 0.01) entity.getWorld().dropItemNaturally(entity.getLocation(), itemManager.soulUpgradeItem());
        }

    }

}

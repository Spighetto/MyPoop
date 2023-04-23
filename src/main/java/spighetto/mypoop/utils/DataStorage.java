package spighetto.mypoop.utils;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import spighetto.mypoop.MyPoop;
import spighetto.mypoop.config.ConfigManager;
import spighettoapi.common.interfaces.IPoop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataStorage {
    public static Map<UUID, Integer> playersLevelFood = new HashMap<>();
    public static ArrayList<UUID> listPoops = new ArrayList<>();

    public static void addEntry(IPoop poop) {
        listPoops.add(poop.getPoopItem().getUniqueId());
        Chunk poopChunk = poop.getPoopItem().getLocation().getChunk();

        Bukkit.getScheduler().scheduleSyncDelayedTask(MyPoop.getPluginInstance(), () -> {
            listPoops.remove(poop.getPoopItem().getUniqueId());

            boolean needToLoad = poopChunk.isLoaded();
            if(!needToLoad)
                poopChunk.load();

            poop.delete();

            if(!needToLoad)
                poopChunk.unload();

        }, ConfigManager.getPoopConfig().getDelay());
    }

    public static void deleteEntries() {
        if(listPoops.size() > 0) {
            ArrayList<Entity> entityBuffer = new ArrayList<>();

            for(World world : MyPoop.getPluginInstance().getServer().getWorlds()) {
                for(Entity entityInWorld : world.getEntities()) {
                    for(UUID ii : listPoops) {
                        if(ii.equals(entityInWorld.getUniqueId())) {
                            entityBuffer.add(entityInWorld);
                        }
                    }
                }
            }

            listPoops.clear();

            entityBuffer.forEach(Entity::remove);		// Method reference technique
        }
    }

    public static boolean isPlayerFoodTrigger(Player player){
        return playersLevelFood.get(player.getUniqueId()) >= ConfigManager.getPoopConfig().getTrigger();
    }
    public static boolean isPlayerFoodLimit(Player player){
        return playersLevelFood.get(player.getUniqueId()) >= ConfigManager.getPoopConfig().getLimit();
    }
}

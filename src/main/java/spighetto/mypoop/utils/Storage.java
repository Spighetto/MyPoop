package spighetto.mypoop.utils;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import spighetto.mypoop.MyPoop;
import spighettoapi.common.interfaces.IPoop;

import java.util.*;

public class Storage {
    private static Plugin pluginInstance;
    public static Map<UUID, Integer> playersLevelFood;
    public static ArrayList<UUID> listPoops;

    public static void initStorage(Plugin plugin) {
        pluginInstance = plugin;
        playersLevelFood = new HashMap<>();
        listPoops = new ArrayList<>();
    }

    public static void newPoop(IPoop poop) {
        Storage.listPoops.add(poop.getPoopItem().getUniqueId());
        Chunk poopChunk = poop.getPoopItem().getLocation().getChunk();

        Bukkit.getScheduler().scheduleSyncDelayedTask(pluginInstance, () -> {
            Storage.listPoops.remove(poop.getPoopItem().getUniqueId());

            boolean needToLoad = poopChunk.isLoaded();
            if(!needToLoad)
                poopChunk.load();

            poop.delete();

            if(!needToLoad)
                poopChunk.unload();

        }, MyPoop.getPoopConfig().getDelay());
    }

    public static void deletePoops(List<World> worlds) {
        if(Storage.listPoops.size() > 0) {
            ArrayList<Entity> en = new ArrayList<>();

            for(World world : worlds) {
                for(Entity entityInWorld : world.getEntities()) {
                    for(UUID ii : Storage.listPoops) {
                        if(ii.equals(entityInWorld.getUniqueId())) {
                            en.add(entityInWorld);
                        }
                    }
                }
            }

            Storage.listPoops.clear();

            en.forEach(Entity::remove);		// Method reference technique
        }
    }

    public static boolean isPlayerFoodTrigger(Player player){
        return Storage.playersLevelFood.get(player.getUniqueId()) >= MyPoop.getPoopConfig().getTrigger();
    }
    public static boolean isPlayerFoodLimit(Player player){
        return Storage.playersLevelFood.get(player.getUniqueId()) >= MyPoop.getPoopConfig().getLimit();
    }
}

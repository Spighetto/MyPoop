package spighetto.mypoop.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import spighetto.mypoop.config.ConfigManager;
import spighetto.mypoop.utils.DataStorage;
import spighetto.mypoop.utils.VersionManager;
import spighettoapi.common.interfaces.IPoop;

import static spighetto.mypoop.utils.Utils.*;

public class PlayerEvents implements Listener {
    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if (!DataStorage.playersLevelFood.containsKey(player.getUniqueId()) || !DataStorage.isPlayerFoodTrigger(player) || !player.isSneaking()) return;

        int serverVersion = VersionManager.getServerVersion();
        IPoop poop;

        if(serverVersion >= 8 && serverVersion <= 11) {
            unimplemented("Adapter v1_8");
            //poop = new Poop_v1_8(player);
        } else if (serverVersion >= 12 && serverVersion <= 18) {
            unimplemented("Adapter v1_13");
            //poop = new Poop_v1_13(player);
        } else if (serverVersion == 19) {
            unimplemented("Adapter v1_19");
            //poop = new MyPoop_v1_19_4(player);
        }
        else {
            return;
        }

        if (ConfigManager.getPoopConfig().getNamedPoop()) {
            toFix("Version adapters");
            //poop.setName(player.getName(), plugin.getPoopConfig().getColorPoopName());
        } else {
            toFix("Version adapters");
            //poop.setName(player.getName());
        }

        toFix("Version adapters");
        //plugin.newPoop(poop);
        DataStorage.playersLevelFood.remove(player.getUniqueId());

    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!DataStorage.playersLevelFood.containsKey(player.getUniqueId()))
                DataStorage.playersLevelFood.put(player.getUniqueId(), 0);

            if (!DataStorage.isPlayerFoodLimit(player) && event.getFoodLevel() - player.getFoodLevel() > 0) {
                int playerFoodLevel = DataStorage.playersLevelFood.get(player.getUniqueId());
                DataStorage.playersLevelFood.put(player.getUniqueId(), playerFoodLevel + event.getFoodLevel() - player.getFoodLevel());

                if (DataStorage.isPlayerFoodTrigger(player)) {
                    printMessage(player, ConfigManager.getPoopConfig().getMessage());
                }
            }
        }
    }

    @EventHandler
    public void checkCanEat(PlayerItemConsumeEvent event) {
        if (!DataStorage.playersLevelFood.containsKey(event.getPlayer().getUniqueId())) return;

        if (DataStorage.isPlayerFoodLimit(event.getPlayer())) {
            event.setCancelled(true);
            printMessage(event.getPlayer(), ConfigManager.getPoopConfig().getMessageAtLimit());
        }
    }

    @EventHandler
    public void checkCanEatCake(PlayerInteractEvent event) {
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getClickedBlock() == null) return;

        Material blockType = event.getClickedBlock().getType();

        if(!blockType.equals(Material.CAKE)) return;

        if (DataStorage.isPlayerFoodLimit(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}

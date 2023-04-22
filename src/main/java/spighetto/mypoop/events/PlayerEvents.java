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
import spighetto.mypoop.MyPoop;
import spighetto.mypoop.utils.Storage;
import spighettoapi.common.interfaces.IPoop;

import static spighetto.mypoop.utils.Utils.*;

public class PlayerEvents implements Listener {
    private final int serverVersion;

    public PlayerEvents(int serverVersion){
        this.serverVersion = serverVersion;
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if (Storage.playersLevelFood.containsKey(player.getUniqueId())) {
            if (Storage.isPlayerFoodTrigger(player)) {
                if (player.isSneaking()) {
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

                    if (MyPoop.getPoopConfig().getNamedPoop()) {
                        toFix("Version adapters");
                        //poop.setName(player.getName(), plugin.getPoopConfig().getColorPoopName());
                    } else {
                        toFix("Version adapters");
                        //poop.setName(player.getName());
                    }

                    toFix("Version adapters");
                    //plugin.newPoop(poop);
                    Storage.playersLevelFood.remove(player.getUniqueId());
                }
            }
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!Storage.playersLevelFood.containsKey(player.getUniqueId()))
                Storage.playersLevelFood.put(player.getUniqueId(), 0);

            if (!Storage.isPlayerFoodLimit(player)) {
                if (event.getFoodLevel() - player.getFoodLevel() > 0) {

                    Storage.playersLevelFood.put(player.getUniqueId(), Storage.playersLevelFood.get(player.getUniqueId()) + event.getFoodLevel() - player.getFoodLevel());

                    if (Storage.isPlayerFoodTrigger(player)) {
                        printMessage(player, MyPoop.getPoopConfig().getMessage(), serverVersion);
                    }
                }
            }
        }
    }

    @EventHandler
    public void checkCanEat(PlayerItemConsumeEvent event) {
        if (Storage.playersLevelFood.containsKey(event.getPlayer().getUniqueId())) {
            if (Storage.isPlayerFoodLimit(event.getPlayer())) {
                event.setCancelled(true);
                printMessage(event.getPlayer(), MyPoop.getPoopConfig().getMessageAtLimit(), serverVersion);
            }
        }
    }

    @EventHandler
    public void checkCanEatCake(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            if (event.getClickedBlock().getType().equals(Material.CAKE))
                if (Storage.isPlayerFoodLimit(event.getPlayer()))
                    event.setCancelled(true);

    }
}

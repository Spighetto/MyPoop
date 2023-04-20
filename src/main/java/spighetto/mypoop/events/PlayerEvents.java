package spighetto.mypoop.events;

import org.bukkit.ChatColor;
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
import spighettoapi.common.interfaces.IMessages;
import spighettoapi.common.interfaces.IPoop;

import static spighetto.mypoop.utils.Utils.toFix;
import static spighetto.mypoop.utils.Utils.unimplemented;

public class PlayerEvents implements Listener {
    private final MyPoop plugin;

    public PlayerEvents(MyPoop plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if (plugin.playersLevelFood.containsKey(player.getUniqueId())) {
            if (isPlayerFoodTrigger(player)) {
                if (player.isSneaking()) {
                    IPoop poop;

                    if(plugin.serverVersion >= 8 && plugin.serverVersion <= 11) {
                        unimplemented("Adapter v1_8");
                        //poop = new Poop_v1_8(player);
                    } else if (plugin.serverVersion >= 12 && plugin.serverVersion <= 18) {
                        unimplemented("Adapter v1_13");
                        //poop = new Poop_v1_13(player);
                    } else if (plugin.serverVersion == 19) {
                        unimplemented("Adapter v1_19");
                        //poop = new MyPoop_v1_19_4(player);
                    }
                    else {
                        return;
                    }

                    if (plugin.getPoopConfig().getNamedPoop()) {
                        toFix("Version adapters");
                        //poop.setName(player.getName(), plugin.getPoopConfig().getColorPoopName());
                    } else {
                        toFix("Version adapters");
                        //poop.setName(player.getName());
                    }

                    toFix("Version adapters");
                    //plugin.newPoop(poop);
                    plugin.playersLevelFood.remove(player.getUniqueId());
                }
            }
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!plugin.playersLevelFood.containsKey(player.getUniqueId()))
                plugin.playersLevelFood.put(player.getUniqueId(), 0);

            if (!isPlayerFoodLimit(player)) {
                if (event.getFoodLevel() - player.getFoodLevel() > 0) {

                    plugin.playersLevelFood.put(player.getUniqueId(), plugin.playersLevelFood.get(player.getUniqueId()) + event.getFoodLevel() - player.getFoodLevel());

                    if (isPlayerFoodTrigger(player)) {
                        printMessage(player, plugin.getPoopConfig().getMessage());
                    }
                }
            }
        }
    }

    @EventHandler
    public void checkCanEat(PlayerItemConsumeEvent event) {
        if (plugin.playersLevelFood.containsKey(event.getPlayer().getUniqueId())) {
            if (isPlayerFoodLimit(event.getPlayer())) {
                event.setCancelled(true);
                printMessage(event.getPlayer(), plugin.getPoopConfig().getMessageAtLimit());
            }
        }
    }

    @EventHandler
    public void checkCanEatCake(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            if (event.getClickedBlock().getType().equals(Material.CAKE))
                if (isPlayerFoodLimit(event.getPlayer()))
                    event.setCancelled(true);

    }

    public void printMessage(Player player, String msg) {
        IMessages message;

        if(plugin.serverVersion >= 8 && plugin.serverVersion <= 11) {
            unimplemented("Messages adapter v1_8");
            //message = new Messages_v1_8(player, msg);
        } else if (plugin.serverVersion >= 11 && plugin.serverVersion <= 19) {
            unimplemented("Messages adapter v1_11");
            //message = new Messages_v1_11(player, msg);
        } else {
            return;
        }

        toFix("Messages adapters");
        switch (plugin.getPoopConfig().getWherePrint()) {
            case 2:
                //message.sendTitle();
                break;
            case 3:
                //message.sendSubtitle();
                break;
            case 4:
                //message.printActionBar();
                break;
            default:
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                break;
        }
    }

    private boolean isPlayerFoodTrigger(Player player){
        return plugin.playersLevelFood.get(player.getUniqueId()) >= plugin.getPoopConfig().getTrigger();
    }
    private boolean isPlayerFoodLimit(Player player){
        return plugin.playersLevelFood.get(player.getUniqueId()) >= plugin.getPoopConfig().getLimit();
    }
}

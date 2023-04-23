package spighetto.mypoop.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import spighetto.mypoop.config.ConfigManager;
import spighettoapi.common.interfaces.IMessages;

public class Utils {
    public static void unimplemented(String context) {
        throw new Error("[ERROR] " + context + " is not implemented yet");
    }

    public static void toFix(String context) {
        throw new Error("[ERROR] " + context + " need to be fixed before usage");
    }

    public static void log(String text) {
        Bukkit.getConsoleSender().sendMessage("MyPoop: " + text);
    }

    public static void printMessage(Player player, String msg) {
        int serverVersion = VersionManager.getServerVersion();
        IMessages message;

        if(serverVersion >= 8 && serverVersion <= 11) {
            unimplemented("Messages adapter v1_8");
            //message = new Messages_v1_8(player, msg);
        } else if (serverVersion >= 11 && serverVersion <= 19) {
            unimplemented("Messages adapter v1_11");
            //message = new Messages_v1_11(player, msg);
        } else {
            return;
        }

        toFix("Messages adapters");
        switch (ConfigManager.getPoopConfig().getWherePrint()) {
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
}

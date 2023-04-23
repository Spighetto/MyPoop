package spighetto.mypoop.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import spighetto.mypoop.MyPoop;
import spighetto.mypoop.config.ConfigManager;
import spighetto.mypoop.utils.DataStorage;

public class ReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String name, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("mypoop") || args.length == 0) return false;

        if (!args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(ChatColor.RED + "MyPoop: Unknown command");
            return false;
        }

        DataStorage.deleteEntries();

        ConfigManager.reloadConfig();
        MyPoop.getPluginInstance().restartPlugin();

        sender.sendMessage(ChatColor.GREEN + "MyPoop: Reload complete");

        return true;
    }
}
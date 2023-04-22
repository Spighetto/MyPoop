package spighetto.mypoop.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import spighetto.mypoop.MyPoop;
import spighetto.mypoop.utils.Storage;

public class ReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String nome, String[] args) {
        if (cmd.getName().equalsIgnoreCase("mypoop")) {
            if (args.length == 0) return false;

            if (args[0].equalsIgnoreCase("reload")) {

                Storage.deletePoops(MyPoop.getPluginInstance().getServer().getWorlds());

                try {
                    onReload();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                sender.sendMessage(ChatColor.GREEN + "MyPoop: Reload complete");
            } else {
                sender.sendMessage(ChatColor.RED + "MyPoop: Unknown command");
            }
        }

        return false;
    }

    public void onReload() {
        Plugin plugin = MyPoop.getPluginInstance();

        plugin.reloadConfig();
        plugin.getServer().getPluginManager().disablePlugin(plugin);
        plugin.getServer().getPluginManager().enablePlugin(plugin);
    }
}
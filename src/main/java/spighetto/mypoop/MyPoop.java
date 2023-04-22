package spighetto.mypoop;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import spighetto.mypoop.commands.ReloadCommand;
import spighetto.mypoop.config.ConfigManager;
import spighetto.mypoop.events.PlayerEvents;
import spighetto.mypoop.stats.Metrics;
import spighetto.mypoop.utils.DataStorage;
import spighetto.mypoop.utils.UpdateChecker;
import spighetto.mypoop.utils.VersionManager;

import java.util.Objects;

public final class MyPoop extends JavaPlugin {
    private static MyPoop pluginInstance;

    @Override
    public void onEnable() {
        pluginInstance = this;

        VersionManager.init(Bukkit.getBukkitVersion());

        saveDefaultConfig();
        ConfigManager.init(this.getConfig());

        super.getServer().getPluginManager().registerEvents(new PlayerEvents(), this);

        Objects.requireNonNull(getCommand("mypoop")).setExecutor(new ReloadCommand());

        new Metrics(this, 8159);

        if (getConfig().getBoolean("updateChecker")) {
            new UpdateChecker(this, 77372, "MyPoop");
        }
    }

    @Override
    public void onDisable() {
        DataStorage.deleteEntries();
    }

    public static MyPoop getPluginInstance() {
        return pluginInstance;
    }

    public void restartPlugin() {
        getServer().getPluginManager().disablePlugin(this);
        getServer().getPluginManager().enablePlugin(this);
    }
}
package spighetto.mypoop;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import spighetto.mypoop.commands.ReloadCommand;
import spighetto.mypoop.events.PlayerEvents;
import spighetto.mypoop.stats.Metrics;
import spighetto.mypoop.utils.Config;
import spighetto.mypoop.utils.Storage;
import spighetto.mypoop.utils.UpdateChecker;

import java.util.Objects;

public final class MyPoop extends JavaPlugin {
    private static Plugin pluginInstance;
    private static Config config;
    private static int serverVersion;

    @Override
    public void onEnable() {
        pluginInstance = this;
        serverVersion = getServerVersion();

        Storage.initStorage(this);

        if(!isCompatibleVersion()){
            Bukkit.getConsoleSender().sendMessage("MyPoop: Error: incompatible server version");
        }

        saveDefaultConfig();
        readConfig();

        super.getServer().getPluginManager().registerEvents(new PlayerEvents(serverVersion), this);
        new Metrics(this, 8159);

        Objects.requireNonNull(getCommand("mypoop")).setExecutor(new ReloadCommand());

        if (this.getConfig().getBoolean("updateChecker")) {
            new UpdateChecker(this, 77372, "MyPoop");
        }
    }

    @Override
    public void onDisable() {
        Storage.deletePoops(this.getServer().getWorlds());
    }

    public static Plugin getPluginInstance() {
        return pluginInstance;
    }

    public static int getServerVersion() {
        String version = Bukkit.getBukkitVersion().split("-")[0].split("\\.")[1];

        try {
            return Integer.parseInt(version);
        } catch (Exception e){
            log("Error: the server version could not be verified or incorrect server version");
        }

        return -1;
    }

    private boolean isCompatibleVersion() {
        return serverVersion >= 8 && serverVersion <= 19;
    }

    private void readConfig() {
        try {
            FileConfiguration savedConfig = this.getConfig();

            config = new Config(
                    savedConfig.getInt("poopSettings.trigger"),
                    savedConfig.getInt("poopSettings.limit"),
                    savedConfig.getLong("poopSettings.delay") * 20,
                    savedConfig.getBoolean("poopSettings.namedPoop"),
                    savedConfig.getString("poopSettings.colorPoopName"),
                    savedConfig.getString("poopSettings.poopDisplayName"),
                    savedConfig.getString("alerts.message"),
                    savedConfig.getString("alerts.messageAtLimit"),
                    savedConfig.getInt("alerts.wherePrint"),
                    savedConfig.getBoolean("growingSettings.allCropsNearby"),
                    savedConfig.getDouble("growingSettings.radius"),
                    savedConfig.getBoolean("growingSettings.randomGrow")
            );

            if(config.getLimit() < config.getTrigger()){
                config.setLimit(config.getTrigger());
            }

        } catch (Exception e) {
            log("Error: some value inside the config.yml has not been configured correctly. " +
                    "\n\t\tTip: save a copy of your current config.yml, delete from MyPoop's folder and reload the plugin to regenerate it as default. " +
                    "Finally pay attention to recopy your saved values to the new config.yml");
        }
    }

    public static Config getPoopConfig(){
        return config;
    }

    public static void log(String text) {
        Bukkit.getConsoleSender().sendMessage("MyPoop: " + text);
    }
}
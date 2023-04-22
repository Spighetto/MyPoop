package spighetto.mypoop.config;

import org.bukkit.configuration.file.FileConfiguration;
import spighetto.mypoop.MyPoop;

import static spighetto.mypoop.utils.Utils.log;

public class ConfigManager {
    private static Config config;

    public static void init(FileConfiguration savedConfig) {
        try {
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

    public static void reloadConfig() {
        MyPoop.getPluginInstance().reloadConfig();
    }
}

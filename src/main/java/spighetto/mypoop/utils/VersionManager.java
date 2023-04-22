package spighetto.mypoop.utils;

import org.bukkit.Bukkit;

import static spighetto.mypoop.utils.Utils.log;

public class VersionManager {
    private static int minorVersion;
    private static int patchVersion;

    public static void init(String bukkitVersion) {
        String minecraftVersion = bukkitVersion.split("-")[0];

        minorVersion = parseMinorVersion(minecraftVersion);
        patchVersion = parsePatchVersion(minecraftVersion);

        if(!isInCompatibleVersions()){
            Bukkit.getConsoleSender().sendMessage("MyPoop: Error: incompatible server version"); // or throw an Error
        }
    }

    private static int parseMinorVersion(String minecraftVersion) {
        try {
            String version = minecraftVersion.split("\\.")[1];

            return Integer.parseInt(version);
        } catch (Exception e){
            log("Error: the server version could not be verified or incorrect server version");
        }

        return -1;
    }

    private static int parsePatchVersion(String minecraftVersion) {
        try {
            String version = minecraftVersion.split("\\.")[2];

            return Integer.parseInt(version);
        } catch (Exception e){
            return 0;
        }
    }

    private static boolean isInCompatibleVersions() {
        return minorVersion >= 8 && minorVersion <= 19;
    }

    public static int getPatchVersion() {
        return patchVersion;
    }

    public static int getMinorVersion() {
        return minorVersion;
    }

    public static int getServerVersion() {
        return getMinorVersion();
    }
}

package spighetto.mypoop.utils;

public class Utils {
    public static void unimplemented(String context) {
        throw new Error("[ERROR] " + context + " is not implemented yet");
    }

    public static void toFix(String context) {
        throw new Error("[ERROR] " + context + " need to be fixed before usage");
    }
}

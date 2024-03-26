package me.dimitri.libertyweb.utils;

public class EnvironmentDataHolder {

    private static boolean isOfflineMode = false;

    public static boolean isOfflineMode() {
        return isOfflineMode;
    }

    public static void setOfflineMode(boolean offlineMode) {
        isOfflineMode = offlineMode;
    }

}

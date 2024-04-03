package me.dimitri.libertyweb.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PlatformChecker {
    static boolean isAMinecraftServer = false;
    static Path rootPath;

    // net.minecraft.server.MinecraftServer is traditionally present on vanilla forks/platforms
    public static void checkIfIsAMinecraftServer() {
        try {
            Class.forName("net.minecraft.server.MinecraftServer", false, PlatformChecker.class.getClassLoader());
            setAMinecraftServer(true);
        } catch (ClassNotFoundException e) {
            setAMinecraftServer(false);
        }
    }

    public static boolean isAMinecraftServer() {
        return isAMinecraftServer;
    }

    public static void setAMinecraftServer(boolean AMinecraftServer) {
        isAMinecraftServer = AMinecraftServer;
    }

    // This method checks whether we are in a modded or a Bukkit based environment.
    public static Path checkForResourcesFolder() {
        Path currentPath = Paths.get("").toAbsolutePath();
        Path modsPath = currentPath.resolve("mods");
        Path pluginsPath = currentPath.resolve("plugins");

        if (Files.exists(modsPath)) {
            return modsPath.resolve("LibertyWeb");
        } else if (Files.exists(pluginsPath)) {
            return pluginsPath.resolve("LibertyWeb");
        }
        return null;
    }

    public static Path getRootPath() {
        return rootPath;
    }

    public static void setRootPath(Path rootPath) {
        PlatformChecker.rootPath = rootPath;
    }


}

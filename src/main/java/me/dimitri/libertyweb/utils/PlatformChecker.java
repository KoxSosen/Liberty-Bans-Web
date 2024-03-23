package me.dimitri.libertyweb.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class PlatformChecker {
    boolean isAMinecraftServer = false;

    // net.minecraft.server.MinecraftServer is traditionally present on vanilla forks/platforms
    public void checkIfIsAMinecraftServer() {
        try {
            Class.forName("net.minecraft.server.MinecraftServer", false, getClass().getClassLoader());
            isAMinecraftServer = true;
        } catch (ClassNotFoundException e) {
            isAMinecraftServer = false;
        }
    }

    public boolean isAMinecraftServer() {
        return isAMinecraftServer;
    }

    public void setAMinecraftServer(boolean AMinecraftServer) {
        isAMinecraftServer = AMinecraftServer;
    }

    // This method checks whether we are in a modded or a Bukkit based environment.
    public Path checkForResourcesFolder() {
        Path modsPath = Path.of(".", "/mods");
        Path pluginsPath = Path.of(".", "/plugins");

        if (Files.exists(modsPath)) {
            return modsPath.resolve("/LibertyWeb");
        } else if (Files.exists(pluginsPath)) {
            return pluginsPath.resolve("/LibertyWeb");
        }
        return null;
    }


}

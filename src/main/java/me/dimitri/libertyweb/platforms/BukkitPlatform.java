package me.dimitri.libertyweb.platforms;

import me.dimitri.libertyweb.Application;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitPlatform extends JavaPlugin {
    @Override
    public void onEnable() {
        Application.main(new String[]{});
    }
    @Override
    public void onDisable() {
    }
}

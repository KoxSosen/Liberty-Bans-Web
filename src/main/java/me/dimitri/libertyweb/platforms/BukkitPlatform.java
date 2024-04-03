package me.dimitri.libertyweb.platforms;

import jakarta.inject.Inject;
import me.dimitri.libertyweb.Application;
import me.dimitri.libertyweb.api.LibertyWeb;
import me.dimitri.libertyweb.utils.EventListener;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitPlatform extends JavaPlugin {

    @Override
    public void onEnable() {
        Application.main(new String[]{});
    }
    @Override
    public void onDisable() {
        EventListener.getLibertyWeb().getBase().shutdown();
    }
}

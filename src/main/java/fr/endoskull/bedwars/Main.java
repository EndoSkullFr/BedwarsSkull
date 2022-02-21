package fr.endoskull.bedwars;

import fr.endoskull.bedwars.listeners.StartListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new StartListener(), this);
        super.onEnable();
    }

    public static Main getInstance() {
        return instance;
    }
}

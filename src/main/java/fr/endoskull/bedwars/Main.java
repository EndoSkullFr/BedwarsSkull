package fr.endoskull.bedwars;

import fr.endoskull.bedwars.listeners.StartListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new StartListener(), this);
        super.onEnable();
        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "gamerule doDaylightCycle false");
        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "time set 0");
        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "gamerule doWeatherCycle false");
    }

    public static Main getInstance() {
        return instance;
    }
}

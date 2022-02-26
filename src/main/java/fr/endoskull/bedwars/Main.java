package fr.endoskull.bedwars;

import fr.endoskull.bedwars.listeners.JoinListener;
import fr.endoskull.bedwars.utils.MapManager;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {
    private static Main instance;
    private List<Arena> games = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;
        PluginManager pm = Bukkit.getPluginManager();
        //pm.registerEvents(new StartListener(), this);
        pm.registerEvents(new JoinListener(this), this);
        super.onEnable();
        /*Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "time set 0");
        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "gamerule doDaylightCycle false");
        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "gamerule doWeatherCycle false");*/
        MapManager.createRessource("map1");
        MapManager.loadArena("map1");
    }

    public static Main getInstance() {
        return instance;
    }

    public List<Arena> getGames() {
        return games;
    }
}

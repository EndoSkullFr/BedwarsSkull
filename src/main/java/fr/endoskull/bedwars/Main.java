package fr.endoskull.bedwars;

import fr.endoskull.bedwars.board.FastBoard;
import fr.endoskull.bedwars.listeners.CustomGuiListener;
import fr.endoskull.bedwars.listeners.WaitingListener;
import fr.endoskull.bedwars.listeners.playing.*;
import fr.endoskull.bedwars.listeners.JoinListener;
import fr.endoskull.bedwars.tasks.ArmorStandTask;
import fr.endoskull.bedwars.tasks.BoardRunnable;
import fr.endoskull.bedwars.tasks.GameRunnable;
import fr.endoskull.bedwars.utils.MapManager;
import fr.endoskull.bedwars.utils.NmsUtils;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {
    private static Main instance;
    private List<Arena> games = new ArrayList<>();
    private List<FastBoard> boards = new ArrayList<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        PluginManager pm = Bukkit.getPluginManager();
        //pm.registerEvents(new StartListener(), this);
        pm.registerEvents(new JoinListener(this), this);
        pm.registerEvents(new GameListener(this), this);
        pm.registerEvents(new ExplosiveListener(this), this);
        pm.registerEvents(new DamageListener(), this);
        pm.registerEvents(new EggListener(), this);
        pm.registerEvents(new WaitingListener(), this);
        pm.registerEvents(new DeathListener(this), this);
        pm.registerEvents(new ChestPlaceListener(), this);
        pm.registerEvents(new CustomGuiListener(), this);
        super.onEnable();
        /*Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "time set 0");
        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "gamerule doDaylightCycle false");
        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "gamerule doWeatherCycle false");*/
        MapManager.createRessource("map1");
        MapManager.loadArena("map1");
        saveResource("languages/French.yml", false);

        for (Arena game : games) {
            game.getWorld().setDifficulty(Difficulty.PEACEFUL);
            game.getWorld().setGameRuleValue("doDaylightCycle", "false");
            game.getWorld().setGameRuleValue("doWeatherCycle", "false");
            game.getWorld().setGameRuleValue("doMobSpawning", "false");
            game.getWorld().setGameRuleValue("doMobLoot", "false");
            game.getWorld().setGameRuleValue("mobGriefing", "false");
        }

        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskTimer(this, new BoardRunnable(this), 20, 5);
        scheduler.runTaskTimer(this, new GameRunnable(this), 20, 20);
        scheduler.runTaskTimer(this, new ArmorStandTask(this), 20, 1);
        NmsUtils.registerEntities();
    }

    public static Main getInstance() {
        return instance;
    }

    public List<Arena> getGames() {
        return games;
    }

    public List<FastBoard> getBoards() {
        return boards;
    }
}

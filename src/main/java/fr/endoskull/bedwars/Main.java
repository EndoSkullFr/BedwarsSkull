package fr.endoskull.bedwars;

import fr.endoskull.api.commons.EndoSkullAPI;
import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import fr.endoskull.bedwars.board.FastBoard;
import fr.endoskull.bedwars.commands.BedwarsCommand;
import fr.endoskull.bedwars.listeners.*;
import fr.endoskull.bedwars.listeners.playing.*;
import fr.endoskull.bedwars.tasks.ArmorStandTask;
import fr.endoskull.bedwars.tasks.BoardRunnable;
import fr.endoskull.bedwars.tasks.GameRunnable;
import fr.endoskull.bedwars.tasks.ParticlesTask;
import fr.endoskull.bedwars.utils.*;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsPlayer;
import me.neznamy.tab.api.TabAPI;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {
    private static Main instance;
    private List<Arena> games = new ArrayList<>();
    private List<FastBoard> boards = new ArrayList<>();
    private Scoreboard scoreboard;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
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
        pm.registerEvents(new SpectatorListener(), this);
        pm.registerEvents(new WeatherListener(), this);
        pm.registerEvents(new ClickListener(), this);
        pm.registerEvents(new ChatListener(), this);
        pm.registerEvents(new GoulagListener(this), this);

        getCommand("bedwars").setExecutor(new BedwarsCommand());
        super.onEnable();
        /*Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "time set 0");
        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "gamerule doDaylightCycle false");
        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "gamerule doWeatherCycle false");*/
        //MapManager.createRessource("bw_fast_food");
        ServerInfo.clearInfo();
        File mapFolder = new File(getDataFolder(), "maps");
        if (!getDataFolder().exists()) getDataFolder().mkdir();
        if (mapFolder.exists()) mapFolder.mkdir();
        for (File file : mapFolder.listFiles()) {
            if (file.getName().endsWith(".yml")) {
                MapManager.loadArena(file.getName().substring(0, file.getName().length() - 4));
            }
        }
        saveResource("languages/french.yml", false);

        /*for (Arena game : games) {
            game.getWorld().setDifficulty(Difficulty.PEACEFUL);
            game.getWorld().setGameRuleValue("doDaylightCycle", "false");
            game.getWorld().setGameRuleValue("doWeatherCycle", "false");
            game.getWorld().setGameRuleValue("doMobSpawning", "false");
            game.getWorld().setGameRuleValue("doMobLoot", "false");
            game.getWorld().setGameRuleValue("mobGriefing", "false");
        }*/

        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskTimer(this, new BoardRunnable(this), 20, 5);
        scheduler.runTaskTimer(this, new GameRunnable(this), 20, 20);
        scheduler.runTaskTimer(this, new ArmorStandTask(this), 20, 1);
        scheduler.runTaskTimer(this, new ParticlesTask(this), 20, 1);
        NmsUtils.registerEntities();

        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Team team = scoreboard.registerNewTeam("default");
        team.setPrefix("§7");
        Objective health = scoreboard.registerNewObjective("showHealth", "health");
        health.setDisplayName("§c❤");
        health.setDisplaySlot(DisplaySlot.BELOW_NAME);
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%bedwars_prefix%", 1000, tabPlayer -> {
            if (tabPlayer.getPlayer() != null) {
                Player player = (Player) tabPlayer.getPlayer();
                Arena game = GameUtils.getGame(player);
                if (game != null) {
                    if (game.getGameState() == GameState.waiting || game.getGameState() == GameState.starting) {
                        User user = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId());
                        String prefix = user.getCachedData().getMetaData().getPrefix();
                        if (prefix == null) prefix = "§7";
                        return ChatColor.translateAlternateColorCodes('&', EndoSkullAPI.getPrefix(player.getUniqueId()));
                    } else {
                        BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
                        if (bwPlayer != null) {
                            fr.endoskull.bedwars.utils.bedwars.Team t = bwPlayer.getTeam();
                            if (t != null) {
                                return t.getColor().chat() + "§l" + t.getName().substring(0, 1).toUpperCase() + " " + t.getColor().chat();
                            }
                        }
                    }
                }
            }
            return "§7";
        });

        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%bedwars_kill%", 1000, tabPlayer -> {
            if (tabPlayer.getPlayer() != null) {
                Player player = (Player) tabPlayer.getPlayer();
                Arena game = GameUtils.getGame(player);
                if (game != null) {
                    BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
                    return String.valueOf(bwPlayer.getKill());
                }
            }
            return "0";
        });
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%bedwars_finalkill%", 1000, tabPlayer -> {
            if (tabPlayer.getPlayer() != null) {
                Player player = (Player) tabPlayer.getPlayer();
                Arena game = GameUtils.getGame(player);
                if (game != null) {
                    BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
                    return String.valueOf(bwPlayer.getFinalKill());
                }
            }
            return "0";
        });
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%bedwars_bedbroken%", 1000, tabPlayer -> {
            if (tabPlayer.getPlayer() != null) {
                Player player = (Player) tabPlayer.getPlayer();
                Arena game = GameUtils.getGame(player);
                if (game != null) {
                    BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
                    return String.valueOf(bwPlayer.getBedBroken());
                }
            }
            return "0";
        });
    }

    @Override
    public void onDisable() {
        for (Arena game : games) {
            ServerInfo.removeInfo(game);
        }
        super.onDisable();
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

    public Scoreboard getScoreboard() {
        return scoreboard;
    }
}

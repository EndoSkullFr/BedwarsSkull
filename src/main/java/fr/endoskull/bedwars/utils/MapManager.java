package fr.endoskull.bedwars.utils;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.*;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsLocation;
import fr.endoskull.bedwars.utils.bedwars.Team;
import fr.endoskull.bedwars.utils.bedwars.TeamColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class MapManager {

    private static HashMap<Integer, Integer> colorMap = new HashMap<>();

    public static void createRessource(String name) {
        Main.getInstance().saveResource(name + ".yml", false);
    }

    public static void loadArena(String name) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(Main.getInstance().getDataFolder(), name + ".yml"));
        Arena arena = new Arena();
        arena.setName(config.getString("display-name"));
        //Bukkit.createWorld(new WorldCreator(name));
        arena.setOldWorld(name);
        cloneArenaWorld(arena);
        arena.setBorderSize(config.getInt("worldBorder"));
        arena.setLobby(new BedwarsLocation(config.getString("waiting.Loc").split(",")));
        //arena.getWorld().setSpawnLocation((int) Math.round(arena.getLobby().getX()), (int) Math.round(arena.getLobby().getY()), (int) Math.round(arena.getLobby().getZ()));
        arena.setCorner1(new BedwarsLocation(config.getString("waiting.Pos1").split(",")));
        arena.setCorner2(new BedwarsLocation(config.getString("waiting.Pos2").split(",")));
        arena.setGoulagSpawn1(new BedwarsLocation(config.getString("goulag.Spawn1").split(",")));
        arena.setGoulagSpawn2(new BedwarsLocation(config.getString("goulag.Spawn2").split(",")));
        arena.setGoulagLoc1(new BedwarsLocation(config.getString("goulag.Loc1").split(",")));
        arena.setGoulagLoc2(new BedwarsLocation(config.getString("goulag.Loc2").split(",")));
        arena.setSpawnProtection(config.getInt("spawn-protection"));
        arena.setBaseRadius(config.getInt("baseRadius"));
        arena.setHeightLimit(config.getInt("max-build-y"));
        arena.setMaxTeamSize(config.getInt("maxInTeam"));
        arena.setMin(config.getInt("minPlayers"));
        arena.setNeedColoration(config.getBoolean("needColoration"));
        for (String s : config.getStringList("game-rules")) {
            arena.getWorld().setGameRuleValue(s.split(":")[0], s.split(":")[1]);
        }
        for (String s : config.getConfigurationSection("Team").getKeys(false)) {
            Team team = new Team(s, TeamColor.valueOf(config.getString("Team." + s + ".Color")));
            arena.getTeams().add(team);
            arena.getSpawns().put(team, new BedwarsLocation(config.getString("Team." + s + ".Spawn").split(",")));
            arena.getBeds().put(team, new BedwarsLocation(config.getString("Team." + s + ".Bed").split(",")));
            arena.getGenerators().put(team, new BedwarsLocation(config.getStringList("Team." + s + ".Iron").get(0).split(",")));
            arena.getShops().put(team, new BedwarsLocation(config.getString("Team." + s + ".Shop").split(",")));
            arena.getUpgrades().put(team, new BedwarsLocation(config.getString("Team." + s + ".Upgrade").split(",")));
        }
        for (String s : config.getStringList("generator.Emerald")) {
            arena.getEmeraldGenerators().add(new BedwarsLocation(s.split(",")));
        }
        for (String s : config.getStringList("generator.Diamond")) {
            arena.getDiamondGenerators().add(new BedwarsLocation(s.split(",")));
        }
        arena.setGoulagTimer(ConfigUtils.getGoulagTimer());

        Main.getInstance().getGames().add(arena);
    }

    public static Arena findAvaibleGame(int size) {
        Arena arena = null;
        for (Arena game : Main.getInstance().getGames()) {
            if (game.getGameState() != GameState.waiting && game.getGameState() != GameState.starting) continue;
            if (game.getPlayers().size() + size > game.getMaxTeamSize() * game.getTeams().size()) continue;
            if (arena == null) {
                arena = game;
            } else if (arena.getPlayers().size() < game.getPlayers().size()) {
                arena = game;
            }
        }
        return arena;
    }

    public static void cloneArenaWorld(Arena arena) {
        final SlimePlugin plugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        SlimeLoader fileLoader = plugin.getLoader("file");
        SlimePropertyMap prop = new SlimePropertyMap();
        prop.setString(SlimeProperties.DIFFICULTY, "peaceful");
        prop.setBoolean(SlimeProperties.ALLOW_ANIMALS, false);
        prop.setBoolean(SlimeProperties.ALLOW_MONSTERS, false);
        prop.setBoolean(SlimeProperties.PVP, true);
        SlimeWorld slimeWorld = null;
        try {
            slimeWorld = plugin.loadWorld(fileLoader, arena.getOldWorld(), true, prop).clone(arena.getOldWorld() + "_copy", fileLoader);
            plugin.generateWorld(slimeWorld);
            arena.setWorld(Bukkit.getWorld(arena.getOldWorld() + "_copy"));
            arena.setSlimeWorld(slimeWorld);
        } catch (UnknownWorldException | CorruptedWorldException | IOException | NewerFormatException | WorldInUseException | WorldAlreadyExistsException e) {
            e.printStackTrace();
        }
    }
}

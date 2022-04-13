package fr.endoskull.bedwars.utils;

import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsLocation;
import fr.endoskull.bedwars.utils.bedwars.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MapManager {

    public static void createRessource(String name) {
        Main.getInstance().saveResource(name + ".yml", false);
    }

    public static void loadArena(String name) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(Main.getInstance().getDataFolder(), name + ".yml"));
        Arena arena = new Arena();
        arena.setName(config.getString("name"));
        Bukkit.createWorld(new WorldCreator(config.getString("world")));
        arena.setWorld(Bukkit.getWorld(config.getString("world")));
        arena.setBorderSize(config.getInt("boarderSize"));
        arena.setLobby(new BedwarsLocation(config.getString("lobby").split(":")));
        arena.setCorner1(new BedwarsLocation(config.getString("corner1").split(":")));
        arena.setCorner2(new BedwarsLocation(config.getString("corner2").split(":")));
        arena.setSpawnProtection(config.getInt("spawnProtection"));
        arena.setBaseRadius(config.getInt("baseRadius"));
        arena.setHeightLimit(config.getInt("heightLimit"));
        arena.setMaxTeamSize(config.getInt("maxTeamSize"));
        arena.setMin(config.getInt("min"));
        for (String s : config.getConfigurationSection("teams").getKeys(false)) {
            Team team = new Team(s, config.getString("teams." + s + ".name"), Color.fromRGB(config.getInt("teams." + s + ".color")), ChatColor.getByChar(config.getString("teams." + s + ".chatColor")));
            arena.getTeams().add(team);
        }
        for (String s : config.getConfigurationSection("spawns").getKeys(false)) {
            arena.getSpawns().put(arena.getTeamByName(s), new BedwarsLocation(config.getString("spawns." + s).split(":")));
        }
        for (String s : config.getConfigurationSection("generators").getKeys(false)) {
            arena.getGenerators().put(arena.getTeamByName(s), new BedwarsLocation(config.getString("generators." + s).split(":")));
        }
        for (String s : config.getConfigurationSection("shops").getKeys(false)) {
            arena.getShops().put(arena.getTeamByName(s), new BedwarsLocation(config.getString("shops." + s).split(":")));
        }
        for (String s : config.getConfigurationSection("upgrades").getKeys(false)) {
            arena.getUpgrades().put(arena.getTeamByName(s), new BedwarsLocation(config.getString("upgrades." + s).split(":")));
        }
        for (String s : config.getStringList("emeraldGenerators")) {
            arena.getEmeraldGenerators().add(new BedwarsLocation(s.split(":")));
        }
        for (String s : config.getStringList("diamondGenerators")) {
            arena.getDiamondGenerators().add(new BedwarsLocation(s.split(":")));
        }
        for (String s : config.getConfigurationSection("beds").getKeys(false)) {
            arena.getBeds().put(arena.getTeamByName(s), new BedwarsLocation(config.getString("beds." + s).split(":")));
        }

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
}

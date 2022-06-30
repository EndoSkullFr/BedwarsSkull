package fr.endoskull.bedwars.utils;

import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsPlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameUtils {

    public static Arena getGame(Player player) {
        List<Arena> games = new ArrayList<>();
        for (Arena game : Main.getInstance().getGames()) {
            BedwarsPlayer bedwarsPlayer = game.getPlayers().stream().filter(bwPlayer -> bwPlayer.getUuid().equals(player.getUniqueId())).findFirst().orElse(null);
            if (bedwarsPlayer != null) games.add(game);
        }
        if (games.isEmpty()) return null;
        if (games.size() == 1) return games.get(0);
        for (Arena game : games) {
            if (game.getBwPlayerByUUID(player.getUniqueId()).isAlive()) return game;
        }
        return null;
    }

    public static Arena getGame(World world) {
        return Main.getInstance().getGames().stream().filter(arena -> arena.getWorld().equals(world)).findFirst().orElse(null);
    }

    public static void clearGame(Arena game) {
        game = null;
        System.gc();
    }
}

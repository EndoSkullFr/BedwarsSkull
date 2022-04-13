package fr.endoskull.bedwars.utils;

import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import org.bukkit.entity.Player;

public class GameUtils {

    public static Arena getGame(Player player) {
        for (Arena game : Main.getInstance().getGames()) {
            if (game.getPlayers().containsKey(player)) return game;
        }
        return null;
    }
}

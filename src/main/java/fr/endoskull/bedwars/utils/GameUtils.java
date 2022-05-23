package fr.endoskull.bedwars.utils;

import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsPlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class GameUtils {

    public static Arena getGame(Player player) {
        for (Arena game : Main.getInstance().getGames()) {
            BedwarsPlayer bedwarsPlayer = game.getPlayers().stream().filter(bwPlayer -> bwPlayer.getUuid().equals(player.getUniqueId())).findFirst().orElse(null);
            if (bedwarsPlayer != null) return game;
        }
        return null;
    }

    public static Arena getGame(World world) {
        return Main.getInstance().getGames().stream().filter(arena -> arena.getWorld().equals(world)).findFirst().orElse(null);
    }
}

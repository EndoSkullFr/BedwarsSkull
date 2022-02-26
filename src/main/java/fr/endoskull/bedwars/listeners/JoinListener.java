package fr.endoskull.bedwars.listeners;

import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.board.FastBoard;
import fr.endoskull.bedwars.utils.MapManager;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class JoinListener implements Listener {
    private Main main;

    public JoinListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Arena game = MapManager.findAvaibleGame(1);
        FastBoard board = new FastBoard(player);
        main.getBoards().add(board);
        if (game != null) {
            game.addPlayer(player);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        for (FastBoard board : new ArrayList<>(main.getBoards())) {
            if (board.getPlayer().equals(player)) {
                main.getBoards().remove(board);
                break;
            }
        }
    }
}

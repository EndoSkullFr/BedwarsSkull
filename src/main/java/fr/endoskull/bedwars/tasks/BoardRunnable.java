package fr.endoskull.bedwars.tasks;

import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.board.FastBoard;
import fr.endoskull.bedwars.utils.BoardConfig;
import fr.endoskull.bedwars.utils.GameEvent;
import fr.endoskull.bedwars.utils.GameState;
import fr.endoskull.bedwars.utils.MessagesUtils;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BoardRunnable extends BukkitRunnable {
    private Main main;

    public BoardRunnable(Main main) {
        this.main = main;
    }

    @Override
    public void run() {
        for (FastBoard board : main.getBoards()) {
            Player player = board.getPlayer();
            if (player == null) continue;
            for (Arena game : main.getGames()) {
                if (game.getAllPlayers().contains(player)) {
                    if (game.getGameState() == GameState.waiting) {
                        List<String> lines = new ArrayList<>();
                        for (String s : BoardConfig.getWaitingLines()) {
                            lines.add(s.replace("{date}", new SimpleDateFormat("dd/MM/yy").format(new Date()))
                                    .replace("{server}", Bukkit.getServerName())
                                    .replace("{map}", game.getName())
                                    .replace("{on}", String.valueOf(game.getPlayers().size()))
                                    .replace("{max}", String.valueOf(game.getTeams().size() * game.getMaxTeamSize())));
                        }
                        board.updateTitle(BoardConfig.getWaitingTitle());
                        board.updateLines(lines);
                    }
                    if (game.getGameState() == GameState.starting) {
                        List<String> lines = new ArrayList<>();
                        for (String s : BoardConfig.getStartingLines()) {
                            lines.add(s.replace("{date}", new SimpleDateFormat("dd/MM/yy").format(new Date()))
                                    .replace("{server}", Bukkit.getServerName())
                                    .replace("{map}", game.getName())
                                    .replace("{on}", String.valueOf(game.getPlayers().size()))
                                    .replace("{max}", String.valueOf(game.getTeams().size() * game.getMaxTeamSize()))
                                    .replace("{time}", String.valueOf(game.getStartTimer())));
                        }
                        board.updateTitle(BoardConfig.getStartingTitle());
                        board.updateLines(lines);
                    }
                    if (game.getGameState() == GameState.playing) {
                        List<String> lines = new ArrayList<>();
                        for (String s : BoardConfig.getPlayingLines(game, player)) {
                            lines.add(s.replace("{date}", new SimpleDateFormat("dd/MM/yy").format(new Date()))
                                    .replace("{server}", Bukkit.getServerName())
                                    .replace("{map}", game.getName())
                                    .replace("{on}", String.valueOf(game.getPlayers().size()))
                                    .replace("{max}", String.valueOf(game.getTeams().size() * game.getMaxTeamSize()))
                                    .replace("{time}", getText(game.getEventTimer()))
                                    .replace("{nextEvent}", MessagesUtils.getEventDisplayname(player, game.getGameEvent()))
                                    .replace("{goulag}", getGoulag(game, player)));
                        }
                        board.updateTitle(BoardConfig.getPlayingTitle());
                        int i = 0;
                        for (String line : new ArrayList<>(lines)) {
                            if (line.length() > 30) {
                                line = line.substring(0, 30);
                            }
                            lines.set(i, line);
                            i++;
                        }
                        board.updateLines(lines);
                    }
                }
            }
        }
    }

    private String getGoulag(Arena game, Player player) {
        if (game.isFinalGoulag()) {
            return MessagesUtils.GOULAG_NOW.getMessage(player);
        }
        if (!game.isGoulagOpen()) {
            return "§c✘";
        }
        return "§a" + getText(game.getGoulagTimer());
    }

    private String getText(int time) {
        if (time < 0) time = 0;
        int minute = 0;
        while (time >= 60) {
            minute++;
            time -= 60;
        }
        return minute + ":" + (time >= 10 ? time : "0" + time);
    }
}

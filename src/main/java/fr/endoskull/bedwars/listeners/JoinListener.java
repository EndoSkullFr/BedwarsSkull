package fr.endoskull.bedwars.listeners;

import fr.endoskull.api.commons.lang.MessageUtils;
import fr.endoskull.api.commons.paf.Party;
import fr.endoskull.api.commons.paf.PartyUtils;
import fr.endoskull.api.spigot.utils.Languages;
import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.board.FastBoard;
import fr.endoskull.bedwars.utils.FavoritesUtils;
import fr.endoskull.bedwars.utils.GameState;
import fr.endoskull.bedwars.utils.GameUtils;
import fr.endoskull.bedwars.utils.MapManager;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import org.bukkit.Bukkit;
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
        e.setJoinMessage(null);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.equals(player)) continue;
            player.hidePlayer(onlinePlayer);
            onlinePlayer.hidePlayer(player);
        }
        FavoritesUtils.loadFavorites(player);
        Arena game = MapManager.findAvaibleGame(1);
        int partySize = 1;
        if (PartyUtils.isInParty(player.getUniqueId())) {
            Party party = PartyUtils.getParty(player.getUniqueId());
            if (party.getLeader().equals(player.getUniqueId())) {
                partySize = party.getPlayers().size();
                game = MapManager.findAvaibleGame(partySize);
            } else {
                Player leader = Bukkit.getPlayer(party.getLeader());
                if (leader != null) {
                    Arena leaderGame = GameUtils.getGame(leader);
                    if ((leaderGame.getGameState() == GameState.starting || leaderGame.getGameState() == GameState.waiting) && leaderGame.getPlayers().size() < leaderGame.getTeams().size() * leaderGame.getMaxTeamSize()) {
                        game = leaderGame;
                    }
                }
            }
        }
        FastBoard board = new FastBoard(player);
        main.getBoards().add(board);
        player.setScoreboard(main.getScoreboard());
        main.getScoreboard().getTeam("default").addPlayer(player);
        if (game != null) {
            game.addPlayer(player);
        } else {
            player.kickPlayer(Languages.getLang(player).getMessage(MessageUtils.Global.ANY_SERVER));
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        e.setQuitMessage(null);
        FavoritesUtils.saveFavorites(player);
        for (FastBoard board : new ArrayList<>(main.getBoards())) {
            if (board.getPlayer().equals(player)) {
                main.getBoards().remove(board);
                break;
            }
        }
        Arena game = GameUtils.getGame(player);
        if (game != null) {
            if (game.getGameState() == GameState.waiting || game.getGameState() == GameState.starting) {
                game.removePlayer(player);
            } else if (game.getGameState() == GameState.playing) {
                game.leavePlayer(player);
            }
        }
    }
}

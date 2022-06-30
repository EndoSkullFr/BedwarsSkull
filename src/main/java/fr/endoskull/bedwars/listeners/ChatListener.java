package fr.endoskull.bedwars.listeners;

import fr.endoskull.bedwars.utils.GameState;
import fr.endoskull.bedwars.utils.GameUtils;
import fr.endoskull.bedwars.utils.MessagesUtils;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsPlayer;
import fr.endoskull.bedwars.utils.bedwars.Team;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        Player player = e.getPlayer();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
        String message = e.getMessage();
        if (game.getGameState() == GameState.playing) {
            if (!bwPlayer.isAlive() || bwPlayer.isRespawning() || bwPlayer.isWaitingGoulag()) {
                sendSpectatorMessage(game, bwPlayer, message);
            } else {
                sendGlobalMessage(game, bwPlayer, message);
            }
        } else {
            User user = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId());
            String prefix = user.getCachedData().getMetaData().getPrefix();
            if (prefix == null) prefix = "§7";
            for (Player pls : game.getAllPlayers()) {
                pls.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + bwPlayer.getName() + " §7» §f" + message);
            }
        }
    }

    public void sendGlobalMessage(Arena game, BedwarsPlayer bwPlayer, String message) {
        Team team = bwPlayer.getTeam();
        for (Player allPlayer : game.getAllPlayers()) {
            allPlayer.sendMessage("§7[" + team.getColor().chat() + MessagesUtils.getTeamDisplayName(allPlayer, team.getName()) + "§7] " + team.getColor().chat() + bwPlayer.getName() + " §7» §f" + message);
        }
    }

    public void sendSpectatorMessage(Arena game, BedwarsPlayer bwPlayer, String message) {
        for (BedwarsPlayer bedwarsPlayer : game.getPlayers()) {
            Player pls = bedwarsPlayer.getPlayer();
            if (pls == null) continue;
            if (!bedwarsPlayer.isAlive() || bwPlayer.isRespawning() || bwPlayer.isWaitingGoulag()) {
                pls.sendMessage("§7[" + MessagesUtils.SPECTATOR.getMessage(pls) + "] " + bwPlayer.getName() + " » " + message);
            }
        }
    }
}

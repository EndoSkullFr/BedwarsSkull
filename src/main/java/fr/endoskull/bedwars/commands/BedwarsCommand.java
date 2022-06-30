package fr.endoskull.bedwars.commands;

import fr.endoskull.api.spigot.utils.Title;
import fr.endoskull.bedwars.utils.GameState;
import fr.endoskull.bedwars.utils.GameUtils;
import fr.endoskull.bedwars.utils.MessagesUtils;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsPlayer;
import fr.endoskull.bedwars.utils.bedwars.Team;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class BedwarsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cVous devez être un joueur pour éxécuter cette commande");
            return false;
        }
        Player player = (Player) sender;
        Arena game = GameUtils.getGame(player);
        if (game == null) {
            player.sendMessage("§cVous n'êtes pas dans une partie");
            return false;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("start")) {
            if (game.getGameState() == GameState.waiting) game.setGameState(GameState.starting);
            if (game.getGameState() == GameState.starting && game.getStartTimer() > 5) {
                game.setStartTimer(6);
            }
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("next")) {
            if (game.getGameState() == GameState.playing && game.getEventTimer() > 5) {
                game.setEventTimer(1);
            }
        }
        if (args.length >= 2 && args[0].equalsIgnoreCase("break")) {
            Team team = game.getTeamByName(args[1].toUpperCase());
            if (team == null) {
                player.sendMessage("§cCette équipe n'existe pas");
                return false;
            }
            if (!team.isAvailable()) {
                player.sendMessage("§cCette équipe n'est pas valable");
                return false;
            }
            if (!team.isHasBed()) {
                player.sendMessage("§cCette équipe n'a pas de lit");
                return false;
            }
            Block block = game.getBeds().get(team).getLocation(game.getWorld()).getBlock();
            block.setType(Material.AIR);
            team.setHasBed(false);
            for (Player p : game.getAllPlayers()) {
                p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
                String message;
                BedwarsPlayer bwP = game.getBwPlayerByUUID(p.getUniqueId());
                if (bwP != null && bwP.getTeam().equals(team)) {
                    message = MessagesUtils.BED_BREAK_VICTIM.getMessage(p);
                    Title.sendTitle(p, 10, 40, 10, MessagesUtils.BED_BREAK_TITLE.getMessage(p), MessagesUtils.BED_BREAK_SUBTITLE.getMessage(p));
                } else {
                    message = MessagesUtils.BED_BREAK.getMessage(p);
                }
                message = message.replace("{TeamColor}", team.getColor().chat().toString()).replace("{TeamName}", MessagesUtils.getTeamDisplayName(p, team.getName()))
                        .replace("{PlayerColor}", "§4").replace("{PlayerName}", player.getDisplayName());
                p.sendMessage(message);
            }
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("checkwin")) {
            if (game.getGameState() != GameState.playing) {
                return false;
            }
            for (Team team : game.getTeams()) {
                if (team.isAvailable()) {
                    game.checkTeam(team);
                    if (game.getGameState() != GameState.playing) {
                        break;
                    }
                }
            }

            if (game.getGameState() == GameState.playing) {
                game.checkWin();
            }
        }
        return false;
    }
}

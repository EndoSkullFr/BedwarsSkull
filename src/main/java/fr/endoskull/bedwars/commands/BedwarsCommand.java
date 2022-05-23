package fr.endoskull.bedwars.commands;

import fr.endoskull.bedwars.utils.GameState;
import fr.endoskull.bedwars.utils.GameUtils;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.Team;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

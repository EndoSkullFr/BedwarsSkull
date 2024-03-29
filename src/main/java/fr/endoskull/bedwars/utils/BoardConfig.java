package fr.endoskull.bedwars.utils;

import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsPlayer;
import fr.endoskull.bedwars.utils.bedwars.Team;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BoardConfig {
    private static File file = new File(Main.getInstance().getDataFolder(), "languages/french.yml");
    private static YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    public static String getWaitingTitle() {
        return config.getStringList("board.waiting").get(0);
    }

    public static List<String> getWaitingLines() {
        List<String> lines = config.getStringList("board.waiting");
        lines.remove(0);
        return lines;
    }

    public static String getStartingTitle() {
        return config.getStringList("board.starting").get(0);
    }

    public static List<String> getStartingLines() {
        List<String> lines = config.getStringList("board.starting");
        lines.remove(0);
        return lines;
    }

    public static String getPlayingTitle() {
        return config.getStringList("board.playing").get(0);
    }

    public static List<String> getPlayingLines(Arena game, Player player) {
        List<String> lines = config.getStringList("board.playing");
        lines.remove(0);
        for (String line : new ArrayList<>(lines)) {
            if (line.equalsIgnoreCase("{teams}")) {
                int index = lines.indexOf(line);
                lines.remove(index);
                for (Team team : game.getTeams()) {
                    if (!team.isAvailable()) continue;
                    String status;
                    if (team.isHasBed()) {
                        status = "§a✔";
                    } else {
                        int size = 0;
                        for (BedwarsPlayer bwPLayer : game.getPlayersPerTeam(team)) {
                            if (bwPLayer.isAlive()) size++;
                        }
                        status = (size > 0 ? "§a" + size : "§c✖");
                    }
                    BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
                    if (bwPlayer != null && bwPlayer.getTeam().equals(team)) status += MessagesUtils.YOU.getMessage(player);
                    lines.add(index, team.getColor().chat() + MessagesUtils.getTeamDisplayName(player, team.getName()).substring(0, 1) + "§f " + MessagesUtils.getTeamDisplayName(player, team.getName()) + "§f: " + status);
                    index++;
                }
            }
        }
        return lines;
    }
    public static String getFinishTitle() {
        return config.getStringList("board.finish").get(0);
    }

    public static List<String> getFinishLines() {
        List<String> lines = config.getStringList("board.finish");
        lines.remove(0);
        return lines;
    }
}

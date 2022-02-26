package fr.endoskull.bedwars.tasks;

import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.utils.GameState;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import org.bukkit.scheduler.BukkitRunnable;

public class StartRunnable extends BukkitRunnable {
    private Main main;

    public StartRunnable(Main main) {
        this.main = main;
    }

    @Override
    public void run() {
        for (Arena game : main.getGames()) {
            if (game.getGameState() == GameState.starting) {
                if (game.getStartTimer() <= 0) {
                    game.start();
                    continue;
                }
                game.setStartTimer(game.getStartTimer() - 1);
            }
        }
    }
}

package fr.endoskull.bedwars.tasks;

import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.Team;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class AlarmTrapTask extends BukkitRunnable {
    private int i = 0;
    private boolean b = false;
    private Team team;
    private Arena game;

    public AlarmTrapTask(Team team, Arena game) {
        this.team = team;
        this.game = game;
        Bukkit.getScheduler().runTaskTimer(Main.getInstance(), this, 10, 10);
    }

    @Override
    public void run() {
        if (i > 20) {
            cancel();
            return;
        }
        Sound sound;
        if (b) {
            sound = Sound.NOTE_PIANO;
        } else {
            sound = Sound.NOTE_PLING;
        }
        b = !b;
        i++;
        game.getWorld().playSound(game.getSpawns().get(team).getLocation(game.getWorld()), sound, 1, 1);
    }
}

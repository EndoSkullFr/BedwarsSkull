package fr.endoskull.bedwars.tasks;

import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.Team;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitTask;

public class AlarmTrapTask implements Runnable {
    private int i = 0;
    private boolean b = false;
    private Team team;
    private Arena game;
    private BukkitTask task;

    public AlarmTrapTask(Team team, Arena game) {
        this.team = team;
        this.game = game;
        task = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), this, 5, 5);
    }

    @Override
    public void run() {
        if (i > 40) {
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
        game.getWorld().playSound(game.getBeds().get(team).getLocation(game.getWorld()), sound, 10, 1);
    }

    public void cancel(){
        task.cancel();
    }
}

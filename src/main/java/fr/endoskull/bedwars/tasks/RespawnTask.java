package fr.endoskull.bedwars.tasks;

import fr.endoskull.api.spigot.utils.Title;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.Team;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class RespawnTask extends BukkitRunnable {
    private Player player;
    private Arena game;
    private int timer;

    public RespawnTask(Player player, Arena game, int timer) {
        this.player = player;
        this.game = game;
        this.timer = timer;
    }

    @Override
    public void run() {
        if (timer == 0) {
            /**
             * todo invincibility
             * + particules tnt au dessus tete
             * + particules pas invisible
             */
            game.resetPlayer(player);
            cancel();
            return;
        }
        Title.sendTitle(player, 0, 20, 0, "§c" + timer);
        player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1f, 1f);
        timer--;

    }
}

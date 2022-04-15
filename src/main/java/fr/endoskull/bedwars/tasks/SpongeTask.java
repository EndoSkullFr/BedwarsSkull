package fr.endoskull.bedwars.tasks;

import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.utils.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class SpongeTask implements Runnable {
    private Block sponge;
    private int timer = 4;
    private BukkitTask task;

    public SpongeTask(Block sponge) {
        this.sponge = sponge;
        task = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), this, 0, 10);
    }

    @Override
    public void run() {
        sponge.getWorld().playSound(sponge.getLocation(), Sound.NOTE_STICKS, 1f, 1f);
        if (timer == 0) {
            sponge.setType(Material.AIR);
            cancel();
        }
        for (Block block : new Cuboid(sponge.getLocation().clone().add(2, 2, 2), sponge.getLocation().clone().subtract(2, 2, 2))) {
            block.getWorld().playEffect(block.getLocation().clone().add(0.5, 0.5, 0.5), Effect.CLOUD, 100);
        }
        timer--;
    }

    private void cancel() {
        task.cancel();
    }
}

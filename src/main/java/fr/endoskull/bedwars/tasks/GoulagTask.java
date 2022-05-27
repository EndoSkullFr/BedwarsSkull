package fr.endoskull.bedwars.tasks;

import fr.endoskull.api.spigot.utils.Title;
import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.utils.Cuboid;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class GoulagTask implements Runnable {

    private Arena game;
    private BukkitTask task;
    private static HashMap<Integer, String> titles = new HashMap<Integer, String>(){{
        put(5, "§a5");
        put(4, "§a4");
        put(3, "§e3");
        put(2, "§62");
        put(1, "§c1");
    }};

    public GoulagTask(Arena game) {
        this.game = game;
        task = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), this, 0, 20);
    }

    public Arena getGame() {
        return game;
    }

    private int startingTimer = 5;
    private int timer = 30;

    @Override
    public void run() {
        if (startingTimer > 0) {
            for (BedwarsPlayer bedwarsPlayer : game.getInGoulag()) {
                Player player = bedwarsPlayer.getPlayer();
                if (player == null) continue;
                if (titles.containsKey(startingTimer)) {
                    Title.sendTitle(player, 5, 20,5, titles.get(startingTimer), "");
                    player.playSound(player.getLocation(), Sound.BLAZE_HIT, 1f, 1f);
                }
            }

        } else if (startingTimer == 0) {
            for (Block block : new Cuboid(game.getGoulagLoc1().getLocation(game.getWorld()), game.getGoulagLoc2().getLocation(game.getWorld()))) {
                block.setType(Material.AIR);
            }
        } else {
            if (timer == 0) {
                //todo kill player
            }
            timer--;
        }
        startingTimer--;
    }

    public void cancel(){
        task.cancel();
    }
}

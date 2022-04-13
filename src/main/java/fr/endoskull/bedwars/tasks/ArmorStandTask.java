package fr.endoskull.bedwars.tasks;

import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsLocation;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

public class ArmorStandTask extends BukkitRunnable {
    private Main main;
    public ArmorStandTask(Main main) {
        this.main = main;
    }

    @Override
    public void run() {
        for (Arena game : main.getGames()) {
            for (BedwarsLocation bedwarsLocation : game.getAsDiamonds().keySet()) {
                ArmorStand as = game.getAsDiamonds().get(bedwarsLocation);
                EulerAngle eulerAngle = as.getHeadPose();
                as.setHeadPose(new EulerAngle(eulerAngle.getX(), eulerAngle.getY() + 0.1, eulerAngle.getZ()));
            }
            for (BedwarsLocation bedwarsLocation : game.getAsEmeralds().keySet()) {
                ArmorStand as = game.getAsEmeralds().get(bedwarsLocation);
                EulerAngle eulerAngle = as.getHeadPose();
                as.setHeadPose(new EulerAngle(eulerAngle.getX(), eulerAngle.getY() + 0.1, eulerAngle.getZ()));
            }
        }
    }
}

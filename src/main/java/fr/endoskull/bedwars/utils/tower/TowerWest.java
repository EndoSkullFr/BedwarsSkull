package fr.endoskull.bedwars.utils.tower;

import java.util.ArrayList;
import java.util.List;

import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.utils.NmsUtils;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class TowerWest {
  private BukkitTask task;
  
  public TowerWest(Location loc, Block chest, DyeColor color, Player p) {
    ItemStack itemInHand = p.getInventory().getItemInHand();
    NmsUtils.minusHand(p);
    List<String> relloc = new ArrayList<>();
    relloc.add("-2, 0, 1");
    relloc.add("-1, 0, 2");
    relloc.add("0, 0, 2");
    relloc.add("1, 0, 1");
    relloc.add("1, 0, 0");
    relloc.add("1, 0, -1");
    relloc.add("0, 0, -2");
    relloc.add("-1, 0, -2");
    relloc.add("-2, 0, -1");
    relloc.add("0, 0, 0, ladder4");
    relloc.add("-2, 1, 1");
    relloc.add("-1, 1, 2");
    relloc.add("0, 1, 2");
    relloc.add("1, 1, 1");
    relloc.add("1, 1, 0");
    relloc.add("1, 1, -1");
    relloc.add("0, 1, -2");
    relloc.add("-1, 1, -2");
    relloc.add("-2, 1, -1");
    relloc.add("0, 1, 0, ladder4");
    relloc.add("-2, 2, 1");
    relloc.add("-1, 2, 2");
    relloc.add("0, 2, 2");
    relloc.add("1, 2, 1");
    relloc.add("1, 2, 0");
    relloc.add("1, 2, -1");
    relloc.add("0, 2, -2");
    relloc.add("-1, 2, -2");
    relloc.add("-2, 2, -1");
    relloc.add("0, 2, 0, ladder4");
    relloc.add("-2, 3, 0");
    relloc.add("-2, 3, 1");
    relloc.add("-1, 3, 2");
    relloc.add("0, 3, 2");
    relloc.add("1, 3, 1");
    relloc.add("1, 3, 0");
    relloc.add("1, 3, -1");
    relloc.add("0, 3, -2");
    relloc.add("-1, 3, -2");
    relloc.add("-2, 3, -1");
    relloc.add("0, 3, 0, ladder4");
    relloc.add("-2, 4, 0");
    relloc.add("-2, 4, 1");
    relloc.add("-1, 4, 2");
    relloc.add("0, 4, 2");
    relloc.add("1, 4, 1");
    relloc.add("1, 4, 0");
    relloc.add("1, 4, -1");
    relloc.add("0, 4, -2");
    relloc.add("-1, 4, -2");
    relloc.add("-2, 4, -1");
    relloc.add("0, 4, 0, ladder4");
    relloc.add("1, 5, 2");
    relloc.add("0, 5, 2");
    relloc.add("-1, 5, 2");
    relloc.add("-2, 5, 2");
    relloc.add("1, 5, 1");
    relloc.add("0, 5, 1");
    relloc.add("-1, 5, 1");
    relloc.add("-2, 5, 1");
    relloc.add("1, 5, 0");
    relloc.add("-1, 5, 0");
    relloc.add("-2, 5, 0");
    relloc.add("1, 5, -1");
    relloc.add("0, 5, -1");
    relloc.add("-1, 5, -1");
    relloc.add("-2, 5, -1");
    relloc.add("0, 5, 0, ladder4");
    relloc.add("1, 5, -2");
    relloc.add("0, 5, -2");
    relloc.add("-1, 5, -2");
    relloc.add("-2, 5, -2");
    relloc.add("-2, 5, 3");
    relloc.add("-2, 6, 3");
    relloc.add("-2, 7, 3");
    relloc.add("-1, 6, 3");
    relloc.add("0, 6, 3");
    relloc.add("1, 5, 3");
    relloc.add("1, 6, 3");
    relloc.add("1, 7, 3");
    relloc.add("2, 5, 2");
    relloc.add("2, 6, 2");
    relloc.add("2, 7, 2");
    relloc.add("2, 6, 1");
    relloc.add("2, 5, 0");
    relloc.add("2, 6, 0");
    relloc.add("2, 7, 0");
    relloc.add("2, 6, -1");
    relloc.add("2, 5, -2");
    relloc.add("2, 6, -2");
    relloc.add("2, 7, -2");
    relloc.add("-2, 5, -3");
    relloc.add("-2, 6, -3");
    relloc.add("-2, 7, -3");
    relloc.add("-1, 6, -3");
    relloc.add("0, 6, -3");
    relloc.add("1, 5, -3");
    relloc.add("1, 6, -3");
    relloc.add("1, 7, -3");
    relloc.add("-3, 5, 2");
    relloc.add("-3, 6, 2");
    relloc.add("-3, 7, 2");
    relloc.add("-3, 6, 1");
    relloc.add("-3, 5, 0");
    relloc.add("-3, 6, 0");
    relloc.add("-3, 7, 0");
    relloc.add("-3, 6, -1");
    relloc.add("-3, 5, -2");
    relloc.add("-3, 6, -2");
    relloc.add("-3, 7, -2");
    int[] i = { 0 };
    this.task = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
          if (Bukkit.getServer().getClass().getPackage().getName().contains("v1_8")) {
            loc.getWorld().playSound(loc, Sound.valueOf("CHICKEN_EGG_POP"), 1.0F, 0.5F);
          } else {
            loc.getWorld().playSound(loc, Sound.valueOf("ENTITY_CHICKEN_EGG"), 1.0F, 0.5F);
          } 
          if (relloc.size() + 1 == i[0] + 1) {
            this.task.cancel();
            return;
          } 
          String c1 = relloc.get(i[0]);
          if (c1.contains("ladder")) {
            int ldata = Integer.parseInt(c1.split("ladder")[1]);
            new NewPlaceBlock(chest, c1, color, p, true, ldata);
          } else {
            new NewPlaceBlock(chest, c1, color, p, false, 0);
          } 
          if (relloc.size() + 1 == i[0] + 2) {
            this.task.cancel();
            return;
          } 
          String c2 = relloc.get(i[0] + 1);
          if (c2.contains("ladder")) {
            int ldata = Integer.parseInt(c2.split("ladder")[1]);
            new NewPlaceBlock(chest, c2, color, p, true, ldata);
          } else {
            new NewPlaceBlock(chest, c2, color, p, false, 0);
          } 
          i[0] = i[0] + 2;
        }, 0L, 1L);
  }
}

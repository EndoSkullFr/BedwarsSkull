/*
 * BedWars1058 - A bed wars mini-game.
 * Copyright (C) 2021 Andrei Dascălu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * Contact e-mail: andrew.dascalu@gmail.com
 */

package fr.endoskull.bedwars.tasks;

import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.listeners.playing.EggListener;
import fr.endoskull.bedwars.utils.GameUtils;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsPlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

@SuppressWarnings("WeakerAccess")
public class EggBridgeTask implements Runnable {

    private Egg projectile;
    private DyeColor color;
    private Player player;
    private Arena arena;
    private BukkitTask task;

    public EggBridgeTask(Player player, Egg projectile, DyeColor color) {
        Arena a = GameUtils.getGame(player);
        if (a == null) return;
        this.arena = a;
        this.projectile = projectile;
        this.color = color;
        this.player = player;
        task = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), this, 0, 1);
    }

    public DyeColor getColor() {
        return color;
    }

    public Egg getProjectile() {
        return projectile;
    }

    public Player getPlayer() {
        return player;
    }

    public Arena getArena() {
        return arena;
    }

    @Override
    public void run() {

        Location loc = getProjectile().getLocation();

        BedwarsPlayer bwPlayer = arena.getBwPlayerByUUID(player.getUniqueId());
        if (getProjectile().isDead()
                || bwPlayer == null
                || bwPlayer.isSpectator()
                || bwPlayer.isRespawning()
                || getPlayer().getLocation().distance(getProjectile().getLocation()) > 27
                || getPlayer().getLocation().getY() - getProjectile().getLocation().getY() > 9) {
            EggListener.removeEgg(projectile);
            return;
        }

        if (getPlayer().getLocation().distance(loc) > 4.0D) {

            Block b2 = loc.clone().subtract(0.0D, 2.0D, 0.0D).getBlock();
            if (arena.isAvaibleBlock(b2)) {
                if (b2.getType() == Material.AIR) {
                    b2.setType(Material.WOOL);
                    b2.setData(color.getData());
                    getArena().getPlacedBlocks().add(b2);
                    loc.getWorld().playEffect(b2.getLocation(), Effect.MOBSPAWNER_FLAMES, 3);
                    player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1f, 1f);
                }
            }

            Block b3 = loc.clone().subtract(1.0D, 2.0D, 0.0D).getBlock();
            if (arena.isAvaibleBlock(b3)) {
                if (b3.getType() == Material.AIR) {
                    b3.setType(Material.WOOL);
                    b3.setData(color.getData());
                    getArena().getPlacedBlocks().add(b3);
                    loc.getWorld().playEffect(b3.getLocation(), Effect.MOBSPAWNER_FLAMES, 3);
                    player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1f, 1f);
                }
            }

            Block b4 = loc.clone().subtract(0.0D, 2.0D, 1.0D).getBlock();
            if (arena.isAvaibleBlock(b4)) {
                    b4.setType(Material.WOOL);
                    b4.setData(color.getData());
                    getArena().getPlacedBlocks().add(b4);
                    loc.getWorld().playEffect(b4.getLocation(), Effect.MOBSPAWNER_FLAMES, 3);
                    player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1f, 1f);
            }
        }
    }

    public void cancel(){
        task.cancel();
    }
}

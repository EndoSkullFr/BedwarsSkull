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

package fr.endoskull.bedwars.listeners.playing;

import fr.endoskull.bedwars.tasks.EggBridgeTask;
import fr.endoskull.bedwars.utils.GameUtils;
import fr.endoskull.bedwars.utils.NmsUtils;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsPlayer;
import fr.endoskull.bedwars.utils.mobs.Golem;
import fr.endoskull.bedwars.utils.mobs.Silverfish;
import net.minecraft.server.v1_8_R3.EntityIronGolem;
import net.minecraft.server.v1_8_R3.EntitySilverfish;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftIronGolem;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSilverfish;
import org.bukkit.entity.Egg;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class EggListener implements Listener {

    //Active eggBridges
    private static HashMap<Egg, EggBridgeTask> bridges = new HashMap<>();

    @EventHandler
    public void onLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Egg) {
            Egg projectile = (Egg) event.getEntity();
            if (projectile.getShooter() instanceof Player) {
                Player shooter = (Player) projectile.getShooter();
                Arena arena = GameUtils.getGame(shooter);
                if (arena != null) {
                    BedwarsPlayer bwPlayer = arena.getBwPlayerByUUID(shooter.getUniqueId());
                    if (!bwPlayer.isSpectator() && !bwPlayer.isRespawning()) {
                        if (event.isCancelled()) {
                            event.setCancelled(true);
                            return;
                        }
                        bridges.put(projectile, new EggBridgeTask(shooter, projectile, bwPlayer.getTeam().getColor().dye()));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Egg) {
            removeEgg((Egg) e.getEntity());
        }
        if (e.getEntity() instanceof Snowball) {
            Snowball snowball = (Snowball) e.getEntity();
            if (!(snowball.getShooter() instanceof Player)) return;
            Player player = (Player) snowball.getShooter();
            Arena arena = GameUtils.getGame(player);
            if (arena == null) return;
            BedwarsPlayer bwPlayer = arena.getBwPlayerByUUID(player.getUniqueId());
            NmsUtils.spawnSilverfish(e.getEntity().getLocation(), bwPlayer.getTeam(), arena, 0.25, 8, 15, 4, player);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        ItemStack current = e.getItem();
        if (current == null || current.getType() == Material.AIR) return;
        if (current.getType() != Material.MONSTER_EGG || current.getDurability() != (short) 99) return;
        BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
        NmsUtils.minusHand(player);
        NmsUtils.spawnGolem(e.getClickedBlock().getLocation().clone().add(0.5, 1, 0.5), bwPlayer.getTeam(), game, 0.25, 100, 240, player);
    }

    /**
     * Remove an egg from the active eggs list
     *
     * @since API 7
     */
    public static void removeEgg(Egg e) {
        if (bridges.containsKey(e)) {
            if (bridges.get(e) != null) {
                bridges.get(e).cancel();
            }
            bridges.remove(e);
        }
    }

    /**
     * Get active egg bridges.
     * Modified  in api 11
     *
     * @since API 11
     */
    public static Map<Egg, EggBridgeTask> getBridges() {
        return Collections.unmodifiableMap(bridges);
    }

    @EventHandler
    public void onEntDamageByEnt(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        Player player = (Player) e.getDamager();
        if (e.getEntity() instanceof org.bukkit.entity.Silverfish) {
            org.bukkit.entity.Silverfish s = (org.bukkit.entity.Silverfish) e.getEntity();
            EntitySilverfish es = ((CraftSilverfish) s).getHandle();
            if (es instanceof Silverfish) {
                Silverfish silverfish = (Silverfish) es;
                Arena game = GameUtils.getGame(player);
                if (game == null) return;
                BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
                if (bwPlayer.isSpectator() || bwPlayer.isRespawning()) return;
                if (bwPlayer.getTeam().equals(silverfish.getTeam())) e.setCancelled(true);
            }
        } else if (e.getEntity() instanceof IronGolem) {
            IronGolem g = (IronGolem) e.getEntity();
            EntityIronGolem eg = ((CraftIronGolem) g).getHandle();
            if (eg instanceof Golem) {
                Golem golem = (Golem) eg;
                Arena game = GameUtils.getGame(player);
                if (game == null) return;
                BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
                if (bwPlayer.isSpectator() || bwPlayer.isRespawning()) return;
                if (bwPlayer.getTeam().equals(golem.getTeam())) e.setCancelled(true);
            }
        }
    }
}

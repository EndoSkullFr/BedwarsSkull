package fr.endoskull.bedwars.listeners.playing;

import fr.endoskull.bedwars.utils.GameState;
import fr.endoskull.bedwars.utils.GameUtils;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class DamageListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        if (game.getRespawnings().containsKey(player)) {
            e.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Snowball || e.getDamager() instanceof Egg) {
            e.setCancelled(true);
            return;
        }
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            Arena a = GameUtils.getGame(p);
            if (a != null) {
                if (a.getGameState() != GameState.playing) {
                    e.setCancelled(true);
                    return;
                }
                //check spectator
                //if (a.isSpectator(p) || a.isReSpawning(p)) {
                //    e.setCancelled(true);
                //    return;
                //}

                Player damager = null;
                boolean projectile = false;
                if (e.getDamager() instanceof Player) {
                    damager = (Player) e.getDamager();
                } else if (e.getDamager() instanceof Projectile) {
                    ProjectileSource shooter = ((Projectile) e.getDamager()).getShooter();
                    if (shooter instanceof Player) {
                        damager = (Player) shooter;
                        if (e.getDamager() instanceof Fireball) {
                            e.setDamage(5);
                            Fireball fireball = (Fireball) e.getDamager();
                            LivingEntity damaged = (LivingEntity) e.getEntity();
                            /*Vector distance = damaged.getLocation().subtract(0, 0, 0).toVector().subtract(fireball.getLocation().toVector());
                            Vector direction = distance.clone().normalize();
                            double force = ((fireball.getYield() * fireball.getYield()) / (3 + distance.length()));
                            Vector resultingForce = direction.clone().multiply(force);
                            resultingForce.setY(resultingForce.getY() / (distance.length() + 1.5));
                            damaged.setVelocity(resultingForce);*/
                            Location loc = damaged.getLocation();
                            double distance = loc.distance(fireball.getLocation());
                            distance = Math.max(distance, 1);

                            double hf1 = Math.max(-4, Math.min(4, 1));
                            double rf1 = Math.max(-4, Math.min(4, -1*2));

                            damaged.setVelocity(fireball.getLocation().toVector().subtract(loc.toVector()).normalize().multiply(rf1).setY(hf1).multiply(1/distance));
                        }
                    } else return;
                    projectile = true;
                } else if (e.getDamager() instanceof Player) {
                    damager = (Player) e.getDamager();
                    /*if (a.isReSpawning(damager)) {
                        e.setCancelled(true);
                        return;
                    }*/
                } else if (e.getDamager() instanceof TNTPrimed) {
                    TNTPrimed tnt = (TNTPrimed) e.getDamager();
                    if (tnt.getSource() != null) {
                        if (tnt.getSource() instanceof Player) {
                            damager = (Player) tnt.getSource();
                            if (damager.equals(p)) {
                                e.setDamage(1);
                                // tnt jump. credits to feargames.it
                                LivingEntity damaged = (LivingEntity) e.getEntity();
                                Vector distance = damaged.getLocation().subtract(0, 0.5, 0).toVector().subtract(tnt.getLocation().toVector());
                                Vector direction = distance.clone().normalize();
                                double force = ((tnt.getYield() * tnt.getYield()) / (5 + distance.length()));
                                Vector resultingForce = direction.clone().multiply(force);
                                resultingForce.setY(resultingForce.getY() / (distance.length() + 2));
                                damaged.setVelocity(resultingForce);
                            } else {
                                Team currentTeam = a.getPlayers().get(p);
                                Team damagerTeam = a.getPlayers().get(damager);
                                if (currentTeam.equals(damagerTeam)) {
                                    e.setDamage(5);
                                } else {
                                    e.setDamage(10);
                                }
                            }
                        } else return;
                    }
                } else if ((e.getDamager() instanceof Silverfish) || (e.getDamager() instanceof IronGolem)) {
                    /*LastHit lh = LastHit.getLastHit(p);
                    if (lh != null) {
                        lh.setDamager(e.getDamager());
                        lh.setTime(System.currentTimeMillis());
                    } else {
                        new LastHit(p, e.getDamager(), System.currentTimeMillis());
                    }*/
                }
                if (damager != null) {
                    /*if (a.isSpectator(damager) || a.isReSpawning(damager.getUniqueId())) {
                        e.setCancelled(true);
                        return;
                    }*/

                    if (a.getPlayers().get(p).equals(a.getPlayers().get(damager))) {
                        if (!(e.getDamager() instanceof TNTPrimed)) {
                            e.setCancelled(true);
                        }
                        return;
                    }

                    // protection after re-spawn
                    /*if (BedWarsTeam.reSpawnInvulnerability.containsKey(p.getUniqueId())) {
                        if (BedWarsTeam.reSpawnInvulnerability.get(p.getUniqueId()) > System.currentTimeMillis()) {
                            e.setCancelled(true);
                            return;
                        } else BedWarsTeam.reSpawnInvulnerability.remove(p.getUniqueId());
                    }*/
                    // but if the damageR is the re-spawning player remove protection
                    //BedWarsTeam.reSpawnInvulnerability.remove(damager.getUniqueId());

                    /*LastHit lh = LastHit.getLastHit(p);
                    if (lh != null) {
                        lh.setDamager(damager);
                        lh.setTime(System.currentTimeMillis());
                    } else {
                        new LastHit(p, damager, System.currentTimeMillis());
                    }*/

                    // #274
                    // if player gets hit show him
                    /*if (a.getShowTime().containsKey(p)) {
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            for (Player on : a.getWorld().getPlayers()) {
                                BedWars.nms.showArmor(p, on);
                                //BedWars.nms.showPlayer(p, on);
                            }
                            a.getShowTime().remove(p);
                            p.removePotionEffect(PotionEffectType.INVISIBILITY);
                            ITeam team = a.getTeam(p);
                            Bukkit.getPluginManager().callEvent(new PlayerInvisibilityPotionEvent(PlayerInvisibilityPotionEvent.Type.REMOVED, team, p, a));
                        });
                    }*/
                    //
                }
            }
        } else if (/*nms.isDespawnable(e.getEntity())*/false) {
            Player damager;
            if (e.getDamager() instanceof Player) {
                damager = (Player) e.getDamager();
            } else if (e.getDamager() instanceof Projectile) {
                Projectile proj = (Projectile) e.getDamager();
                damager = (Player) proj.getShooter();
            } else if (e.getDamager() instanceof TNTPrimed) {
                TNTPrimed tnt = (TNTPrimed) e.getDamager();
                if (tnt.getSource() instanceof Player) {
                    damager = (Player) tnt.getSource();
                } else return;
            } else return;
            Arena a = GameUtils.getGame(damager);
            if (a != null) {
                if (a.getPlayers().containsKey(damager)) {
                    // do not hurt own mobs
                    if (a.getPlayers().get(damager) == /*nms.getDespawnablesList().get(e.getEntity().getUniqueId()).getTeam()*/null) {
                        e.setCancelled(true);
                    }
                } else {
                    e.setCancelled(true);
                }
            }
        } /*else if (e.getEntity() instanceof IronGolem) {
            Player damager;
            if (e.getDamager() instanceof Player) {
                damager = (Player) e.getDamager();
            } else if (e.getDamager() instanceof Projectile) {
                Projectile proj = (Projectile) e.getDamager();
                damager = (Player) proj.getShooter();
            } else {
                return;
            }
            Arena a = Arena.getArenaByPlayer(damager);
            if (a != null) {
                if (a.isPlayer(damager)) {
                    if (nms.isDespawnable(e.getEntity())) {
                        if (a.getTeam(damager) == ((OwnedByTeam) nms.getDespawnablesList().get(e.getEntity().getUniqueId())).getOwner()) {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }*/
    }
}

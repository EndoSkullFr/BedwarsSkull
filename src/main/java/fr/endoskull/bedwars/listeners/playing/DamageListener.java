package fr.endoskull.bedwars.listeners.playing;

import fr.endoskull.bedwars.utils.DamageType;
import fr.endoskull.bedwars.utils.GameState;
import fr.endoskull.bedwars.utils.GameUtils;
import fr.endoskull.bedwars.utils.NmsUtils;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsPlayer;
import fr.endoskull.bedwars.utils.bedwars.LastHit;
import fr.endoskull.bedwars.utils.bedwars.Team;
import fr.endoskull.bedwars.utils.mobs.Despawnable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class DamageListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
        if (bwPlayer.isRespawning() || bwPlayer.isSpectator()) {
            e.setCancelled(true);
            return;
        }
        if (game.getInvincibility().containsKey(bwPlayer)) {
            if (game.getInvincibility().get(bwPlayer) > System.currentTimeMillis()) {
                e.setCancelled(true);
                return;
            } else {
                game.getInvincibility().remove(bwPlayer);
            }
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
                BedwarsPlayer bwPlayer = a.getBwPlayerByUUID(p.getUniqueId());
                if (a.getGameState() != GameState.playing) {
                    e.setCancelled(true);
                    return;
                }

                if (bwPlayer.isSpectator() || bwPlayer.isRespawning()) {
                    e.setCancelled(true);
                    return;
                }

                Player damager = null;
                BedwarsPlayer bwDamager = null;
                DamageType damageType = null;
                if (e.getDamager() instanceof Player) {
                    damager = (Player) e.getDamager();
                    bwDamager = a.getBwPlayerByUUID(damager.getUniqueId());
                    damageType = DamageType.PLAYER;
                } else if (e.getDamager() instanceof Projectile) {
                    ProjectileSource shooter = ((Projectile) e.getDamager()).getShooter();
                    if (shooter instanceof Player) {
                        damager = (Player) shooter;
                        bwDamager = a.getBwPlayerByUUID(damager.getUniqueId());
                        if (e.getDamager() instanceof Fireball) {
                            damageType = DamageType.FIREBALL;
                            e.setDamage(5);
                            Fireball fireball = (Fireball) e.getDamager();
                            LivingEntity damaged = (LivingEntity) e.getEntity();
                            Location loc = damaged.getLocation();
                            double distance = loc.distance(fireball.getLocation());
                            distance = Math.max(distance, 1);

                            double hf1 = Math.max(-4, Math.min(4, 1));
                            double rf1 = Math.max(-4, Math.min(4, -1*2));

                            damaged.setVelocity(fireball.getLocation().toVector().subtract(loc.toVector()).normalize().multiply(rf1).setY(hf1).multiply(1/distance));
                        } else {
                            damageType = DamageType.ARROW;
                        }
                    } else return;
                } else if (e.getDamager() instanceof TNTPrimed) {
                    TNTPrimed tnt = (TNTPrimed) e.getDamager();
                    if (tnt.getSource() != null) {
                        if (tnt.getSource() instanceof Player) {
                            damager = (Player) tnt.getSource();
                            bwDamager = a.getBwPlayerByUUID(damager.getUniqueId());
                            damageType = DamageType.TNT;
                            if (damager.equals(p)) {
                                e.setDamage(5);

                                LivingEntity damaged = (LivingEntity) e.getEntity();
                                Vector distance = damaged.getLocation().subtract(0, 0.5, 0).toVector().subtract(tnt.getLocation().toVector());
                                Vector direction = distance.clone().normalize();
                                double force = ((tnt.getYield() * tnt.getYield()) / (5 + distance.length()));
                                Vector resultingForce = direction.clone().multiply(force);
                                resultingForce.setY(resultingForce.getY() / (distance.length() + 2));
                                damaged.setVelocity(resultingForce);
                            } else {
                                Team currentTeam = bwPlayer.getTeam();
                                Team damagerTeam = bwDamager.getTeam();
                                if (currentTeam.equals(damagerTeam)) {
                                    e.setDamage(5);
                                } else {
                                    e.setDamage(10);
                                }
                            }
                        } else return;
                    }
                } else if ((e.getDamager() instanceof IronGolem)) {
                    Despawnable despawnable = Despawnable.getDespawnableMap().get(e.getDamager().getUniqueId());
                    if (despawnable != null) {
                        damager = despawnable.getPlayer();
                        damageType = DamageType.GOLEM;
                    }
                } else if ((e.getDamager() instanceof Silverfish)) {
                    Despawnable despawnable = Despawnable.getDespawnableMap().get(e.getDamager().getUniqueId());
                    if (despawnable != null) {
                        damager = despawnable.getPlayer();
                        damageType = DamageType.SILVERFISH;
                    }
                }
                if (damager != null) {
                    a.getInvincibility().remove(bwDamager);
                    if (bwDamager == null) {
                        e.setCancelled(true);
                        return;
                    }
                    if (bwDamager.isSpectator() || bwDamager.isRespawning()) {
                        e.setCancelled(true);
                        return;
                    }

                    if (bwDamager.getTeam().equals(bwPlayer.getTeam())) {
                        /*if (!(e.getDamager() instanceof TNTPrimed) && !(e.getDamager() instanceof Fireball)) {
                            e.setCancelled(true);
                        }*/
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

                    new LastHit(p, damager.getUniqueId(), damageType, System.currentTimeMillis());

                    if (p.getActivePotionEffects().stream().filter(potionEffect -> potionEffect.getType().equals(PotionEffectType.INVISIBILITY)).findFirst().orElse(null) != null) {
                        p.removePotionEffect(PotionEffectType.INVISIBILITY);
                        for (Player receiver : a.getIngamePlayers()) {
                            BedwarsPlayer bwReceiver = a.getBwPlayerByUUID(receiver.getUniqueId());
                            if (bwReceiver.getTeam().equals(bwPlayer.getTeam())) continue;
                            NmsUtils.showArmor(p, receiver);
                        }
                    }

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
        }
    }

    private HashMap<UUID, Long> cooldown = new HashMap<>();

    @EventHandler
    public void onFallVoid(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        if (game.getGameState() != GameState.playing) return;
        if (e.getTo().getY() > game.getSpawns().get(game.getTeams().get(0)).getY() - 50) return;
        for (UUID uuid : new ArrayList<>(cooldown.keySet())) {
            long along = cooldown.get(uuid);
            if (System.currentTimeMillis() > along) {
                cooldown.remove(uuid);
            }
        }
        BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
        if (bwPlayer.isSpectator() || bwPlayer.isRespawning()) return;
        if (cooldown.containsKey(player.getUniqueId())) return;
        LastHit lastHit = LastHit.getLastHit(player);
        if (lastHit == null) {
            new LastHit(player, null, DamageType.VOID, System.currentTimeMillis());
        } else {
            if (lastHit.getType() == DamageType.VOID || lastHit.getType() == DamageType.OTHER) {
                lastHit.setType(DamageType.VOID);
            } else if (!lastHit.getType().toString().endsWith("VOID")) {
                lastHit.setType(DamageType.valueOf(lastHit.getType() + "_VOID"));
            }
        }
        cooldown.put(player.getUniqueId(), System.currentTimeMillis() + 5000);
        player.teleport(game.getLobby().getLocation(game.getWorld()));
        player.setHealth(0);
    }

    @EventHandler
    public void onFall(EntityDamageEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        if (game.getGameState() != GameState.playing) return;
        BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
        if (bwPlayer.isSpectator() || bwPlayer.isRespawning()) return;
        LastHit lastHit = LastHit.getLastHit(player);
        if (lastHit == null) {
            new LastHit(player, null, DamageType.OTHER, System.currentTimeMillis());
        }
    }
}

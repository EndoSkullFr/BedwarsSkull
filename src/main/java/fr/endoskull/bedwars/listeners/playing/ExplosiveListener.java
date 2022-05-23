package fr.endoskull.bedwars.listeners.playing;

import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.tasks.SpongeTask;
import fr.endoskull.bedwars.utils.Cuboid;
import fr.endoskull.bedwars.utils.GameState;
import fr.endoskull.bedwars.utils.GameUtils;
import fr.endoskull.bedwars.utils.NmsUtils;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ExplosiveListener implements Listener {
    private Main main;
    public ExplosiveListener(Main main) {
        this.main = main;
    }

    private final HashMap<UUID, Long> fireballCooldown = new HashMap<>();

    @EventHandler
    public void onTntPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        if (game.getGameState() != GameState.playing) return;
        Block block = e.getBlock();
        if (block.getType() == Material.TNT) {
            block.setType(Material.AIR);
            TNTPrimed tnt = block.getWorld().spawn(block.getLocation().clone().add(0.5, 0.5, 0.5), TNTPrimed.class);
            tnt.setFuseTicks(45);
            NmsUtils.setSource(tnt, player);
        } else if (block.getType() == Material.SPONGE) {
            new SpongeTask(block);
        }
    }

    @EventHandler
    public void onBlow(EntityExplodeEvent e) {
        if (e.isCancelled()) return;
        if (e.blockList().isEmpty()) return;
        Arena game = GameUtils.getGame(e.getLocation().getWorld());
        if (game != null) {
            if (game.getGameState() != GameState.playing) return;
            List<Block> destroyed = e.blockList();
            for (Block block : new ArrayList<>(destroyed)) {
                if (block.getType() == Material.STAINED_GLASS) {
                    e.blockList().clear();
                    break;
                }
                if (!game.getPlacedBlocks().contains(block)) {
                    e.blockList().remove(block);
                }
            }
        }
    }

    @EventHandler
    public void onBlow(BlockExplodeEvent e) {
        if (e.isCancelled()) return;
        if (e.blockList().isEmpty()) return;
        Arena game = GameUtils.getGame(e.getBlock().getWorld());
        if (game != null) {
            if (game.getGameState() != GameState.playing) return;
            List<Block> destroyed = e.blockList();
            for (Block block : new ArrayList<>(destroyed)) {
                if (block.getType() == Material.STAINED_GLASS) {
                    e.blockList().clear();
                    break;
                }
                if (!game.getPlacedBlocks().contains(block)) {
                    e.blockList().remove(block);
                } else if (block.getType() == Material.ENDER_STONE || block.getType() == Material.STAINED_CLAY) {
                    e.blockList().remove(block);
                }
            }
            game.getPlacedBlocks().removeAll(e.blockList());
        }
    }

    @EventHandler
    public void onFireball(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack inHand = e.getItem();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            if (inHand == null) return;
            Arena a = GameUtils.getGame(p);
            if (a != null) {
                if (a.getGameState() != GameState.playing) return;
                BedwarsPlayer bwPlayer = a.getBwPlayerByUUID(p.getUniqueId());
                if (!bwPlayer.isSpectator() && !bwPlayer.isRespawning()) {
                    if (inHand.getType() == Material.FIREBALL) {
                        e.setCancelled(true);
                        if (fireballCooldown.containsKey(p.getUniqueId()) && fireballCooldown.get(p.getUniqueId()) > System.currentTimeMillis()) return;
                        Fireball fb = p.launchProjectile(Fireball.class);
                        Vector direction = p.getEyeLocation().getDirection();
                        fb = NmsUtils.setFireballDirection(fb, direction);
                        fb.setVelocity(fb.getDirection().multiply(2));
                        fb.setIsIncendiary(false);
                        fb.setMetadata("bw1058", new FixedMetadataValue(main, "ceva"));
                        fb.setYield(3);
                        NmsUtils.minusHand(p);
                        fireballCooldown.put(p.getUniqueId(), System.currentTimeMillis() + 1000);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onFireball(ExplosionPrimeEvent e) {
        if (e.getEntity() instanceof Fireball) {
            Arena game = GameUtils.getGame(e.getEntity().getWorld());
            if (game == null) return;
            if (game.getGameState() != GameState.playing) return;
            double radius = e.getRadius();
            Block block = e.getEntity().getLocation().getBlock();
            List<Block> toBreak = new ArrayList<>();
            for (Block b : new Cuboid(block.getLocation().clone().add(radius, radius, radius), block.getLocation().clone().subtract(radius, radius, radius))) {
                if (b.getLocation().distance(block.getLocation()) <= radius) {
                    if (!game.getPlacedBlocks().contains(b)) continue;
                    if (b.getType() == Material.STAINED_GLASS) {
                        toBreak.clear();
                        break;
                    }
                    if (b.getType() == Material.WOOD || b.getType() == Material.LADDER || b.getType() == Material.WOOL) {
                        //b.breakNaturally(new ItemStack(Material.DIAMOND_AXE));
                        toBreak.add(b);
                        //game.getPlacedBlocks().remove(b);
                    }
                }
            }
            game.getPlacedBlocks().addAll(toBreak);
            for (Block b : toBreak) {
                b.breakNaturally(new ItemStack(Material.DIAMOND_AXE));
            }
        }
    }
    @EventHandler
    public void onItem(ItemSpawnEvent e) {
        Arena game = GameUtils.getGame(e.getEntity().getWorld());
        if (game == null) return;
        if (game.getGameState() != GameState.playing) return;
        if (e.getEntity().getItemStack().getType() == Material.BED) e.setCancelled(true);
    }
}

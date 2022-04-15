package fr.endoskull.bedwars.listeners.playing;

import fr.endoskull.api.spigot.utils.Title;
import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.guis.ShopGui;
import fr.endoskull.bedwars.utils.*;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsLocation;
import fr.endoskull.bedwars.utils.bedwars.Team;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.IMerchant;
import net.minecraft.server.v1_8_R3.InventoryMerchant;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Bed;

import java.lang.reflect.Field;

public class GameListener implements Listener {
    private Main main;
    public GameListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onEntClick(PlayerInteractAtEntityEvent e) {
        Player player = e.getPlayer();
        for (Arena game : main.getGames()) {
            if (game.getPlayers().containsKey(player)) {
                e.setCancelled(true);
                Entity ent = e.getRightClicked();
                for (Team team : game.getShops().keySet()) {
                    if (ent.getLocation().equals(game.getShops().get(team).getLocation(game.getWorld()))) {
                        new ShopGui(ShopCategories.QUICK_BUY, player).open(player);
                    }
                }
            }
        }
    }

    @EventHandler
    public void villDisableTrade(InventoryOpenEvent e) {
        if(e.getInventory().getType() == InventoryType.MERCHANT) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Arena game = GameUtils.getGame(player);
        if (game != null) {
            if (game.getGameState() != GameState.playing) return;
            Block block = e.getBlock();
            if (block.getType() == Material.BED_BLOCK) {
                for (Team team : game.getTeams()) {
                    if (!team.isAvailable() || !team.isHasBed()) continue;
                    BedwarsLocation bedTeamLoc = game.getBeds().get(team);
                    Block teamBedBlock = bedTeamLoc.getLocation(game.getWorld()).getBlock();
                    if (teamBedBlock.getType() == Material.BED_BLOCK) {
                        if (teamBedBlock.getLocation().distance(block.getLocation()) <= 1) {
                            if (game.getPlayersPerTeam(team).contains(player)) {
                                e.setCancelled(true);
                                player.sendMessage(MessagesUtils.BREAK_SELF_BED.getMessage(player));
                                if (e.getPlayer().getLocation().getBlock().getType().toString().contains("BED")) {
                                    e.getPlayer().teleport(e.getPlayer().getLocation().add(0, 0.5, 0));
                                }
                            } else {
                                e.setCancelled(true);
                                block.setType(Material.AIR);
                                team.setHasBed(false);
                                for (Player p : game.getAllPlayers()) {
                                    p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
                                    String message;
                                    if (game.getPlayersPerTeam(team).contains(p)) {
                                        message = MessagesUtils.BED_BREAK_VICTIM.getMessage(p);
                                        Title.sendTitle(p, 10, 40, 10, MessagesUtils.BED_BREAK_TITLE.getMessage(p), MessagesUtils.BED_BREAK_SUBTITLE.getMessage(p));
                                    } else {
                                        message = MessagesUtils.BED_BREAK.getMessage(p);
                                    }
                                    message = message.replace("{TeamColor}", team.getColor().chat().toString()).replace("{TeamName}", team.getDisplayName())
                                            .replace("{PlayerColor}", game.getPlayers().get(player).getColor().chat().toString()).replace("{PlayerName}", player.getDisplayName());
                                    p.sendMessage(message);
                                }

                            }
                        }
                    }
                }
            } else if (!game.getPlacedBlocks().contains(block)) {
                e.setCancelled(true);
                player.sendMessage(MessagesUtils.BREAK_BLOCK.getMessage(player));
            } else {
                game.getPlacedBlocks().remove(block);
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Arena game = GameUtils.getGame(player);
        if (game != null) {
            if (game.getGameState() != GameState.playing) return;
            if (e.getBlock().getY() > game.getHeightLimit()) {
                e.setCancelled(true);
                player.sendMessage(MessagesUtils.HEIGH_LIMIT.getMessage(player));
                return;
            }
            if (!game.isAvaibleBlock(e.getBlock())) {
                e.setCancelled(true);
                player.sendMessage(MessagesUtils.CANT_PLACE_HERE.getMessage(player));
                return;
            }
            game.getPlacedBlocks().add(e.getBlock());
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        Player player = e.getPlayer();
        if (e.getItem().getType() == Material.POTION) {
            Bukkit.getScheduler().runTaskLater(main, () -> player.getInventory().remove(Material.GLASS_BOTTLE), 3);
        }
        if (e.getItem().getType() == Material.MILK_BUCKET) {
            Bukkit.getScheduler().runTaskLater(main, () -> player.getInventory().remove(Material.BUCKET), 3);
        }
    }

    @EventHandler
    public void onBucket(PlayerBucketEmptyEvent e) {
        Player player = e.getPlayer();
        Arena game = GameUtils.getGame(player);
        Block block = e.getBlockClicked().getRelative(e.getBlockFace());
        if (game != null) {
            if (game.getGameState() != GameState.playing) return;
            if (block.getY() > game.getHeightLimit()) {
                e.setCancelled(true);
                player.sendMessage(MessagesUtils.HEIGH_LIMIT.getMessage(player));
                return;
            }
            if (!game.isAvaibleBlock(block)) {
                e.setCancelled(true);
                player.sendMessage(MessagesUtils.CANT_PLACE_HERE.getMessage(player));
                return;
            }
            Bukkit.getScheduler().runTaskLater(main, () -> player.getInventory().remove(Material.BUCKET), 3);
        }
    }

    @EventHandler
    public void onPhysic(BlockFromToEvent e) {
        Block block1 = e.getBlock();
        Block block2 = e.getToBlock();
        Arena game = GameUtils.getGame(block1.getWorld());
        if (game != null) {
            if (game.getGameState() != GameState.playing) return;
            if (!game.isAvaibleBlock(block1) || !game.isAvaibleBlock(block2)) {
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void soilChangeEntity(EntityChangeBlockEvent e) {
        if (e.getTo() == Material.DIRT) {
            if (e.getBlock().getType().toString().equals("FARMLAND") || e.getBlock().getType().toString().equals("SOIL")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onLoseHunger(FoodLevelChangeEvent e) {
        e.setCancelled(true);
        e.setFoodLevel(20);
    }

    @EventHandler
    public void onClickBed(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Arena game = GameUtils.getGame(player);
        Block block = e.getClickedBlock();
        if (game == null) return;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (block == null) return;
        if (block.getType() == Material.BED_BLOCK && !player.isSneaking()) {
            e.setCancelled(true);
            return;
        }
        if (block.getType() == Material.CHEST) {
            for (Team team : game.getTeams()) {
                if (!team.isAvailable() || (!team.isHasBed() && game.getPlayersPerTeam(team).isEmpty())) continue;
                if (game.getSpawns().get(team).getLocation(game.getWorld()).distance(block.getLocation()) <= game.getSpawnProtection()) {
                    if (!game.getPlayers().get(player).equals(team)) {
                        player.sendMessage(MessagesUtils.CANT_CHEST.getMessage(player));
                        e.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }
}

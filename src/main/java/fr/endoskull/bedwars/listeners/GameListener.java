package fr.endoskull.bedwars.listeners;

import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.guis.ShopGui;
import fr.endoskull.bedwars.utils.GameUtils;
import fr.endoskull.bedwars.utils.MessagesUtils;
import fr.endoskull.bedwars.utils.ShopCategories;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsLocation;
import fr.endoskull.bedwars.utils.bedwars.Team;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.IMerchant;
import net.minecraft.server.v1_8_R3.InventoryMerchant;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
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
            Block block = e.getBlock();
            if (block.getType() == Material.BED_BLOCK) {
                for (Team team : game.getTeams()) {
                    BedwarsLocation bedTeamLoc = game.getBeds().get(team);
                    Block teamBedBlock = bedTeamLoc.getLocation(game.getWorld()).getBlock();
                    if (teamBedBlock.getType() == Material.BED_BLOCK) {
                        if (teamBedBlock.getLocation().distance(block.getLocation()) <= 1) {
                            if (game.getPlayersPerTeam(team).contains(player)) {
                                e.setCancelled(true);
                                player.sendMessage(MessagesUtils.BREAK_SELF_BED.getMessage(player));
                            } else {
                                player.sendMessage("bravo");
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
            game.getPlacedBlocks().add(e.getBlock());
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        Player player = e.getPlayer();
        if (e.getItem().getType() == Material.POTION) {
            Bukkit.getScheduler().runTaskLater(main, () -> player.getInventory().remove(Material.GLASS_BOTTLE), 3);
        }
    }
}

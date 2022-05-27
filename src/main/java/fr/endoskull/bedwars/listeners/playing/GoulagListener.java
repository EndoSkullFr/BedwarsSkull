package fr.endoskull.bedwars.listeners.playing;

import fr.endoskull.api.spigot.utils.Title;
import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.guis.ShopGui;
import fr.endoskull.bedwars.guis.UpgradesGui;
import fr.endoskull.bedwars.utils.GameState;
import fr.endoskull.bedwars.utils.GameUtils;
import fr.endoskull.bedwars.utils.MessagesUtils;
import fr.endoskull.bedwars.utils.ShopCategories;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsLocation;
import fr.endoskull.bedwars.utils.bedwars.BedwarsPlayer;
import fr.endoskull.bedwars.utils.bedwars.Team;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GoulagListener implements Listener {
    private Main main;
    public GoulagListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {

        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
        if (bwPlayer.isWaitingGoulag()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamageBy(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        Player player = (Player) e.getDamager();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
        if (bwPlayer.isWaitingGoulag()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
        if (bwPlayer.isWaitingGoulag() || bwPlayer.isInGoulag()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
        if (bwPlayer.isWaitingGoulag() || bwPlayer.isInGoulag()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
        if (bwPlayer.isWaitingGoulag() || bwPlayer.isInGoulag()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        Player player = e.getPlayer();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
        if (bwPlayer.isWaitingGoulag() || bwPlayer.isInGoulag()) {
            e.setCancelled(true);
        }
    }
}

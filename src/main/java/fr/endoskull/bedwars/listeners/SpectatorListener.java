package fr.endoskull.bedwars.listeners;

import fr.endoskull.bedwars.utils.GameState;
import fr.endoskull.bedwars.utils.GameUtils;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class SpectatorListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        if (game.getGameState() != GameState.playing) return;
        BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
        if (bwPlayer.isSpectator()) e.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        if (game.getGameState() != GameState.playing) return;
        BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
        if (bwPlayer.isSpectator()) e.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        if (game.getGameState() != GameState.playing) return;
        BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
        if (bwPlayer.isSpectator()) e.setCancelled(true);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        Player player = e.getPlayer();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        if (game.getGameState() != GameState.playing) return;
        BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
        if (bwPlayer.isSpectator()) e.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        if (game.getGameState() != GameState.playing) return;
        BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
        if (bwPlayer.isSpectator()) e.setCancelled(true);
    }


    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        if (game.getGameState() != GameState.playing) return;
        BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
        if (bwPlayer.isSpectator()) e.setCancelled(true);
    }

    @EventHandler
    public void onDamageBy(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        Player player = (Player) e.getDamager();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        if (game.getGameState() != GameState.playing) return;
        BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
        if (bwPlayer.isSpectator()) e.setCancelled(true);
    }
}

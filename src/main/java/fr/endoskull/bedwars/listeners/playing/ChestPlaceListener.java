package fr.endoskull.bedwars.listeners.playing;

import fr.endoskull.bedwars.utils.GameState;
import fr.endoskull.bedwars.utils.GameUtils;
import fr.endoskull.bedwars.utils.MessagesUtils;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsPlayer;
import fr.endoskull.bedwars.utils.tower.TowerEast;
import fr.endoskull.bedwars.utils.tower.TowerNorth;
import fr.endoskull.bedwars.utils.tower.TowerSouth;
import fr.endoskull.bedwars.utils.tower.TowerWest;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class ChestPlaceListener implements Listener {
  @EventHandler(priority = EventPriority.HIGH)
  public void onPlace(BlockPlaceEvent e) {
    Player player = e.getPlayer();
    Arena game = GameUtils.getGame(player);
    if (game == null) return;
    BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
    if (!game.isAvaibleBlock(e.getBlock())) return;
    if (game.getGameState() != GameState.playing) return;
    if (e.getBlockPlaced().getType() == Material.CHEST && 
      bwPlayer.isAlive() &&
      !e.isCancelled()) {
      e.setCancelled(true);
      Location loc = e.getBlockPlaced().getLocation();
      Block chest = e.getBlockPlaced();
      DyeColor col = bwPlayer.getTeam().getColor().dye();
      double rotation = ((player.getLocation().getYaw() - 90.0F) % 360.0F);
      if (rotation < 0.0D)
        rotation += 360.0D; 
      if (45.0D <= rotation && rotation < 135.0D) {
        new TowerSouth(loc, chest, col, player);
      } else if (225.0D <= rotation && rotation < 315.0D) {
        new TowerNorth(loc, chest, col, player);
      } else if (135.0D <= rotation && rotation < 225.0D) {
        new TowerWest(loc, chest, col, player);
      } else if (0.0D <= rotation && rotation < 45.0D) {
        new TowerEast(loc, chest, col, player);
      } else if (315.0D <= rotation && rotation < 360.0D) {
        new TowerEast(loc, chest, col, player);
      } 
    } 
  }
}
package fr.endoskull.bedwars.listeners;

import fr.endoskull.bedwars.utils.GameState;
import fr.endoskull.bedwars.utils.GameUtils;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ClickListener implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        ItemStack item = e.getItem();
        if (item == null) return;
        if (item.getType() == Material.BED) {
            if (game.getGameState() == GameState.waiting || game.getGameState() == GameState.starting || game.getGameState() == GameState.finish) {
                player.kickPlayer("§cRetour au lobby");
            }
        }
        if (item.getType() == Material.PAPER) {
            if (game.getGameState() == GameState.finish) {
                player.kickPlayer("§cBientôt");
            }
        }
        if (item.getType() == Material.COMPASS) {
            if (game.getGameState() == GameState.finish) {
                player.kickPlayer("§cBientôt");
            }
        }
    }
}

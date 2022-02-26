package fr.endoskull.bedwars.listeners;

import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.utils.MapManager;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    private Main main;

    public JoinListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Arena game = MapManager.findAvaibleGame(1);
        if (game != null) {
            System.out.println(game.getLobby());
            System.out.println(game.getWorld());
            player.teleport(game.getLobby().getLocation(game.getWorld()));
            player.sendMessage("§aÇa marche !");
        }
    }
}

package fr.endoskull.bedwars.listeners.playing;

import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.utils.GameState;
import fr.endoskull.bedwars.utils.GameUtils;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {
    private Main main;
    public DeathListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player victim = e.getEntity();
        e.setDeathMessage(null);
        e.setKeepInventory(true);
        e.setKeepLevel(true);
        Arena game = GameUtils.getGame(victim);
        if (game == null) {
            victim.spigot().respawn();
            return;
        }

        if (game.getGameState() != GameState.playing) {
            victim.spigot().respawn();
            return;
        }
        if (!game.getPlayers().containsKey(victim)) {
            victim.spigot().respawn();
            return;
        }
        Team team = game.getPlayers().get(victim);
        if (!team.isHasBed()) {
            /**
             * todo handle goulag
             */
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                victim.spigot().respawn();
                game.getPlayers().remove(victim);
                /**
                 * todo check win
                 * + faire une fonction removePlayer() qui contient le checkwin
                 */
                game.addSpectator(victim);
            }, 3L);
            return;
        }
        game.addRespawning(victim);
    }
}

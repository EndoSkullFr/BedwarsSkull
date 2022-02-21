package fr.endoskull.bedwars.listeners;

import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.utils.ActionBar;
import net.md_5.bungee.api.ChatMessageType;
import net.minecraft.server.v1_8_R3.ChatMessage;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class StartListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage("§a[" + "§2+" + "§a]" + " " + e.getPlayer().getName());
        e.getPlayer().setGameMode(GameMode.ADVENTURE);
        if(Main.getInstance().getServer().getOnlinePlayers().size() < 10) {
            int getAllPlayers = Main.getInstance().getServer().getOnlinePlayers().size();
            ActionBar actionBar = new ActionBar(ChatColor.GREEN + "En attente de joueurs" + "  " + getAllPlayers + "/12");
            actionBar.sendToAll();
        }
        if(Main.getInstance().getServer().getOnlinePlayers().size() > 10) {

        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        e.setQuitMessage("§a[" + "§c+" + "§a]" + " " + e.getPlayer().getName());
        if(Main.getInstance().getServer().getOnlinePlayers().size() < 10) {
            int getAllPlayers = Main.getInstance().getServer().getOnlinePlayers().size();
            ActionBar actionBar = new ActionBar(ChatColor.GREEN + "En attente de joueurs" + "  " + getAllPlayers + "/12");
            actionBar.sendToAll();
        }
    }

    @EventHandler
    public void onDeadOnVoid(EntityDamageEvent e) {
        if(e.getEntity().getType().equals(EntityType.PLAYER)) {
            if(e.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
                e.setCancelled(true);
                Entity entity = e.getEntity();
                entity.teleport(new Location(e.getEntity().getWorld(), 1345.655, 24, -148));
            }
        }
    }
}

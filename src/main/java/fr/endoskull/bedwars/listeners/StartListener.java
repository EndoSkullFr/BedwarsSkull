package fr.endoskull.bedwars.listeners;

import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.utils.ActionBar;
import net.md_5.bungee.api.ChatMessageType;
import net.minecraft.server.v1_8_R3.ChatMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class StartListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage("§a[" + "§2+" + "§a]" + " " + e.getPlayer().getName());
        if(Main.getInstance().getServer().getOnlinePlayers().size() < 10) {
            int getAllPlayers = Main.getInstance().getServer().getOnlinePlayers().size();
            ActionBar actionBar = new ActionBar(ChatColor.GREEN + "En attende de joueur..." + "    " + getAllPlayers + "/12");
            actionBar.sendToAll();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        e.setQuitMessage("§a[" + "§c+" + "§a]" + " " + e.getPlayer().getName());
    }
}

package fr.endoskull.bedwars.listeners;

import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.utils.ActionBar;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StartListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        ItemStack quitI = new ItemStack(Material.BED, 1);
        ItemMeta quitM = quitI.getItemMeta();
        quitM.setDisplayName("§cQuitter");
        quitM.setLore(Arrays.asList("", "§cFaites un clic droit pour quitter le mode de jeux !"));
        quitI.setItemMeta(quitM);
        p.getInventory().clear();
        p.getInventory().setItem(8, quitI);
        ItemStack team = new ItemStack(Material.WOOL, 1);
        ItemMeta teamM = team.getItemMeta();
        teamM.setDisplayName("§fChoisir une équipe");
        teamM.setLore(Arrays.asList("", "§cFaites un clic droit pour quitter le mode de jeux !"));
        team.setItemMeta(teamM);
        p.getInventory().setItem(4, team);
        p.updateInventory();


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
        if(e.getEntity().getType().equals(EntityType.PLAYER)) {
            if(e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                e.setCancelled(true);
            }
        }
    }
}

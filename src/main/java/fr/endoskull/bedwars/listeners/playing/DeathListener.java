package fr.endoskull.bedwars.listeners.playing;

import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.utils.*;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsPlayer;
import fr.endoskull.bedwars.utils.bedwars.LastHit;
import fr.endoskull.bedwars.utils.bedwars.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;

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
        BedwarsPlayer bwVictim = game.getBwPlayerByUUID(victim.getUniqueId());
        if (bwVictim.isRespawning() || bwVictim.isSpectator() || bwVictim.isWaitingGoulag()) {
            victim.spigot().respawn();
            return;
        }
        Team team = bwVictim.getTeam();
        boolean finalKill = !team.isHasBed();
        LastHit lastHit = LastHit.getLastHit(victim);
        DamageType damageType;
        if (lastHit == null) {
            damageType = DamageType.OTHER;
        } else {
            damageType = lastHit.getType();
        }
        Player killer = null;
        BedwarsPlayer bwKiller = null;
        if (lastHit != null && lastHit.getDamager() != null) {
            killer = game.getPlayerByUUID(lastHit.getDamager());
            bwKiller = game.getBwPlayerByUUID(lastHit.getDamager());
        }
        if (killer == null && damageType != DamageType.VOID && damageType != DamageType.OTHER) {
            damageType = DamageType.OTHER;
        }
        HashMap<ShopItems.ShopMaterial, Integer> loots = new HashMap<>();
        for (ShopItems.ShopMaterial value : ShopItems.ShopMaterial.values()) {
            for (int i = 0; i < 36; i++) {
                ItemStack it = victim.getInventory().getItem(i);
                if (it == null) continue;
                if (it.getType() == value.getType()) {
                    if (loots.containsKey(value)) {
                        loots.put(value, loots.get(value) + it.getAmount());
                    } else {
                        loots.put(value, it.getAmount());
                    }
                }
            }
        }
        if (killer != null) {
            for (ShopItems.ShopMaterial shopMaterial : loots.keySet()) {
                killer.sendMessage(MessagesUtils.getMaterial(killer, shopMaterial, 1).substring(0, 2) + "+" + loots.get(shopMaterial) + " " + MessagesUtils.getMaterial(killer, shopMaterial, loots.get(shopMaterial)));
                killer.getInventory().addItem(new ItemStack(shopMaterial.getType(), loots.get(shopMaterial)));
            }
        }
        if (finalKill) {
            Location loc;
            if (killer == null) {
                loc = game.getSpawns().get(bwVictim.getTeam()).getLocation(game.getWorld());
            } else {
                loc = game.getSpawns().get(bwKiller.getTeam()).getLocation(game.getWorld());
            }
            for (ItemStack content : victim.getEnderChest().getContents()) {
                if (content == null) continue;
                Item item = loc.getWorld().dropItem(loc, content);
                item.setVelocity(new Vector(0, 0, 0));
            }
        }
        for (Player p : game.getAllPlayers()) {
            String message = MessagesUtils.getKillMessage(p, damageType).replace("%player%", bwVictim.getTeam().getColor().chat() + victim.getName());
            if (killer != null) {
                message = message.replace("%killer%", bwKiller.getTeam().getColor().chat() + killer.getName());
            }
            if (!team.isHasBed()) message += " §b§lFINAL KILL !";
            p.sendMessage(message);
        }
        bwVictim.incrementDeaths();
        if (bwKiller != null) bwKiller.incrementKills();
        if (!team.isHasBed()) {
            if (bwKiller != null) bwKiller.incrementFinalKills();
        }
        LastHit.getLastHit().remove(victim.getUniqueId());
        if (finalKill && game.isGoulagOpen() && !bwVictim.isWaitingGoulag() && !bwVictim.isInGoulag()) {
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                victim.spigot().respawn();
                game.sendToGoulag(bwVictim);
                for (Player p : game.getAllPlayers()) {
                    p.sendMessage("");
                    p.sendMessage(MessagesUtils.GOULAG_SEND.getMessage(p).replace("{PlayerColor}", bwVictim.getTeam().getColor().chat().toString()).replace("{PlayerName}", victim.getDisplayName()));
                    p.sendMessage("");
                }
            }, 3L);
            return;
        }
        bwVictim.addRespawning();
        if (game.getInGoulag().contains(bwVictim)) {
            game.getInGoulag().remove(bwVictim);
            BedwarsPlayer bwWinner = game.getInGoulag().get(0);
            game.winGoulag(bwWinner);
        }
    }
}

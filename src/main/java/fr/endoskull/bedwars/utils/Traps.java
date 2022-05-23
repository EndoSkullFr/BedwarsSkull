package fr.endoskull.bedwars.utils;

import fr.endoskull.bedwars.tasks.AlarmTrapTask;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsPlayer;
import fr.endoskull.bedwars.utils.bedwars.Team;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public enum Traps {
    ITS_A_TRAP(new ItemStack(Material.TRIPWIRE_HOOK), 38, Traps::playItsATrap),
    COUNTER(new ItemStack(Material.FEATHER), 39, Traps::playCounterTrap),
    ALARM(new ItemStack(Material.REDSTONE_TORCH_ON), 41, Traps::playAlarmTrap),
    MINER_FATIGUE(new ItemStack(Material.IRON_PICKAXE), 42, Traps::playMiningTrap);

    private ItemStack item;
    private int slot;
    private TrapAction action;

    Traps(ItemStack item, int slot, TrapAction action) {
        this.item = item;
        this.slot = slot;
        this.action = action;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getSlot() {
        return slot;
    }

    public TrapAction getAction() {
        return action;
    }

    public static void playItsATrap(List<BedwarsPlayer> players, Team team, Arena game) {
        for (BedwarsPlayer bwPLayer : players) {
            Player player = bwPLayer.getPlayer();
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*8, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*8, 0));
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
        }
    }

    public static void playCounterTrap(List<BedwarsPlayer> players, Team team, Arena game) {
        for (BedwarsPlayer bwPLayer : players) {
            Player player = bwPLayer.getPlayer();
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
        }
        for (BedwarsPlayer bedwarsPlayer : game.getPlayersPerTeam(team)) {
            Player player = bedwarsPlayer.getPlayer();
            if (player == null) continue;
            if (!bedwarsPlayer.isAlive() || bedwarsPlayer.isSpectator() || bedwarsPlayer.isRespawning()) continue;
            if (player.getLocation().distance(game.getSpawns().get(team).getLocation(game.getWorld())) < game.getSpawnProtection() + 10) continue;
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*15, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20*15, 1));
        }
    }

    public static void playAlarmTrap(List<BedwarsPlayer> players, Team team, Arena game) {
        for (BedwarsPlayer bwPLayer : players) {
            Player player = bwPLayer.getPlayer();
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
        new AlarmTrapTask(team, game);
    }
    public static void playMiningTrap(List<BedwarsPlayer> players, Team team, Arena game) {
        for (BedwarsPlayer bwPLayer : players) {
            Player player = bwPLayer.getPlayer();
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20*10, 0));
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
        }
    }

    public interface TrapAction {
        void click(List<BedwarsPlayer> players, Team team, Arena game);
    }
}

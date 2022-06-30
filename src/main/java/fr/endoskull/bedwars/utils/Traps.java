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
    ITS_A_TRAP(new ItemStack(Material.TRIPWIRE_HOOK), 38, Traps::playItsATrap, Traps::initItsATrap),
    COUNTER(new ItemStack(Material.FEATHER), 39, Traps::playCounterTrap, Traps::initCounterTrap),
    ALARM(new ItemStack(Material.REDSTONE_TORCH_ON), 41, Traps::playAlarmTrap, Traps::initAlarmTrap),
    MINER_FATIGUE(new ItemStack(Material.IRON_PICKAXE), 42, Traps::playMiningTrap, Traps::initMiningTrap);

    private ItemStack item;
    private int slot;
    private TrapAction action;
    private TrapAction init;

    Traps(ItemStack item, int slot, TrapAction action, TrapAction init) {
        this.item = item;
        this.slot = slot;
        this.action = action;
        this.init = init;
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

    public TrapAction getInit() {
        return init;
    }


    public static void initItsATrap(List<BedwarsPlayer> players, Team team, Arena game) {

    }

    public static void playItsATrap(List<BedwarsPlayer> players, Team team, Arena game) {
        for (BedwarsPlayer bwPLayer : players) {
            Player player = bwPLayer.getPlayer();
            if (player == null) continue;
            if (player.hasPotionEffect(PotionEffectType.BLINDNESS) && player.hasPotionEffect(PotionEffectType.SLOW)) continue;
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*8, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*8, 0));
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
        }
    }

    public static void initCounterTrap(List<BedwarsPlayer> players, Team team, Arena game) {
        for (BedwarsPlayer bedwarsPlayer : game.getPlayersPerTeam(team)) {
            Player player = bedwarsPlayer.getPlayer();
            if (player == null) continue;
            if (!bedwarsPlayer.isAlive() || bedwarsPlayer.isSpectator() || bedwarsPlayer.isRespawning() || game.getInGoulag().contains(bedwarsPlayer)) continue;
            if (player.getLocation().distance(game.getSpawns().get(team).getLocation(game.getWorld())) > game.getSpawnProtection() + 10 && player.getLocation().distance(game.getBeds().get(team).getLocation(game.getWorld())) > game.getSpawnProtection() + 10) continue;
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*15, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20*15, 1));
        }
        for (BedwarsPlayer bwPLayer : players) {
            if (bwPLayer.getTeam().equals(team)) continue;
            Player player = bwPLayer.getPlayer();
            if (player == null) continue;
            if (!bwPLayer.isAlive() || bwPLayer.isSpectator() || bwPLayer.isRespawning() || game.getInGoulag().contains(bwPLayer)) continue;
            if (player.getLocation().distance(game.getSpawns().get(team).getLocation(game.getWorld())) > game.getSpawnProtection() && player.getLocation().distance(game.getBeds().get(team).getLocation(game.getWorld())) > game.getSpawnProtection()) continue;
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
        }
    }

    public static void playCounterTrap(List<BedwarsPlayer> players, Team team, Arena game) {
        for (BedwarsPlayer bedwarsPlayer : game.getPlayersPerTeam(team)) {
            Player player = bedwarsPlayer.getPlayer();
            if (player == null) continue;
            if (!bedwarsPlayer.isAlive() || bedwarsPlayer.isSpectator() || bedwarsPlayer.isRespawning() || game.getInGoulag().contains(bedwarsPlayer)) continue;
            if (player.getLocation().distance(game.getSpawns().get(team).getLocation(game.getWorld())) > game.getSpawnProtection() + 10 && player.getLocation().distance(game.getBeds().get(team).getLocation(game.getWorld())) > game.getSpawnProtection() + 10) continue;
            if (player.hasPotionEffect(PotionEffectType.SPEED) && player.hasPotionEffect(PotionEffectType.JUMP)) continue;
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*15, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20*15, 1));
        }
    }


    public static void initAlarmTrap(List<BedwarsPlayer> players, Team team, Arena game) {
        new AlarmTrapTask(team, game);
    }

    public static void playAlarmTrap(List<BedwarsPlayer> players, Team team, Arena game) {
        for (BedwarsPlayer bwPLayer : players) {
            if (bwPLayer.getTeam().equals(team)) continue;
            Player player = bwPLayer.getPlayer();
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
    }


    public static void initMiningTrap(List<BedwarsPlayer> players, Team team, Arena game) {
        for (BedwarsPlayer bwPLayer : players) {
            Player player = bwPLayer.getPlayer();
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
        }
    }

    public static void playMiningTrap(List<BedwarsPlayer> players, Team team, Arena game) {
        for (BedwarsPlayer bwPLayer : players) {
            Player player = bwPLayer.getPlayer();
            if (player.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) continue;
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20*10, 0));
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
        }
    }

    public interface TrapAction {
        void click(List<BedwarsPlayer> players, Team team, Arena game);
    }
}

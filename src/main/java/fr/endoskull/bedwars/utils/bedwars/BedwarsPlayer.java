package fr.endoskull.bedwars.utils.bedwars;

import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.tasks.RespawnTask;
import fr.endoskull.bedwars.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BedwarsPlayer {

    private UUID uuid;
    private String name;
    private Team team;
    private boolean isAlive;
    private boolean isRespawning;
    private boolean isSpectator;

    private RespawnTask respawnTask = null;
    private TierTool tierTool;
    private List<ShopItems> alreadyBought;

    private int kill = 0;
    private int death = 0;
    private int finalKill = 0;
    private int bedBroken = 0;
    private int goulagWin = 0;

    private int magicMilk = 0;

    private Arena game;

    public BedwarsPlayer(Player player, Team team, boolean isAlive, boolean isRespawning, boolean isSpectator, Arena game) {
        this.uuid = player.getUniqueId();
        this.name = player.getDisplayName();
        this.team = team;
        this.isAlive = isAlive;
        this.isRespawning = isRespawning;
        this.isSpectator = isSpectator;
        this.tierTool = new TierTool(player);
        this.alreadyBought = new ArrayList<>();
        this.game = game;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Team getTeam() {
        return team;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public boolean isRespawning() {
        return isRespawning;
    }

    public boolean isSpectator() {
        return isSpectator;
    }

    public int getKill() {
        return kill;
    }

    public int getDeath() {
        return death;
    }

    public int getFinalKill() {
        return finalKill;
    }

    public int getGoulagWin() {
        return goulagWin;
    }

    public void incrementKills() {
        kill++;
    }
    public void incrementBedBroken() {
        bedBroken++;
    }

    public void incrementDeaths() {
        death++;
    }

    public void incrementFinalKills() {
        finalKill++;
    }

    public void incrementGoulagWin() {
        goulagWin++;
    }

    public RespawnTask getRespawnTask() {
        return respawnTask;
    }

    public void setRespawnTask(RespawnTask respawnTask) {
        this.respawnTask = respawnTask;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public TierTool getTierTool() {
        return tierTool;
    }

    public List<ShopItems> getAlreadyBought() {
        return alreadyBought;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void setRespawning(boolean respawning) {
        isRespawning = respawning;
    }

    public void setSpectator(boolean spectator) {
        isSpectator = spectator;
    }

    public boolean isWaitingGoulag() {
        return game.getWaitingGoulag().contains(this);
    }

    public boolean isInGoulag() {
        return game.getInGoulag().contains(this);
    }

    public void reset() {
        Player player = getPlayer();
        if (player == null) return;
        setRespawnTask(null);
        setRespawning(false);

        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.getInventory().setHelmet(new CustomItemStack(Material.LEATHER_HELMET).setLeatherColor(team.getColor().bukkitColor()).setUnbreakable());
        player.getInventory().setChestplate(new CustomItemStack(Material.LEATHER_CHESTPLATE).setLeatherColor(team.getColor().bukkitColor()).setUnbreakable());
        player.getInventory().setLeggings(new CustomItemStack(Material.LEATHER_LEGGINGS).setLeatherColor(team.getColor().bukkitColor()).setUnbreakable());
        player.getInventory().setBoots(new CustomItemStack(Material.LEATHER_BOOTS).setLeatherColor(team.getColor().bukkitColor()).setUnbreakable());
        player.getInventory().addItem(new CustomItemStack(Material.WOOD_SWORD).setUnbreakable());
        player.teleport(game.getSpawns().get(team).getLocation(game.getWorld()));
        player.setMaxHealth(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setExp(0);
        player.setFlying(false);
        player.setAllowFlight(false);
        player.setFallDistance(0);
        player.getInventory().setHeldItemSlot(0);
        game.getInvincibility().put(this, System.currentTimeMillis() + 3000);
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
        for (ShopItems shopItems : alreadyBought) {
            if (!shopItems.getFamily().equalsIgnoreCase("")) {
                if (tierTool.getUpgrades().containsKey(shopItems.getFamily())) {
                    ShopItems max = ShopItems.getFromFamily(this, shopItems.getFamily(), tierTool.getUpgrades().get(shopItems.getFamily()));
                    if (!max.isPermanent()) {
                        if (max.getTier() == 1) {
                            continue;
                        } else {
                            tierTool.getUpgrades().put(shopItems.getFamily(), max.getTier() - 1);
                            shopItems = ShopItems.getFromFamily(this, shopItems.getFamily(), max.getTier() - 1);
                        }
                    }
                }
            }
            if (shopItems.isArmor()) {
                shopItems.giveArmor(player);
            } else {
                if (shopItems.getItem(player).getType().toString().contains("SWORD")) {
                    player.getInventory().remove(Material.WOOD_SWORD);
                }
                player.getInventory().addItem(shopItems.getItem(player));
            }
        }
        checkUpgrades();
    }

    public void checkUpgrades() {
        Player player = getPlayer();
        if (player == null) return;
        if (isSpectator || !isAlive || isRespawning) return;
        PlayerInventory inv = player.getInventory();
        if (!isInGoulag() && !isWaitingGoulag() && !hasItem(player, Material.WOOD_SWORD) && !hasItem(player, Material.GOLD_SWORD) && !hasItem(player, Material.STONE_SWORD) && !hasItem(player, Material.IRON_SWORD) && !hasItem(player, Material.DIAMOND_SWORD)) {
            player.getInventory().addItem(new CustomItemStack(Material.WOOD_SWORD).setUnbreakable());
        } else if ((hasItem(player, Material.GOLD_SWORD) || hasItem(player, Material.STONE_SWORD) || hasItem(player, Material.IRON_SWORD) || hasItem(player, Material.DIAMOND_SWORD)) && hasItem(player, Material.WOOD_SWORD)) {
            inv.remove(Material.WOOD_SWORD);
        }
        if (team == null) return;
        for (int i = 0; i < 36; i++) {
            ItemStack it = player.getInventory().getItem(i);
            if (it == null) continue;
            if (it.getType().toString().contains("SWORD")) {
                it.removeEnchantment(Enchantment.DAMAGE_ALL);
                if (team.getUpgrades().getMap().containsKey(Upgrades.SHARPNESS)) {
                    int level = team.getUpgrades().getMap().get(Upgrades.SHARPNESS);
                    player.getInventory().setItem(i, new CustomItemStack(it).addCustomEnchantment(Enchantment.DAMAGE_ALL, level));
                }
            }
        }

        ItemStack helmet = player.getInventory().getHelmet();
        if (helmet != null) helmet.removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
        ItemStack chestplate = player.getInventory().getChestplate();
        if (chestplate != null) chestplate.removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
        ItemStack leggings = player.getInventory().getLeggings();
        if (leggings != null) leggings.removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
        ItemStack boots = player.getInventory().getBoots();
        if (boots != null) boots.removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);

        if (team.getUpgrades().getMap().containsKey(Upgrades.PROTECTION) && !isInGoulag()) {
            int level = team.getUpgrades().getMap().get(Upgrades.PROTECTION);
            if (helmet != null) player.getInventory().setHelmet(new CustomItemStack(helmet).addCustomEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, level));
            if (chestplate != null) player.getInventory().setChestplate(new CustomItemStack(chestplate).addCustomEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, level));
            if (leggings != null) player.getInventory().setLeggings(new CustomItemStack(leggings).addCustomEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, level));
            if (boots != null) player.getInventory().setBoots(new CustomItemStack(boots).addCustomEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, level));
        }

        if (team.getUpgrades().getMap().containsKey(Upgrades.HASTE)) {
            int level = team.getUpgrades().getMap().get(Upgrades.HASTE) - 1;
            player.removePotionEffect(PotionEffectType.FAST_DIGGING);
            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, level, false, false));
        }
    }

    private static boolean hasItem(Player player, Material material) {
        PlayerInventory inv = player.getInventory();
        if (inv.contains(material)) return true;
        if (player.getItemOnCursor() != null && player.getItemOnCursor().getType() == material) return true;
        return false;
    }

    public void addSpectator() {
        isSpectator = true;
        isAlive = false;
        isRespawning = false;
        Player player = getPlayer();
        if (player == null) return;
        player.teleport(game.getLobby().getLocation(game.getWorld()));
        player.setAllowFlight(true);
        player.setFlying(true);
        InventoryUtils.setSpectateInv(player, true);
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
        for (BedwarsPlayer gamePlayer : game.getPlayers()) {
            Player p = gamePlayer.getPlayer();
            if (p == null) continue;
            if (gamePlayer.isSpectator()) {
                player.showPlayer(p);
                p.showPlayer(player);
            } else {
                p.hidePlayer(player);
            }
        }
    }

    public void addGoulagWaiting() {
        Player player = getPlayer();
        if (player == null) return;
        player.teleport(game.getLobby().getLocation(game.getWorld()));
        player.setAllowFlight(true);
        player.setFlying(true);
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
    }

    public void addRespawning() {
        Player victim = getPlayer();

        if (victim == null) return;
        victim.getInventory().clear();
        victim.getInventory().setArmorContents(new ItemStack[4]);
        for (PotionEffect potionEffect : victim.getActivePotionEffects()) {
            victim.removePotionEffect(potionEffect.getType());
        }
        victim.setAllowFlight(true);
        victim.setFlying(true);
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            victim.spigot().respawn();
            if (getPlayer() == null) return;
            for (BedwarsPlayer bwPlayer : game.getPlayers()) {
                Player player = bwPlayer.getPlayer();
                if (player == null) continue;
                player.hidePlayer(getPlayer());
            }
            victim.teleport(game.getLobby().getLocation(game.getWorld()));
        }, 3L);
        if (team.isHasBed()) {
            respawnTask = new RespawnTask(this, game, 5);
            respawnTask.runTaskTimer(Main.getInstance(), 5, 20);
        } else {
            addSpectator();
            game.checkTeam(team);
        }
    }

    public int getMagicMilk() {
        return magicMilk;
    }

    public void setMagicMilk(int magicMilk) {
        this.magicMilk = magicMilk;
    }

    public int getBedBroken() {
        return bedBroken;
    }

    public void saveStats() {
        Account account = AccountProvider.getAccount(uuid);
        account.incrementStatistic("bedwars/kill", kill);
        kill = 0;
        account.incrementStatistic("bedwars/finalkill", finalKill);
        finalKill = 0;
        account.incrementStatistic("bedwars/death", death);
        death = 0;
        account.incrementStatistic("bedwars/bedbroken", bedBroken);
        bedBroken = 0;
        account.incrementStatistic("bedwars/goulagwin", goulagWin);
        goulagWin = 0;
    }

    public void addGamePlayed() {
        Account account = AccountProvider.getAccount(uuid);
        account.incrementStatistic("bedwars/gameplayed", 1);
    }

    public void addWin() {
        Account account = AccountProvider.getAccount(uuid);
        account.incrementStatistic("bedwars/win", 1);
    }
}

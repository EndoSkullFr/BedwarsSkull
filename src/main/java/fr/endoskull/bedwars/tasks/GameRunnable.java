package fr.endoskull.bedwars.tasks;

import fr.endoskull.api.spigot.utils.Hologram;
import fr.endoskull.api.spigot.utils.Title;
import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.utils.*;
import fr.endoskull.bedwars.utils.bedwars.*;
import fr.endoskull.bedwars.utils.mobs.Despawnable;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.team.TeamManager;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GameRunnable extends BukkitRunnable {
    private Main main;
    private HashMap<Integer, String> startTitle = new HashMap<Integer, String>() {{
        put(5, "§a➄");
        put(4, "§a➃");
        put(3, "§e➂");
        put(2, "§6➁");
        put(1, "§c➀");
    }};

    private Integer[] startText = {60, 30, 15, 5, 4, 3, 2, 1};
    public GameRunnable(Main main) {
        this.main = main;
    }

    @Override
    public void run() {
        for (Arena game : main.getGames()) {
            if (game.getGameState() == GameState.starting) {
                game.setStartTimer(game.getStartTimer() - 1);
                if (game.getStartTimer() <= 0) {
                    game.start();
                } else {
                    if (Arrays.asList(startText).contains(game.getStartTimer())) {
                        for (Player player : game.getIngamePlayers()) {
                            player.sendMessage(MessagesUtils.STARTING.getMessage(player).replace("{time}", String.valueOf(game.getStartTimer())).replace("{s}", game.getStartTimer() > 1 ? "s" : ""));
                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 1f);
                        }
                    }
                    if (startTitle.containsKey(game.getStartTimer())) {
                        for (Player player : game.getIngamePlayers()) {
                            Title.sendTitle(player, 5, 15, 5, startTitle.get(game.getStartTimer()), "");
                        }
                    }
                }
            }
            else if (game.getGameState() == GameState.playing) {
                /*for (Player player : game.getIngamePlayers()) {
                    LastHit lastHit = LastHit.getLastHit(player);
                    if (lastHit == null || lastHit.getTime() < System.currentTimeMillis() - 5000) {
                        if (player.getHealth() >= player.getMaxHealth() - 1) {
                            player.setHealth(player.getMaxHealth());
                        } else {
                            player.setHealth(player.getHealth() + 1);
                        }
                    }
                }*/
                if (game.getGoulagTimer() > 0 && game.isGoulagOpen()) {
                    game.setGoulagTimer(game.getGoulagTimer() - 1);
                    if (game.getGoulagTimer() == 0) {
                        game.closeGoulag();
                    }
                }
                for (Team team : game.getTeams()) {
                    if (team.getUpgrades().getMap().containsKey(Upgrades.FORGE)) {
                        int level = team.getUpgrades().getMap().get(Upgrades.FORGE);
                        if (level >= 3) {
                            if (game.getEmeraldAtBaseTimer().containsKey(team)) {
                                game.getEmeraldAtBaseTimer().put(team, game.getEmeraldAtBaseTimer().get(team) + 1);
                            } else {
                                game.getEmeraldAtBaseTimer().put(team, 0);
                            }
                            if (game.getEmeraldAtBaseTimer().get(team) == ConfigUtils.getEmeraldAtBaseTimer()) {
                                game.getEmeraldAtBaseTimer().put(team, 0);
                            }
                            if (game.getEmeraldAtBaseTimer().get(team) == 0) {
                                int maxAmount = 4;
                                int amount = 0;
                                for (Entity ent : game.getAsGenerators().get(team).getNearbyEntities(3, 3, 3)) {
                                    if (ent instanceof Item) {
                                        Item item = (Item) ent;
                                        if (item.getItemStack().getType() == ShopItems.ShopMaterial.emerald.getType()) {
                                            amount += item.getItemStack().getAmount();
                                        }
                                    }
                                }
                                if (amount < maxAmount) {
                                    double genAmount = 1;
                                    if (level == 4) genAmount = 2;
                                    Item item = game.getWorld().dropItem(game.getGenerators().get(team).getLocation(game.getWorld()), new ItemStack(ShopItems.ShopMaterial.emerald.getType(), (int) genAmount));
                                    item.setVelocity(new Vector(0, 0, 0));
                                    game.getSplitItems().add(item);
                                }
                            }
                        }
                    }
                    if (!team.getUpgrades().getTraps().isEmpty()) {
                        for (Player pls : game.getIngamePlayers()) {
                            if (pls.getLocation().distance(game.getSpawns().get(team).getLocation(game.getWorld())) < game.getSpawnProtection() || pls.getLocation().distance(game.getBeds().get(team).getLocation(game.getWorld())) < game.getSpawnProtection()) {
                                BedwarsPlayer bwP = game.getBwPlayerByUUID(pls.getUniqueId());
                                if (bwP.getTeam().equals(team)) continue;
                                List<BedwarsPlayer> players = new ArrayList<>();
                                int radius = game.getSpawnProtection() + 10;
                                List<Entity> entities = new ArrayList<>(game.getWorld().getNearbyEntities(game.getSpawns().get(team).getLocation(game.getWorld()), radius, radius, radius));
                                for (Entity ent : game.getWorld().getNearbyEntities(game.getBeds().get(team).getLocation(game.getWorld()), radius, radius, radius)) {
                                    if (!entities.contains(ent)) entities.add(ent);
                                }

                                for (Entity ent : entities) {
                                    if (ent instanceof Player) {
                                        Player player = (Player) ent;
                                        if (game.getIngamePlayers().contains(player)) {
                                            BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
                                            if (bwPlayer.getTeam().equals(team)) continue;
                                            if (bwPlayer.getMagicMilk() > 0) continue;
                                            players.add(bwPlayer);
                                        }
                                    }
                                }
                                if (!players.isEmpty()) {
                                    boolean alertTrap = false;
                                    for (Traps value : Traps.values()) {
                                        if (team.getUpgrades().getTraps().contains(value)) {
                                            value.getAction().click(players, team, game);
                                            if (!team.getUpgrades().getWaitingRemove().contains(value)) {
                                                alertTrap = true;
                                                value.getInit().click(players, team, game);
                                                team.getUpgrades().getWaitingRemove().add(value);
                                                Bukkit.getScheduler().runTaskLater(main, () -> {
                                                    team.getUpgrades().getTraps().remove(value);
                                                    team.getUpgrades().getWaitingRemove().remove(value);
                                                }, 20 * 10);
                                            }
                                        }
                                    }
                                    if (alertTrap) {
                                        for (BedwarsPlayer bedwarsPlayer : game.getPlayersPerTeam(team)) {
                                            Player player = bedwarsPlayer.getPlayer();
                                            if (player == null) continue;
                                            if (bedwarsPlayer.isAlive()) {
                                                Title.sendTitle(player, 5, 40, 5, MessagesUtils.TRAP_TITLE.getMessage(player), "");
                                                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                for (ShopItems.ShopMaterial value : ShopItems.ShopMaterial.values()) {
                    if (!value.isSpawningAtBase()) continue;
                    if (game.getTimer() % ConfigUtils.getGeneratorTimer(value) == 0) {
                        game.getGenerators().forEach((team, bedwarsLocation) -> {
                            int maxAmount = ConfigUtils.getGeneratorLimit(value);
                            int amount = 0;
                            for (Entity ent : game.getAsGenerators().get(team).getNearbyEntities(3, 3, 3)) {
                                if (ent instanceof Item) {
                                    Item item = (Item) ent;
                                    if (item.getItemStack().getType() == value.getType()) {
                                        amount += item.getItemStack().getAmount();
                                    }
                                }
                            }
                            if (amount < maxAmount) {
                                double genAmount = ConfigUtils.getGeneratorAmount(value);
                                double multiplier = 1;
                                if (team.getUpgrades().getMap().containsKey(Upgrades.FORGE)) {
                                    int level = team.getUpgrades().getMap().get(Upgrades.FORGE);
                                    if (level == 1) multiplier = 1.5;
                                    if (level == 2) multiplier = 2;
                                    if (level == 4) multiplier = 3;
                                }
                                genAmount *= multiplier;
                                if (game.getGenRest().contains(value)) {
                                    genAmount += 0.5;
                                    game.getGenRest().remove(value);
                                }
                                if (genAmount != (int) genAmount) {
                                    game.getGenRest().add(value);
                                }
                                Item item = game.getWorld().dropItem(bedwarsLocation.getLocation(game.getWorld()), new ItemStack(value.getType(), (int) genAmount));
                                item.setVelocity(new Vector(0, 0, 0));
                                game.getSplitItems().add(item);
                            }
                        });
                    }
                }
                if (game.getEventTimer() > 0) game.setEventTimer(game.getEventTimer() - 1);
                if (game.getEventTimer() == 0) {
                    game.nextEvent();
                }
                game.setTimer(game.getTimer() + 1);
                game.setDiamondTimer(game.getDiamondTimer() - 1);
                game.setEmeraldTimer(game.getEmeraldTimer() - 1);
                if (game.getDiamondTimer() == 0) {
                    game.setDiamondTimer(ConfigUtils.getTieredGeneratorTimer(ShopItems.ShopMaterial.diamond, game.getDiamondTier()));
                    int maxAmount = ConfigUtils.getTieredGeneratorLimit(ShopItems.ShopMaterial.diamond, game.getDiamondTier());
                    for (BedwarsLocation diamondGenerator : game.getDiamondGenerators()) {
                        int amount = 0;
                        for (Entity ent : game.getAsDiamonds().get(diamondGenerator).getNearbyEntities(3, 3, 3)) {
                            if (ent instanceof Item) {
                                Item item = (Item) ent;
                                if (item.getItemStack().getType() == ShopItems.ShopMaterial.diamond.getType()) {
                                    amount += item.getItemStack().getAmount();
                                }
                            }
                        }
                        if (amount < maxAmount) {
                            Item item =game.getWorld().dropItem(diamondGenerator.getLocation(game.getWorld()), new ItemStack(ShopItems.ShopMaterial.diamond.getType(), 1));
                            item.setVelocity(new Vector(0, 0, 0));
                        }
                    }
                }
                if (game.getEmeraldTimer() == 0) {
                    game.setEmeraldTimer(ConfigUtils.getTieredGeneratorTimer(ShopItems.ShopMaterial.emerald, game.getEmeraldTier()));
                    int maxAmount = ConfigUtils.getTieredGeneratorLimit(ShopItems.ShopMaterial.emerald, game.getEmeraldTier());
                    for (BedwarsLocation emeraldGenerator : game.getEmeraldGenerators()) {
                        int amount = 0;
                        for (Entity ent : game.getAsEmeralds().get(emeraldGenerator).getNearbyEntities(3, 3, 3)) {
                            if (ent instanceof Item) {
                                Item item = (Item) ent;
                                if (item.getItemStack().getType() == ShopItems.ShopMaterial.emerald.getType()) {
                                    amount += item.getItemStack().getAmount();
                                }
                            }
                        }
                        if (amount < maxAmount) {
                            Item item = game.getWorld().dropItem(emeraldGenerator.getLocation(game.getWorld()), new ItemStack(ShopItems.ShopMaterial.emerald.getType(), 1));
                            item.setVelocity(new Vector(0, 0, 0));
                        }
                    }
                }
                for (BedwarsLocation bedwarsLocation : game.getDiamondsHologram().keySet()) {
                    Hologram hologram = game.getDiamondsHologram().get(bedwarsLocation);
                    hologram.setLines("§eTier §c" + ConfigUtils.integerToRoman(game.getDiamondTier()), "§b§lDiamant", "§eSpawn dans §c" + game.getDiamondTimer() + " §eseconde" + (game.getDiamondTimer() > 1 ? "s" : ""));
                }
                for (BedwarsLocation bedwarsLocation : game.getEmeraldsHologram().keySet()) {
                    Hologram hologram = game.getEmeraldsHologram().get(bedwarsLocation);
                    hologram.setLines("§eTier §c" + ConfigUtils.integerToRoman(game.getEmeraldTier()), "§2§lÉmeraude", "§eSpawn dans §c" + game.getEmeraldTimer() + " §eseconde" + (game.getEmeraldTimer() > 1 ? "s" : ""));
                }

                for (Player player : game.getIngamePlayers()) {
                    LastHit lastHit = LastHit.getLastHit(player);
                    if (lastHit != null) {
                        if (lastHit.getTime() < System.currentTimeMillis() - 30000) {
                            lastHit.remove();
                        }
                    }
                    BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
                    if (bwPlayer.getMagicMilk() > 0) {
                        bwPlayer.setMagicMilk(bwPlayer.getMagicMilk() - 1);
                        if (bwPlayer.getMagicMilk() == 0) {
                            player.sendMessage(MessagesUtils.MAGIC_MILK_END.getMessage(player));
                        }
                    }
                    if (player.getActivePotionEffects().stream().filter(potionEffect -> potionEffect.getType().equals(PotionEffectType.INVISIBILITY)).findFirst().orElse(null) != null) {
                        /*if (player.getPassenger() == null || !(player.getPassenger() instanceof Pig)) {
                            Pig as = player.getWorld().spawn(player.getLocation().clone().add(0, 3, 0), Pig.class);
                            //as.setVisible(false);
                            //as.setMarker(true);
                            player.setPassenger(as);
                        }*/
                        TabAPI tabAPI = TabAPI.getInstance();
                        TabPlayer tabPlayer = tabAPI.getPlayer(player.getUniqueId());
                        TeamManager teamManager = tabAPI.getTeamManager();
                        teamManager.hideNametag(tabPlayer);
                        for (Player receiver : game.getIngamePlayers()) {
                            BedwarsPlayer bwReceiver = game.getBwPlayerByUUID(receiver.getUniqueId());
                            if (bwPlayer.getTeam().equals(bwReceiver.getTeam())) continue;
                            NmsUtils.hideArmor(player, receiver);
                        }
                    } else {
                        /*if (player.getPassenger() != null || player.getPassenger() instanceof Pig) {
                            Pig as = (Pig) player.getPassenger();
                            as.remove();
                        }*/
                        TabAPI tabAPI = TabAPI.getInstance();
                        TabPlayer tabPlayer = tabAPI.getPlayer(player.getUniqueId());
                        TeamManager teamManager = tabAPI.getTeamManager();
                        teamManager.showNametag(tabPlayer);
                        for (Player receiver : game.getIngamePlayers()) {
                            BedwarsPlayer bwReceiver = game.getBwPlayerByUUID(receiver.getUniqueId());
                            if (bwPlayer.getTeam().equals(bwReceiver.getTeam())) continue;
                            NmsUtils.showArmor(player, receiver);
                        }
                    }
                }
                for (BedwarsPlayer bwPlayer : game.getPlayers()) {
                    Player player = bwPlayer.getPlayer();
                    if (player == null) continue;
                    if (!bwPlayer.isRespawning() && !bwPlayer.isSpectator()) {
                        bwPlayer.checkUpgrades();
                        Team team = bwPlayer.getTeam();
                        if (team.getUpgrades().getMap().containsKey(Upgrades.HEAL)) {
                            int level = team.getUpgrades().getMap().get(Upgrades.HEAL);
                            if (player.getLocation().distance(game.getSpawns().get(team).getLocation(game.getWorld()) )< game.getBaseRadius() || player.getLocation().distance(game.getBeds().get(team).getLocation(game.getWorld()) )< game.getBaseRadius()) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, level - 1));
                            } else {
                                player.removePotionEffect(PotionEffectType.REGENERATION);
                            }
                        }
                    }
                }
            }
        }

        for (Despawnable d : Despawnable.getDespawnableMap().values()){
            d.refresh();
        }
    }
}

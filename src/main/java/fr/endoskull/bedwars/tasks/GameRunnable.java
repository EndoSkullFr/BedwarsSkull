package fr.endoskull.bedwars.tasks;

import fr.endoskull.api.spigot.utils.Hologram;
import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.utils.ConfigUtils;
import fr.endoskull.bedwars.utils.GameState;
import fr.endoskull.bedwars.utils.ShopItems;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsLocation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class GameRunnable extends BukkitRunnable {
    private Main main;

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
                }
            }
            else if (game.getGameState() == GameState.playing) {
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
                                game.getWorld().dropItemNaturally(bedwarsLocation.getLocation(game.getWorld()), new ItemStack(value.getType(), ConfigUtils.getGeneratorAmount(value)));
                            }
                        });
                    }
                }
                game.setEventTimer(game.getEventTimer() - 1);
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
                            game.getWorld().dropItemNaturally(diamondGenerator.getLocation(game.getWorld()), new ItemStack(ShopItems.ShopMaterial.diamond.getType(), 1));
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
                            game.getWorld().dropItemNaturally(emeraldGenerator.getLocation(game.getWorld()), new ItemStack(ShopItems.ShopMaterial.emerald.getType(), 1));
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

                for (Player player : game.getPlayers().keySet()) {
                    if (player.getActivePotionEffects().stream().filter(potionEffect -> potionEffect.getType().equals(PotionEffectType.INVISIBILITY)).findFirst().orElse(null) != null) {
                        PacketPlayOutEntityEquipment helmetPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 1, null);
                        PacketPlayOutEntityEquipment chestPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 2, null);
                        PacketPlayOutEntityEquipment legPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 3, null);
                        PacketPlayOutEntityEquipment bootsPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 4, null);
                        for (Player receiver : game.getPlayers().keySet()) {
                            if (game.getPlayersPerTeam(game.getPlayers().get(player)).contains(receiver)) continue;
                            ((CraftPlayer)receiver).getHandle().playerConnection.sendPacket(helmetPacket);
                            ((CraftPlayer)receiver).getHandle().playerConnection.sendPacket(chestPacket);
                            ((CraftPlayer)receiver).getHandle().playerConnection.sendPacket(legPacket);
                            ((CraftPlayer)receiver).getHandle().playerConnection.sendPacket(bootsPacket);
                        }
                    } else {
                        PacketPlayOutEntityEquipment helmetPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 1, CraftItemStack.asNMSCopy(player.getInventory().getHelmet()));
                        PacketPlayOutEntityEquipment chestPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 2, CraftItemStack.asNMSCopy(player.getInventory().getChestplate()));
                        PacketPlayOutEntityEquipment legPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 3, CraftItemStack.asNMSCopy(player.getInventory().getLeggings()));
                        PacketPlayOutEntityEquipment bootsPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 4, CraftItemStack.asNMSCopy(player.getInventory().getBoots()));
                        for (Player receiver : game.getPlayers().keySet()) {
                            ((CraftPlayer)receiver).getHandle().playerConnection.sendPacket(helmetPacket);
                            ((CraftPlayer)receiver).getHandle().playerConnection.sendPacket(chestPacket);
                            ((CraftPlayer)receiver).getHandle().playerConnection.sendPacket(legPacket);
                            ((CraftPlayer)receiver).getHandle().playerConnection.sendPacket(bootsPacket);
                        }
                    }
                }
            }
        }
    }
}

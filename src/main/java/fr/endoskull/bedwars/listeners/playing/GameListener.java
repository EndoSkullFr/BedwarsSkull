package fr.endoskull.bedwars.listeners.playing;

import fr.endoskull.api.spigot.utils.Title;
import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.guis.ShopGui;
import fr.endoskull.bedwars.guis.UpgradesGui;
import fr.endoskull.bedwars.utils.*;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsLocation;
import fr.endoskull.bedwars.utils.bedwars.BedwarsPlayer;
import fr.endoskull.bedwars.utils.bedwars.Team;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.IMerchant;
import net.minecraft.server.v1_8_R3.InventoryMerchant;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Bed;

import java.lang.reflect.Field;

public class GameListener implements Listener {
    private Main main;
    public GameListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onEntClick(PlayerInteractAtEntityEvent e) {
        Player player = e.getPlayer();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        e.setCancelled(true);
        BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
        if (bwPlayer.isSpectator() || bwPlayer.isRespawning()) return;
        Entity ent = e.getRightClicked();
        for (Team team : game.getShops().keySet()) {
            if (ent.getLocation().equals(game.getShops().get(team).getLocation(game.getWorld()))) {
                Bukkit.getScheduler().runTaskLater(main, () ->  {
                    new ShopGui(ShopCategories.QUICK_BUY, player).open(player);
                }, 1);
            }
            if (ent.getLocation().equals(game.getUpgrades().get(team).getLocation(game.getWorld()))) {
                Bukkit.getScheduler().runTaskLater(main, () ->  {
                    new UpgradesGui(player).open(player);
                }, 1);
            }
        }
    }

    @EventHandler
    public void villDisableTrade(InventoryOpenEvent e) {
        if(e.getInventory().getType() == InventoryType.MERCHANT) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Arena game = GameUtils.getGame(player);
        if (game != null) {
            BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
            if (bwPlayer.isSpectator() || bwPlayer.isRespawning()) return;
            if (game.getGameState() != GameState.playing) return;
            Block block = e.getBlock();
            if (block.getType() == Material.BED_BLOCK) {
                for (Team team : game.getTeams()) {
                    if (!team.isAvailable() || !team.isHasBed()) continue;
                    BedwarsLocation bedTeamLoc = game.getBeds().get(team);
                    Block teamBedBlock = bedTeamLoc.getLocation(game.getWorld()).getBlock();
                    if (teamBedBlock.getType() == Material.BED_BLOCK) {
                        if (teamBedBlock.getLocation().distance(block.getLocation()) <= 1) {
                            if (bwPlayer.getTeam().equals(team)) {
                                e.setCancelled(true);
                                player.sendMessage(MessagesUtils.BREAK_SELF_BED.getMessage(player));
                                if (e.getPlayer().getLocation().getBlock().getType().toString().contains("BED")) {
                                    e.getPlayer().teleport(e.getPlayer().getLocation().add(0, 0.5, 0));
                                }
                            } else {
                                e.setCancelled(true);
                                block.setType(Material.AIR);
                                team.setHasBed(false);
                                for (Player p : game.getAllPlayers()) {
                                    p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
                                    String message;
                                    BedwarsPlayer bwP = game.getBwPlayerByUUID(p.getUniqueId());
                                    if (bwP != null && bwP.getTeam().equals(team)) {
                                        message = MessagesUtils.BED_BREAK_VICTIM.getMessage(p);
                                        Title.sendTitle(p, 10, 40, 10, MessagesUtils.BED_BREAK_TITLE.getMessage(p), MessagesUtils.BED_BREAK_SUBTITLE.getMessage(p));
                                    } else {
                                        message = MessagesUtils.BED_BREAK.getMessage(p);
                                    }
                                    message = message.replace("{TeamColor}", team.getColor().chat().toString()).replace("{TeamName}", MessagesUtils.getTeamDisplayName(p, team.getName()))
                                            .replace("{PlayerColor}", bwPlayer.getTeam().getColor().chat().toString()).replace("{PlayerName}", player.getDisplayName());
                                    p.sendMessage(message);
                                }

                            }
                        }
                    }
                }
            } else if (!game.getPlacedBlocks().contains(block)) {
                e.setCancelled(true);
                player.sendMessage(MessagesUtils.BREAK_BLOCK.getMessage(player));
            } else {
                game.getPlacedBlocks().remove(block);
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Arena game = GameUtils.getGame(player);
        if (game != null) {
            if (game.getGameState() != GameState.playing) return;
            if (e.getBlock().getY() > game.getHeightLimit()) {
                e.setCancelled(true);
                player.sendMessage(MessagesUtils.HEIGH_LIMIT.getMessage(player));
                return;
            }
            if (!game.isAvaibleBlock(e.getBlock())) {
                e.setCancelled(true);
                player.sendMessage(MessagesUtils.CANT_PLACE_HERE.getMessage(player));
                return;
            }
            game.getPlacedBlocks().add(e.getBlock());
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        Player player = e.getPlayer();
        if (e.getItem().getType() == Material.POTION) {
            Bukkit.getScheduler().runTaskLater(main, () -> player.getInventory().remove(Material.GLASS_BOTTLE), 3);
        }
        if (e.getItem().getType() == Material.MILK_BUCKET) {
            Bukkit.getScheduler().runTaskLater(main, () -> player.getInventory().remove(Material.BUCKET), 3);
        }
    }

    @EventHandler
    public void onBucket(PlayerBucketEmptyEvent e) {
        Player player = e.getPlayer();
        Arena game = GameUtils.getGame(player);
        Block block = e.getBlockClicked().getRelative(e.getBlockFace());
        if (game != null) {
            if (game.getGameState() != GameState.playing) return;
            if (block.getY() > game.getHeightLimit()) {
                e.setCancelled(true);
                player.sendMessage(MessagesUtils.HEIGH_LIMIT.getMessage(player));
                return;
            }
            if (!game.isAvaibleBlock(block)) {
                e.setCancelled(true);
                player.sendMessage(MessagesUtils.CANT_PLACE_HERE.getMessage(player));
                return;
            }
            Bukkit.getScheduler().runTaskLater(main, () -> player.getInventory().remove(Material.BUCKET), 3);
        }
    }

    @EventHandler
    public void onPhysic(BlockFromToEvent e) {
        Block block1 = e.getBlock();
        Block block2 = e.getToBlock();
        Arena game = GameUtils.getGame(block1.getWorld());
        if (game != null) {
            if (game.getGameState() != GameState.playing) return;
            if (!game.isAvaibleBlock(block1) || !game.isAvaibleBlock(block2)) {
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void soilChangeEntity(EntityChangeBlockEvent e) {
        if (e.getTo() == Material.DIRT) {
            if (e.getBlock().getType().toString().equals("FARMLAND") || e.getBlock().getType().toString().equals("SOIL")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onLoseHunger(FoodLevelChangeEvent e) {
        e.setCancelled(true);
        e.setFoodLevel(20);
    }

    @EventHandler
    public void onClickBed(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Arena game = GameUtils.getGame(player);
        Block block = e.getClickedBlock();
        if (game == null) return;
        BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
        if (bwPlayer.isSpectator() || bwPlayer.isRespawning()) return;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (block == null) return;
        if (block.getType() == Material.BED_BLOCK && !player.isSneaking()) {
            e.setCancelled(true);
            return;
        }
        if (block.getType() == Material.CHEST) {
            for (Team team : game.getTeams()) {
                if (!team.isAvailable() || (!team.isHasBed() && game.getPlayersPerTeam(team).isEmpty())) continue;
                if (game.getSpawns().get(team).getLocation(game.getWorld()).distance(block.getLocation()) <= game.getSpawnProtection()) {
                    if (!bwPlayer.getTeam().equals(team)) {
                        player.sendMessage(MessagesUtils.CANT_CHEST.getMessage(player));
                        e.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        Player player = e.getPlayer();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
        if (bwPlayer.isSpectator() || bwPlayer.isRespawning()) return;
        Item item = e.getItem();
        if (game.getSplitItems().contains(item)) {
            for (Entity ent : player.getNearbyEntities(2.0D, 2.0D, 2.0D)) {
                if (ent instanceof Player) {
                    Player target = (Player) ent;
                    BedwarsPlayer bwTarget = game.getBwPlayerByUUID(target.getUniqueId());
                    if (bwTarget == null) return;
                    if (bwTarget.isSpectator() || bwTarget.isRespawning()) continue;
                    if (bwPlayer.getTeam().equals(bwPlayer.getTeam())) {
                        ItemStack it = new ItemStack(item.getItemStack().getType(), item.getItemStack().getAmount());
                        target.getInventory().addItem(it);
                        target.playSound(target.getLocation(), Sound.ITEM_PICKUP, 0.8f, 1f);
                    }
                }
            }
            game.getSplitItems().remove(item);
        }
    }

    @EventHandler
    public void onArmorClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player player = (Player) e.getWhoClicked();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        if (e.getClickedInventory() == null || e.getClickedInventory().getType() == null) return;
        if (e.getClickedInventory().getType() == InventoryType.PLAYER) {
            if (e.getSlotType() == InventoryType.SlotType.ARMOR) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDropWoodSword(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        if (e.getItemDrop().getItemStack().getType() == Material.WOOD_SWORD) e.setCancelled(true);
    }

    @EventHandler
    public void onDropFalling(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        if (player.getFallDistance() > 3) e.setCancelled(true);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player player = (Player) e.getWhoClicked();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        /*System.out.println(e.getCurrentItem());
        System.out.println(e.getCursor());
        System.out.println(e.getClickedInventory().getType());*/
        ItemStack current = e.getCurrentItem();
        ItemStack cursor = e.getCursor();
        if (e.getClickedInventory() == null || e.getClickedInventory().getType() == null) return;
        if ((e.getClickedInventory().getType() == InventoryType.CHEST || e.getClickedInventory().getType() == InventoryType.ENDER_CHEST) && cursor != null && cursor.getType() == Material.WOOD_SWORD) {
            e.setCancelled(true);
        }
        Inventory openInv = player.getOpenInventory().getTopInventory();
        if (openInv != null) {
            if (e.getClickedInventory().getType() == InventoryType.PLAYER && (openInv.getType() == InventoryType.CHEST || openInv.getType() == InventoryType.ENDER_CHEST) && e.getClick().isShiftClick() && current != null && current.getType() == Material.WOOD_SWORD) {
                e.setCancelled(true);
            }
        }
        if (e.getClick() == ClickType.NUMBER_KEY) {
            int slot = e.getHotbarButton();
            ItemStack it = player.getInventory().getItem(slot);
            if (it != null && it.getType() == Material.WOOD_SWORD) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSwap(InventoryMoveItemEvent e) {
        System.out.println("InventoryMoveItemEvent");
    }

}

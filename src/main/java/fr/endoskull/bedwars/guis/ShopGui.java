package fr.endoskull.bedwars.guis;

import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.utils.*;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsPlayer;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;

public class ShopGui extends CustomGui {
    public static int[] itemsSlot = {19,20,21,22,23,24,25, 28,29,30,31,32,33,34, 37,38,39,40,41,42,43};


    public ShopGui(ShopCategories category, Player p) {
        super(6, MessagesUtils.getCategoryName(p, category));
        Arena game = GameUtils.getGame(p);
        if (game == null) return;
        BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(p.getUniqueId());
        int i = 0;
        for (ShopCategories value : ShopCategories.values()) {
            setItem(i, new CustomItemStack(value.getItem()).setName("§a" + MessagesUtils.getCategoryName(p, value)), player -> {
                new ShopGui(value, player).open(player);
            });
            i++;
        }
        setItem(45, new CustomItemStack(Material.LEVER).setName("§a§lImporter les favoris depuis HYPIXEL"), player -> {
            player.closeInventory();
            if (game.getAlreadyHypixel().contains(player.getUniqueId())) {
                player.sendMessage(MessagesUtils.HYPIXEL_ALREADY.getMessage(player));
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
                return;
            }
            player.sendMessage(MessagesUtils.HYPIXEL_START.getMessage(player));
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                String url = "https://api.hypixel.net/player?uuid=" + player.getUniqueId() + "&key=1489f709-c502-4c0d-9e8c-345142681b97";
                try {
                    String json = IOUtils.toString(new URL(url));
                    if(json.isEmpty()) {
                        player.sendMessage(MessagesUtils.HYPIXEL_ERROR.getMessage(player));
                        player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1f, 1f);
                        return;
                    }
                    JSONObject object = (JSONObject) JSONValue.parseWithException(json);
                    if (!(boolean) object.get("success")) {
                        player.sendMessage(MessagesUtils.HYPIXEL_ERROR.getMessage(player));
                        player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1f, 1f);
                        game.getAlreadyHypixel().add(player.getUniqueId());
                        return;
                    }
                    String pls = "player";
                    JSONObject plsObject = (JSONObject) object.get(pls);
                    String stats = "stats";
                    JSONObject statsObject = (JSONObject) plsObject.get(stats);
                    String bed = "Bedwars";
                    JSONObject bedObject = (JSONObject) statsObject.get(bed);
                    String fav = "favourites_2";
                    String[] items = bedObject.get(fav).toString().split(",");
                    int j = 0;
                    FavoritesUtils.getFavorites(player).clear();
                    for (String item : items) {
                        ShopItems shopItems = ShopItems.getFromHypixel().get(item);
                        if (shopItems != null) {
                            FavoritesUtils.getFavorites(player).put(itemsSlot[j], shopItems);
                        }
                        j++;
                    }
                    player.sendMessage(MessagesUtils.HYPIXEL_SUCCESS.getMessage(player));
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
                    game.getAlreadyHypixel().add(player.getUniqueId());
                    new ShopGui(category, player).open(player);

                } catch (IOException | ParseException | NullPointerException e) {
                    player.sendMessage(MessagesUtils.HYPIXEL_ERROR.getMessage(player));
                    player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1f, 1f);
                    game.getAlreadyHypixel().add(player.getUniqueId());
                }
            });
        });
        i = 0;

        if (category == ShopCategories.QUICK_BUY) {
            for (int slot : itemsSlot) {
                if (FavoritesUtils.getFavorites(p).containsKey(slot)) {
                    ShopItems value = FavoritesUtils.getFavorites(p).get(slot);
                    if (!value.getFamily().equalsIgnoreCase("") && value.isStackFamily()) {
                        if (bwPlayer.getTierTool().getUpgrades().containsKey(value.getFamily()) && ShopItems.getMaxFamilyTier(value.getFamily()) <= bwPlayer.getTierTool().getUpgrades().get(value.getFamily())) {
                            value = ShopItems.getFromFamily(bwPlayer, value.getFamily(), ShopItems.getMaxFamilyTier(value.getFamily()));
                        } else if (bwPlayer.getTierTool().getUpgrades().containsKey(value.getFamily())) {
                            value = ShopItems.getNextFromFamily(bwPlayer, value.getFamily());
                        }
                    }
                    CustomItemStack customItemStack = getItem(p, value, true, game, bwPlayer);
                    if (customItemStack == null) continue;
                    setItem(slot, customItemStack, getAction(p, value, true, game, category, slot, bwPlayer));
                } else {
                    setItem(slot, new CustomItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14).setName(MessagesUtils.SHIFT_CLICK.getMessage(p)));
                }
            }
            return;
        }
        for (ShopItems value : ShopItems.values()) {
            if (value.getCategory() == category) {
                CustomItemStack customItemStack = getItem(p, value, false, game, bwPlayer);
                if (customItemStack == null) continue;
                setItem(itemsSlot[i], customItemStack, getAction(p, value, false, game, category, itemsSlot[i], bwPlayer));
                i++;
            }
        }
    }

    private CustomGuiSuperAction getAction(Player p, ShopItems value, boolean favorite, Arena game, ShopCategories category, int slot, BedwarsPlayer bwPlayer) {
        return (player, clickType) -> {
            if (clickType.isShiftClick()) {
                if (favorite) {
                    FavoritesUtils.getFavorites(player).remove(slot);
                    new ShopGui(category, player).open(player);
                } else {
                    openFavouriteGui(player, value);
                }
                return;
            }
            boolean buyAction = true;
            if (!value.getFamily().equalsIgnoreCase("")) {
                if (value.isStackFamily()) {
                    if (!value.getFamily().equalsIgnoreCase("")) {
                        if (bwPlayer.getTierTool().getUpgrades().containsKey(value.getFamily()) && ShopItems.getMaxFamilyTier(value.getFamily()) <= bwPlayer.getTierTool().getUpgrades().get(value.getFamily())) {
                            if (ShopItems.getFromFamily(bwPlayer, value.getFamily(), ShopItems.getMaxFamilyTier(value.getFamily())).getTier() == value.getTier()) {
                                buyAction = false;
                            }
                        }
                    }
                } else {
                    if (value.isPermanent() && bwPlayer.getAlreadyBought().contains(value)) {
                        buyAction = false;
                    } else if (bwPlayer.getTierTool().getUpgrades().containsKey(value.getFamily()) && bwPlayer.getTierTool().getUpgrades().get(value.getFamily()) > value.getTier()) {
                        buyAction = false;
                    }
                }
            }
            if (value.isPermanent() && bwPlayer.getAlreadyBought().contains(value)) {
                buyAction = false;
            }
            if (!buyAction) return;
            if (hasMaterial(p, value.getCost(), value.getAmount())) {
                clear(p, value.getCost(), value.getAmount());
                if (value.isPermanent()) {
                    bwPlayer.getAlreadyBought().add(value);
                }
                if (value.getItem(player).getType().toString().contains("SWORD")) {
                    player.getInventory().remove(Material.WOOD_SWORD);
                }
                if (value.getFamily().equalsIgnoreCase("")) {
                    if (value.isArmor()) {
                        value.giveArmor(player);
                        bwPlayer.checkUpgrades();
                    } else {
                        player.getInventory().addItem(value.getItem(player));
                        bwPlayer.checkUpgrades();
                    }
                } else {
                    if (value.getTier() == 1 || value.isArmor()) {
                        if (value.isArmor()) {
                            value.giveArmor(player);
                            bwPlayer.checkUpgrades();
                        } else {
                            player.getInventory().addItem(value.getItem(player));
                            bwPlayer.checkUpgrades();
                        }
                    } else {
                        replace(player, ShopItems.getActualFamily(bwPlayer, value.getFamily()).getItem(player), value.getItem(player));
                    }
                    bwPlayer.getTierTool().getUpgrades().put(value.getFamily(), value.getTier());
                }
                player.sendMessage(MessagesUtils.BOUGHT_ITEM.getMessage(player).replace("%item%", MessagesUtils.getItemName(player, value)).replace("%amount%", String.valueOf(value.getItem(player).getAmount())));
                player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1f, 1f);
                new ShopGui(category, player).open(player);
            } else {
                player.sendMessage(MessagesUtils.CANT_AFFORD.getMessage(player));
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
            }
        };
    }

    private CustomItemStack getItem(Player player, ShopItems value, boolean favorite, Arena game, BedwarsPlayer bwPlayer) {
        if (!value.getFamily().equalsIgnoreCase("")) {
            if (value.isStackFamily()) {
                if (!value.getFamily().equalsIgnoreCase("")) {
                    if (bwPlayer.getTierTool().getUpgrades().containsKey(value.getFamily()) && ShopItems.getMaxFamilyTier(value.getFamily()) <= bwPlayer.getTierTool().getUpgrades().get(value.getFamily())) {
                        if (ShopItems.getFromFamily(bwPlayer, value.getFamily(), ShopItems.getMaxFamilyTier(value.getFamily())).getTier() == value.getTier()) {
                            return new CustomItemStack(value.getItem(player))
                                    .setName("§a"+ MessagesUtils.getItemName(player, value))
                                    .setLore((favorite ? MessagesUtils.FAVORITE_MAX_LORE : MessagesUtils.MAX_ITEM_LORE).getMessage(player));
                        } else {
                            return null;
                        }
                    } else if (!ShopItems.getNextFromFamily(bwPlayer, value.getFamily()).equals(value)) {
                        return null;
                    }
                }
            } else {
                if (value.isPermanent() && bwPlayer.getAlreadyBought().contains(value)) {
                    return new CustomItemStack(value.getItem(player))
                            .setName("§c"+ MessagesUtils.getItemName(player, value))
                            .setLore((favorite ? MessagesUtils.FAVORITE_ALREADY : MessagesUtils.ITEM_ALREADY_LORE).getMessage(player));
                } else if (bwPlayer.getTierTool().getUpgrades().containsKey(value.getFamily()) && bwPlayer.getTierTool().getUpgrades().get(value.getFamily()) > value.getTier()) {
                    return new CustomItemStack(value.getItem(player))
                            .setName("§c"+ MessagesUtils.getItemName(player, value))
                            .setLore((favorite ? MessagesUtils.FAVORITE_ALREADY_BEST : MessagesUtils.ITEM_ALREADY_BEST_LORE).getMessage(player));
                }
            }
        }
        if (value.isPermanent() && bwPlayer.getAlreadyBought().contains(value)) {
            return new CustomItemStack(value.getItem(player))
                    .setName("§c" + MessagesUtils.getItemName(player, value))
                    .setLore((favorite ? MessagesUtils.FAVORITE_ALREADY : MessagesUtils.ITEM_ALREADY_LORE).getMessage(player));
        }
        String material = MessagesUtils.getMaterial(player, ShopItems.ShopMaterial.getByType(value.getCost()), value.getAmount());
        return new CustomItemStack(value.getItem(player))
                .setName("§" + (hasMaterial(player, value.getCost(), value.getAmount()) ? "a" : "c") + MessagesUtils.getItemName(player, value))
                .setLore(hasMaterial(player, value.getCost(), value.getAmount()) ? (favorite ? MessagesUtils.FAVORITE_ITEM_LORE : MessagesUtils.DEFAULT_ITEM_LORE).getMessage(player)
                        .replace("%price%", material.substring(0, 2)  + value.getAmount() + " " + material.substring(2))
                        .replace("%desc%", MessagesUtils.split(MessagesUtils.getItemDescription(player, value)).replace("\n", "\n§7")) :
                        (favorite ? MessagesUtils.LESS_FAVORITE_LORE : MessagesUtils.LESS_ITEM_LORE).getMessage(player)
                                .replace("%price%", material.substring(0, 2)  + value.getAmount() + " " + material.substring(2))
                                .replace("%desc%", MessagesUtils.split(MessagesUtils.getItemDescription(player, value)).replace("\n", "\n§7")));
    }

    private static boolean hasMaterial(Player player, Material material, int amount) {
        PlayerInventory inv = player.getInventory();
        return inv.contains(material, amount);
    }

    private static void clear(Player player, Material material, int amount) {
        PlayerInventory inv = player.getInventory();
        inv.removeItem(new ItemStack(material, amount));
    }

    private static void replace(Player player, ItemStack previous, ItemStack it) {
        PlayerInventory inv = player.getInventory();
        boolean complete = false;
        for (int i = 0; i < 36; i++) {
            ItemStack current = inv.getItem(i);
            if (current == null) continue;
            if (current.getType() == previous.getType()) {
                inv.setItem(i, it);
                complete = true;
            }
        }
        if (!complete) {
            inv.addItem(it);
        }
    }

    private static void openFavouriteGui(Player player, ShopItems item) {
        new FavouriteGui(player, item).open(player);
    }
}

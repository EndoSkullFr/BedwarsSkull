package fr.endoskull.bedwars.guis;

import fr.endoskull.bedwars.utils.*;
import fr.endoskull.bedwars.utils.bedwars.BedwarsPlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class FavouriteGui extends CustomGui {


    public FavouriteGui(Player p, ShopItems item) {
        super(6, MessagesUtils.getCategoryName(p, ShopCategories.QUICK_BUY));
        setItem(49, new CustomItemStack(item.getItem(p)).setName(MessagesUtils.getItemName(p, item)));
        for (int slot : ShopGui.itemsSlot) {
            if (FavoritesUtils.getFavorites(p).containsKey(slot)) {
                ShopItems value = FavoritesUtils.getFavorites(p).get(slot);
                String material = MessagesUtils.getMaterial(p, ShopItems.ShopMaterial.getByType(value.getCost()), value.getAmount());
                setItem(slot, new CustomItemStack(value.getItem(p))
                        .setName("§a" + MessagesUtils.getItemName(p, value))
                        .setLore(MessagesUtils.FAVORITE_REPLACE.getMessage(p)
                                .replace("%price%", material.substring(0, 2)  + value.getAmount() + " " + material.substring(2))
                                .replace("%desc%", MessagesUtils.split(MessagesUtils.getItemDescription(p, value)).replace("\n", "\n§7"))), player -> {
                    FavoritesUtils.getFavorites(player).put(slot, item);
                    new ShopGui(ShopCategories.QUICK_BUY, player).open(player);
                });
            } else {
                setItem(slot, new CustomItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14).setName(MessagesUtils.ADD_FAVORITE.getMessage(p)), player -> {
                    addToFavorite(player, item, slot);
                    new ShopGui(ShopCategories.QUICK_BUY, player).open(player);
                });
            }
        }
    }

    public void addToFavorite(Player player, ShopItems item, int slot) {
        if (item.getFamily().equalsIgnoreCase("") || !item.isStackFamily()) {
            FavoritesUtils.getFavorites(player).put(slot, item);
        } else {
            BedwarsPlayer bwPlayer = GameUtils.getGame(player).getBwPlayerByUUID(player.getUniqueId());
            FavoritesUtils.getFavorites(player).put(slot, ShopItems.getFromFamily(bwPlayer, item.getFamily(), 1));
        }
    }
}

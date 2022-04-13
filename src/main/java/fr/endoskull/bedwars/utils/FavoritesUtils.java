package fr.endoskull.bedwars.utils;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class FavoritesUtils {

    private static HashMap<UUID, HashMap<Integer, ShopItems>> favorites = new HashMap<>();

    private final static HashMap<Integer, ShopItems> defaultFavorites = new HashMap<Integer, ShopItems>() {{
        put(19, ShopItems.WOOL);
        put(20, ShopItems.WOOD);
        put(21, ShopItems.SANDSTONE);
        put(22, ShopItems.ENDSTONE);
    }};

    public static HashMap<UUID, HashMap<Integer, ShopItems>> getFavorites() {
        return favorites;
    }

    public static void initFavorites(Player player) {
        favorites.putIfAbsent(player.getUniqueId(), defaultFavorites);
    }

    public static HashMap<Integer, ShopItems> getFavorites(Player player) {
        return favorites.get(player.getUniqueId());
    }
}

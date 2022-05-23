package fr.endoskull.bedwars.utils;

import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class FavoritesUtils {

    private static final HashMap<UUID, HashMap<Integer, ShopItems>> favorites = new HashMap<>();

    private final static HashMap<Integer, ShopItems> defaultFavorites = new HashMap<Integer, ShopItems>() {{
        put(19, ShopItems.WOOL);
        put(20, ShopItems.WOOD);
        put(21, ShopItems.SANDSTONE);
        put(22, ShopItems.ENDSTONE);
    }};

    public static HashMap<UUID, HashMap<Integer, ShopItems>> getFavorites() {
        return favorites;
    }

    public static void loadFavorites(Player player) {
        Account account = AccountProvider.getAccount(player.getUniqueId());
        if (!account.getProperty("bedwars/favorites", "").equalsIgnoreCase("")) {
            favorites.put(player.getUniqueId(), deserialize(account.getProperty("bedwars/favorites")));
        } else {
            favorites.put(player.getUniqueId(), new HashMap<>(defaultFavorites));
        }
    }

    public static void saveFavorites(Player player) {
        Account account = AccountProvider.getAccount(player.getUniqueId());
        account.setProperty("bedwars/favorites", serialize(getFavorites(player)));
    }

    public static HashMap<Integer, ShopItems> getFavorites(Player player) {
        return favorites.get(player.getUniqueId());
    }

    public static String serialize(HashMap<Integer, ShopItems> map) {
        String result = "";
        for (Integer integer : map.keySet()) {
            result += integer + ":" + map.get(integer);
            result += ",";
        }
        result = result.substring(0, result.length() - 1);
        return result;
    }

    public static HashMap<Integer, ShopItems> deserialize(String s) {
        HashMap<Integer, ShopItems> result = new HashMap<>();
        if (s.contains(",")) {
            for (String s1 : s.split(",")) {
                result.put(Integer.parseInt(s1.split(":")[0]), ShopItems.valueOf(s1.split(":")[1]));
            }
        }
        return result;
    }
}

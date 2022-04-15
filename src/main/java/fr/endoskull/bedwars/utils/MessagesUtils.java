package fr.endoskull.bedwars.utils;

import fr.endoskull.bedwars.Main;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public enum MessagesUtils {
    YOU,
    BOUGHT_ITEM,
    CANT_AFFORD,
    SHIFT_CLICK,
    DEFAULT_ITEM_LORE,
    LESS_ITEM_LORE,
    MAX_ITEM_LORE,
    ITEM_ALREADY_LORE,
    ITEM_ALREADY_BEST_LORE,
    FAVORITE_ITEM_LORE,
    LESS_FAVORITE_LORE,
    FAVORITE_MAX_LORE,
    FAVORITE_REPLACE,
    FAVORITE_ALREADY,
    FAVORITE_ALREADY_BEST,
    ADD_FAVORITE,
    BREAK_SELF_BED,
    BREAK_BLOCK,
    HYPIXEL_START,
    HYPIXEL_ERROR,
    HYPIXEL_SUCCESS,
    HYPIXEL_ALREADY,
    BED_BREAK,
    BED_BREAK_VICTIM,
    BED_BREAK_TITLE,
    BED_BREAK_SUBTITLE,
    LEAVE_BED,
    SPECTATE_COMPASS,
    REPLAY_PAPER,
    HEIGH_LIMIT,
    CANT_PLACE_HERE,
    CANT_CHEST;

    public static String split(String message) {
        if (message.length() < 30) return message;
        if (!message.contains(" ")) return message;
        String[] messages = message.split(" ");
        String newMessage = "";
        int i = 0;
        for (String s : messages) {
            if (i > 30) {
                i = 0;
                newMessage += "\n";
            }
            newMessage += s + " ";
            i += s.length();
        }
        return newMessage;
    }

    private static File file = new File(Main.getInstance().getDataFolder(), "languages/French.yml");
    private static YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    public String getMessage(Player player) {
        return config.getString(this.toString().toLowerCase().replace("_", "-"));
    }

    public static String getEventDisplayname(Player player, GameEvent gameEvent) {
        return config.getString("events." + gameEvent.toString());
    }

    public static String getItemName(Player player, ShopItems item) {
        return config.getString("items." + item.name() + ".name");
    }

    public static String getItemDescription(Player player, ShopItems item) {
        return config.getString("items." + item.name() + ".desc");
    }

    public static String getCategoryName(Player player, ShopCategories category) {
        return config.getString("categories." + category.name());
    }

    public static String getMaterial(Player player, ShopItems.ShopMaterial material, int amount) {
        return config.getString(material.getName() + (amount > 1 ? "s" : ""));
    }
}

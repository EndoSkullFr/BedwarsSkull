package fr.endoskull.bedwars.utils;

import fr.endoskull.bedwars.Main;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public enum MessagesUtils {
    YOU;

    private static File file = new File(Main.getInstance().getDataFolder(), "languages/French.yml");
    private static YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    public String getMessage(Player player) {
        return config.getString(this.toString().toLowerCase().replace("_", "-"));
    }
}

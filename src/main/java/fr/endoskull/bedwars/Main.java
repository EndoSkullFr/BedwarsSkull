package fr.endoskull.bedwars;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        super.onEnable();
    }

    public static Main getInstance() {
        return instance;
    }
}

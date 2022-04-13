package fr.endoskull.bedwars.utils;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class TierTool {
    private Player player;
    private HashMap<String, Integer> upgrades = new HashMap<>();

    public TierTool(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public HashMap<String, Integer> getUpgrades() {
        return upgrades;
    }
}

package fr.endoskull.bedwars.utils;

import fr.endoskull.api.data.redis.JedisAccess;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;

public class ServerInfo {

    public static void updateInfo(Arena game) {
        Jedis j = null;
        try {
            j = JedisAccess.getServerpool().getResource();
            j.set("online/" + "Bedwars-" + game.getUniqueId(), String.valueOf(game.getPlayers().size()));
            j.set("max/" + "Bedwars-" + game.getUniqueId(), String.valueOf(game.getTeams().size() * game.getMaxTeamSize()));
            j.set("server/" + "Bedwars-" + game.getUniqueId(), Bukkit.getServerName());
        } finally {
            j.close();
        }
    }

    public static void removeInfo(Arena game) {
        Jedis j = null;
        try {
            j = JedisAccess.getServerpool().getResource();
            j.del("online/" + "Bedwars-" + game.getUniqueId());
            j.del("max/" + "Bedwars-" + game.getUniqueId());
            j.del("server/" + "Bedwars-" + game.getUniqueId());
        } finally {
            j.close();
        }
    }

    public static void clearInfo() {
        Jedis j = null;
        try {
            j = JedisAccess.getServerpool().getResource();
            for (String key : j.keys("server/Bedwars-*")) {
                if (j.get(key).equalsIgnoreCase(Bukkit.getServerName())) {
                    j.del("online/" + key.substring(7));
                    j.del("max/" + key.substring(7));
                    j.del(key);
                }
            }
        } finally {
            j.close();
        }
    }
}

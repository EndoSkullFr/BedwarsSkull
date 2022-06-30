package fr.endoskull.bedwars.tasks;

import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsPlayer;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class ParticlesTask implements Runnable {
    private Main main;

    public ParticlesTask(Main main) {
        this.main = main;
    }

    @Override
    public void run() {
        for (Arena game : main.getGames()) {
            for (Player player : game.getIngamePlayers()) {
                if (player.getInventory().contains(Material.TNT) && !player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                    Color color = Color.RED;
                    /*PacketPlayOutWorldParticles particlePacket = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true, (float) player.getLocation().getX(),
                            (float) (player.getLocation().getY() + 2.2), (float) player.getLocation().getZ(), color.getRed() + 001f, color.getGreen(), color.getBlue(), 0, 1);
                    for (Player pls : game.getAllPlayers()) {
                        if (pls.equals(player)) continue;
                        ((CraftPlayer) pls).getHandle().playerConnection.sendPacket(particlePacket);
                    }*/
                    sendRedstoneParticle(game, player.getLocation().clone().add(0, 2.2, 0), player, 1, 0, 0);
                }
            }
        }
    }

    public static void sendRedstoneParticle(Arena game, Location location, Player player, float red, float green, float blue) {
        for (Player pls : game.getAllPlayers()) {
            if (pls.equals(player)) continue;
            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(
                    EnumParticle.REDSTONE, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), red, green, blue, 1, 0);
            ((CraftPlayer) pls).getHandle().playerConnection.sendPacket(packet);
        }
    }
}

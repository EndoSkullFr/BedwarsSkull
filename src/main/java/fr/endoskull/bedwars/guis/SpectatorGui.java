package fr.endoskull.bedwars.guis;

import fr.endoskull.bedwars.utils.CustomGui;
import fr.endoskull.bedwars.utils.CustomItemStack;
import fr.endoskull.bedwars.utils.MessagesUtils;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsPlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SpectatorGui extends CustomGui {

    public SpectatorGui(Arena game, Player p) {
        super(3, MessagesUtils.SPECTATE_COMPASS.getMessage(p));
        int i = 0;
        for (Player pls : game.getIngamePlayers()) {
            BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(pls.getUniqueId());
            setItem(i, CustomItemStack.getPlayerSkull(pls.getName()).setName(bwPlayer.getTeam().getColor().chat() + bwPlayer.getName()), player -> {
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1f, 1f);
                Player target = bwPlayer.getPlayer();
                if (target != null) {
                    player.teleport(target.getLocation());
                }
            });
            i++;
        }
    }
}

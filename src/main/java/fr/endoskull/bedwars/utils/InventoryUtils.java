package fr.endoskull.bedwars.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {

    private static ItemStack getLeaveBed(Player player) {
        return new CustomItemStack(Material.BED).setName(MessagesUtils.LEAVE_BED.getMessage(player));
    }

    private static ItemStack getSpectateCompass(Player player) {
        return new CustomItemStack(Material.COMPASS).setName(MessagesUtils.SPECTATE_COMPASS.getMessage(player));
    }

    private static ItemStack getReplayPaper(Player player) {
        return new CustomItemStack(Material.PAPER).setName(MessagesUtils.REPLAY_PAPER.getMessage(player));
    }

    public static void setWaitingInv(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.getInventory().setItem(8, getLeaveBed(player));
    }

    public static void setSpectateInv(Player player, boolean replay) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.getInventory().setItem(0, getSpectateCompass(player));
        if (replay) player.getInventory().setItem(4, getReplayPaper(player));
        player.getInventory().setItem(8, getLeaveBed(player));
    }
}

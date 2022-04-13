package fr.endoskull.bedwars.listeners;

import fr.endoskull.bedwars.utils.CustomGui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class CustomGuiListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if (!(e.getWhoClicked() instanceof Player)){
            return;
        }
        Player player = (Player) e.getWhoClicked();
        UUID playerUUID = player.getUniqueId();

        UUID inventoryUUID = CustomGui.openInventories.get(playerUUID);
        if (inventoryUUID != null){
            e.setCancelled(true);
            CustomGui gui = CustomGui.getInventoriesByUUID().get(inventoryUUID);
            CustomGui.CustomGuiAction action = gui.getActions().get(e.getSlot());
            CustomGui.CustomGuiSuperAction superAction = gui.getSuperActions().get(e.getSlot());

            if (action != null){
                action.click(player);
            }
            if (superAction != null){
                superAction.click(player, e.getClick());
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClose(InventoryCloseEvent e){
        Player player = (Player) e.getPlayer();
        UUID playerUUID = player.getUniqueId();

        CustomGui.openInventories.remove(playerUUID);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();

        CustomGui.openInventories.remove(playerUUID);
    }
}

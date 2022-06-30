package fr.endoskull.bedwars.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.endoskull.api.commons.account.Account;
import fr.endoskull.api.commons.account.AccountProvider;
import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.guis.SpectatorGui;
import fr.endoskull.bedwars.utils.GameState;
import fr.endoskull.bedwars.utils.GameUtils;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ClickListener implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Arena game = GameUtils.getGame(player);
        if (game == null) return;
        BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
        ItemStack item = e.getItem();
        if (item == null) return;
        if (item.getType() == Material.BED) {
            if (game.getGameState() == GameState.waiting || game.getGameState() == GameState.starting || game.getGameState() == GameState.finish) {
                player.kickPlayer("§cRetour au lobby");
            }
        }
        if (game.getGameState() == GameState.finish || !bwPlayer.isAlive()) {
            if (item.getType() == Material.PAPER) {
                /*
                envoyer au lobby
                écrire une properties
                pas de msg de join
                send sur le bw
                 */
                Account account = AccountProvider.getAccount(player.getUniqueId());
                account.setProperty("bedwars/playagain", "1");
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Connect");
                out.writeUTF("Lobby");
                player.sendPluginMessage(Main.getInstance(), "BungeeCord", out.toByteArray());
            }
            if (item.getType() == Material.COMPASS) {
                if (game.getGameState() == GameState.finish) {
                    new SpectatorGui(game, player).open(player);
                }
            }
        }
    }

    @EventHandler
    public void onClickBadBlock(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if (block == null) return;
        if (block.getType() == Material.DROPPER || block.getType() == Material.DISPENSER || block.getType() == Material.TRAPPED_CHEST || block.getType() == Material.ENCHANTMENT_TABLE || block.getType() == Material.BREWING_STAND || block.getType() == Material.ANVIL || block.getType() == Material.WORKBENCH || block.getType() == Material.FURNACE || block.getType() == Material.BURNING_FURNACE) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onClickInv(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        if (e.getClickedInventory() == null) return;
        if (e.getClickedInventory().getType() == InventoryType.CRAFTING) e.setCancelled(true);
    }
}

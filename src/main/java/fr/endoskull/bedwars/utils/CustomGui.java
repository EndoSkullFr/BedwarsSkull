package fr.endoskull.bedwars.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class CustomGui {
    public static Map<UUID, CustomGui> inventoriesByUUID = new HashMap<>();
    public static Map<UUID, UUID> openInventories = new HashMap<>();
    private UUID uuid;
    private Inventory yourInventory;
    private Map<Integer, CustomGuiAction> actions;
    private Map<Integer, CustomGuiSuperAction> superActions;
    private int line;

    public CustomGui(int line, String invName) {
        uuid = UUID.randomUUID();
        yourInventory = Bukkit.createInventory(null, line * 9, invName);
        this.line = line;
        actions = new HashMap<>();
        superActions = new HashMap<>();
        inventoriesByUUID.put(getUuid(), this);
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getLine() {
        return line;
    }

    public interface CustomGuiAction {
        void click(Player player);
    }

    public interface CustomGuiSuperAction {
        void click(Player player, ClickType clickType);
    }

    public void setItem(int slot, ItemStack stack, CustomGuiAction action){
        yourInventory.setItem(slot, stack);
        if (action != null){
            actions.put(slot, action);
        }
    }

    public void setItem(int slot, ItemStack stack, CustomGuiSuperAction action){
        yourInventory.setItem(slot, stack);
        if (action != null){
            superActions.put(slot, action);
        }
    }

    public void setItem(int slot, ItemStack stack){
        yourInventory.setItem(slot, stack);
    }

    public void open(Player p){
        p.openInventory(yourInventory);
        openInventories.put(p.getUniqueId(), getUuid());
    }

    public static Map<UUID, CustomGui> getInventoriesByUUID() {
        return inventoriesByUUID;
    }

    public static Map<UUID, UUID> getOpenInventories() {
        return openInventories;
    }

    public Map<Integer, CustomGuiAction> getActions() {
        return actions;
    }

    public Map<Integer, CustomGuiSuperAction> getSuperActions() {
        return superActions;
    }

    public void delete(){
        for (Player p : Bukkit.getOnlinePlayers()){
            UUID u = openInventories.get(p.getUniqueId());
            if (u.equals(getUuid())){
                p.closeInventory();
            }
        }
        inventoriesByUUID.remove(getUuid());
    }

}


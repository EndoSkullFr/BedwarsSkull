package fr.endoskull.bedwars.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Upgrades {
    SHARPNESS(new ItemStack(Material.IRON_SWORD), 12, 4),
    PROTECTION(new ItemStack(Material.IRON_CHESTPLATE), 13, 2, 4, 8, 16),
    HASTE(new ItemStack(Material.GOLD_PICKAXE), 14, 2, 4),
    FORGE(new ItemStack(Material.FURNACE), 21, 2, 4, 6, 8),
    HEAL(new ItemStack(Material.BEACON), 22, 1),
    GOULAG(new ItemStack(Material.SKULL_ITEM), 23, 5);

    private ItemStack item;
    private int slot;
    private int[] price;

    Upgrades(ItemStack item, int slot, int price) {
        this.item = item;
        this.slot = slot;
        this.price = new int[]{price};
    }

    Upgrades(ItemStack item, int slot, int... price) {
        this.item = item;
        this.slot = slot;
        this.price = price;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getSlot() {
        return slot;
    }

    public int[] getPrice() {
        return price;
    }
}

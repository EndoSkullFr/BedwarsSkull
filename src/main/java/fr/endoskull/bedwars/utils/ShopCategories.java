package fr.endoskull.bedwars.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum ShopCategories {
    QUICK_BUY(new ItemStack(Material.NETHER_STAR)),
    BLOCKS(new ItemStack(Material.CLAY)),
    MELEE(new ItemStack(Material.GOLD_SWORD)),
    ARMOR(new ItemStack(Material.IRON_BOOTS)),
    TOOLS(new ItemStack(Material.STONE_PICKAXE)),
    RANGED(new ItemStack(Material.BOW)),
    POTIONS(new ItemStack(Material.BREWING_STAND_ITEM)),
    UTILITY(new ItemStack(Material.TNT));

    public ItemStack getItem() {
        return item;
    }

    ShopCategories(ItemStack item) {
        this.item = item;
    }

    private ItemStack item;
}

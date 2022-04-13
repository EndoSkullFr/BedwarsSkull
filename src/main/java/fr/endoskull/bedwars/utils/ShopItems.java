package fr.endoskull.bedwars.utils;

import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ShopItems {
    WOOL(new ItemStack(Material.WOOL, 16), ShopCategories.BLOCKS, Material.IRON_INGOT, 4, true),
    SANDSTONE(new ItemStack(Material.STAINED_CLAY, 16), ShopCategories.BLOCKS, Material.IRON_INGOT, 12, true),
    GLASS(new ItemStack(Material.STAINED_GLASS, 4), ShopCategories.BLOCKS, Material.IRON_INGOT, 12, true),
    ENDSTONE(new ItemStack(Material.ENDER_STONE, 12), ShopCategories.BLOCKS, Material.IRON_INGOT, 24),
    LADDER(new ItemStack(Material.LADDER, 8), ShopCategories.BLOCKS, Material.IRON_INGOT, 4),
    WOOD(new ItemStack(Material.WOOD, 16), ShopCategories.BLOCKS, Material.GOLD_INGOT, 4),
    OBSIDIAN(new ItemStack(Material.OBSIDIAN, 4), ShopCategories.BLOCKS, Material.EMERALD, 4),

    STICK(new CustomItemStack(Material.STICK).addCustomEnchantment(Enchantment.KNOCKBACK, 1), ShopCategories.MELEE, Material.GOLD_INGOT, 5),
    SWORD1(new ItemStack(Material.STONE_SWORD), ShopCategories.MELEE, Material.IRON_INGOT, 10),
    SWORD2(new ItemStack(Material.IRON_SWORD), ShopCategories.MELEE, Material.GOLD_INGOT, 7),
    SWORD3(new ItemStack(Material.DIAMOND_SWORD), ShopCategories.MELEE, Material.EMERALD, 4),

    ARMOR1(new ItemStack(Material.CHAINMAIL_BOOTS), ShopCategories.ARMOR, Material.IRON_INGOT, 40, "armor", 1, false, true, true),
    ARMOR2(new ItemStack(Material.IRON_BOOTS), ShopCategories.ARMOR, Material.GOLD_INGOT, 12, "armor", 2, false, true, true),
    ARMOR3(new ItemStack(Material.DIAMOND_BOOTS), ShopCategories.ARMOR, Material.EMERALD, 6, "armor", 3, false, true, true),

    ARROW(new ItemStack(Material.ARROW, 6), ShopCategories.RANGED, Material.GOLD_INGOT, 4),
    BOW1(new ItemStack(Material.BOW), ShopCategories.RANGED, Material.GOLD_INGOT, 12),
    BOW2(new CustomItemStack(Material.BOW).addCustomEnchantment(Enchantment.ARROW_DAMAGE, 1), ShopCategories.RANGED, Material.GOLD_INGOT, 24),
    BOW3(new CustomItemStack(Material.BOW).addCustomEnchantment(Enchantment.ARROW_DAMAGE, 1).addCustomEnchantment(Enchantment.ARROW_KNOCKBACK, 1), ShopCategories.RANGED, Material.EMERALD, 6),

    POTION_SPEED(new CustomItemStack(Material.POTION, 1, (byte) 8194).addPotionEffect(PotionEffectType.SPEED, 1, 45), ShopCategories.POTIONS, Material.EMERALD, 1),
    POTION_JUMP(new CustomItemStack(Material.POTION, 1, (byte) 8203).addPotionEffect(PotionEffectType.JUMP, 4, 45), ShopCategories.POTIONS, Material.EMERALD, 1),
    POTION_INVI(new CustomItemStack(Material.POTION, 1, (byte) 8238).addPotionEffect(PotionEffectType.INVISIBILITY, 0, 30), ShopCategories.POTIONS, Material.EMERALD, 2),

    SHEARS(new ItemStack(Material.SHEARS), ShopCategories.TOOLS, Material.IRON_INGOT, 20, false, true),
    AXE1(new ItemStack(Material.WOOD_AXE), ShopCategories.TOOLS, Material.IRON_INGOT, 10, false, true, "axe", 1),
    AXE2(new ItemStack(Material.STONE_AXE), ShopCategories.TOOLS, Material.IRON_INGOT, 10, false, false, "axe", 2),
    AXE3(new ItemStack(Material.IRON_AXE), ShopCategories.TOOLS, Material.GOLD_INGOT, 3, false, false, "axe", 3),
    AXE4(new ItemStack(Material.DIAMOND_AXE), ShopCategories.TOOLS, Material.GOLD_INGOT, 6, false, false, "axe", 4),
    PICKAXE1(new ItemStack(Material.WOOD_PICKAXE), ShopCategories.TOOLS, Material.IRON_INGOT, 10, false, true, "pickaxe", 1),
    PICKAXE2(new ItemStack(Material.IRON_PICKAXE), ShopCategories.TOOLS, Material.IRON_INGOT, 10, false, false, "pickaxe", 2),
    PICKAXE3(new ItemStack(Material.GOLD_PICKAXE), ShopCategories.TOOLS, Material.GOLD_INGOT, 3, false, false, "pickaxe", 3),
    PICKAXE4(new ItemStack(Material.DIAMOND_PICKAXE), ShopCategories.TOOLS, Material.GOLD_INGOT, 6, false, false, "pickaxe", 4),;

    public static int getMaxFamilyTier(String family) {
        int tier = 0;
        for (ShopItems value : values()) {
            if (!value.getFamily().equalsIgnoreCase(family)) continue;
            if (value.getTier() > tier) tier = value.getTier();
        }
        return tier;
    }
    public static ShopItems getFromFamily(Player player, String family, int tier) {
        return Arrays.stream(values()).filter(shopItems -> shopItems.getFamily().equalsIgnoreCase(family) && shopItems.getTier() == tier).findFirst().orElse(null);
    }

    public static ShopItems getActualFamily(Player player, String family) {
        return getFromFamily(player, family, GameUtils.getGame(player).getItemsTier().get(player).getUpgrades().getOrDefault(family, 1));
    }

    public static ShopItems getNextFromFamily(Player player, String family) {
        if (GameUtils.getGame(player).getItemsTier().get(player).getUpgrades().containsKey(family)) {
            return getFromFamily(player, family, GameUtils.getGame(player).getItemsTier().get(player).getUpgrades().get(family) + 1);
        } else {
            return getFromFamily(player, family, 1);
        }
    }

    private static boolean hasItem(Player player, ItemStack is) {
        PlayerInventory inv = player.getInventory();
        return inv.contains(is);
    }

    private ItemStack item;
    private ShopCategories category;
    private Material cost;
    private int amount;
    private boolean colorable = false;
    private boolean permanent = false;
    private String family = "";
    private int tier = 1;
    private boolean stackFamily = true;
    private boolean armor = false;
    private boolean oneTimeBought = false;

    ShopItems(ItemStack item, ShopCategories category, Material cost, int amount) {
        this.item = item;
        this.category = category;
        this.cost = cost;
        this.amount = amount;
    }

    ShopItems(ItemStack item, ShopCategories category, Material cost, int amount, boolean colorable) {
        this.item = item;
        this.category = category;
        this.cost = cost;
        this.amount = amount;
        this.colorable = colorable;
    }

    ShopItems(ItemStack item, ShopCategories category, Material cost, int amount, boolean colorable, boolean permanent) {
        this.item = item;
        this.category = category;
        this.cost = cost;
        this.amount = amount;
        this.colorable = colorable;
        this.permanent = permanent;
    }

    ShopItems(ItemStack item, ShopCategories category, Material cost, int amount, boolean colorable, boolean permanent, String family, int tier) {
        this.item = item;
        this.category = category;
        this.cost = cost;
        this.amount = amount;
        this.colorable = colorable;
        this.permanent = permanent;
        this.family = family;
        this.tier = tier;
    }

    ShopItems(ItemStack item, ShopCategories category, Material cost, int amount, String family, int tier, boolean stackFamily, boolean armor, boolean oneTimeBought) {
        this.item = item;
        this.category = category;
        this.cost = cost;
        this.amount = amount;
        this.family = family;
        this.tier = tier;
        this.stackFamily = stackFamily;
        this.armor = armor;
        this.oneTimeBought = oneTimeBought;
    }

    public ItemStack getItem(Player player) {
        ItemStack it = item.clone();
        if (colorable) {
            for (Arena game : Main.getInstance().getGames()) {
                if (game.getPlayers().containsKey(player)) {
                    it.setDurability(DyeColor.getByColor(game.getPlayers().get(player).getColor()).getWoolData());
                }
            }
        }
        return new CustomItemStack(it).setUnbreakable();
    }

    public ShopCategories getCategory() {
        return category;
    }

    public Material getCost() {
        return cost;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isColorable() {
        return colorable;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public String getFamily() {
        return family;
    }

    public int getTier() {
        return tier;
    }

    public void giveArmor(Player player) {
        if (armor) {
            player.getInventory().setBoots(new ItemStack(Material.valueOf(getItem(player).getType().name().split("_")[0] + "_BOOTS")));
            player.getInventory().setLeggings(new ItemStack(Material.valueOf(getItem(player).getType().name().split("_")[0] + "_LEGGINGS")));
        }
    }

    public boolean isArmor() {
        return armor;
    }

    public boolean isStackFamily() {
        return stackFamily;
    }

    public boolean isOneTimeBought() {
        return oneTimeBought;
    }

    public static enum ShopMaterial {
        iron("iron", Material.IRON_INGOT),
        gold("gold", Material.GOLD_INGOT),
        diamond("diamond", Material.DIAMOND, false),
        emerald("emerald", Material.EMERALD, false);

        private String name;
        private Material type;
        private boolean spawningAtBase = true;

        ShopMaterial(String name, Material type) {
            this.name = name;
            this.type = type;
        }

        ShopMaterial(String name, Material type, boolean spawningAtBase) {
            this.name = name;
            this.type = type;
            this.spawningAtBase = spawningAtBase;
        }

        public String getName() {
            return name;
        }

        public Material getType() {
            return type;
        }

        public static ShopMaterial getByType(Material type) {
            return Arrays.stream(values()).filter(shopMaterial -> shopMaterial.getType() == type).findFirst().orElse(ShopMaterial.iron);
        }

        public boolean isSpawningAtBase() {
            return spawningAtBase;
        }
    }
}

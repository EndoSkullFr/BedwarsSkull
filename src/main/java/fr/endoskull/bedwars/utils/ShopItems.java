package fr.endoskull.bedwars.utils;

import fr.endoskull.bedwars.Main;
import fr.endoskull.bedwars.utils.bedwars.Arena;
import fr.endoskull.bedwars.utils.bedwars.BedwarsPlayer;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public enum ShopItems {
    WOOL(new ItemStack(Material.WOOL, 16), ShopCategories.BLOCKS, Material.IRON_INGOT, 4, true, false, "", 1, false, false, false),
    SANDSTONE(new ItemStack(Material.STAINED_CLAY, 16), ShopCategories.BLOCKS, Material.IRON_INGOT, 12, true, false, "", 1, false, false, false),
    GLASS(new ItemStack(Material.STAINED_GLASS, 4), ShopCategories.BLOCKS, Material.IRON_INGOT, 12, true, false, "", 1, false, false, false),
    ENDSTONE(new ItemStack(Material.ENDER_STONE, 12), ShopCategories.BLOCKS, Material.IRON_INGOT, 24, false, false, "", 1, false, false, false),
    LADDER(new ItemStack(Material.LADDER, 8), ShopCategories.BLOCKS, Material.IRON_INGOT, 4, false, false, "", 1, false, false, false),
    WOOD(new ItemStack(Material.WOOD, 16), ShopCategories.BLOCKS, Material.GOLD_INGOT, 4, false, false, "", 1, false, false, false),
    OBSIDIAN(new ItemStack(Material.OBSIDIAN, 4), ShopCategories.BLOCKS, Material.EMERALD, 4, false, false, "", 1, false, false, false),

    SWORD1(new ItemStack(Material.STONE_SWORD), ShopCategories.MELEE, Material.IRON_INGOT, 10, false, false, "", 1, false, false, true),
    SWORD2(new ItemStack(Material.IRON_SWORD), ShopCategories.MELEE, Material.GOLD_INGOT, 7, false, false, "", 1, false, false, true),
    SWORD3(new ItemStack(Material.DIAMOND_SWORD), ShopCategories.MELEE, Material.EMERALD, 4, false, false, "", 1, false, false, true),
    STICK(new CustomItemStack(Material.STICK).addCustomEnchantment(Enchantment.KNOCKBACK, 1), ShopCategories.MELEE, Material.GOLD_INGOT, 5, false, false, "", 1, false, false, false),

    ARMOR1(new ItemStack(Material.CHAINMAIL_BOOTS), ShopCategories.ARMOR, Material.IRON_INGOT, 40, false, true, "armor", 1, false, true, true),
    ARMOR2(new ItemStack(Material.IRON_BOOTS), ShopCategories.ARMOR, Material.GOLD_INGOT, 12, false, true, "armor", 2, false, true, true),
    ARMOR3(new ItemStack(Material.DIAMOND_BOOTS), ShopCategories.ARMOR, Material.EMERALD, 6, false, true, "armor", 3, false, true, true),

    SHEARS(new ItemStack(Material.SHEARS), ShopCategories.TOOLS, Material.IRON_INGOT, 20, false, true, "", 1, false, false, true),
    AXE1(new CustomItemStack(Material.WOOD_AXE).addCustomEnchantment(Enchantment.DIG_SPEED, 1), ShopCategories.TOOLS, Material.IRON_INGOT, 10, false, true, "axe", 1, true, false, true),
    AXE2(new CustomItemStack(Material.STONE_AXE).addCustomEnchantment(Enchantment.DIG_SPEED, 1), ShopCategories.TOOLS, Material.IRON_INGOT, 10, false, false, "axe", 2, true, false, true),
    AXE3(new CustomItemStack(Material.IRON_AXE).addCustomEnchantment(Enchantment.DIG_SPEED, 2), ShopCategories.TOOLS, Material.GOLD_INGOT, 3, false, false, "axe", 3, true, false, true),
    AXE4(new CustomItemStack(Material.DIAMOND_AXE).addCustomEnchantment(Enchantment.DIG_SPEED, 3), ShopCategories.TOOLS, Material.GOLD_INGOT, 6, false, false, "axe", 4, true, false, true),
    PICKAXE1(new CustomItemStack(Material.WOOD_PICKAXE).addCustomEnchantment(Enchantment.DIG_SPEED,1), ShopCategories.TOOLS, Material.IRON_INGOT, 10, false, true, "pickaxe", 1, true, false, true),
    PICKAXE2(new CustomItemStack(Material.IRON_PICKAXE).addCustomEnchantment(Enchantment.DIG_SPEED, 2), ShopCategories.TOOLS, Material.IRON_INGOT, 10, false, false, "pickaxe", 2, true, false, true),
    PICKAXE3(new CustomItemStack(Material.GOLD_PICKAXE).addCustomEnchantment(Enchantment.DIG_SPEED, 3).addCustomEnchantment(Enchantment.DAMAGE_ALL, 2), ShopCategories.TOOLS, Material.GOLD_INGOT, 3, false, false, "pickaxe", 3, true, false, true),
    PICKAXE4(new CustomItemStack(Material.DIAMOND_PICKAXE).addCustomEnchantment(Enchantment.DIG_SPEED, 3), ShopCategories.TOOLS, Material.GOLD_INGOT, 6, false, false, "pickaxe", 4, true, false, true),

    ARROW(new ItemStack(Material.ARROW, 6), ShopCategories.RANGED, Material.GOLD_INGOT, 4, false, false, "", 1, false, false, false),
    BOW1(new ItemStack(Material.BOW), ShopCategories.RANGED, Material.GOLD_INGOT, 12, false, false, "", 1, false, false, true),
    BOW2(new CustomItemStack(Material.BOW).addCustomEnchantment(Enchantment.ARROW_DAMAGE, 1), ShopCategories.RANGED, Material.GOLD_INGOT, 24, false, false, "", 1, false, false, true),
    BOW3(new CustomItemStack(Material.BOW).addCustomEnchantment(Enchantment.ARROW_DAMAGE, 1).addCustomEnchantment(Enchantment.ARROW_KNOCKBACK, 1), ShopCategories.RANGED, Material.EMERALD, 6, false, false, "", 1, false, false, true),

    POTION_SPEED(new CustomItemStack(Material.POTION, 1, (byte) 8194).addPotionEffect(PotionEffectType.SPEED, 1, 45), ShopCategories.POTIONS, Material.EMERALD, 1, false, false, "", 1, false, false, false),
    POTION_JUMP(new CustomItemStack(Material.POTION, 1, (byte) 8203).addPotionEffect(PotionEffectType.JUMP, 4, 45), ShopCategories.POTIONS, Material.EMERALD, 1, false, false, "", 1, false, false, false),
    POTION_INVI(new CustomItemStack(Material.POTION, 1, (byte) 8238).addPotionEffect(PotionEffectType.INVISIBILITY, 0, 30), ShopCategories.POTIONS, Material.EMERALD, 2, false, false, "", 1, false, false, false),

    GOLDEN_APPLE(new CustomItemStack(Material.GOLDEN_APPLE), ShopCategories.UTILITY, Material.GOLD_INGOT, 3, false, false, "", 1, false, false, false),
    BED_BUG(new CustomItemStack(Material.SNOW_BALL), ShopCategories.UTILITY, Material.IRON_INGOT, 30, false, false, "", 1, false, false, false),
    GOLEM(new CustomItemStack(Material.MONSTER_EGG, 1, (byte) 99), ShopCategories.UTILITY, Material.IRON_INGOT, 120, false, false, "", 1, false, false, false),
    FIREBALL(new CustomItemStack(Material.FIREBALL), ShopCategories.UTILITY, Material.IRON_INGOT, 40, false, false, "", 1, false, false, false),
    TNT(new CustomItemStack(Material.TNT), ShopCategories.UTILITY, Material.GOLD_INGOT, 4, false, false, "", 1, false, false, false),
    ENDER_PEARL(new CustomItemStack(Material.ENDER_PEARL), ShopCategories.UTILITY, Material.EMERALD, 4, false, false, "", 1, false, false, false),
    WATER_BUCKET(new CustomItemStack(Material.WATER_BUCKET), ShopCategories.UTILITY, Material.GOLD_INGOT, 3, false, false, "", 1, false, false, false),
    BRIDGE_EGG(new CustomItemStack(Material.EGG), ShopCategories.UTILITY, Material.EMERALD, 1, false, false, "", 1, false, false, false),
    MILK(new CustomItemStack(Material.MILK_BUCKET), ShopCategories.UTILITY, Material.GOLD_INGOT, 4, false, false, "", 1, false, false, false),
    SPONGE(new CustomItemStack(Material.SPONGE, 4), ShopCategories.UTILITY, Material.GOLD_INGOT, 3, false, false, "", 1, false, false, false),
    POPUP_TOWER(new CustomItemStack(Material.CHEST), ShopCategories.UTILITY, Material.IRON_INGOT, 24, false, false, "", 1, false, false, false),;


    private static final HashMap<String, ShopItems> fromHypixel = new HashMap<String, ShopItems>() {{
        put("wool", WOOL);
        put("hardened_clay", SANDSTONE);
        put("stick_(knockback_i)", STICK);
        put("wooden_pickaxe", PICKAXE1);
        put("compact_pop-up_tower", POPUP_TOWER);
        put("tnt", TNT);
        put("oak_wood_planks", WOOD);
        put("iron_sword", SWORD2);
        put("iron_boots", ARMOR2);
        put("shears", SHEARS);
        put("arrow", ARROW);
        put("invisibility_potion_(30_seconds)", POTION_INVI);
        put("water_bucket", WATER_BUCKET);
        put("wooden_axe", AXE1);
        put("ladder", LADDER);
        put("end_stone", ENDSTONE);
        put("fireball", FIREBALL);
        put("golden_apple", GOLDEN_APPLE);
        put("diamond_boots", ARMOR3);
        put("ender_pearl", ENDER_PEARL);
        put("blast-proof_glass", GLASS);
        put("stone_sword", SWORD1);
        put("chainmail_boots", ARMOR1);
        put("bow", BOW1);
        put("bow_(power_i)", BOW2);
        put("bow_(power_i__punch_i)", BOW3);
        put("speed_ii_potion_(45_seconds)", POTION_SPEED);
        put("bedbug", BED_BUG);
        put("sponge", SPONGE);
        put("jump_v_potion_(45_seconds)", POTION_JUMP);
        put("dream_defender", GOLEM);
        put("bridge_egg", BRIDGE_EGG);
        put("magic_milk", WATER_BUCKET);
        put("obsidian", OBSIDIAN);
        put("diamond_sword", SWORD3);
    }};

    public static int getMaxFamilyTier(String family) {
        int tier = 0;
        for (ShopItems value : values()) {
            if (!value.getFamily().equalsIgnoreCase(family)) continue;
            if (value.getTier() > tier) tier = value.getTier();
        }
        return tier;
    }
    public static ShopItems getFromFamily(BedwarsPlayer bwPlayer, String family, int tier) {
        return Arrays.stream(values()).filter(shopItems -> shopItems.getFamily().equalsIgnoreCase(family) && shopItems.getTier() == tier).findFirst().orElse(null);
    }

    public static ShopItems getActualFamily(BedwarsPlayer bwPlayer, String family) {
        return getFromFamily(bwPlayer, family, bwPlayer.getTierTool().getUpgrades().getOrDefault(family, 1));
    }

    public static ShopItems getNextFromFamily(BedwarsPlayer bwPlayer, String family) {
        if (bwPlayer.getTierTool().getUpgrades().containsKey(family)) {
            return getFromFamily(bwPlayer, family, bwPlayer.getTierTool().getUpgrades().get(family) + 1);
        } else {
            return getFromFamily(bwPlayer, family, 1);
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
    private boolean unbreakable = false;

    ShopItems(ItemStack item, ShopCategories category, Material cost, int amount, boolean colorable, boolean permanent, String family, int tier, boolean stackFamily, boolean armor, boolean unbreakable) {
        this.item = item;
        this.category = category;
        this.cost = cost;
        this.amount = amount;
        this.colorable = colorable;
        this.permanent = permanent;
        this.family = family;
        this.tier = tier;
        this.stackFamily = stackFamily;
        this.armor = armor;
        this.unbreakable = unbreakable;
    }

    /*ShopItems(ItemStack item, ShopCategories category, Material cost, int amount) {
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

    ShopItems(ItemStack item, ShopCategories category, Material cost, int amount, String family, int tier, boolean stackFamily, boolean armor, boolean permanent) {
        this.item = item;
        this.category = category;
        this.cost = cost;
        this.amount = amount;
        this.family = family;
        this.tier = tier;
        this.stackFamily = stackFamily;
        this.armor = armor;
        this.permanent = permanent;
    }*/

    public ItemStack getItem(Player player) {
        ItemStack it = item.clone();
        if (colorable) {
            for (Arena game : Main.getInstance().getGames()) {
                BedwarsPlayer bwPlayer = game.getBwPlayerByUUID(player.getUniqueId());
                if (bwPlayer != null) {
                    it.setDurability(bwPlayer.getTeam().getColor().dye().getWoolData());
                }
            }
        }
        return new CustomItemStack(it).setUnbreakable(unbreakable);
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
            player.getInventory().setBoots(new CustomItemStack(Material.valueOf(getItem(player).getType().name().split("_")[0] + "_BOOTS")).setUnbreakable(unbreakable));
            player.getInventory().setLeggings(new CustomItemStack(Material.valueOf(getItem(player).getType().name().split("_")[0] + "_LEGGINGS")).setUnbreakable(unbreakable));
        }
    }

    public boolean isArmor() {
        return armor;
    }

    public boolean isStackFamily() {
        return stackFamily;
    }

    public boolean isUnbreakable() {
        return unbreakable;
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

    public static HashMap<String, ShopItems> getFromHypixel() {
        return fromHypixel;
    }
}

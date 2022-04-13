package fr.endoskull.bedwars.utils;

import fr.endoskull.bedwars.Main;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigUtils {

    private static FileConfiguration config = Main.getInstance().getConfig();

    public static int getGeneratorTimer(ShopItems.ShopMaterial material) {
        return config.getInt("generators." + material.getName() + ".delay");
    }

    public static int getGeneratorAmount(ShopItems.ShopMaterial material) {
        return config.getInt("generators." + material.getName() + ".amount");
    }

    public static int getGeneratorLimit(ShopItems.ShopMaterial material) {
        return config.getInt("generators." + material.getName() + ".spawn-limit");
    }

    public static int getTieredGeneratorTimer(ShopItems.ShopMaterial material, int tier) {
        return config.getInt("generators." + material.getName() + ".tier" + integerToRoman(tier) + ".delay");
    }

    public static int getTieredGeneratorLimit(ShopItems.ShopMaterial material, int tier) {
        return config.getInt("generators." + material.getName() + ".tier" + integerToRoman(tier) + ".spawn-limit");
    }

    public static String integerToRoman(int num) {

        int[] values = {1000,900,500,400,100,90,50,40,10,9,5,4,1};
        String[] romanLiterals = {"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};

        StringBuilder roman = new StringBuilder();

        for(int i=0;i<values.length;i++) {
            while(num >= values[i]) {
                num -= values[i];
                roman.append(romanLiterals[i]);
            }
        }
        return roman.toString();
    }
}

package me.kiwigamerine.lumbertycoon.EnchantmentAPI.service;

import org.bukkit.Material;

public enum MaterialClass {
    WOOD(15),
    STONE(5),
    IRON(14),
    GOLD(25),
    DIAMOND(10),
    LEATHER(15),
    CHAIN(12),
    DEFAULT(10);

    private final int enchantability;

    private MaterialClass(int enchantabilty) {
        this.enchantability = enchantabilty;
    }

    public int getEnchantability() {
        return this.enchantability;
    }

    public static int getEnchantabilityFor(Material material) {
        int enchantability = DEFAULT.getEnchantability();
        MaterialClass[] arr$ = values();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            MaterialClass materialClass = arr$[i$];
            if (material.name().contains(materialClass.name() + "_")) {
                enchantability = materialClass.getEnchantability();
                break;
            }
        }

        return enchantability;
    }
}

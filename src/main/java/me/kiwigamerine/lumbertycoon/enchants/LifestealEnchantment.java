package me.kiwigamerine.lumbertycoon.enchants;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.kiwigamerine.lumbertycoon.EnchantmentAPI.CustomEnchantment;

// All enchantments extend CustomEnchantment
public class LifestealEnchantment extends CustomEnchantment {

    // List of items to enchant onto through an enchanting table
    static final Material[] LIFESTEAL_ITEMS = new Material[] {
            Material.WOOD_HOE, Material.STONE_HOE,
            Material.IRON_HOE, Material.GOLD_HOE,
            Material.DIAMOND_HOE
    };

    // Fields for a cool down effect so players don't heal constantly
    static final int COOLDOWN = 5000;
    long timer;

    public LifestealEnchantment() {
        // Pass in the enchantment name and the designated items
        super("Lifesteal", LIFESTEAL_ITEMS);

        // Gives your enchantment a description for the detailed list and detail book
        description = "Steals health on hit";

        // Maximum level of your enchantment
        setMaxLevel(5);

        // Modifies how early you can get your enchantment
        setBase(10);

        // Modifies how easily higher levels are obtained relative to the base value
        setInterval(8);
    }

    // Applies the enchantment effect on hit
    @Override
    public void applyEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageByEntityEvent event) {
        if (System.currentTimeMillis() - timer < COOLDOWN) return;
        double health = user.getHealth() + enchantLevel;
        if (health > user.getMaxHealth()) health = user.getMaxHealth();
        user.setHealth(health);
        timer = System.currentTimeMillis();
    }
}
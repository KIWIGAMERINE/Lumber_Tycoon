package me.kiwigamerine.lumbertycoon.EnchantmentAPI.enchanting;

import me.kiwigamerine.lumbertycoon.EnchantmentAPI.CustomEnchantment;
import me.kiwigamerine.lumbertycoon.LumberTycoon;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.config.RootConfig;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.config.RootNode;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.MaterialClass;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class VanillaEnchantment extends CustomEnchantment {
    Enchantment vanilla;
    Map<MaterialClass, Integer> enchantability;

    public VanillaEnchantment(Enchantment vanilla, Material[] items, String group, String[] suffixGroup, int weight, double base, double interval, int max, String name) {
        super(name, items, group, weight);
        this.base = base;
        this.interval = interval;
        this.max = max;
        this.vanilla = vanilla;
        this.weight = new HashMap();
        this.weight.put(MaterialClass.DEFAULT, weight);
        this.enchantability = new HashMap();
        String[] arr$ = suffixGroup;
        int len$ = suffixGroup.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String sGroup = arr$[i$];
            this.suffixGroups.add(sGroup);
        }

    }

    public Enchantment getVanillaEnchant() {
        return this.vanilla;
    }

    public String name() {
        return this.vanilla.getName();
    }

    public boolean isTableEnabled() {
        return this.isTableEnabled && ((RootConfig)((LumberTycoon)Bukkit.getPluginManager().getPlugin("LumberTycoon")).getModuleForClass(RootConfig.class)).getBoolean(RootNode.VANILLA_TABLE);
    }

    public ItemStack addToItem(ItemStack item, int level) {
        if (item.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta)item.getItemMeta();
            if (meta.hasStoredEnchant(this.vanilla)) {
                if (meta.getEnchantLevel(this.vanilla) >= level) {
                    return item;
                }

                meta.removeStoredEnchant(this.vanilla);
                item.setItemMeta(meta);
            }
        }

        item.addUnsafeEnchantment(this.vanilla, level);
        return item;
    }

    public ItemStack removeFromItem(ItemStack item) {
        if (item.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta)item.getItemMeta();
            if (meta.hasStoredEnchant(this.vanilla)) {
                meta.removeStoredEnchant(this.vanilla);
                item.setItemMeta(meta);
                return item;
            }
        }

        item.removeEnchantment(this.vanilla);
        return item;
    }

    @Override
    public void applyEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageByEntityEvent event) {

    }
}

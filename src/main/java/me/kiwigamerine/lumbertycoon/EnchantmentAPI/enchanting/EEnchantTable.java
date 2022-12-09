package me.kiwigamerine.lumbertycoon.EnchantmentAPI.enchanting;

import me.kiwigamerine.lumbertycoon.EnchantmentAPI.CustomEnchantment;
import me.kiwigamerine.lumbertycoon.LumberTycoon;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.MaterialClass;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EEnchantTable {
    static final int MAX_TRIES = 10;

    public EEnchantTable() {
    }

    public static EnchantResult enchant(Player enchanter, ItemStack item, int enchantLevel, int maxEnchants) {
        return enchant(enchanter, item, enchantLevel, maxEnchants, true);
    }

    public static EnchantResult enchant(Player enchanter, ItemStack item, int enchantLevel, int maxEnchants, boolean apply) {
        EnchantResult result = new EnchantResult();
        boolean chooseEnchantment = true;
        Map<CustomEnchantment, Integer> chosenEnchantWithCost = new HashMap();
        List<CustomEnchantment> validEnchants = enchanter != null ? LumberTycoon.getAllValidEnchants(item, enchanter) : LumberTycoon.getAllValidEnchants(item);
        int totalWeight = weightOfAllEnchants(validEnchants);
        int max = 0;

        while(chooseEnchantment && validEnchants.size() > 0) {
            chooseEnchantment = false;
            enchantLevel = modifiedLevel(enchantLevel, MaterialClass.getEnchantabilityFor(item.getType()));
            result.setLevel(enchantLevel);
            int tries = 0;
            max = enchantLevel > max ? enchantLevel : max;

            CustomEnchantment enchant;
            do {
                enchant = weightedRandom(validEnchants, totalWeight);
                int level = enchant.getEnchantLevel(max);
                if (level >= 1) {
                    chosenEnchantWithCost.put(enchant, level);
                    totalWeight -= enchant.getWeight();
                    validEnchants.remove(enchant);
                    result.setFirstEnchant(enchant);
                    break;
                }
            } while(result.getFirstEnchant() == null && (tries++ < 10 || result.getFirstEnchant() == null));

            if (Math.random() < (double)(enchantLevel + 1) / 50.0) {
                chooseEnchantment = true;
            }

            enchantLevel /= 2;
            if (item.getType() == Material.BOOK && chosenEnchantWithCost.size() == 2) {
                chooseEnchantment = false;
            }

            if (chosenEnchantWithCost.size() == maxEnchants) {
                chooseEnchantment = false;
            }

            if (chooseEnchantment) {
                for(int i = 0; i < validEnchants.size(); ++i) {
                    if (enchant.conflictsWith((CustomEnchantment)validEnchants.get(i))) {
                        totalWeight -= ((CustomEnchantment)validEnchants.get(i)).getWeight();
                        validEnchants.remove(i--);
                    }
                }

                if (validEnchants.size() == 0) {
                    chooseEnchantment = false;
                }
            }
        }

        result.setAddedEnchants(chosenEnchantWithCost);
        boolean success = false;
        if (apply) {
            for(Iterator i$ = chosenEnchantWithCost.entrySet().iterator(); i$.hasNext(); success = true) {
                Map.Entry<CustomEnchantment, Integer> enchantCostEntry = (Map.Entry)i$.next();
                CustomEnchantment selectedEnchant = (CustomEnchantment)enchantCostEntry.getKey();
                int levelCost = (Integer)enchantCostEntry.getValue();
                if (selectedEnchant == null) {
                    result.setItem(item);
                    return result;
                }

                selectedEnchant.addToItem(item, levelCost);
            }
        }

        if (success) {
            result.setItem(item);
        }

        return result;
    }

    static int modifiedLevel(int expLevel, int enchantability) {
        expLevel = expLevel + random(enchantability / 4 * 2) + 1;
        double bonus = random(0.3) + 0.85;
        return (int)((double)expLevel * bonus + 0.5);
    }

    static int random(int max) {
        return (int)(Math.random() * (double)max / 2.0 + Math.random() * (double)max / 2.0);
    }

    static double random(double max) {
        return Math.random() * max / 2.0 + Math.random() * max / 2.0;
    }

    static CustomEnchantment weightedRandom(Collection<CustomEnchantment> enchantments, int totalWeight) {
        int random = (new Random()).nextInt(totalWeight);
        Iterator<CustomEnchantment> iter = enchantments.iterator();
        CustomEnchantment enchantment = null;
        int current = 0;

        do {
            if (!iter.hasNext()) {
                return enchantment;
            }

            enchantment = (CustomEnchantment)iter.next();
            current += enchantment.getWeight();
        } while(random >= current);

        return enchantment;
    }

    static int weightOfAllEnchants(Collection<CustomEnchantment> validEnchants) {
        int count = 0;

        CustomEnchantment enchantment;
        for(Iterator i$ = validEnchants.iterator(); i$.hasNext(); count += enchantment.getWeight()) {
            enchantment = (CustomEnchantment)i$.next();
        }

        return count;
    }
}

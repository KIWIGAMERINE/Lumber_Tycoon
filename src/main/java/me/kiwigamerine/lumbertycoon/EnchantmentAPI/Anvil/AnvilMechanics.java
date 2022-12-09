package me.kiwigamerine.lumbertycoon.EnchantmentAPI.Anvil;

import me.kiwigamerine.lumbertycoon.EnchantmentAPI.CustomEnchantment;
import me.kiwigamerine.lumbertycoon.LumberTycoon;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AnvilMechanics {
    private static final HashMap<String, Material> REPAIR_MATS = new HashMap<String, Material>() {
        {
            this.put("WOOD_", Material.WOOD);
            this.put("STONE_", Material.COBBLESTONE);
            this.put("CHAINMAIL_", Material.IRON_INGOT);
            this.put("IRON_", Material.IRON_INGOT);
            this.put("GOLD_", Material.GOLD_INGOT);
            this.put("DIAMOND_", Material.DIAMOND);
        }
    };

    public AnvilMechanics() {
    }

    public static void updateResult(AnvilView view, ItemStack[] input, String name) {
        if (input.length < 2) {
            view.setResultSlot((ItemStack)null);
        } else {
            ItemStack first = input[0];
            ItemStack second = input[1];
            int cost;
            ItemStack result;
            ItemMeta meta;
            if (isFilled(first) && !isFilled(second) && name != null) {
                result = first.clone();
                meta = result.getItemMeta() == null ? Bukkit.getItemFactory().getItemMeta(result.getType()) : result.getItemMeta();
                meta.setDisplayName(name);
                result.setItemMeta(meta);
                view.setResultSlot(result);
                cost = getBaseCost(first, false) + (first.getType().getMaxDurability() != 0 ? 7 : 5 * first.getAmount());
                view.setRepairCost(cost > 39 ? 39 : cost);
            } else if (!isFilled(first) && isFilled(second) && name != null) {
                result = second.clone();
                meta = result.getItemMeta() == null ? Bukkit.getItemFactory().getItemMeta(result.getType()) : result.getItemMeta();
                meta.setDisplayName(name);
                result.setItemMeta(meta);
                view.setResultSlot(result);
                cost = getBaseCost(first, false) + (first.getType().getMaxDurability() != 0 ? 7 : 5 * first.getAmount());
                view.setRepairCost(cost > 39 ? 39 : cost);
            } else if (isFilled(first) && isFilled(second)) {
                if (first.getType() != second.getType()) {
                    if (isBook(first)) {
                        validateBook(view, first, second);
                    } else if (isBook(second)) {
                        validateBook(view, second, first);
                    } else {
                        Iterator i$ = REPAIR_MATS.entrySet().iterator();

                        while(true) {
                            while(i$.hasNext()) {
                                Map.Entry<String, Material> entry = (Map.Entry)i$.next();
                                if (first.getType().name().contains((CharSequence)entry.getKey()) && second.getType() == entry.getValue()) {
                                    cost = getMaterialRepairCost(first, second) + getBaseCost(first, false);
                                    view.setResultSlot(makeItem(first, second.getAmount()));
                                    view.setRepairCost(cost);
                                } else if (second.getType().name().contains((CharSequence)entry.getKey()) && first.getType() == entry.getValue()) {
                                    cost = getMaterialRepairCost(second, first) + getBaseCost(second, false);
                                    view.setResultSlot(makeItem(second, first.getAmount()));
                                    view.setRepairCost(cost);
                                }
                            }

                            return;
                        }
                    }
                } else if (first.getAmount() > 1) {
                    view.setResultSlot((ItemStack)null);
                } else if (second.getAmount() > 1) {
                    view.setResultSlot((ItemStack)null);
                } else if (LumberTycoon.getAllEnchantments(first).size() * LumberTycoon.getAllEnchantments(second).size() > 0) {
                    view.setResultSlot(makeItem(first, second));
                    view.setRepairCost(getCombineCost(first, second));
                } else if (first.getDurability() > 0) {
                    view.setResultSlot(makeItem(first, second));
                    view.setRepairCost(getCombineCost(first, second));
                } else {
                    view.setResultSlot((ItemStack)null);
                }
            } else {
                view.setResultSlot((ItemStack)null);
            }

        }
    }

    public static void updateResult(AnvilView view, ItemStack[] input) {
        updateResult(view, input, (String)null);
    }

    private static int getBaseCost(ItemStack item, boolean withBook) {
        int cost = 0;
        int count = 0;

        for(Iterator i$ = LumberTycoon.getAllEnchantments(item).entrySet().iterator(); i$.hasNext(); ++count) {
            Map.Entry<CustomEnchantment, Integer> entry = (Map.Entry)i$.next();
            cost += ((CustomEnchantment)entry.getKey()).getCostPerLevel(withBook) * (Integer)entry.getValue();
        }

        return cost + (int)((double)(count + 1) * ((double)count / 2.0) + 0.5);
    }

    private static int getMaterialRepairCost(ItemStack item, ItemStack material) {
        int m = material.getType() == Material.DIAMOND ? 3 : 1;
        int cost = 4 * m * item.getDurability() / item.getType().getMaxDurability() + 1;
        if (cost > m * material.getAmount()) {
            cost = m * material.getAmount();
        }

        return cost;
    }

    private static int getCombineCost(ItemStack first, ItemStack second) {
        boolean book = isBook(second);
        double m = book ? 0.5 : 1.0;
        int cost = 0;
        int extra = getBaseCost(first, book);
        if (first.getDurability() > 0 && first.getType() == second.getType()) {
            int extraCost = (int)(((double)durability(second) + 0.12 * (double)first.getType().getMaxDurability()) / 100.0);
            cost += extraCost > 0 ? extraCost : 1;
        }

        Set<Map.Entry<CustomEnchantment, Integer>> enchants = LumberTycoon.getAllEnchantments(first).entrySet();
        int newEnchants = 0;
        Iterator i$ = LumberTycoon.getAllEnchantments(second).entrySet().iterator();

        while(i$.hasNext()) {
            Map.Entry<CustomEnchantment, Integer> entry = (Map.Entry)i$.next();
            boolean conflict = false;
            boolean needsCost = false;
            if (((CustomEnchantment)entry.getKey()).canEnchantOnto(first)) {
                i$ = enchants.iterator();

                while(i$.hasNext()) {
                    Map.Entry<CustomEnchantment, Integer> e = (Map.Entry)i$.next();
                    if (((CustomEnchantment)e.getKey()).name().equals(((CustomEnchantment)entry.getKey()).name())) {
                        if ((Integer)e.getValue() < (Integer)entry.getValue()) {
                            cost += ((Integer)entry.getValue() - (Integer)e.getValue()) * ((CustomEnchantment)entry.getKey()).getCostPerLevel(book);
                            extra += ((Integer)entry.getValue() - (Integer)e.getValue()) * ((CustomEnchantment)entry.getKey()).getCostPerLevel(book);
                        } else if (((Integer)e.getValue()).equals(entry.getValue())) {
                            cost += ((CustomEnchantment)entry.getKey()).getCostPerLevel(false);
                            extra += ((CustomEnchantment)entry.getKey()).getCostPerLevel(false);
                        } else {
                            needsCost = true;
                        }

                        conflict = true;
                    } else if (((CustomEnchantment)e.getKey()).conflictsWith((CustomEnchantment)entry.getKey())) {
                        conflict = true;
                        needsCost = true;
                    }
                }
            } else {
                needsCost = true;
                conflict = true;
            }

            if (!conflict) {
                ++newEnchants;
                cost += (Integer)entry.getValue() * ((CustomEnchantment)entry.getKey()).getCostPerLevel(book);
                extra += (Integer)entry.getValue() * ((CustomEnchantment)entry.getKey()).getCostPerLevel(book);
            }

            if (needsCost) {
                cost += (Integer)entry.getValue();
            }
        }

        if (newEnchants > 0) {
            extra += newEnchants * (enchants.size() + newEnchants - 1) + 1;
        }

        return cost + (int)(m * (double)extra);
    }

    static void validateBook(AnvilView inv, ItemStack book, ItemStack target) {
        Map<CustomEnchantment, Integer> enchants = LumberTycoon.getAllEnchantments(book);
        boolean valid = false;
        if (target.getType() != Material.BOOK && target.getType() != Material.ENCHANTED_BOOK) {
            Iterator i$ = enchants.keySet().iterator();

            while(i$.hasNext()) {
                CustomEnchantment e = (CustomEnchantment)i$.next();
                if (e.canEnchantOnto(target)) {
                    valid = true;
                }
            }
        }

        if (valid && enchants.size() > 0 && book.getAmount() == 1 && target.getAmount() == 1) {
            inv.setResultSlot(makeItem(target, book));
            inv.setRepairCost(getCombineCost(target, book));
        } else {
            inv.setResultSlot((ItemStack)null);
        }

    }

    static ItemStack makeItem(ItemStack item, int amount) {
        ItemStack newItem = item.clone();
        if ((double)item.getDurability() - (double)item.getType().getMaxDurability() * 0.25 * (double)amount < 0.0) {
            newItem.setDurability((short)0);
        } else {
            newItem.setDurability((short)((int)((double)item.getDurability() - (double)(item.getType().getMaxDurability() * amount) * 0.25)));
        }

        return newItem;
    }

    static ItemStack makeItem(ItemStack primary, ItemStack secondary) {
        ItemStack item = new ItemStack(primary.getType());
        if (primary.hasItemMeta()) {
            item.setItemMeta(primary.getItemMeta());
        }

        if (primary.getDurability() > 0 && primary.getType() == secondary.getType()) {
            if ((double)(durability(item) + durability(secondary)) < 0.88 * (double)primary.getType().getMaxDurability()) {
                setDurability(item, (short)((int)((double)(durability(primary) + durability(secondary)) + 0.12 * (double)primary.getType().getMaxDurability())));
            }
        } else {
            item.setDurability(primary.getDurability());
        }

        Set<Map.Entry<CustomEnchantment, Integer>> enchants = LumberTycoon.getAllEnchantments(primary).entrySet();
        Iterator i$ = LumberTycoon.getAllEnchantments(secondary).entrySet().iterator();

        while(i$.hasNext()) {
            Map.Entry<CustomEnchantment, Integer> entry = (Map.Entry)i$.next();
            boolean conflict = false;
            if (((CustomEnchantment)entry.getKey()).canEnchantOnto(item)) {
                i$ = enchants.iterator();

                while(i$.hasNext()) {
                    Map.Entry<CustomEnchantment, Integer> e = (Map.Entry)i$.next();
                    if (((CustomEnchantment)e.getKey()).name().equals(((CustomEnchantment)entry.getKey()).name())) {
                        ((CustomEnchantment)e.getKey()).removeFromItem(item);
                        int level = (Integer)e.getValue();
                        if ((Integer)entry.getValue() > level) {
                            level = (Integer)entry.getValue();
                        } else if ((Integer)entry.getValue() == level) {
                            level = Math.min(level + 1, ((CustomEnchantment)e.getKey()).getMaxLevel());
                        }

                        ((CustomEnchantment)e.getKey()).addToItem(item, level);
                        conflict = true;
                    } else if (((CustomEnchantment)e.getKey()).conflictsWith((CustomEnchantment)entry.getKey())) {
                        conflict = true;
                    }
                }
            } else {
                conflict = true;
            }

            if (!conflict) {
                ((CustomEnchantment)entry.getKey()).addToItem(item, (Integer)entry.getValue());
            }
        }

        return item;
    }

    static boolean isFilled(ItemStack item) {
        return item != null && item.getType() != Material.AIR;
    }

    static boolean isBook(ItemStack item) {
        return item.getType() == Material.BOOK || item.getType() == Material.ENCHANTED_BOOK;
    }

    static short durability(ItemStack item) {
        return (short)(item.getType().getMaxDurability() - item.getDurability());
    }

    static ItemStack setDurability(ItemStack item, short value) {
        item.setDurability((short)(item.getType().getMaxDurability() - value));
        return item;
    }
}

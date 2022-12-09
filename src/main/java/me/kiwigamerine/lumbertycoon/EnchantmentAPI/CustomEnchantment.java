package me.kiwigamerine.lumbertycoon.EnchantmentAPI;

import me.kiwigamerine.lumbertycoon.EnchantmentAPI.config.RootConfig;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.config.RootNode;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.ENameParser;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.ERomanNumeral;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.MaterialClass;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.MaterialsParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.kiwigamerine.lumbertycoon.LumberTycoon;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class CustomEnchantment implements Comparable<CustomEnchantment> {
    public static final String DEFAULT_GROUP = "Default";
    protected List<String> suffixGroups;
    protected final String enchantName;
    protected String description;
    protected Material[] naturalItems;
    protected Map<MaterialClass, Integer> weight;
    protected boolean isEnabled;
    protected boolean isTableEnabled;
    protected String group;
    protected double interval;
    protected double base;
    protected int max;
    protected boolean stacks;

    public CustomEnchantment(String name) {
        this(name, (String)null, new Material[0], "Default", 5);
    }

    /** @deprecated */
    public CustomEnchantment(String name, String[] naturalItems) {
        this(name, (String)null, MaterialsParser.toMaterial(naturalItems), "Default", 5);
    }

    public CustomEnchantment(String name, Material[] naturalItems) {
        this(name, (String)null, naturalItems, "Default", 5);
    }

    public CustomEnchantment(String name, String description) {
        this(name, description, new Material[0], "Default", 5);
    }

    /** @deprecated */
    public CustomEnchantment(String name, String[] naturalItems, int weight) {
        this(name, (String)null, MaterialsParser.toMaterial(naturalItems), "Default", weight);
    }

    public CustomEnchantment(String name, Material[] naturalItems, int weight) {
        this(name, (String)null, naturalItems, "Default", weight);
    }

    public CustomEnchantment(String name, Material[] naturalItems, String group) {
        this(name, (String)null, naturalItems, group, 5);
    }

    public CustomEnchantment(String name, String description, Material[] naturalItems) {
        this(name, description, naturalItems, "Default", 5);
    }

    public CustomEnchantment(String name, String description, String group) {
        this(name, description, new Material[0], group, 5);
    }

    public CustomEnchantment(String name, String description, int weight) {
        this(name, description, new Material[0], "Default", 5);
    }

    public CustomEnchantment(String name, String description, Material[] naturalItems, String group) {
        this(name, description, naturalItems, group, 5);
    }

    public CustomEnchantment(String name, String description, Material[] naturalItems, int weight) {
        this(name, description, naturalItems, "Default", 5);
    }

    public CustomEnchantment(String name, String description, String group, int weight) {
        this(name, description, new Material[0], group, weight);
    }

    public CustomEnchantment(String name, Material[] naturalItems, String group, int weight) {
        this(name, (String)null, naturalItems, group, weight);
    }

    public CustomEnchantment(String name, String description, Material[] naturalItems, String group, int weight) {
        this.suffixGroups = new ArrayList();
        Validate.notEmpty(name, "Your Enchantment needs a name!");
        Validate.notNull(naturalItems, "Input an empty array instead of \"null\"!");
        Validate.isTrue(weight >= 0, "Weight can't be negative!");
        this.enchantName = name;
        this.description = description;
        this.naturalItems = naturalItems;
        this.isEnabled = true;
        this.group = group;
        this.max = 1;
        this.base = 1.0;
        this.interval = 10.0;
        this.isTableEnabled = true;
        this.stacks = false;
        this.weight = new HashMap();
        this.weight.put(MaterialClass.DEFAULT, weight);
    }

    public String name() {
        return this.enchantName;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean canStack() {
        return this.stacks;
    }

    public void setCanStack(boolean stack) {
        this.stacks = stack;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setTableEnabled(boolean value) {
        this.isTableEnabled = value;
    }

    public boolean isTableEnabled() {
        return this.isTableEnabled && ((RootConfig)((LumberTycoon)Bukkit.getPluginManager().getPlugin("LumberTycoon")).getModuleForClass(RootConfig.class)).getBoolean(RootNode.CUSTOM_TABLE);
    }

    public int getEnchantLevel(int expLevel) {
        for(int i = this.max; i >= 1; --i) {
            if ((double)expLevel >= this.base + this.interval * (double)(i - 1)) {
                return i;
            }
        }

        return 0;
    }

    public int getMaxLevel() {
        return this.max;
    }

    public void setMaxLevel(int value) {
        this.max = value;
    }

    public double getBase() {
        return this.base;
    }

    public void setBase(double value) {
        this.base = value;
    }

    public double getInterval() {
        return this.interval;
    }

    public void setInterval(double value) {
        this.interval = value;
    }

    public List<String> getSuffixGroups() {
        return this.suffixGroups;
    }

    public int getCostPerLevel(boolean withBook) {
        int costIndex = (Integer)this.weight.get(MaterialClass.DEFAULT) * this.max;
        int divisor = withBook ? 2 : 1;
        return (Integer)this.weight.get(MaterialClass.DEFAULT) == 1 ? 8 / divisor : (costIndex < 10 ? 4 / divisor : (costIndex < 30 ? 2 / divisor : 1));
    }

    public void setNaturalMaterials(Material[] materials) {
        this.naturalItems = materials;
    }

    /** @deprecated */
    public String[] getNaturalItems() {
        String[] natItems = new String[this.naturalItems.length];

        for(int i = 0; i < this.naturalItems.length; ++i) {
            natItems[i] = this.naturalItems[i].name();
        }

        return natItems;
    }

    public Material[] getNaturalMaterials() {
        return this.naturalItems;
    }

    public void setWeight(int weight) {
        this.weight.put(MaterialClass.DEFAULT, weight);
    }

    public int getWeight() {
        return (Integer)this.weight.get(MaterialClass.DEFAULT);
    }

    public int getWeight(MaterialClass material) {
        return this.weight.containsKey(material) ? (Integer)this.weight.get(material) : (Integer)this.weight.get(MaterialClass.DEFAULT);
    }

    public boolean canEnchantOnto(ItemStack item) {
        if (this.naturalItems != null && item != null) {
            Material[] arr$ = this.naturalItems;
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                Material validItem = arr$[i$];
                if (item.getType() == validItem) {
                    return true;
                }
            }

            return item.getType() == Material.BOOK || item.getType() == Material.ENCHANTED_BOOK;
        } else {
            return false;
        }
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroup() {
        return this.group;
    }

    public boolean conflictsWith(CustomEnchantment enchantment) {
        Validate.notNull(enchantment);
        return !this.group.equals("Default") && this.group.equalsIgnoreCase(enchantment.group);
    }

    public boolean conflictsWith(List<CustomEnchantment> enchantmentsToCheck) {
        Validate.notNull(enchantmentsToCheck);
        Iterator i$ = enchantmentsToCheck.iterator();

        CustomEnchantment enchantment;
        do {
            if (!i$.hasNext()) {
                return false;
            }

            enchantment = (CustomEnchantment)i$.next();
        } while(!this.conflictsWith(enchantment));

        return true;
    }

    public boolean conflictsWith(CustomEnchantment... enchantmentsToCheck) {
        Validate.notNull(enchantmentsToCheck);
        CustomEnchantment[] arr$ = enchantmentsToCheck;
        int len$ = enchantmentsToCheck.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            CustomEnchantment enchantment = arr$[i$];
            if (this.conflictsWith(enchantment)) {
                return true;
            }
        }

        return false;
    }

    public ItemStack addToItem(ItemStack item, int enchantLevel) {
        Validate.notNull(item);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            meta = Bukkit.getServer().getItemFactory().getItemMeta(item.getType());
        }

        List<String> metaLore = meta.getLore() == null ? new ArrayList() : meta.getLore();
        Iterator i$ = LumberTycoon.getEnchantments(item).entrySet().iterator();

        while(i$.hasNext()) {
            Map.Entry<CustomEnchantment, Integer> entry = (Map.Entry)i$.next();
            if (((CustomEnchantment)entry.getKey()).name().equals(this.name())) {
                if ((Integer)entry.getValue() >= enchantLevel) {
                    return item;
                }

                ((List)metaLore).remove(ChatColor.GRAY + this.name() + " " + ERomanNumeral.numeralOf((Integer)entry.getValue()));
            }
        }

        ((List)metaLore).add(0, ChatColor.GRAY + this.enchantName + " " + ERomanNumeral.numeralOf(enchantLevel));
        meta.setLore((List)metaLore);
        String name = ENameParser.getName(item);
        if (name != null) {
            meta.setDisplayName(name);
        }

        item.setItemMeta(meta);
        return item;
    }

    public ItemStack removeFromItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        } else if (!meta.hasLore()) {
            return item;
        } else {
            List<String> metaLore = meta.getLore();
            Iterator i$ = LumberTycoon.getEnchantments(item).entrySet().iterator();

            while(i$.hasNext()) {
                Map.Entry<CustomEnchantment, Integer> entry = (Map.Entry)i$.next();
                if (((CustomEnchantment)entry.getKey()).name().equals(this.name())) {
                    metaLore.remove(ChatColor.GRAY + this.name() + " " + ERomanNumeral.numeralOf((Integer)entry.getValue()));
                }
            }

            return item;
        }
    }

    public boolean equals(Object obj) {
        return obj instanceof CustomEnchantment && this.name().equalsIgnoreCase(((CustomEnchantment)obj).name());
    }

    public int compareTo(CustomEnchantment customEnchantment) {
        return this.name().compareTo(customEnchantment.name());
    }

    public void applyEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageByEntityEvent event) {
    }

    public void applyDefenseEffect(LivingEntity user, LivingEntity target, int enchantLevel, EntityDamageEvent event) {
    }

    public void applyToolEffect(Player player, Block block, int enchantLevel, BlockEvent event) {
    }

    public void applyMiscEffect(Player player, int enchantLevel, PlayerInteractEvent event) {
    }

    public void applyEquipEffect(Player player, int enchantLevel) {
    }

    public void applyUnequipEffect(Player player, int enchantLevel) {
    }

    public void applyEntityEffect(Player player, int enchantLevel, PlayerInteractEntityEvent event) {
    }

    public void applyProjectileEffect(LivingEntity user, int enchantLevel, ProjectileLaunchEvent event) {
    }
}

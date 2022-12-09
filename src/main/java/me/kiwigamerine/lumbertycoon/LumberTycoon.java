package me.kiwigamerine.lumbertycoon;

import me.kiwigamerine.lumbertycoon.EnchantmentAPI.CustomEnchantment;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.EnchantPlugin;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.commands.Commander;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.config.RootConfig;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.enchanting.EEquip;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.enchanting.VanillaData;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.enchanting.VanillaEnchantment;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.ENameParser;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.ERomanNumeral;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.PermissionNode;
import me.kiwigamerine.lumbertycoon.commands.Test_commands;
import me.kiwigamerine.lumbertycoon.enchants.LifestealEnchantment;
import me.kiwigamerine.lumbertycoon.listeners.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.java.JavaPlugin;


import me.kiwigamerine.lumbertycoon.EnchantmentAPI.Anvil.AnvilListener;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.config.RootNode;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.enchanting.EListener;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.lore.LoreConfig;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.IModule;

import java.util.*;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public final class LumberTycoon extends JavaPlugin {
    private static Hashtable<String, CustomEnchantment> enchantments = new Hashtable();
    private Hashtable<String, List<String>> adjectives;
    private Hashtable<String, List<String>> weapons;
    private Hashtable<String, List<String>> suffixes;
    private final Map<Class<? extends IModule>, IModule> modules = new HashMap();
    private static String TAG = "[EnchantAPI]";

    public void EnchantmentAPI() {
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("The Lumber Tycoon Plugin has Started.");


        //Commands
        Test_commands commands = new Test_commands();
        getCommand("heal").setExecutor(commands);
        getCommand("feed").setExecutor(commands);


        //Listeners
        getServer().getPluginManager().registerEvents(new TemplateListener(),this);
        getServer().getPluginManager().registerEvents(new BreakOakLog(),this);
        getServer().getPluginManager().registerEvents(new BreakSpruceLog(),this);
        getServer().getPluginManager().registerEvents(new BreakBirchLog(),this);
        getServer().getPluginManager().registerEvents(new BreakJungleLog(),this);
        getServer().getPluginManager().registerEvents(new BreakAnyBlock(),this);

        //start from enchantmentAPI logic
        System.out.println("The Lumber Tycoon Enchant API has Started.");
        this.getCommand("enchantapi").setExecutor(new Commander(this));
        this.registerModule(RootConfig.class, new RootConfig(this));
        this.reload();
        
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("The Lumber Tycoon Plugin has Stopped.");

        //stop from enchantmentAPI logic
        HandlerList.unregisterAll(this);
        enchantments.clear();
        EEquip.clear();

    }
    public void reload() {
        this.adjectives = (new LoreConfig(this, "adjectives")).getLists();
        this.weapons = (new LoreConfig(this, "weapons")).getLists();
        this.suffixes = (new LoreConfig(this, "suffixes")).getLists();
        HandlerList.unregisterAll(this);
        EEquip.clear();
        enchantments.clear();
        this.loadVanillaEnchantments();
        Plugin[] arr$ = this.getServer().getPluginManager().getPlugins();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Plugin plugin = arr$[i$];
            if (plugin instanceof EnchantPlugin) {
                try {
                    ((EnchantPlugin)plugin).registerEnchantments();
                    this.getLogger().info("Loaded enchantments from plugin: " + plugin.getName());
                } catch (Exception var6) {
                    this.getLogger().info("Failed to load enchantments from " + plugin.getName() + ": perhaps it is outdated?");
                }
            }
        }

        Iterator i$ = this.getServer().getOnlinePlayers().iterator();

        while(i$.hasNext()) {
            Player player = (Player)i$.next();
            EEquip.loadPlayer(player);
        }

        ((RootConfig)this.getModuleForClass(RootConfig.class)).reload();
        new EListener(this);
        if (((RootConfig)this.getModuleForClass(RootConfig.class)).getBoolean(RootNode.ANVIL_ENABLED)) {
            new AnvilListener(this);
        }

    }

    public static List<CustomEnchantment> getAllValidEnchants(ItemStack item) {
        return getAllValidEnchants(item, 45);
    }

    public static List<CustomEnchantment> getAllValidEnchants(ItemStack item, int level) {
        List<CustomEnchantment> validEnchantments = new ArrayList();
        boolean book = item.getType() == Material.BOOK;
        Iterator i$ = getEnchantments().iterator();

        while(true) {
            CustomEnchantment enchantment;
            do {
                if (!i$.hasNext()) {
                    return validEnchantments;
                }

                enchantment = (CustomEnchantment)i$.next();
            } while(!enchantment.canEnchantOnto(item) && !book);

            if (enchantment.isEnabled() && enchantment.getEnchantLevel(level) >= 1 && enchantment.isTableEnabled()) {
                validEnchantments.add(enchantment);
            }
        }
    }

    public static List<CustomEnchantment> getAllValidEnchants(ItemStack item, Permissible enchanter) {
        return getAllValidEnchants(item, enchanter, 45);
    }

    public static List<CustomEnchantment> getAllValidEnchants(ItemStack item, Permissible enchanter, int level) {
        List<CustomEnchantment> validEnchantments = new ArrayList();
        boolean book = item.getType() == Material.BOOK;
        Iterator i$ = getEnchantments().iterator();

        while(true) {
            CustomEnchantment enchantment;
            do {
                do {
                    do {
                        do {
                            do {
                                if (!i$.hasNext()) {
                                    return validEnchantments;
                                }

                                enchantment = (CustomEnchantment)i$.next();
                            } while(!enchantment.canEnchantOnto(item) && !book);
                        } while(!enchantment.isEnabled());
                    } while(enchantment.getEnchantLevel(level) < 1);
                } while(!enchantment.isTableEnabled());
            } while(!enchanter.hasPermission(PermissionNode.ENCHANT.getNode()) && (!enchanter.hasPermission(PermissionNode.ENCHANT_VANILLA.getNode()) || !(enchantment instanceof VanillaEnchantment)) && !enchanter.hasPermission(PermissionNode.ENCHANT.getNode() + "." + enchantment.name().replace(" ", "").toLowerCase()));

            validEnchantments.add(enchantment);
        }
    }

    public String getAdjective(int level) {
        if (this.adjectives.containsKey("tier" + level)) {
            List<String> list = (List)this.adjectives.get("tier" + level);
            return (String)list.get((int)(Math.random() * (double)list.size()));
        } else {
            ArrayList<String> all = new ArrayList();
            Iterator i$ = this.adjectives.values().iterator();

            while(i$.hasNext()) {
                List<String> list = (List)i$.next();
                i$ = list.iterator();

                while(i$.hasNext()) {
                    String adjective = (String)i$.next();
                    all.add(adjective);
                }
            }

            return (String)all.get((int)(Math.random() * (double)all.size()));
        }
    }

    public String getWeapon(String item) {
        String[] pieces = item.split("_");
        String type = pieces[pieces.length - 1].toLowerCase();
        if (this.weapons.containsKey(type)) {
            List<String> list = (List)this.weapons.get(type);
            return (String)list.get((int)(Math.random() * (double)list.size()));
        } else {
            return type.substring(0, 1).toUpperCase() + type.substring(1);
        }
    }

    public String getSuffix(CustomEnchantment enchant) {
        ArrayList<String> options = new ArrayList();
        Iterator i$;
        String suffix;
        if (enchant.getSuffixGroups().size() == 0) {
            i$ = this.suffixes.values().iterator();

            while(i$.hasNext()) {
                List<String> list = (List)i$.next();
                i$ = list.iterator();

                while(i$.hasNext()) {
                    suffix = (String)i$.next();
                    if (!options.contains(suffix)) {
                        options.add(suffix);
                    }
                }
            }
        } else {
            i$ = enchant.getSuffixGroups().iterator();

            while(i$.hasNext()) {
                String group = (String)i$.next();
                i$ = ((List)this.suffixes.get(group)).iterator();

                while(i$.hasNext()) {
                    suffix = (String)i$.next();
                    if (!options.contains(suffix)) {
                        options.add(suffix);
                    }
                }
            }
        }

        return (String)options.get((int)(Math.random() * (double)options.size()));
    }

    private void loadVanillaEnchantments() {
        VanillaData[] arr$ = VanillaData.values();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            VanillaData defaults = arr$[i$];
            VanillaEnchantment vanilla = new VanillaEnchantment(defaults.getEnchantment(), defaults.getItems(), defaults.getGroup(), defaults.getSuffixGroup(), defaults.getEnchantWeight(), defaults.getBase(), defaults.getInterval(), defaults.getMaxLevel(), defaults.name());
            registerCustomEnchantment(vanilla);
        }

    }

    public static boolean isRegistered(String enchantmentName) {
        return enchantments.containsKey(enchantmentName.toUpperCase());
    }

    public static CustomEnchantment getEnchantment(String name) {
        return (CustomEnchantment)enchantments.get(name.toUpperCase());
    }

    public static Set<String> getEnchantmentNames() {
        return enchantments.keySet();
    }

    public static Collection<CustomEnchantment> getEnchantments() {
        return enchantments.values();
    }

    public static boolean registerCustomEnchantment(CustomEnchantment enchantment) {
        if (enchantments.containsKey(enchantment.name().toUpperCase())) {
            return false;
        } else if (!enchantment.isEnabled()) {
            return false;
        } else {
            enchantments.put(enchantment.name().toUpperCase(), enchantment);
            return true;
        }
    }

    public static void registerCustomEnchantments(CustomEnchantment... enchantments) {
        CustomEnchantment[] arr$ = enchantments;
        int len$ = enchantments.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            CustomEnchantment enchantment = arr$[i$];
            registerCustomEnchantment(enchantment);
        }

    }

    public static boolean unregisterCustomEnchantment(String enchantmentName) {
        if (enchantments.containsKey(enchantmentName.toUpperCase())) {
            enchantments.remove(enchantmentName.toUpperCase());
            return true;
        } else {
            return false;
        }
    }

    public static Map<CustomEnchantment, Integer> getEnchantments(ItemStack item) {
        HashMap<CustomEnchantment, Integer> list = new HashMap();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return list;
        } else if (!meta.hasLore()) {
            return list;
        } else {
            Iterator i$ = meta.getLore().iterator();

            while(i$.hasNext()) {
                String lore = (String)i$.next();
                String name = ENameParser.parseName(lore);
                int level = ENameParser.parseLevel(lore);
                if (name != null && level != 0 && isRegistered(name)) {
                    CustomEnchantment enchant = getEnchantment(name);
                    if (enchant.canStack() && list.containsKey(enchant)) {
                        level += (Integer)list.get(enchant);
                    }

                    list.put(enchant, level);
                }
            }

            return list;
        }
    }

    public static Map<CustomEnchantment, Integer> getAllEnchantments(ItemStack item) {
        Map<CustomEnchantment, Integer> map = getEnchantments(item);
        if (item.hasItemMeta() && item.getItemMeta().hasEnchants()) {
            Iterator i$ = item.getEnchantments().entrySet().iterator();

            while(i$.hasNext()) {
                Map.Entry<Enchantment, Integer> entry = (Map.Entry)i$.next();
                map.put(getEnchantment(((Enchantment)entry.getKey()).getName()), entry.getValue());
            }
        }

        if (item.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta)item.getItemMeta();
            Iterator i$ = meta.getStoredEnchants().entrySet().iterator();

            while(i$.hasNext()) {
                Map.Entry<Enchantment, Integer> entry = (Map.Entry)i$.next();
                map.put(getEnchantment(((Enchantment)entry.getKey()).getName()), entry.getValue());
            }
        }

        return map;
    }

    public static boolean itemHasEnchantment(ItemStack item, String enchantmentName) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        } else if (!meta.hasLore()) {
            return false;
        } else {
            Iterator i$ = meta.getLore().iterator();

            String lore;
            do {
                if (!i$.hasNext()) {
                    return false;
                }

                lore = (String)i$.next();
            } while(!lore.contains(enchantmentName) || ENameParser.parseLevel(lore) <= 0);

            return true;
        }
    }

    public static ItemStack removeEnchantments(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        } else if (!meta.hasLore()) {
            return item;
        } else {
            List<String> lore = meta.getLore();
            Iterator i$ = getEnchantments(item).entrySet().iterator();

            while(i$.hasNext()) {
                Map.Entry<CustomEnchantment, Integer> entry = (Map.Entry)i$.next();
                lore.remove(ChatColor.GRAY + ((CustomEnchantment)entry.getKey()).name() + " " + ERomanNumeral.numeralOf((Integer)entry.getValue()));
            }

            meta.setLore(lore);
            item.setItemMeta(meta);
            return item;
        }
    }

    public String getTag() {
        return TAG;
    }

    public static void main(String[] args) {
    }

    <T extends IModule> void registerModule(Class<T> clazz, T module) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null");
        } else if (module == null) {
            throw new IllegalArgumentException("Module cannot be null");
        } else {
            this.modules.put(clazz, module);
            module.starting();
        }
    }

    public <T extends IModule> T deregisterModuleForClass(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null");
        } else {
            T module = (T) clazz.cast(this.modules.get(clazz));
            if (module != null) {
                module.closing();
            }

            return module;
        }
    }

    public <T extends IModule> T getModuleForClass(Class<T> clazz) {
        return (T) clazz.cast(this.modules.get(clazz));
    }

    //Enchants loader
    private class CustomEnchants extends EnchantPlugin {
        //Enchants loader
        @Override
        public void registerEnchantments() {
            registerCustomEnchantment(new LifestealEnchantment());
        }
    }


}

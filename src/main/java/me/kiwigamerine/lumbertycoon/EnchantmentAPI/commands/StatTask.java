package me.kiwigamerine.lumbertycoon.EnchantmentAPI.commands;

import me.kiwigamerine.lumbertycoon.EnchantmentAPI.CustomEnchantment;
import me.kiwigamerine.lumbertycoon.LumberTycoon;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.config.RootConfig;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.config.RootNode;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.enchanting.EEnchantTable;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.enchanting.VanillaEnchantment;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.ENameParser;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class StatTask extends BukkitRunnable {
    private static final DecimalFormat format = new DecimalFormat("##0.0#");
    private final ItemStack item;
    private final CommandSender sender;
    private final int level;
    private final int maxEnchants;

    public StatTask(CommandSender sender, ItemStack item, int level) {
        this.sender = sender;
        this.level = level;
        this.item = item;
        this.maxEnchants = ((RootConfig)((LumberTycoon)Bukkit.getPluginManager().getPlugin("LumberTycoon")).getModuleForClass(RootConfig.class)).getInt(RootNode.MAX_ENCHANTS);
    }

    public void run() {
        List<CustomEnchantment> validEnchants = LumberTycoon.getAllValidEnchants(this.item);
        Hashtable<String, int[]> data = new Hashtable();

        for(int i = 0; i < 100000; ++i) {
            Map<CustomEnchantment, Integer> list = EEnchantTable.enchant((Player)null, this.item, this.level, this.maxEnchants, false).getAddedEnchants();

            Map.Entry entry;
            int[] values;
            for(Iterator i$ = list.entrySet().iterator(); i$.hasNext(); ++values[(Integer)entry.getValue() - 1]) {
                entry = (Map.Entry)i$.next();
                String name = ((CustomEnchantment)entry.getKey()).name();
                if (!data.containsKey(name)) {
                    data.put(name, new int[((CustomEnchantment)entry.getKey()).getMaxLevel()]);
                }

                values = (int[])data.get(name);
            }
        }

        this.sender.sendMessage(ChatColor.GOLD + this.item.getType().name() + ChatColor.DARK_GREEN + " - Enchantment Stats (Lv " + this.level + ")");
        Iterator i$ = validEnchants.iterator();

        while(true) {
            CustomEnchantment enchant;
            do {
                if (!i$.hasNext()) {
                    return;
                }

                enchant = (CustomEnchantment)i$.next();
            } while(enchant.getMaxLevel() == 0);

            String message = enchant.name() + " (";
            if (enchant instanceof VanillaEnchantment) {
                message = ENameParser.getVanillaName(((VanillaEnchantment)enchant).getVanillaEnchant()) + " (";
            }

            if (data.containsKey(enchant.name())) {
                int[] values = (int[])data.get(enchant.name());
                int index;

                for(int i = 0; i < enchant.getMaxLevel(); ++i) {
                    message = message + ChatColor.GOLD + format.format((double)values[i] / 1000.0) + "%" + ChatColor.DARK_GREEN + ", ";
                }
            } else {
                for(int i = 1; i <= enchant.getMaxLevel(); ++i) {
                    message = message + ChatColor.GOLD + "0.0%" + ChatColor.DARK_GREEN + ", ";
                }
            }

            this.sender.sendMessage(ChatColor.DARK_GREEN + message.substring(0, message.length() - 2) + ")");
        }
    }
}
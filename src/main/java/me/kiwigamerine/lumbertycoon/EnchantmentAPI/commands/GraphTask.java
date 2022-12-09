package me.kiwigamerine.lumbertycoon.EnchantmentAPI.commands;

import me.kiwigamerine.lumbertycoon.EnchantmentAPI.CustomEnchantment;
import me.kiwigamerine.lumbertycoon.LumberTycoon;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.config.RootConfig;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.config.RootNode;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.enchanting.EEnchantTable;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class GraphTask extends BukkitRunnable {
    private static final DecimalFormat format = new DecimalFormat("##0.0");
    private final CommandSender sender;
    private final ItemStack item;
    private final CustomEnchantment enchant;
    private final int maxEnchants;

    public GraphTask(CommandSender sender, ItemStack item, CustomEnchantment enchant) {
        this.sender = sender;
        this.item = item;
        this.enchant = enchant;
        this.maxEnchants = ((RootConfig)((LumberTycoon)Bukkit.getPluginManager().getPlugin("LumberTycoon")).getModuleForClass(RootConfig.class)).getInt(RootNode.MAX_ENCHANTS);
    }

    public void run() {
        Hashtable<String, int[]> points = new Hashtable();

        int max;
        for(max = 1; max <= 30; ++max) {
            points.put(this.enchant.name() + max, new int[this.enchant.getMaxLevel()]);
        }

        int i;
        int[] data;
        for(max = 1; max <= 30; ++max) {
            for(i = 0; i < 100000; ++i) {
                Map<CustomEnchantment, Integer> list = EEnchantTable.enchant((Player)null, this.item, max, this.maxEnchants, false).getAddedEnchants();
                Iterator i$ = list.entrySet().iterator();

                while(i$.hasNext()) {
                    Map.Entry<CustomEnchantment, Integer> entry = (Map.Entry)i$.next();
                    if (((CustomEnchantment)entry.getKey()).equals(this.enchant)) {
                        data = (int[])points.get(this.enchant.name() + max);
                        ++data[(Integer)entry.getValue() - 1];
                    }
                }
            }
        }

        max = 0;

        for(i = 1; i <= 30; ++i) {
            data = (int[]) points.get(this.enchant.name() + i);
            if (data != null) {
                for(int k = 0; k < this.enchant.getMaxLevel(); ++k) {
                    if (data[k] > max) {
                        max = data[k];
                    }
                }
            }
        }

        max = (max + 999) / 1000;

        for(i = 9; i >= 0; --i) {
            ChatColor lc = i == 0 ? ChatColor.GRAY : ChatColor.DARK_GRAY;

            String line;
            for(line = ChatColor.GOLD + "" + format.format((double)(i * max) / 10.0) + "-" + format.format((double)((i + 1) * max) / 10.0) + "%" + lc + "_"; line.length() < 16; line = line + "_") {
            }

            line = line + ChatColor.GRAY + "|";

            for(int j = 1; j <= 30; ++j) {
                data = (int[])points.get(this.enchant.name() + j);
                String piece = lc + "_";

                for(int k = 0; k < this.enchant.getMaxLevel(); ++k) {
                    if (data[k] > i * max * 100 && data[k] <= (i + 1) * max * 100) {
                        piece = ChatColor.getByChar((char)(49 + k % 6)) + "X";
                    }
                }

                line = line + piece;
            }

            this.sender.sendMessage(line);
        }

        this.sender.sendMessage(ChatColor.DARK_GRAY + "||__________" + ChatColor.GRAY + "|" + ChatColor.DARK_GRAY + "____" + ChatColor.GRAY + "5" + ChatColor.DARK_GRAY + "____" + ChatColor.GRAY + "10" + ChatColor.DARK_GRAY + "___" + ChatColor.GRAY + "15" + ChatColor.DARK_GRAY + "___" + ChatColor.GRAY + "20" + ChatColor.DARK_GRAY + "___" + ChatColor.GRAY + "25" + ChatColor.DARK_GRAY + "___" + ChatColor.GRAY + "30");
    }
}

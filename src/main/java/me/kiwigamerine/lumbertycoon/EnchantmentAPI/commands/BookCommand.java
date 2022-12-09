package me.kiwigamerine.lumbertycoon.EnchantmentAPI.commands;

import me.kiwigamerine.lumbertycoon.EnchantmentAPI.CustomEnchantment;
import me.kiwigamerine.lumbertycoon.LumberTycoon;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.enchanting.VanillaEnchantment;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.ENameParser;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.ICommand;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class BookCommand implements ICommand {
    public BookCommand() {
    }

    public boolean execute(LumberTycoon plugin, CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        } else {
            ItemStack book = new ItemStack(Material.BOOK_AND_QUILL);
            BookMeta meta = (BookMeta)book.getItemMeta();
            meta.addPage(new String[]{"EnchantmentAPI\nMade by Steven Sucy\n(Eniripsa96)\n\n Enchantment details \n\nCommand:\n\n/enchantapi list #"});
            meta.setAuthor("Eniripsa96");
            meta.setTitle("EnchantmentAPI");
            ArrayList<CustomEnchantment> enchants = new ArrayList(LumberTycoon.getEnchantments());
            Collections.sort(enchants);
            Iterator i$ = enchants.iterator();

            while(true) {
                CustomEnchantment enchantment;
                do {
                    do {
                        if (!i$.hasNext()) {
                            book.setItemMeta(meta);
                            ((Player)sender).getInventory().addItem(new ItemStack[]{book});
                            return true;
                        }

                        enchantment = (CustomEnchantment)i$.next();
                    } while(enchantment instanceof VanillaEnchantment);
                } while(enchantment.getDescription() == null);

                String page = enchantment.name() + " - " + enchantment.getDescription() + "\n\nItems: ";
                if (enchantment.getNaturalMaterials().length <= 0) {
                    page = page + "None";
                } else {
                    Material[] arr$ = enchantment.getNaturalMaterials();
                    int len$ = arr$.length;

                    for(int what = 0; what < len$; ++what) {
                        Material item = arr$[what];
                        page = page + ChatColor.stripColor(ENameParser.getName(item)) + ", ";
                    }

                    page = page.substring(0, page.length() - 2);
                }

                meta.addPage(new String[]{page});
            }
        }
    }
}

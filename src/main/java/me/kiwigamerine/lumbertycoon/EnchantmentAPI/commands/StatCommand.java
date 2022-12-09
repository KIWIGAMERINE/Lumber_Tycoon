package me.kiwigamerine.lumbertycoon.EnchantmentAPI.commands;

import me.kiwigamerine.lumbertycoon.LumberTycoon;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.ICommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

public class StatCommand implements ICommand {
    public StatCommand() {
    }

    public boolean execute(LumberTycoon plugin, CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            try {
                Material mat = Material.getMaterial(args[0].toUpperCase());
                if (mat == null) {
                    mat = Material.getMaterial(Integer.parseInt(args[0]));
                }

                if (mat == null) {
                    return false;
                } else {
                    ItemStack item = new ItemStack(mat);
                    if (LumberTycoon.getAllValidEnchants(item).size() == 0) {
                        sender.sendMessage(ChatColor.DARK_RED + "That item has no natural enchantments");
                        return true;
                    } else {
                        int level = Integer.parseInt(args[1]);
                        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new StatTask(sender, item, level));
                        return true;
                    }
                }
            } catch (Exception var9) {
                return false;
            }
        } else {
            return false;
        }
    }
}

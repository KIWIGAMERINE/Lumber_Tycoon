package me.kiwigamerine.lumbertycoon.EnchantmentAPI.commands;

import me.kiwigamerine.lumbertycoon.EnchantmentAPI.CustomEnchantment;
import me.kiwigamerine.lumbertycoon.LumberTycoon;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.ENameParser;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.ICommand;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.PermissionNode;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddEnchantCommand implements ICommand {
    public AddEnchantCommand() {
    }

    public boolean execute(LumberTycoon plugin, CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && sender.hasPermission(PermissionNode.ADMIN.getNode())) {
            String name = args[0];
            int difference = 0;
            int level = 1;

            try {
                level = Integer.parseInt(args[args.length - 1]);
                difference = 1;
            } catch (Exception var11) {
            }

            for(int i = 1; i < args.length - difference; ++i) {
                name = name + " " + args[i];
            }

            name = ENameParser.getBukkitName(name);
            Player player = (Player)sender;
            CustomEnchantment enchantment = LumberTycoon.getEnchantment(name);
            if (enchantment == null) {
                sender.sendMessage(ChatColor.DARK_RED + name + " is not a registered enchantment!");
            } else {
                player.setItemInHand(enchantment.addToItem(player.getItemInHand(), level));
                player.sendMessage(ChatColor.GREEN + "Enchantment has been applied.");
            }
        }

        return true;
    }
}

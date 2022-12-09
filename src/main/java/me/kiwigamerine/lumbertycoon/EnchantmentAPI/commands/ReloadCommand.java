package me.kiwigamerine.lumbertycoon.EnchantmentAPI.commands;

import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.ICommand;
import me.kiwigamerine.lumbertycoon.LumberTycoon;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
public class ReloadCommand implements ICommand {
    public ReloadCommand() {
    }

    public boolean execute(LumberTycoon plugin, CommandSender sender, Command command, String label, String[] args) {
        plugin.reload();
        sender.sendMessage(ChatColor.GREEN + "Enchantment API has been reloaded.");
        return true;
    }
}
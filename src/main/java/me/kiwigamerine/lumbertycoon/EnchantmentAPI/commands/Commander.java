package me.kiwigamerine.lumbertycoon.EnchantmentAPI.commands;

import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.CommandHandler;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.ICommand;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.PermissionNode;
import me.kiwigamerine.lumbertycoon.LumberTycoon;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Commander extends CommandHandler {
    public Commander(LumberTycoon plugin) {
        super(plugin, "EnchantAPI");
        HelpCommand help = new HelpCommand();
        this.registerCommand("help", help);
        this.registerCommand("?", help);
        this.registerCommand("reload", new ReloadCommand());
        this.registerCommand("list", new EnchantListCommand());
        this.registerCommand("add", new AddEnchantCommand());
        this.registerCommand("book", new BookCommand());
        this.registerCommand("stats", new StatCommand());
        this.registerCommand("graph", new GraphCommand());
    }

    public boolean noArgs(CommandSender sender, Command command, String label) {
        sender.sendMessage(ChatColor.GRAY + "========= " + ChatColor.RED + this.plugin.getName() + ChatColor.GRAY + " =========");
        sender.sendMessage(" /enchantapi");
        sender.sendMessage("    help         " + ChatColor.YELLOW + "- Show the help menu");
        if (sender.hasPermission(PermissionNode.LIST.getNode())) {
            sender.sendMessage("    list          " + ChatColor.YELLOW + "- List all enchantments");
            sender.sendMessage("    list <page> " + ChatColor.YELLOW + "- List a page of enchantments with descriptions");
        }

        if (sender.hasPermission(PermissionNode.BOOK.getNode())) {
            sender.sendMessage("    book        " + ChatColor.YELLOW + "- Gives a book with enchantment descriptions");
        }

        if (sender.hasPermission(PermissionNode.ADMIN.getNode())) {
            sender.sendMessage("    reload      " + ChatColor.YELLOW + "- Reload the plugin");
            sender.sendMessage("    add          " + ChatColor.YELLOW + "- Add an enchantment to an item");
            sender.sendMessage("    stats <item> <level>      " + ChatColor.YELLOW + "- Displays enchantment stats");
            sender.sendMessage("    graph <item> <enchant> " + ChatColor.YELLOW + "- Displays a graph of stats");
        }

        return true;
    }

    public boolean unknownCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.YELLOW + this.plugin.getTag() + " Unknown command: " + ChatColor.WHITE + args[0]);
        return true;
    }

    private class HelpCommand implements ICommand {
        private HelpCommand() {
        }

        public boolean execute(LumberTycoon plugin, CommandSender sender, Command command, String label, String[] args) {
            return Commander.this.noArgs(sender, command, label);
        }
    }
}

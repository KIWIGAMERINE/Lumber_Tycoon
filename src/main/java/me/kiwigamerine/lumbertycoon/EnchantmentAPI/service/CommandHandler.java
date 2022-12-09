package me.kiwigamerine.lumbertycoon.EnchantmentAPI.service;

import me.kiwigamerine.lumbertycoon.LumberTycoon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class CommandHandler implements CommandExecutor {
    private final Map<String, ICommand> registeredCommands = new HashMap();
    private final Map<String, CommandHandler> registeredHandlers = new HashMap();
    protected final LumberTycoon plugin;
    private final String cmd;

    protected CommandHandler(LumberTycoon plugin, String cmd) {
        this.plugin = plugin;
        this.cmd = cmd;
    }

    protected void registerCommand(String label, ICommand command) {
        if (this.registeredCommands.containsKey(label)) {
            this.plugin.getLogger().warning("Replacing existing command for: " + label);
        }

        this.registeredCommands.put(label, command);
    }

    public void unregisterCommand(String label) {
        this.registeredCommands.remove(label);
    }

    public void registerHandler(CommandHandler handler) {
        if (this.registeredHandlers.containsKey(handler.getCommand())) {
            this.plugin.getLogger().warning("Replacing existing handler for: " + handler.getCommand());
        }

        this.registeredHandlers.put(handler.getCommand(), handler);
    }

    public void unregisterHandler(String label) {
        this.registeredHandlers.remove(label);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return this.noArgs(sender, command, label);
        } else {
            String subcmd = args[0].toLowerCase();
            CommandHandler handler = (CommandHandler)this.registeredHandlers.get(subcmd);
            if (handler != null) {
                return handler.onCommand(sender, command, label, this.shortenArgs(args));
            } else {
                ICommand subCommand = (ICommand)this.registeredCommands.get(subcmd);
                if (subCommand == null) {
                    return this.unknownCommand(sender, command, label, args);
                } else {
                    boolean value = true;

                    try {
                        value = subCommand.execute(this.plugin, sender, command, label, this.shortenArgs(args));
                    } catch (ArrayIndexOutOfBoundsException var10) {
                        sender.sendMessage(ChatColor.GRAY + this.plugin.getTag() + ChatColor.RED + " Missing parameters.");
                    }

                    return value;
                }
            }
        }
    }

    protected abstract boolean noArgs(CommandSender var1, Command var2, String var3);

    protected abstract boolean unknownCommand(CommandSender var1, Command var2, String var3, String[] var4);

    String[] shortenArgs(String[] args) {
        if (args.length == 0) {
            return args;
        } else {
            List<String> argList = new ArrayList();

            for(int i = 1; i < args.length; ++i) {
                argList.add(args[i]);
            }

            return (String[])argList.toArray(new String[argList.size()]);
        }
    }

    String getCommand() {
        return this.cmd;
    }
}
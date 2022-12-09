package me.kiwigamerine.lumbertycoon.EnchantmentAPI.service;

import me.kiwigamerine.lumbertycoon.LumberTycoon;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface ICommand {
    boolean execute(LumberTycoon var1, CommandSender var2, Command var3, String var4, String[] var5);
}


package me.kiwigamerine.lumbertycoon.commands;

import me.kiwigamerine.lumbertycoon.LumberTycoon;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Test_commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        // /heal
        if (cmd.getName().equalsIgnoreCase("heal")){
            if (!sender.hasPermission("LumberTycoon.heal")){
            double maxHealth = player.getMaxHealth();
            player.setHealth(maxHealth);
            return true;
            }

        }

        // /feed
        if (cmd.getName().equalsIgnoreCase("feed")){
            if (!sender.hasPermission("LumberTycoon.feed")) {
                player.setFoodLevel(20);
                player.setSaturation(20);
                return true;
            }
        }


        return true;
    }
}

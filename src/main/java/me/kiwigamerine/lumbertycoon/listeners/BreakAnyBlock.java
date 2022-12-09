package me.kiwigamerine.lumbertycoon.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;


public class BreakAnyBlock implements Listener {

    @EventHandler
    public void onBreakOakLog(BlockBreakEvent b) {
        String player = b.getPlayer().getDisplayName();
        String item = b.getPlayer().getItemInHand().getType().toString();
        Material block = b.getBlock().getType();
        Bukkit.broadcastMessage(player);
        Bukkit.broadcastMessage("Broke");
        Bukkit.broadcastMessage(String.valueOf(block));
        if (item.equals("AIR")) {
            Bukkit.broadcastMessage("with an empty hand");
        }
        else {
            Bukkit.broadcastMessage("with a");
            Bukkit.broadcastMessage(item);
        }

    }


//Remember to register event in Main Class
//getServer().getPluginManager().registerEvents(new FileName(),this);
}

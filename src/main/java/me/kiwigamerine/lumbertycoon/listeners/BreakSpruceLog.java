package me.kiwigamerine.lumbertycoon.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;


public class BreakSpruceLog implements Listener {

    @EventHandler
    public void onBreakOakLog(BlockBreakEvent b) {
        String player = b.getPlayer().getDisplayName();
        String item = b.getPlayer().getItemInHand().getType().toString();
        Block block = b.getBlock();
        byte meta = b.getBlock().getData();

        if (block.getType() == Material.LOG && block.getData() == 1) {
            // "broke a Vertical Spruce Log with a";
            Bukkit.broadcastMessage(String.valueOf(meta));
        }
        if (block.getType() == Material.LOG && block.getData() == 5) {
            // "broke a X axis Spruce Log with a";
            Bukkit.broadcastMessage(String.valueOf(meta));
        }
        if (block.getType() == Material.LOG && block.getData() == 9) {
            // "broke a Y axis Spruce Log with a";
            Bukkit.broadcastMessage(String.valueOf(meta));
        }
        if (block.getType() == Material.LOG && block.getData() == 13) {
            // "broke a Faceless Spruce Log with a";
            Bukkit.broadcastMessage(String.valueOf(meta));
        }

    }


//Remember to register event in Main Class
//getServer().getPluginManager().registerEvents(new FileName(),this);
}

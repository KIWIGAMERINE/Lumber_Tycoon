package me.kiwigamerine.lumbertycoon.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;


public class BreakBirchLog implements Listener {

    @EventHandler
    public void onBreakOakLog(BlockBreakEvent b) {
        String player = b.getPlayer().getDisplayName();
        String item = b.getPlayer().getItemInHand().getType().toString();
        Block block = b.getBlock();
        byte meta = b.getBlock().getData();

        if (block.getType() == Material.LOG && block.getData() == 2) {
            // "broke a Vertical Birch Log with a";
            Bukkit.broadcastMessage(String.valueOf(meta));
        }
        if (block.getType() == Material.LOG && block.getData() == 6) {
            // "broke a X axis Birch Log with a";
            Bukkit.broadcastMessage(String.valueOf(meta));
        }
        if (block.getType() == Material.LOG && block.getData() == 10) {
            // "broke a Y axis Birch Log with a";
            Bukkit.broadcastMessage(String.valueOf(meta));
        }
        if (block.getType() == Material.LOG && block.getData() == 14) {
            // "broke a Faceless Birch Log with a";
            Bukkit.broadcastMessage(String.valueOf(meta));//
        }

    }


//Remember to register event in Main Class
//getServer().getPluginManager().registerEvents(new FileName(),this);
}

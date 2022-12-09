package me.kiwigamerine.lumbertycoon.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Stack;


public class BreakOakLog implements Listener {

    @EventHandler
    public void onBreakOakLog(BlockBreakEvent b) {
        String player = b.getPlayer().getDisplayName();
        String item = b.getPlayer().getItemInHand().getType().toString();
        Block block = b.getBlock();
        byte meta = b.getBlock().getData();

        if (block.getType() == Material.LOG && meta == 0) {
            if (item.equals("DIAMOND_AXE")){
                b.getBlock().breakNaturally();
            }
            else b.setCancelled(true);
            // "broke a Vertical Oak Log with a";
            Bukkit.broadcastMessage(String.valueOf(meta));
        }
        if (block.getType() == Material.LOG && meta == 4) {
            // "broke an X axis Oak Log with a";
            Bukkit.broadcastMessage(String.valueOf(meta));
        }
        if (block.getType() == Material.LOG && meta == 8) {
            // "broke a Y axis Oak Log with a";
            Bukkit.broadcastMessage(String.valueOf(meta));
        }
        if (block.getType() == Material.LOG && meta == 12) {
            // "broke a Faceless Oak Log with a";
            Bukkit.broadcastMessage(String.valueOf(meta));
        }


    }


//Remember to register event in Main Class
//getServer().getPluginManager().registerEvents(new FileName(),this);
}

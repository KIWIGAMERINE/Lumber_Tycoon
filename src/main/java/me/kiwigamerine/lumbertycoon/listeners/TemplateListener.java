package me.kiwigamerine.lumbertycoon.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExpBottleEvent;

public class TemplateListener implements Listener {

    @EventHandler
    public void onBreakBlock(ExpBottleEvent e) {
        // my name/\       event name/\
       e.setExperience(500);
    }
//Remember to register event in Main Class
//getServer().getPluginManager().registerEvents(new TemplateListener(),this);
}

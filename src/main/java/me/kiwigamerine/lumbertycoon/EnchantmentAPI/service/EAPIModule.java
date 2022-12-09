package me.kiwigamerine.lumbertycoon.EnchantmentAPI.service;

import me.kiwigamerine.lumbertycoon.LumberTycoon;

public abstract class EAPIModule implements IModule {
    protected final LumberTycoon plugin;

    protected EAPIModule(LumberTycoon plugin) {
        this.plugin = plugin;
    }
}

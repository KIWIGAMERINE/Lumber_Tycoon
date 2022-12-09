package me.kiwigamerine.lumbertycoon.EnchantmentAPI.service;

public enum PermissionNode {
    ADMIN("admin"),
    LIST("list"),
    BOOK("book"),
    NAMES("names"),
    TABLE("table"),
    ENCHANT("enchant"),
    ENCHANT_VANILLA("enchant.vanilla");

    private static final String PREFIX = "LumberTycoon.";
    private final String node;

    private PermissionNode(String subperm) {
        this.node = "LumberTycoon." + subperm;
    }

    public String getNode() {
        return this.node;
    }
}

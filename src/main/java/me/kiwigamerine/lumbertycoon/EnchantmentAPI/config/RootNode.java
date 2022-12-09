package me.kiwigamerine.lumbertycoon.EnchantmentAPI.config;

import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.ConfigNode;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.ConfigNode.VarType;

public enum RootNode implements ConfigNode {
    ITEM_LORE("Generate Unique Name", VarType.BOOLEAN, true),
    MAX_ENCHANTS("Max Enchantments", VarType.INTEGER, 5),
    ANVIL_ENABLED("Anvil Enabled", VarType.BOOLEAN, true),
    VANILLA_ENABLED(getVisibleSettingsNode() + getVanillaNode() + "Enabled", VarType.BOOLEAN, true),
    VANILLA_TABLE(getVisibleSettingsNode() + getVanillaNode() + "Table", VarType.BOOLEAN, true),
    VANILLA_WEIGHT(getVisibleSettingsNode() + getVanillaNode() + "Weight", VarType.BOOLEAN, true),
    VANILLA_ITEMS(getVisibleSettingsNode() + getVanillaNode() + "Items", VarType.BOOLEAN, true),
    VANILLA_GROUPS(getVisibleSettingsNode() + getVanillaNode() + "Groups", VarType.BOOLEAN, true),
    VANILLA_MAX(getVisibleSettingsNode() + getVanillaNode() + "Max Level", VarType.BOOLEAN, true),
    VANILLA_BASE(getVisibleSettingsNode() + getVanillaNode() + "Base", VarType.BOOLEAN, true),
    VANILLA_INTERVAL(getVisibleSettingsNode() + getVanillaNode() + "Interval", VarType.BOOLEAN, true),
    CUSTOM_ENABLED(getVisibleSettingsNode() + getCustomNode() + "Enabled", VarType.BOOLEAN, true),
    CUSTOM_TABLE(getVisibleSettingsNode() + getCustomNode() + "Table", VarType.BOOLEAN, true),
    CUSTOM_WEIGHT(getVisibleSettingsNode() + getCustomNode() + "Weight", VarType.BOOLEAN, true),
    CUSTOM_ITEMS(getVisibleSettingsNode() + getCustomNode() + "Items", VarType.BOOLEAN, true),
    CUSTOM_GROUPS(getVisibleSettingsNode() + getCustomNode() + "Groups", VarType.BOOLEAN, true),
    CUSTOM_MAX(getVisibleSettingsNode() + getCustomNode() + "Max Level", VarType.BOOLEAN, true),
    CUSTOM_BASE(getVisibleSettingsNode() + getCustomNode() + "Base", VarType.BOOLEAN, true),
    CUSTOM_INTERVAL(getVisibleSettingsNode() + getCustomNode() + "Interval", VarType.BOOLEAN, true),
    CUSTOM_STACK(getVisibleSettingsNode() + getCustomNode() + "Stacks", VarType.BOOLEAN, true);

    private final String path;
    private final ConfigNode.VarType type;
    private ConfigNode.SubType subType = null;
    private final Object defaultValue;

    private RootNode(String path, ConfigNode.VarType type, Object def) {
        this.path = path;
        this.type = type;
        this.defaultValue = def;
    }

    private RootNode(String path, ConfigNode.VarType type, ConfigNode.SubType subType, Object def) {
        this.path = path;
        this.type = type;
        this.defaultValue = def;
        this.subType = subType;
    }

    public String getPath() {
        return RootConfig.baseNode + getNode() + this.path;
    }

    public ConfigNode.VarType getVarType() {
        return this.type;
    }

    public Object getDefaultValue() {
        return this.defaultValue;
    }

    public ConfigNode.SubType getSubType() {
        return this.subType;
    }

    public static String getNode() {
        return "Settings.";
    }

    public static String getVisibleSettingsNode() {
        return "Visible Settings.";
    }

    public static String getCustomNode() {
        return "Custom Enchantments.";
    }

    public static String getVanillaNode() {
        return "Vanilla Enchantments.";
    }
}

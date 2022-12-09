package me.kiwigamerine.lumbertycoon.EnchantmentAPI.config;

import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.ConfigNode;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.ConfigNode.VarType;
import java.util.Collections;

public enum EnchantmentNode implements ConfigNode {
    ENABLED("Enabled", VarType.BOOLEAN, true),
    TABLE("Table", VarType.BOOLEAN, true),
    WEIGHT("Weight", VarType.INTEGER, 5),
    GROUP("Group", VarType.STRING, "Default"),
    MAX("Max Level", VarType.INTEGER, 1),
    BASE("Base", VarType.DOUBLE, 1),
    INTERVAL("Interval", VarType.DOUBLE, 10),
    ITEMS("Items", VarType.LIST, Collections.emptyList()),
    STACK("Stack", VarType.BOOLEAN, false);

    private final String path;
    private final ConfigNode.VarType type;
    private ConfigNode.SubType subType = null;
    private final Object defaultValue;

    private EnchantmentNode(String path, ConfigNode.VarType type, Object def) {
        this.path = path;
        this.type = type;
        this.defaultValue = def;
    }

    private EnchantmentNode(String path, ConfigNode.VarType type, ConfigNode.SubType subType, Object def) {
        this.path = path;
        this.type = type;
        this.defaultValue = def;
        this.subType = subType;
    }

    public String getPath() {
        return "." + this.path;
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
}

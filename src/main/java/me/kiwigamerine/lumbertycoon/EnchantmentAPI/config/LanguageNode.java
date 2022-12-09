package me.kiwigamerine.lumbertycoon.EnchantmentAPI.config;

import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.ConfigNode;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.ConfigNode.VarType;
import java.util.ArrayList;
import java.util.List;

public enum LanguageNode implements ConfigNode {
    ANVIL_COMPONENT("Anvil Component", VarType.LIST, new ArrayList<String>() {
        {
            this.add("&2Anvil Components");
            this.add("&dPlace components");
            this.add("&dover here!");
        }
    }),
    ANVIL_SEPARATOR("Anvil Separator", VarType.LIST, new ArrayList<String>() {
        {
            this.add("&2Anvil Book");
            this.add("&d<- Components");
            this.add("&8------------");
            this.add("&dResults ->");
        }
    }),
    ANVIL_RESULT("Anvil Result", VarType.LIST, new ArrayList<String>() {
        {
            this.add("&2Anvil Result");
            this.add("&dResults will");
            this.add("&dshow up over");
            this.add("&dhere!");
        }
    }),
    TABLE_ENCHANTABLE("Table Enchantable", VarType.LIST, new ArrayList<String>() {
        {
            this.add("&2Placeholder");
            this.add("&dEnchantable");
        }
    }),
    TABLE_UNENCHANTABLE("Table Unenchantable", VarType.LIST, new ArrayList<String>() {
        {
            this.add("&2Placeholder");
            this.add("&4Unenchantable");
        }
    }),
    NAME_FORMAT("Name Format", VarType.LIST, new ArrayList<String>() {
        {
            this.add("{adjective} {weapon} of {suffix}");
        }
    });

    final String path;
    final ConfigNode.VarType type;
    final List<String> value;

    private LanguageNode(String path, ConfigNode.VarType type, List value) {
        this.type = type;
        this.path = path;
        this.value = value;
    }

    public String getPath() {
        return this.path;
    }

    public String getFullPath() {
        return RootConfig.baseNode + "Language." + this.getPath();
    }

    public ConfigNode.VarType getVarType() {
        return this.type;
    }

    public ConfigNode.SubType getSubType() {
        return null;
    }

    public Object getDefaultValue() {
        return this.value;
    }
}
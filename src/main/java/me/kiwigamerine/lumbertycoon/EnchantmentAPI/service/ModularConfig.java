package me.kiwigamerine.lumbertycoon.EnchantmentAPI.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import me.kiwigamerine.lumbertycoon.LumberTycoon;
import org.bukkit.configuration.ConfigurationSection;

public abstract class ModularConfig extends EAPIModule {
    private final Map<ConfigNode, Object> OPTIONS = new ConcurrentHashMap();

    protected ModularConfig(LumberTycoon plugin) {
        super(plugin);
    }

    protected void updateOption(ConfigNode node, ConfigurationSection config) {
        switch (node.getVarType()) {
            case LIST:
                List<String> list = config.getStringList(node.getPath());
                if (list == null) {
                    list = (List)node.getDefaultValue();
                }

                this.OPTIONS.put(node, list);
                break;
            case DOUBLE:
                this.OPTIONS.put(node, config.getDouble(node.getPath(), (Double)node.getDefaultValue()));
                break;
            case STRING:
                this.OPTIONS.put(node, config.getString(node.getPath(), (String)node.getDefaultValue()));
                break;
            case INTEGER:
                this.OPTIONS.put(node, config.getInt(node.getPath(), (Integer)node.getDefaultValue()));
                break;
            case BOOLEAN:
                this.OPTIONS.put(node, config.getBoolean(node.getPath(), (Boolean)node.getDefaultValue()));
                break;
            default:
                this.OPTIONS.put(node, config.get(node.getPath(), node.getDefaultValue()));
        }

    }

    public abstract void save();

    public void set(ConfigNode node, Object value) {
        this.set(node.getPath(), value);
    }

    protected abstract void set(String var1, Object var2);

    public int getInt(ConfigNode node) {
        int i;
        switch (node.getVarType()) {
            case INTEGER:
                try {
                    i = (Integer)this.OPTIONS.get(node);
                } catch (NullPointerException var4) {
                    i = (Integer)node.getDefaultValue();
                }

                return i;
            default:
                throw new IllegalArgumentException("Attempted to get " + node.toString() + " of type " + node.getVarType() + " as an integer.");
        }
    }

    protected String getString(ConfigNode node) {
        String out = "";
        switch (node.getVarType()) {
            case STRING:
                out = (String)this.OPTIONS.get(node);
                if (out == null) {
                    out = (String)node.getDefaultValue();
                }

                return out;
            default:
                throw new IllegalArgumentException("Attempted to get " + node.toString() + " of type " + node.getVarType() + " as a string.");
        }
    }

    public List<String> getStringList(ConfigNode node) {
        new ArrayList();
        switch (node.getVarType()) {
            case LIST:
                ConfigurationSection config = this.plugin.getConfig();
                List<String> list = config.getStringList(node.getPath());
                if (list == null) {
                    list = (List)node.getDefaultValue();
                }

                return list;
            default:
                throw new IllegalArgumentException("Attempted to get " + node.toString() + " of type " + node.getVarType() + " as a List<String>.");
        }
    }

    public double getDouble(ConfigNode node) {
        double d = 0.0;
        switch (node.getVarType()) {
            case DOUBLE:
                try {
                    d = (Double)this.OPTIONS.get(node);
                } catch (NullPointerException var5) {
                    d = (Double)node.getDefaultValue();
                }

                return d;
            default:
                throw new IllegalArgumentException("Attempted to get " + node.toString() + " of type " + node.getVarType() + " as a double.");
        }
    }

    public boolean getBoolean(ConfigNode node) {
        boolean bool = false;
        switch (node.getVarType()) {
            case BOOLEAN:
                bool = (Boolean)this.OPTIONS.get(node);
                return bool;
            default:
                throw new IllegalArgumentException("Attempted to get " + node.toString() + " of type " + node.getVarType() + " as a boolean.");
        }
    }

    public abstract void reload();

    public abstract void loadSettings(ConfigurationSection var1);

    public abstract void loadDefaults(ConfigurationSection var1);

    public abstract void boundsCheck();
}

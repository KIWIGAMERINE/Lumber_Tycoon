package me.kiwigamerine.lumbertycoon.EnchantmentAPI.lore;

import java.io.File;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class LoreConfig {
    private final String fileName;
    private final JavaPlugin plugin;
    private File configFile;
    private FileConfiguration fileConfiguration;

    public LoreConfig(JavaPlugin plugin, String type) {
        this.plugin = plugin;
        this.fileName = type + ".yml";
        this.configFile = new File(plugin.getDataFolder(), this.fileName);
        this.saveDefaultConfig();
    }

    public Hashtable<String, List<String>> getLists() {
        Hashtable<String, List<String>> table = new Hashtable();
        Iterator i$ = this.getConfig().getKeys(false).iterator();

        while(i$.hasNext()) {
            String key = (String)i$.next();
            table.put(key, this.getConfig().getStringList(key));
        }

        return table;
    }

    private void reloadConfig() {
        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defConfigStream = this.plugin.getResource(this.fileName);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            this.fileConfiguration.setDefaults(defConfig);
        }

    }

    private FileConfiguration getConfig() {
        if (this.fileConfiguration == null) {
            this.reloadConfig();
        }

        return this.fileConfiguration;
    }

    public void saveDefaultConfig() {
        if (!this.configFile.exists()) {
            this.plugin.saveResource(this.fileName, false);
        }

    }
}

package me.kiwigamerine.lumbertycoon.EnchantmentAPI.enchanting;

import me.kiwigamerine.lumbertycoon.EnchantmentAPI.EUpdateTask;
import me.kiwigamerine.lumbertycoon.LumberTycoon;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.config.LanguageNode;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TableTask extends BukkitRunnable {
    final ItemStack p;
    final ItemStack p2;
    final String cantEnchant;
    Player player;
    Plugin plugin;
    ItemStack placeholder;
    ItemStack stored;

    public TableTask(Plugin plugin, Player player) {
        this.p = new ItemStack(Material.BOOK);
        this.p2 = new ItemStack(Material.BOOK);
        this.player = player;
        this.plugin = plugin;
        List<String> enchantable = plugin.getConfig().getStringList(LanguageNode.TABLE_ENCHANTABLE.getFullPath());
        List<String> unenchantable = plugin.getConfig().getStringList(LanguageNode.TABLE_UNENCHANTABLE.getFullPath());
        this.cantEnchant = ((String)unenchantable.get(1)).replace('&', '§');
        ItemMeta meta = this.p.getItemMeta();
        meta.setDisplayName(((String)enchantable.get(0)).replace('&', '§'));
        ArrayList<String> lore = new ArrayList();
        lore.add(((String)enchantable.get(1)).replace('&', '§'));
        meta.setLore(lore);
        this.p.setItemMeta(meta);
        lore.clear();
        lore.add(this.cantEnchant);
        meta.setLore(lore);
        meta.setDisplayName(((String)unenchantable.get(0)).replace('&', '§'));
        this.p2.setItemMeta(meta);
        this.runTaskTimer(plugin, 0L, 1L);
    }

    public void restore() {
        if (this.placeholder != null) {
            this.placeholder.setType(this.stored.getType());
            this.placeholder.setItemMeta(this.stored.getItemMeta());
            this.placeholder.setAmount(this.stored.getAmount());
            this.placeholder = null;
        }

    }

    public void run() {
        InventoryView view = this.player.getOpenInventory();
        if (view == null) {
            this.cancel();
        } else {
            EnchantingInventory inv = (EnchantingInventory)view.getTopInventory();
            if (this.placeholder != null && (inv.getItem() == null || !inv.getItem().hasItemMeta() || !inv.getItem().getItemMeta().hasDisplayName() || !inv.getItem().getItemMeta().getDisplayName().equals(this.placeholder.getItemMeta().getDisplayName()))) {
                this.restore();
                new EUpdateTask(this.plugin, this.player);
            }

            if (inv.getItem() != null && inv.getItem().getType() != Material.AIR && this.placeholder == null) {
                this.stored = inv.getItem().clone();
                this.placeholder = this.createPlaceholder(inv.getItem(), this.stored);
                inv.setItem(this.placeholder);
                this.placeholder = inv.getItem();
            }

        }
    }

    private ItemStack createPlaceholder(ItemStack item, ItemStack storedItem) {
        if (this.canEnchant(item)) {
            item.setType(this.p.getType());
            item.setItemMeta(this.p.getItemMeta());
        } else {
            item.setType(this.p2.getType());
            item.setItemMeta(this.p2.getItemMeta());
        }

        item.setAmount(1);
        List<String> lore = item.getItemMeta().getLore();
        if (storedItem.hasItemMeta() && storedItem.getItemMeta().hasDisplayName()) {
            lore.add(storedItem.getItemMeta().getDisplayName());
        } else {
            lore.add(ChatColor.GRAY + storedItem.getType().name().toLowerCase().replace("_", " "));
        }

        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private boolean canEnchant(ItemStack item) {
        if (LumberTycoon.getAllValidEnchants(item, this.player).size() == 0) {
            return false;
        } else if (item.hasItemMeta() && item.getItemMeta().hasEnchants()) {
            return false;
        } else {
            return LumberTycoon.getEnchantments(item).size() <= 0;
        }
    }
}

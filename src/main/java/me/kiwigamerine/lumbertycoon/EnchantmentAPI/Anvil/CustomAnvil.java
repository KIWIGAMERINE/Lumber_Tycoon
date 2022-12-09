package me.kiwigamerine.lumbertycoon.EnchantmentAPI.Anvil;

import me.kiwigamerine.lumbertycoon.EnchantmentAPI.config.LanguageNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class CustomAnvil implements AnvilView, Listener {
    private static final String NAME = "Anvil (CraftBukkit broke!)";
    private static final String COST;
    private static final ItemStack COMPONENT;
    private static final ItemStack RESULT;
    private static final ItemStack MIDDLE;
    private static final ItemStack[] CONTENTS;
    final Plugin plugin;
    final Inventory anvil;
    final Player player;
    int repairCost;

    public CustomAnvil(Plugin plugin, Player player) {
        this.anvil = plugin.getServer().createInventory((InventoryHolder)null, 9, "Anvil (CraftBukkit broke!)");
        this.anvil.setContents(CONTENTS);
        this.plugin = plugin;
        this.player = player;
        player.openInventory(this.anvil);
    }

    public ItemStack[] getInputSlots() {
        return new ItemStack[]{this.anvil.getItem(1), this.anvil.getItem(2)};
    }

    public ItemStack[] getInputSlots(int slot, ItemStack newItem) {
        if (slot == 1) {
            return new ItemStack[]{newItem, this.anvil.getItem(2)};
        } else if (slot == 2) {
            return new ItemStack[]{this.anvil.getItem(1), newItem};
        } else {
            throw new IllegalArgumentException(slot + " is not an input slot!");
        }
    }

    public int getInputSlotID(int input) {
        if (input != 1 && input != 2) {
            throw new IllegalArgumentException("Invalid input number: " + input);
        } else {
            return input;
        }
    }

    public void setResultSlot(ItemStack item) {
        if (item == null) {
            this.anvil.clear(7);
        } else {
            item = item.clone();
            ItemMeta meta = item.hasItemMeta() ? item.getItemMeta() : this.plugin.getServer().getItemFactory().getItemMeta(item.getType());
            List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList();
            ((List)lore).add(COST + this.repairCost);
            meta.setLore((List)lore);
            item.setItemMeta(meta);
            this.anvil.setItem(7, item);
        }
    }

    public ItemStack getResultSlot() {
        if (this.anvil.getItem(7) == null) {
            return null;
        } else {
            ItemStack result = this.anvil.getItem(7).clone();
            ItemMeta meta = result.getItemMeta();
            List<String> lore = meta.getLore();
            lore.remove(lore.size() - 1);
            meta.setLore(lore);
            result.setItemMeta(meta);
            return result;
        }
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setRepairCost(int repairCost) {
        this.repairCost = repairCost;
        this.setResultSlot(this.getResultSlot());
    }

    public int getRepairCost() {
        return this.repairCost;
    }

    public boolean isInputSlot(int slot) {
        return slot == 1 || slot == 2;
    }

    public int getResultSlotID() {
        return 7;
    }

    public void clearInputs() {
        this.anvil.clear(1);
        this.anvil.clear(2);
        this.anvil.setItem(7, this.getResultSlot());
    }

    public void close() {
        if (this.anvil.getItem(1) != null) {
            this.player.getInventory().addItem(new ItemStack[]{this.anvil.getItem(1)});
        }

        if (this.anvil.getItem(2) != null) {
            this.player.getInventory().addItem(new ItemStack[]{this.anvil.getItem(2)});
        }

    }

    public Inventory getInventory() {
        return this.anvil;
    }

    static ItemStack makeComponentIndicator() {
        List<String> component = getText(LanguageNode.ANVIL_COMPONENT);
        return makeIndicator(component);
    }

    static ItemStack makeMiddleIndicator() {
        List<String> separator = getText(LanguageNode.ANVIL_SEPARATOR);
        return makeIndicator(separator);
    }

    static ItemStack makeResultIndicator() {
        List<String> result = getText(LanguageNode.ANVIL_RESULT);
        return makeIndicator(result);
    }

    static ItemStack makeIndicator(List<String> lines) {
        ItemStack item = new ItemStack(Material.BOOK);
        ArrayList<String> lore = new ArrayList();

        for(int i = 1; i < lines.size(); ++i) {
            lore.add(lines.get(i));
        }

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName((String)lines.get(0));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    static List<String> getText(LanguageNode node) {
        List<String> list = Bukkit.getPluginManager().getPlugin("LumberTycoon").getConfig().getStringList(node.getFullPath());
        List<String> coloredList = new ArrayList();
        Iterator i$ = list.iterator();

        while(i$.hasNext()) {
            String line = (String)i$.next();
            coloredList.add(line.replace('&', '§'));
        }

        return coloredList;
    }

    static {
        COST = ChatColor.DARK_RED + "Cost - ";
        COMPONENT = makeComponentIndicator();
        RESULT = makeResultIndicator();
        MIDDLE = makeMiddleIndicator();
        CONTENTS = new ItemStack[]{COMPONENT, null, null, COMPONENT, MIDDLE, MIDDLE, RESULT, null, RESULT};
    }
}

package me.kiwigamerine.lumbertycoon.EnchantmentAPI.enchanting;

import me.kiwigamerine.lumbertycoon.LumberTycoon;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.service.ENameParser;
import java.util.Hashtable;
import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class EEquip extends BukkitRunnable {
    static Hashtable<String, ItemStack[]> equipment = new Hashtable();
    Player player;

    public static void loadPlayer(Player player) {
        equipment.put(player.getName(), player.getEquipment().getArmorContents());
    }

    public static void clearPlayer(Player player) {
        equipment.remove(player.getName());
    }

    public static void clear() {
        equipment.clear();
    }

    public EEquip(Player player) {
        this.player = player;
    }

    public void run() {
        ItemStack[] equips = this.player.getEquipment().getArmorContents();
        ItemStack[] previous = (ItemStack[])equipment.get(this.player.getName());

        try {
            for(int i = 0; i < equips.length; ++i) {
                if (equips[i] == null && previous != null && previous[i] != null) {
                    this.doUnequip(previous[i]);
                } else if (equips[i] != null && (previous == null || previous[i] == null)) {
                    this.doEquip(equips[i]);
                } else if (previous != null && !equips[i].toString().equalsIgnoreCase(previous[i].toString())) {
                    this.doUnequip(previous[i]);
                    this.doEquip(equips[i]);
                }
            }
        } catch (Exception var4) {
        }

        equipment.put(this.player.getName(), equips);
    }

    private void doEquip(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (meta.hasLore()) {
                Iterator i$ = meta.getLore().iterator();

                while(i$.hasNext()) {
                    String lore = (String)i$.next();
                    String name = ENameParser.parseName(lore);
                    int level = ENameParser.parseLevel(lore);
                    if (name != null && level != 0 && LumberTycoon.isRegistered(name)) {
                        LumberTycoon.getEnchantment(name).applyEquipEffect(this.player, level);
                    }
                }

            }
        }
    }

    private void doUnequip(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (meta.hasLore()) {
                Iterator i$ = meta.getLore().iterator();

                while(i$.hasNext()) {
                    String lore = (String)i$.next();
                    String name = ENameParser.parseName(lore);
                    int level = ENameParser.parseLevel(lore);
                    if (name != null && level != 0 && LumberTycoon.isRegistered(name)) {
                        LumberTycoon.getEnchantment(name).applyUnequipEffect(this.player, level);
                    }
                }

            }
        }
    }
}

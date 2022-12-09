package me.kiwigamerine.lumbertycoon.EnchantmentAPI.Anvil;

import me.kiwigamerine.lumbertycoon.EnchantmentAPI.EUpdateTask;
import me.kiwigamerine.lumbertycoon.EnchantmentAPI.Anvil.v1_8_8.MainAnvil;
import java.util.Hashtable;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class AnvilListener implements Listener {
    private final Plugin plugin;
    private final Hashtable<String, AnvilTask> tasks = new Hashtable();
    private boolean custom = false;

    public AnvilListener(Plugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        if (event.getInventory().getType() == InventoryType.ANVIL) {
            Player player = this.plugin.getServer().getPlayer(event.getPlayer().getName());
            int id = 0;

            try {
                String v = this.plugin.getServer().getVersion();
                int ind = v.indexOf("MC: 1.8.") + 8;
                id = Integer.parseInt(v.substring(ind, ind + 1));
            } catch (Exception var6) {
            }

            Object anvil;
            if (id > 4) {
                anvil = new MainAnvil(this.plugin, event.getInventory(), player);
            }
            else if (id > 0) {
                anvil = new me.kiwigamerine.lumbertycoon.EnchantmentAPI.Anvil.v1_8_8.MainAnvil(this.plugin, event.getInventory(), player);
            }
            else if (this.plugin.getServer().getVersion().contains("MC: 1.8")) {
                anvil = new me.kiwigamerine.lumbertycoon.EnchantmentAPI.Anvil.v1_8_8.MainAnvil(this.plugin, event.getInventory(), player);
            }
            else {
                event.setCancelled(true);
                anvil = new CustomAnvil(this.plugin, player);
                this.custom = true;
            }

            this.tasks.put(player.getName(), new AnvilTask(this.plugin, (AnvilView)anvil));
        }

    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (this.tasks.containsKey(event.getPlayer().getName())) {
            ((AnvilTask)this.tasks.get(event.getPlayer().getName())).getView().close();
            ((AnvilTask)this.tasks.get(event.getPlayer().getName())).cancel();
            this.tasks.remove(event.getPlayer().getName());
        }

    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = this.plugin.getServer().getPlayer(event.getWhoClicked().getName());
        if (this.tasks.containsKey(player.getName()) && this.custom && ((AnvilTask)this.tasks.get(player.getName())).getView().getInventory().getName().equals(event.getInventory().getName())) {
            AnvilView view = ((AnvilTask)this.tasks.get(player.getName())).getView();
            ItemStack[] inputs = view.getInputSlots();
            boolean top = event.getRawSlot() < view.getInventory().getSize();
            if (event.getSlot() == -999) {
                return;
            }

            if (event.isShiftClick()) {
                if (event.getRawSlot() == view.getResultSlotID() && this.isFilled(view.getResultSlot())) {
                    if (player.getGameMode() != GameMode.CREATIVE && (view.getRepairCost() > player.getLevel() || view.getRepairCost() >= 40)) {
                        event.setCancelled(true);
                    } else {
                        view.clearInputs();
                        if (player.getGameMode() != GameMode.CREATIVE) {
                            player.setLevel(player.getLevel() - view.getRepairCost());
                        }
                    }
                } else if (top && !view.isInputSlot(event.getSlot())) {
                    event.setCancelled(true);
                } else if (!top && this.areFilled(inputs[0], inputs[1])) {
                    event.setCancelled(true);
                }
            } else if (!event.isLeftClick()) {
                if (event.isRightClick() && top) {
                    event.setCancelled(true);
                }
            } else if (event.getRawSlot() == view.getResultSlotID() && !this.isFilled(event.getCursor()) && this.isFilled(view.getResultSlot())) {
                if (player.getGameMode() != GameMode.CREATIVE && (view.getRepairCost() > player.getLevel() || view.getRepairCost() >= 40)) {
                    event.setCancelled(true);
                } else {
                    view.clearInputs();
                    if (player.getGameMode() != GameMode.CREATIVE) {
                        player.setLevel(player.getLevel() - view.getRepairCost());
                    }
                }
            } else if (top && !view.isInputSlot(event.getSlot())) {
                event.setCancelled(true);
            }

            new EUpdateTask(this.plugin, player);
        }

    }

    private boolean isFilled(ItemStack item) {
        return item != null && item.getType() != Material.AIR;
    }

    private boolean areFilled(ItemStack item1, ItemStack item2) {
        return this.isFilled(item1) && this.isFilled(item2);
    }
}

package me.kiwigamerine.lumbertycoon.EnchantmentAPI.Anvil.v1_8_8;

import me.kiwigamerine.lumbertycoon.EnchantmentAPI.Anvil.AnvilView;
import java.lang.reflect.Field;
import net.minecraft.server.v1_8_R3.ContainerAnvil;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IInventory;
import net.minecraft.server.v1_8_R3.ItemStack;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryAnvil;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class MainAnvil implements AnvilView {
    final Player player;
    final Plugin plugin;
    CraftInventoryAnvil inv;
    ContainerAnvil anvil;
    int repairCost;

    public MainAnvil(Plugin plugin, Inventory anvil, Player player) {
        this.player = player;
        this.plugin = plugin;
        CraftPlayer craftPlayer = (CraftPlayer)player;
        this.inv = (CraftInventoryAnvil)anvil;
        final EntityPlayer nmsPlayer = craftPlayer.getHandle();
        plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
            public void run() {
                MainAnvil.this.anvil = (ContainerAnvil)nmsPlayer.activeContainer;
            }
        }, 0L);
    }

    public String getNameText() {
        try {
            ItemStack[] arr$ = this.inv.getIngredientsInventory().getContents();
            int len$ = arr$.length;

            ItemStack item;
            for(int i$ = 0; i$ < len$; ++i$) {
                item = arr$[i$];
                org.bukkit.inventory.ItemStack i = CraftItemStack.asBukkitCopy(item);
                if (i.hasItemMeta() && i.getItemMeta().hasDisplayName() && !i.getItemMeta().getDisplayName().equals(ChatColor.stripColor(i.getItemMeta().getDisplayName()))) {
                    return null;
                }
            }

            Field textField = ContainerAnvil.class.getDeclaredField("l");
            textField.setAccessible(true);
            String name = (String)textField.get(this.anvil);
            if (name == null) {
                return null;
            } else {
                Field g = ContainerAnvil.class.getDeclaredField("h");
                g.setAccessible(true);
                item = ((IInventory)g.get(this.anvil)).getItem(0);
                if (item == null) {
                    return null;
                } else if (name.equals(item.getName())) {
                    return null;
                } else {
                    return name;
                }
            }
        } catch (Exception var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public org.bukkit.inventory.ItemStack[] getInputSlots() {
        org.bukkit.inventory.ItemStack[] input = new org.bukkit.inventory.ItemStack[]{CraftItemStack.asCraftMirror(this.inv.getIngredientsInventory().getItem(0)), CraftItemStack.asCraftMirror(this.inv.getIngredientsInventory().getItem(1))};
        return input;
    }

    public org.bukkit.inventory.ItemStack[] getInputSlots(int slot, org.bukkit.inventory.ItemStack newItem) {
        org.bukkit.inventory.ItemStack[] input = new org.bukkit.inventory.ItemStack[]{(org.bukkit.inventory.ItemStack)(slot == 0 ? newItem : CraftItemStack.asCraftMirror(this.inv.getIngredientsInventory().getItem(0))), (org.bukkit.inventory.ItemStack)(slot == 1 ? newItem : CraftItemStack.asCraftMirror(this.inv.getIngredientsInventory().getItem(1)))};
        return input;
    }

    public int getInputSlotID(int input) {
        return input - 1;
    }

    public void setResultSlot(final org.bukkit.inventory.ItemStack result) {
        this.plugin.getServer().getScheduler().runTask(this.plugin, new Runnable() {
            public void run() {
                if (result == null) {
                    MainAnvil.this.inv.getResultInventory().setItem(0, (ItemStack)null);
                } else {
                    MainAnvil.this.inv.getResultInventory().setItem(0, CraftItemStack.asNMSCopy(result));
                }

                ((CraftPlayer)MainAnvil.this.player).getHandle().setContainerData(MainAnvil.this.anvil, 0, MainAnvil.this.anvil.a);
            }
        });
    }

    public org.bukkit.inventory.ItemStack getResultSlot() {
        return CraftItemStack.asCraftMirror(this.inv.getResultInventory().getItem(0));
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setRepairCost(final int repairCost) {
        this.repairCost = repairCost;
        this.plugin.getServer().getScheduler().runTask(this.plugin, new Runnable() {
            public void run() {
                try {
                    MainAnvil.this.anvil.a = repairCost;
                    ((CraftPlayer)MainAnvil.this.player).getHandle().setContainerData(MainAnvil.this.anvil, 0, MainAnvil.this.anvil.a);
                } catch (Exception var2) {
                    var2.printStackTrace();
                }

            }
        });
    }

    public int getRepairCost() {
        return this.repairCost;
    }

    public boolean isInputSlot(int slot) {
        return slot == 0 || slot == 1;
    }

    public int getResultSlotID() {
        return 2;
    }

    public void clearInputs() {
        this.plugin.getServer().getScheduler().runTask(this.plugin, new Runnable() {
            public void run() {
                MainAnvil.this.inv.getIngredientsInventory().setItem(0, (ItemStack)null);
                MainAnvil.this.inv.getIngredientsInventory().setItem(1, (ItemStack)null);
                ((CraftPlayer)MainAnvil.this.player).getHandle().setContainerData(MainAnvil.this.anvil, 0, MainAnvil.this.anvil.a);
            }
        });
    }

    public void close() {
    }

    public Inventory getInventory() {
        return this.inv;
    }
}

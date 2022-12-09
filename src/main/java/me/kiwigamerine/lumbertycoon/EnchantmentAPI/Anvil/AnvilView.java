package me.kiwigamerine.lumbertycoon.EnchantmentAPI.Anvil;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface AnvilView {
    ItemStack[] getInputSlots();

    ItemStack[] getInputSlots(int var1, ItemStack var2);

    int getInputSlotID(int var1);

    void setResultSlot(ItemStack var1);

    ItemStack getResultSlot();

    Player getPlayer();

    void setRepairCost(int var1);

    int getRepairCost();

    boolean isInputSlot(int var1);

    int getResultSlotID();

    void clearInputs();

    void close();

    Inventory getInventory();
}

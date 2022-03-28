package com.SpellBend.util;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GUIUtil {

    public static @NotNull Inventory outlinedGUI(@NotNull ItemStack item, int slots, String name) {
        Inventory inv = Bukkit.createInventory(null, slots, name);
        for (int i=0; i<9;i++) {
            inv.setItem(i, item);
        }
        for (int i=1; i<=(slots+1)/9-2;i++) {
            inv.setItem(i*9, item);
            inv.setItem(i*9+8, item);
        }
        for (int i=slots-9; i<slots;i++) {
            inv.setItem(i, item);
        }
        return inv;
    }
}

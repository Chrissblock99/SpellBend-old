package com.SpellBend.util;

import com.SpellBend.data.Enums;
import com.SpellBend.data.PersistentDataKeys;
import com.SpellBend.spell.SpellHandler;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class dataUtil {
    /**
     *
     * @param item The item to extract the SpellType from
     * @return The SpellType
     */
    public static @Nullable Enums.SpellType getSpellType(@Nullable ItemStack item) {
        if (item == null) return null;
        if (item.getItemMeta() == null) return null;
        if (!item.getItemMeta().getPersistentDataContainer().has(PersistentDataKeys.spellTypeKey, PersistentDataType.STRING)) return null;
        try {
            return Enums.SpellType.valueOf(item.getItemMeta().getPersistentDataContainer().get(PersistentDataKeys.spellTypeKey, PersistentDataType.STRING));
        } catch (IllegalArgumentException exception) {
            Bukkit.getLogger().warning("Item " + item.getItemMeta().getDisplayName() + "§e that has an invalid SpellType!");
            return null;
        }
    }

    /**
     *
     * @param inv The inventory to search for Spells inside
     * @return The amount of Spells inside the inventory
     */
    public static int spellsInside(@NotNull Inventory inv) {
        int num = 0;
        for (ItemStack item : inv.getContents())
            if (item != null && SpellHandler.itemIsSpell(item)) num++;
        return num;
    }
}

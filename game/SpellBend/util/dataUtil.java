package game.SpellBend.util;

import game.SpellBend.data.Enums;
import game.SpellBend.data.PersistentDataKeys;
import game.SpellBend.spell.SpellHandler;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
    public static int spellsInsideInventory(@NotNull Inventory inv) {
        int num = 0;
        for (ItemStack item : inv.getContents())
            if (SpellHandler.itemIsSpell(item))
                num++;
        return num;
    }

    /**
     *
     * @param inv The inventory to search for SpellName
     * @param spellName The spellName to check for
     * @return The amount of Spells inside the inventory
     */
    public static boolean inventoryContainsSpellName(@NotNull Inventory inv, @NotNull String spellName) {
        for (ItemStack item : inv.getContents())
            if (SpellHandler.itemIsSpell(item) && spellName.equals(getPersistentDataValue(item, PersistentDataKeys.spellNameKey)))
                return true;
        return false;
    }

    /**
     *
     * @param inv The inventory to search for Spells inside
     * @param spellNameItem The item whose spellName to check for
     * @return The amount of Spells inside the inventory
     */
    public static boolean inventoryNotContainsSpellName(@NotNull Inventory inv, @NotNull ItemStack spellNameItem) {
        String spellNameString = getPersistentDataValue(spellNameItem, PersistentDataKeys.spellNameKey);
        for (ItemStack item : inv.getContents())
            //noinspection ConstantConditions
            if (SpellHandler.itemIsSpell(item) && spellNameString.equals(getPersistentDataValue(item, PersistentDataKeys.spellNameKey)))
                return false;
        return true;
    }

    /**
     *
     * @param item The item to extract from
     * @return The extracted String
     */
    public static @Nullable String getPersistentDataValue(@Nullable ItemStack item, @NotNull NamespacedKey key) {
        if (item == null) return null;
        if (!item.hasItemMeta()) return null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        if (!meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) return null;
        return meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    /**returns CMD > 100 && CMD <= 700
     *
     * @param item The item to check if clickable
     * @return If the item is a GUI clickable or not
     */
    public static boolean itemIsClickable(@Nullable ItemStack item) {
        if (item == null) return false;
        if (!item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        if (!meta.hasCustomModelData()) return false;
        int CMD = meta.getCustomModelData();
        return CMD > 100 && CMD <= 700;
    }

    /**
     *
     * @param inv The inv to check if it is a shop menu
     * @return If the inv is a shop menu or not
     */
    public static boolean inventoryIsShop(@NotNull Inventory inv) {
        ItemStack item = inv.getItem(4);
        if (item == null) return false;
        if (item.getItemMeta() == null) return false;
        return item.getItemMeta().getDisplayName().contains("Click on a");
    }

    /**Returns the SpellType of the item the player is holding
     * and null if it isn't a Spell
     *
     * @param player The player to get the SpellType from
     * @return The SpellType of the current hold item
     */
    public static @Nullable Enums.SpellType getHeldSpellType(@NotNull Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getItemMeta() == null) return null;
        if (!item.getItemMeta().getPersistentDataContainer().has(PersistentDataKeys.spellTypeKey, PersistentDataType.STRING)) return null;
        try {
            return Enums.SpellType.valueOf(item.getItemMeta().getPersistentDataContainer().get(PersistentDataKeys.spellTypeKey, PersistentDataType.STRING));
        } catch (IllegalArgumentException exception) {
            Bukkit.getLogger().warning(player.getDisplayName() + " has item " + item.getItemMeta().getDisplayName() + "§e that has an invalid SpellType!");
            return null;
        }
    }
}

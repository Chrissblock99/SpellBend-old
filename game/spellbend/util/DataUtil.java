package game.spellbend.util;

import game.spellbend.data.Enums;
import game.spellbend.data.PersistentDataKeys;
import game.spellbend.organize.BadgeObj;
import game.spellbend.organize.ElementObj;
import game.spellbend.organize.RankObj;
import game.spellbend.organize.SpellObj;
import game.spellbend.playerdata.Badges;
import game.spellbend.playerdata.PlayerDataUtil;
import game.spellbend.playerdata.Ranks;
import game.spellbend.spell.SpellHandler;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DataUtil {
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

    /**Returns the inventory the item clicked will be in after the click
     * returns null if the item goes on cursor
     * or somewhere else
     * Example: dropping items
     *
     * IMPORTANT:
     * if The ClickType is SWAP_OFFHAND or NUMBER_KEY the bottom inventory is returned
     *
     * @param invView The inventoryView
     * @param clickedInv The clicked inventory
     * @param clickType The clickType
     * @return The items destination inventory
     */
    public static @Nullable Inventory getItemClickedDestination(@NotNull InventoryView invView, @NotNull Inventory clickedInv, @NotNull ClickType clickType) {
        if (clickType.equals(ClickType.SHIFT_LEFT) || clickType.equals(ClickType.SHIFT_RIGHT)) {
            if (clickedInv.equals(invView.getBottomInventory()))
                return invView.getTopInventory();
            if (clickedInv.equals(invView.getTopInventory()))
                return invView.getBottomInventory();
            throw new IllegalArgumentException("The clicked inventory is not contained in the inventory view!");
        }

        if (clickType.equals(ClickType.NUMBER_KEY) || clickType.equals(ClickType.SWAP_OFFHAND))
            return invView.getBottomInventory();

        /*if (clickType.equals(ClickType.LEFT) || clickType.equals(ClickType.RIGHT))
            return null;

        if (clickType.equals(ClickType.WINDOW_BORDER_LEFT) || clickType.equals(ClickType.WINDOW_BORDER_RIGHT))
            return null;

        if (clickType.equals(ClickType.DROP) || clickType.equals(ClickType.CONTROL_DROP))
            return null;

        if (clickType.equals(ClickType.DOUBLE_CLICK))
            return null;

        if (clickType.equals(ClickType.MIDDLE))
            return null;

        //unknown what the hell stuff
        if (clickType.equals(ClickType.CREATIVE) || clickType.equals(ClickType.UNKNOWN))
            return null;*/
        return null;
    }

    /**Returns the inventory the item on cursor will be in after the click
     * returns null if the item remains on cursor
     * or goes somewhere else
     * Example:
     * if middle click with empty hand on an item the current item goes "nowhere"
     *
     * @param clickedInv The clicked inventory
     * @param clickType The clickType
     * @return The items destination inventory
     */
    public static @Nullable Inventory getItemOnCursorDestination(@NotNull Inventory clickedInv, @NotNull ClickType clickType) {
        if (clickType.equals(ClickType.LEFT) || clickType.equals(ClickType.RIGHT))
            return clickedInv;

        /*if (clickType.equals(ClickType.SHIFT_LEFT) || clickType.equals(ClickType.SHIFT_RIGHT))
            return null;

        if (clickType.equals(ClickType.NUMBER_KEY) || clickType.equals(ClickType.SWAP_OFFHAND))
            return null;

        if (clickType.equals(ClickType.WINDOW_BORDER_LEFT) || clickType.equals(ClickType.WINDOW_BORDER_RIGHT))
            return null;

        if (clickType.equals(ClickType.DROP) || clickType.equals(ClickType.CONTROL_DROP))
            return null;

        if (clickType.equals(ClickType.DOUBLE_CLICK))
            return null;

        if (clickType.equals(ClickType.MIDDLE))
            return null;

        //unknown what the hell stuff
        if (clickType.equals(ClickType.CREATIVE) || clickType.equals(ClickType.UNKNOWN))
            return null;*/
        return null;
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

    /**Calculates the price of all Spells in the element added together
     *
     * @param element The element to calculate the added Spells price of
     * @return The price
     */
    public static int calculateAddedPriceOfAllSpellsInElement(@NotNull ElementObj element) {
        int price = 0;
        for (SpellObj spell : element.getSpells())
            price += spell.getPrice();
        return price;
    }

    /**Checks if the sender has ANY of the passed in requirements and if not returns an Error message
     * if the sender is a player who is an Operator is factored in
     * if the sender has permission null is returned
     * non ConsoleCommandSenders or Players will never have access
     *
     * @param sender The sender to check for permission
     * @param rankNeeded A possibly required Rank
     * @param badgeNeeded A possibly required Badge
     * @param rankingNeeded A possible required Ranking
     * @return The no permission message or null if has permission
     */
    public static @Nullable String senderHasPermission(@NotNull CommandSender sender, @Nullable RankObj rankNeeded, @Nullable BadgeObj badgeNeeded, int rankingNeeded) {
        if (sender instanceof Player player) {
            String errorMsg = "§cYou must be an Operator to gain access to this command/subCommand!";
            boolean shallReturn = true;

            if (rankNeeded != null) {
                if (Ranks.hasRank(player, rankNeeded.rankName)) shallReturn = false;
                else errorMsg = "§cYou do not have the required Rank to use this command/subCommand! " + rankNeeded.rankName;
            }

            if (badgeNeeded != null) {
                if (Badges.hasBadge(player, badgeNeeded.badgeName)) shallReturn = false;
                else errorMsg = "§cYou do not have the required Badge to use this command/subCommand! " + badgeNeeded.badgeName;
            }

            if (PlayerDataUtil.getRanking(player)>=rankingNeeded) shallReturn = false;
            else errorMsg = "§cYou do not have the required Ranking to use this command/subCommand! " + rankingNeeded;

            if (player.isOp())
                shallReturn = false;

            if (shallReturn) {
                return errorMsg;
            }
        } else if (!(sender instanceof ConsoleCommandSender)) {
            return "§4Only Players or the Console can use this command/subCommand!";
        }
        return null;
    }
}

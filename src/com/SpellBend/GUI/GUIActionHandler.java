package com.SpellBend.GUI;

import com.SpellBend.data.Elements;
import com.SpellBend.data.Enums;
import com.SpellBend.organize.ElementObj;
import com.SpellBend.organize.SpellObj;
import com.SpellBend.playerData.Gems;
import com.SpellBend.playerData.Gold;
import com.SpellBend.playerData.SpellsOwned;
import com.SpellBend.util.Item;
import com.SpellBend.util.TextUtil;
import com.SpellBend.util.dataUtil;
import com.SpellBend.util.math.MathUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GUIActionHandler {
    public static void runItemAction(@NotNull String itemAction, @NotNull Player player) {
        if (itemAction.contains("giveSpell")) {
            String currentAction = itemAction.replace("giveSpell", "");
            String nth = TextUtil.removeOtherStrings(currentAction, new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"});
            String element = TextUtil.removeOtherStrings(currentAction, TextUtil.upperCaseABC);

            try {
            giveSpell(player, Enums.Element.valueOf(element), Integer.parseInt(nth));

            } catch (NumberFormatException exception) {
                Bukkit.getLogger().warning("\"" + nth + "\" is not a valid Integer!");
                exception.printStackTrace();
            } catch (IllegalArgumentException exception) {
                Bukkit.getLogger().warning("\"" + element + "\" is not a valid Element!");
                exception.printStackTrace();
            }
            return;
        }


        if (itemAction.contains("openShop")) {
            openShop(player, itemAction.replace("openShop", ""));
            return;
        }


        if (itemAction.contains("sendMessage")) {
            String currentAction = itemAction.replace("sendMessage", "");
            player.sendMessage(currentAction);
            return;
        }


        if (itemAction.contains("buySpell")) {
            String currentAction = itemAction.replace("buySpell", "");
            String nthString = TextUtil.removeOtherStrings(currentAction, new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"});
            String elementString = TextUtil.removeOtherStrings(currentAction, TextUtil.upperCaseABC);

            try {
            buySpell(player, Enums.Element.valueOf(elementString), Integer.parseInt(nthString));

            } catch (NumberFormatException exception) {
                Bukkit.getLogger().warning("\"" + nthString + "\" is not a valid Integer!");
                exception.printStackTrace();
            } catch (IllegalArgumentException exception) {
                Bukkit.getLogger().warning("\"" + elementString + "\" is not a valid Element!");
                exception.printStackTrace();
            }
            return;
        }


        if (itemAction.contains("buyElement")) {
            String elementString = TextUtil.removeOtherStrings(
                    itemAction.replace("buyElement", ""),
                    TextUtil.upperCaseABC);

            try {
                buyElement(player, Enums.Element.valueOf(elementString));
            } catch (IllegalArgumentException exception) {
                Bukkit.getLogger().warning("\"" + elementString + "\" is not a valid Element!");
                exception.printStackTrace();
            }
            return;
        }

        Bukkit.getLogger().warning( "\"" + itemAction + "\" is not a valid itemAction!");
    }

    public static void giveSpell(@NotNull Player player, @NotNull Enums.Element element, int nth) {
        ItemStack item = Item.edit(Elements.getElementByEnum(element).getSpellItem(nth), 1);
        if (dataUtil.spellsInside(player.getInventory())<5) {
            if (!player.getInventory().contains(item))
                player.getInventory().addItem(item);
            else player.sendMessage("§9§lSHOP §8»§c You already have this spell equipped!");
        } else player.sendMessage("§9§lSHOP §8»§c Unequip a spell first! §8(§7Drag into Shop§8)");
    }

    public static void openShop(@NotNull Player player, @NotNull String itemAction) {
        if (itemAction.isEmpty()) {
            player.openInventory(GUICreationUtil.createShop(player));
            return;
        }

        try {
            player.openInventory(GUICreationUtil.createElementGUI(player, Enums.Element.valueOf(itemAction)));
        } catch (IllegalArgumentException exception) {
            Bukkit.getLogger().warning(itemAction + " is not a valid Element");
            exception.printStackTrace();
        }
    }

    public static void buySpell(@NotNull Player player, @NotNull Enums.Element element, int nth) {
        SpellsOwned.setSpellsOwned(player, element, nth+1);
        SpellObj spellObj = Elements.getElementByEnum(element).getSpell(nth);

        Gold.addGold(player, -Elements.getElementByEnum(element).getSpell(nth).getPrice());
        if (dataUtil.spellsInside(player.getInventory()) < 5)
            player.getInventory().addItem(Item.edit(spellObj.getItem(), 1));
        //noinspection ConstantConditions
        player.sendMessage("§9§lSHOP §8»§e Learnt " + spellObj.getItem().getItemMeta().getDisplayName() + " §6for §e" + spellObj.getPrice() + " Gold!");
        player.openInventory(GUICreationUtil.createElementGUI(player, element));
    }

    public static void buyElement(@NotNull Player player, @NotNull Enums.Element element) {
        ElementObj elementObj = Elements.getElementByEnum(element);

        Gems.addGems(player, -Elements.getElementByEnum(element).getPrice());
        SpellsOwned.setSpellsOwned(player, element, 1);
        //SoundLib Shop.elementBought(player);
        if (MathUtil.additiveArrayValue(SpellsOwned.getAllSpellsOwned(player)) == 1) {
            SpellsOwned.setSpellsOwned(player, element, 5);
            Gold.addGold(player, -650);
            for (SpellObj spellObj : elementObj.getSpells())
                if (dataUtil.spellsInside(player.getInventory()) < 5)
                    player.getInventory().addItem(Item.edit(spellObj.getItem(), 1));
        }

        player.openInventory(GUICreationUtil.createElementGUI(player, element));
        //noinspection ConstantConditions
        player.sendMessage("§9§lSHOP §8»§e Purchased " + elementObj.getItem().getItemMeta().getDisplayName() + " §6for §b" + elementObj.getPrice() + " Gems§e!");
    }
}
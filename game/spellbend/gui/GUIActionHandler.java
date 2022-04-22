package game.spellbend.gui;

import game.spellbend.data.Elements;
import game.spellbend.data.Enums;
import game.spellbend.organize.ElementObj;
import game.spellbend.organize.SpellObj;
import game.spellbend.playerdata.Gems;
import game.spellbend.playerdata.Gold;
import game.spellbend.playerdata.SpellsOwned;
import game.spellbend.util.Item;
import game.spellbend.util.TextUtil;
import game.spellbend.util.DataUtil;
import game.spellbend.util.math.MathUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GUIActionHandler {
    public static void runItemAction(@NotNull String itemAction, @Nullable ClickType clickType, @NotNull Player player) {
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
            String currentAction = itemAction.replace("openShop", "");
            if (currentAction.isEmpty()) {
                openShop(player, null);
                return;
            }

            if (clickType != null) {
                if (clickType.equals(ClickType.SHIFT_RIGHT) || clickType.equals(ClickType.SHIFT_LEFT)) {
                    try {
                        addAllSpells(player, Enums.Element.valueOf(currentAction));
                    } catch (IllegalArgumentException exception) {
                        Bukkit.getLogger().warning(currentAction + " is not a valid Element");
                        exception.printStackTrace();
                    }
                    return;
                }
            }

            try {
                openShop(player, Enums.Element.valueOf(currentAction));
            } catch (IllegalArgumentException exception) {
                Bukkit.getLogger().warning(currentAction + " is not a valid Element");
                exception.printStackTrace();
            }
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
        if (DataUtil.spellsInsideInventory(player.getInventory())<5) {
            if (DataUtil.inventoryNotContainsSpellName(player.getInventory(), item))
                player.getInventory().addItem(item);
            else player.sendMessage("§9§lSHOP §8»§c You already have this spell equipped!");
        } else player.sendMessage("§9§lSHOP §8»§c Unequip a spell first! §8(§7Drag into Shop§8)");
    }

    public static void openShop(@NotNull Player player, @Nullable Enums.Element element) {
        if (element == null) {
            player.openInventory(GUICreationUtil.createShop(player));
            return;
        }

        player.openInventory(GUICreationUtil.createElementGUI(player, element));
    }

    public static void buySpell(@NotNull Player player, @NotNull Enums.Element element, int nth) {
        SpellsOwned.setSpellsOwned(player, element, nth+1);
        SpellObj spellObj = Elements.getElementByEnum(element).getSpell(nth);

        Gold.addGold(player, -Elements.getElementByEnum(element).getSpell(nth).getPrice());
        if (DataUtil.spellsInsideInventory(player.getInventory()) < 5)
            if (DataUtil.inventoryNotContainsSpellName(player.getInventory(), spellObj.getItem()))
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
            Gold.addGold(player, -DataUtil.calculateAddedPriceOfAllSpellsInElement(elementObj));
            addAllSpells(player, element);
        }

        player.openInventory(GUICreationUtil.createElementGUI(player, element));
        //noinspection ConstantConditions
        player.sendMessage("§9§lSHOP §8»§e Purchased " + elementObj.getItem().getItemMeta().getDisplayName() + " §6for §b" + elementObj.getPrice() + " Gems§e!");
    }


    public static void addAllSpells(@NotNull Player player, @NotNull Enums.Element element) {
        for (SpellObj spellObj : Elements.getElementByEnum(element).getSpells())
            if (DataUtil.spellsInsideInventory(player.getInventory()) < 5)
                player.getInventory().addItem(Item.edit(spellObj.getItem(), 1));
    }
}

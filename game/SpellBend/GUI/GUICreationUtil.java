package game.SpellBend.GUI;

import game.SpellBend.data.Elements;
import game.SpellBend.data.PersistentDataKeys;
import game.SpellBend.organize.ElementObj;
import game.SpellBend.data.Enums;
import game.SpellBend.organize.SpellObj;
import game.SpellBend.util.Item;
import game.SpellBend.playerData.Badges;
import game.SpellBend.playerData.Ranks;
import game.SpellBend.playerData.SpellsOwned;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GUICreationUtil {

    private static @NotNull String[] createElementOwnageLore(@NotNull Player player, @NotNull ElementObj element) {
        if (!element.playerOwns(player))
            return new String[]{"§8----------------", "§a$ §b" + element.getPrice() + " §3Gems",
                    (element.playerCanBuy(player)) ? "§6You can§e buy§6 this!" : "§cYou can't §e buy§c this yet!"};
        return new String[]{"§8----------------", "§a§lSHIFT CLICK TO EQUIP"};
    }

    private static @NotNull String[] createSpellOwnageLore(@NotNull Player player, @NotNull Enums.Element element, int nth, @NotNull SpellObj spell) {
        if (SpellsOwned.getSpellsOwned(player, element) >= nth+1)
            return new String[]{"§8----------------", "§a§lCLICK TO EQUIP"};
        if (SpellsOwned.getSpellsOwned(player, element) != nth)
            //noinspection ConstantConditions
            return new String[]{"§8----------------", "§cYou must learn " + Elements.getElementByEnum(element).getSpellItem(nth-1).getItemMeta().getDisplayName() + "§c first!"};
        return new String[]{"§8----------------", "§a$ §e" + spell.getPrice() + " §6Gold"};
    }

    @SuppressWarnings("CommentedOutCode")
    public static @NotNull Inventory createShop(@NotNull Player player) {
        Inventory shop = createDefaultShop();

        ArrayList<ElementObj> elementList = Elements.elementList;
        /*int x = i%7;
        int y = i/7;
        shop.setItem(1+9+(9*y)+x, elementList.get(i).getItem());*/
        for (int i = 0;i<elementList.size();i++) {
            ElementObj elementObj = elementList.get(i);
            shop.setItem(10+(9*(i/7))+(i%7),
                    Item.edit(elementList.get(i).getItem(),
                            101,
                            createElementOwnageLore(player, elementList.get(i)),
                            PersistentDataKeys.itemActionKey,
                            (elementObj.playerOwns(player)) ?
                                    "openShop" + elementList.get(i).getElement() :
                                    (!elementObj.playerCanBuy(player)) ?
                                            "sendMessage§9§lSHOP §8»§c Not enough Gems! Need §b" + elementObj.getPrice() + "§c more!" :
                                            "buyElement" + elementObj.getElement()));
        }

        //Patron Cosmetics
        if (Ranks.hasRank(player, "patron")) shop.setItem(37, Item.create(Material.ENDER_CHEST, "§b§lPATRON COSMETICS", 301));

        //Legend Cosmetics
        if (Ranks.hasRank(player, "legend")) shop.setItem(38, Item.create(Material.LODESTONE, "§e§lLEGEND COSMETICS", 301));

        //Builder Cosmetics
        if (Badges.hasBadge(player, "builder")) shop.setItem(43, Item.create(Material.COMPOSTER, "§6§lBUILDER COSMETICS", 301));
        
        return shop;
    }

    public static @NotNull Inventory createElementGUI(@NotNull Player player, @NotNull Enums.Element element) {
        ElementObj elementObj = Elements.getElementByEnum(element);
        //noinspection ConstantConditions
        Inventory inv = createDefaultElementGUI(elementObj.getItem().getItemMeta().getDisplayName());
        int length = elementObj.getSpells().size();
        int[] positions = {12, 14, 22, 30, 32, 33, 34, 35}; //the last 3 are there for the case that we ever do more than 5 spells per Element

        for (int i = 0;i<length;i++) {
            SpellObj spell = elementObj.getSpell(i);
            //noinspection ConstantConditions
            inv.setItem(positions[i], Item.editAddLore(spell.getItem(),
                    101,
                    createSpellOwnageLore(player, element, i, spell),
                    PersistentDataKeys.itemActionKey,
                    (elementObj.playerOwnsSpell(player, i)) ?
                            "giveSpell" + elementObj.getElement() + i :
                            (SpellsOwned.getSpellsOwned(player, elementObj.getIndex()) != i) ?
                                    "sendMessage§9§lSHOP §8»§c You must learn " + elementObj.getSpellItem(i-1).getItemMeta().getDisplayName() + "§c first!" :
                                    (!spell.playerCanBuy(player)) ?
                                            "sendMessage§9§lSHOP §8»§c Not enough Gold! Need §e" + spell.getPrice() + "§c more!" :
                                            "buySpell" + elementObj.getElement() + i));
        }

        return inv;
    }

    private static @NotNull Inventory createDefaultShop() {
        Inventory defaultShop = GUIUtil.outlinedGUI(Item.create(Material.BLUE_STAINED_GLASS_PANE, "§t", 501), 45, "§9§lSHOP");

        /*Sign*/ defaultShop.setItem(4, Item.create(Material.OAK_SIGN, "§r§lClick on an Element", new String[]{"§fto purchase it!"}, 501));
        /*Cosmetics*/ defaultShop.setItem(40, Item.create(Material.CHEST, "§9§lCOSMETICS", 301));

        return defaultShop;
    }

    private static @NotNull Inventory createDefaultElementGUI(@NotNull String name) {
        Inventory defaultElementGUI = GUIUtil.outlinedGUI(Item.create(Material.BLUE_STAINED_GLASS_PANE, "§t", 501), 45, name);

        /*Sign*/ defaultElementGUI.setItem(4, Item.create(Material.OAK_SIGN, "§r§lClick on a move", new String[]{
                "§fto purchase it!",
                "§8---------------",
                "§7Click on§f§l glowing moves",
                "§7to purchase them!",
                "",
                "§f§lDrag §7or§f§l shift click §7moves",
                "§7to unequip them!"
        }, 501, "ItemType", "GUIDeco"));

        /*Back*/ defaultElementGUI.setItem(18, Item.create(Material.ARROW, "§7§lBack", 301, PersistentDataKeys.itemActionKey, "openShop"));
        return defaultElementGUI;
    }
}

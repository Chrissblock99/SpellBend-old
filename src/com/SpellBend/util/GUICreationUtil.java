package com.SpellBend.util;

import com.SpellBend.data.Lists;
import com.SpellBend.organize.ElementObj;
import com.SpellBend.organize.Enums;
import com.SpellBend.data.Maps;
import com.SpellBend.util.playerData.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GUICreationUtil {
    private final static Inventory DEFAULT_SHOP = createDefaultShop();

    private static @NotNull String[] createOwnageLore(@NotNull Player player, @NotNull Enums.Element element) {
        if (SpellsOwned.getSpellsOwned(player, element) == 0) {
            int price = Maps.elementToPriceMap.get(element);
            return new String[]{"§8-----------", "§e$ §b" + price + " §3Gems", (Gems.getGems(player)>price) ? "§2You can buy this." : "§cYou can't §ebuy §cthis yet!"};
        }
        return new String[]{"§8----------------", "§a§lSHIFT CLICK TO EQUIP"};
    }

    public static @NotNull Inventory createShop(@NotNull Player player) {
        Inventory shop = DEFAULT_SHOP;

        ArrayList<ElementObj> elementList = Lists.elementList;
        /*int x = i%7;
        int y = i/7;
        shop.setItem(10+(9*y)+x, elementList.get(i).getItem());*/
        for (int i = 0;i<elementList.size();i++) shop.setItem(10+(9*(i/7))+(i%7), elementList.get(i).getItem());

        //Patron Cosmetics
        if (Ranks.hasRank(player, "patron")) shop.setItem(37, Item.create(Material.ENDER_CHEST, "§b§lPATRON COSMETICS", 301));

        //Legend Cosmetics
        if (Ranks.hasRank(player, "legend")) shop.setItem(38, Item.create(Material.LODESTONE, "§e§lLEGEND COSMETICS", 301));

        //Builder Cosmetics
        if (Badges.hasBadge(player, "builder")) shop.setItem(43, Item.create(Material.COMPOSTER, "§6§lBUILDER COSMETICS", 301));
        
        return shop;
    }

    public static @NotNull Inventory createElementGUI(@NotNull Player player, @NotNull ElementObj element) {
        Inventory inv = createDefaultElementGUI("§c§lEmber");

        inv.setItem(12, Item.edit(element.getSpellItem(0), new String[]{""}));
        inv.setItem(14, element.getSpellItem(1));
        inv.setItem(22, element.getSpellItem(2));
        inv.setItem(30, element.getSpellItem(3));
        inv.setItem(32, element.getSpellItem(4));
        inv.setItem(18, Item.create(Material.ARROW, "Back"));

        return inv;
    }

    public static @NotNull Inventory createEmberGUI(Player player) {
        Inventory inv = createDefaultElementGUI("§c§lEmber");

        inv.setItem(12, Item.create(Material.GOLDEN_HOE, "§c§lMagma Burst", 101));
        inv.setItem(14, Item.create(Material.GOLDEN_HORSE_ARMOR, "§c§lEmber Blast", 101));
        inv.setItem(22, Item.create(Material.GOLDEN_SHOVEL, "§c§lScorching Column", 101));
        inv.setItem(30, Item.create(Material.GOLDEN_SWORD, "§c§lBlazing Spin", 101));
        inv.setItem(32, Item.create(Material.CAMPFIRE, "§c§lFiery Rage", 101));
        return inv;
    }

    public static @NotNull Inventory createWaterGUI(Player player) {
        Inventory inv = createDefaultElementGUI("§9§lWater");

        inv.setItem(12, Item.create(Material.DIAMOND_HORSE_ARMOR, "§9§lHydro Blast", 101));
        inv.setItem(14, Item.create(Material.DIAMOND_HOE, "§9§lWater Spray", 101));
        inv.setItem(22, Item.create(Material.DIAMOND_SHOVEL, "§9§lWater Torrent", 101));
        inv.setItem(30, Item.create(Material.HEART_OF_THE_SEA, "§9§lSea Shield", 101));
        inv.setItem(32, Item.create(Material.COD, "§9§lRising Tide", 101));

        return inv;
    }

    private static @NotNull Inventory createDefaultShop() {
        Inventory defaultShop = GUIUtil.outlinedGUI(Item.create(Material.BLUE_STAINED_GLASS_PANE, "§t", 501), 45, "§9§lSHOP");

        /*Sign*/ defaultShop.setItem(4, Item.create(Material.OAK_SIGN, "§r§lClick on an Element", new String[]{"§fto purchase it!"}, 501));
        /*Cosmetics*/ defaultShop.setItem(40, Item.create(Material.CHEST, "§9§lCOSMETICS", 301));

        return defaultShop;
    }

    private static @NotNull Inventory createDefaultElementGUI(String name) {
        Inventory defaultElementGUI = GUIUtil.outlinedGUI(Item.create(Material.BLUE_STAINED_GLASS_PANE, "§t", 501), 45, name);

        /*Sign*/ defaultElementGUI.setItem(4, Item.create(Material.OAK_SIGN, "§r§lClick on a move", new String[]{
                "§fto purchase it!",
                "§8---------------",
                "§7Click on §f§lglowing moves",
                "§7to purchase them!",
                "",
                "§f§lDrag §7or §f§lshift click §7moves",
                "§7to unequip them!"
        }, 501, "ItemType", "GUIDeco"));

        /*Back*/ defaultElementGUI.setItem(18, Item.create(Material.ARROW, "§7§lBack", 301));
        return defaultElementGUI;
    }
}

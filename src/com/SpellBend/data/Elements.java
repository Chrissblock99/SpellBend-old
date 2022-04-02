package com.SpellBend.data;

import com.SpellBend.organize.ElementObj;
import com.SpellBend.organize.Enums;
import com.SpellBend.organize.SpellObj;
import com.SpellBend.util.Item;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Elements {
    public static final ArrayList<ElementObj> elementList = createElementList();

    private static @NotNull ArrayList<ElementObj> createElementList() {
        ArrayList<ElementObj> elementList = new ArrayList<>();
        ArrayList<SpellObj> spellList = new ArrayList<>();

        spellList.add(new SpellObj( "Magma_Burst",
                Item.create(Material.GOLDEN_HOE,
                    "§l§cMagma Burst",
                    new String[]{
                        "§8---------------",
                        "",
                        "",
                        "",
                        "§8----------------",
                        ""}
                    ), 11111111));
        spellList.add(new SpellObj( "Magma_Burst",
                Item.create(Material.GOLDEN_HOE,
                        "§l§cMagma Burst",
                        new String[]{
                                "§8---------------",
                                "",
                                "",
                                "",
                                "§8----------------",
                                ""}
                ), 11111111));
        spellList.add(new SpellObj( "Magma_Burst",
                Item.create(Material.GOLDEN_HOE,
                        "§l§cMagma Burst",
                        new String[]{
                                "§8---------------",
                                "",
                                "",
                                "",
                                "§8----------------",
                                ""}
                ), 11111111));
        spellList.add(new SpellObj( "Magma_Burst",
                Item.create(Material.GOLDEN_HOE,
                        "§l§cMagma Burst",
                        new String[]{
                                "§8---------------",
                                "",
                                "",
                                "",
                                "§8----------------",
                                ""}
                ), 11111111));
        spellList.add(new SpellObj( "Magma_Burst",
                Item.create(Material.GOLDEN_HOE,
                        "§l§cMagma Burst",
                        new String[]{
                                "§8---------------",
                                "",
                                "",
                                "",
                                "§8----------------",
                                ""}
                ), 11111111));
        elementList.add(new ElementObj("Ember", Enums.Element.EMBER, Item.create(Material.FIRE_CHARGE, "§l§cEmber1"), spellList, 150, 0));
        spellList.clear();

        return elementList;
    }

    public static ElementObj getElementByEnum(Enums.Element element) {
        return elementList.stream()
                .filter(object -> element.equals(object.getElement()))
                .findAny()
                .orElse(null);
    }
}

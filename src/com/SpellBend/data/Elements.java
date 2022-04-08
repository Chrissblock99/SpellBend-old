package com.SpellBend.data;

import com.SpellBend.organize.ElementObj;
import com.SpellBend.organize.SpellObj;
import com.SpellBend.util.Item;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Elements {
    public static final ArrayList<ElementObj> elementList = createElementList();

    private static @NotNull ArrayList<ElementObj> createElementList() {
        ArrayList<ElementObj> elementList = new ArrayList<>();
        ArrayList<SpellObj> spellList = new ArrayList<>();

        spellList.add(new SpellObj( "Magma_Burst",
                Item.create(Material.GOLDEN_HOE,
                        "§c§lMagma Burst",
                        new String[]{
                                "§8---------------",
                                "§6User casts a§e rapid burst",
                                "§eof magma§6 that deals§e low",
                                "§edamage."},
                        new NamespacedKey[]{PersistentDataKeys.spellNameKey, PersistentDataKeys.spellTypeKey},
                        new String[]{"Magma_Burst", "MULTI_PROJECTILE"}
                        ), 0));
        spellList.add(new SpellObj( "Ember_Blast",
                Item.create(Material.GOLDEN_HORSE_ARMOR,
                        "§c§lEmber Blast",
                        new String[]{
                                "§8---------------",
                                "",
                                "",
                                ""},
                        new NamespacedKey[]{PersistentDataKeys.spellNameKey, PersistentDataKeys.spellTypeKey},
                        new String[]{"Ember_Blast", "BLAST"}
                        ), 50));
        spellList.add(new SpellObj( "Scorching_Column",
                Item.create(Material.GOLDEN_SHOVEL,
                        "§c§lScorching Column",
                        new String[]{
                                "§8---------------",
                                "",
                                "",
                                ""},
                        new NamespacedKey[]{PersistentDataKeys.spellNameKey, PersistentDataKeys.spellTypeKey},
                        new String[]{"Scorching_Column", "AOE"}
                        ), 150));
        spellList.add(new SpellObj( "Blazing_Spin",
                Item.create(Material.GOLDEN_SWORD,
                        "§c§lBlazing Spin",
                        new String[]{
                                "§8---------------",
                                "",
                                "",
                                ""},
                        new NamespacedKey[]{PersistentDataKeys.spellNameKey, PersistentDataKeys.spellTypeKey},
                        new String[]{"Blazing_Spin", "SHIELD"}
                        ), 200));
        spellList.add(new SpellObj( "Fiery_Rage",
                Item.create(Material.CAMPFIRE,
                        "§c§lFiery Rage",
                        new String[]{
                                "§8---------------",
                                "",
                                "",
                                ""},
                        new NamespacedKey[]{PersistentDataKeys.spellNameKey, PersistentDataKeys.spellTypeKey},
                        new String[]{"Fiery_Rage", "AURA"}
                        ), 250));
        //noinspection unchecked
        elementList.add(new ElementObj("Ember", Enums.Element.EMBER, Item.create(Material.FIRE_CHARGE, "§c§lEmber"), ((ArrayList<SpellObj>) spellList.clone()), 150, 0));
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

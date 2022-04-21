package game.spellbend.data;

import game.spellbend.organize.ElementObj;
import game.spellbend.organize.SpellObj;
import game.spellbend.util.Item;
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
        elementList.add(new ElementObj("Ember", Enums.Element.EMBER, Item.create(Material.FIRE_CHARGE, "§c§lEmber"),
                ((ArrayList<SpellObj>) spellList.clone()), 150, 0));
        spellList.clear();


        spellList.add(new SpellObj( "Hydro_Blast",
                Item.create(Material.DIAMOND_HORSE_ARMOR,
                        "§9§lHydro Blast",
                        new String[]{
                                "§8---------------",
                                "§6User casts a§e rapid burst",
                                "§eof magma§6 that deals§e low",
                                "§edamage."},
                        new NamespacedKey[]{PersistentDataKeys.spellNameKey, PersistentDataKeys.spellTypeKey},
                        new String[]{"Hydro_Blast", "BLAST"}
                ), 0));
        spellList.add(new SpellObj( "Water_Spray",
                Item.create(Material.DIAMOND_HOE,
                        "§9§lWater Spray",
                        new String[]{
                                "§8---------------",
                                "",
                                "",
                                ""},
                        new NamespacedKey[]{PersistentDataKeys.spellNameKey, PersistentDataKeys.spellTypeKey},
                        new String[]{"Water_Spray", "MULTI_PROJECTILE"}
                ), 50));
        spellList.add(new SpellObj( "Water_Torrent",
                Item.create(Material.DIAMOND_SHOVEL,
                        "§9§lWater Torrent",
                        new String[]{
                                "§8---------------",
                                "",
                                "",
                                ""},
                        new NamespacedKey[]{PersistentDataKeys.spellNameKey, PersistentDataKeys.spellTypeKey},
                        new String[]{"Water_Torrent", "AOE"}
                ), 150));
        spellList.add(new SpellObj( "Sea_Shield",
                Item.create(Material.HEART_OF_THE_SEA,
                        "§9§lSea Shield",
                        new String[]{
                                "§8---------------",
                                "",
                                "",
                                ""},
                        new NamespacedKey[]{PersistentDataKeys.spellNameKey, PersistentDataKeys.spellTypeKey},
                        new String[]{"Sea_Shield", "SHIELD"}
                ), 200));
        spellList.add(new SpellObj( "Rising_Tide",
                Item.create(Material.COD,
                        "§9§lRising Tide",
                        new String[]{
                                "§8---------------",
                                "",
                                "",
                                ""},
                        new NamespacedKey[]{PersistentDataKeys.spellNameKey, PersistentDataKeys.spellTypeKey},
                        new String[]{"Rising Tide", "TRANSPORT"}
                ), 250));
        //noinspection unchecked
        elementList.add(new ElementObj("Water", Enums.Element.WATER, Item.create(Material.WATER_BUCKET, "§9§lWater"),
                ((ArrayList<SpellObj>) spellList.clone()), 150, 1));
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

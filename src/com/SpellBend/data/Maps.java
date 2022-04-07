package com.SpellBend.data;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Maps {
    public final static HashMap<Enums.Element, Integer> elementToIndexMap = createElementToIndexMap();
    public final static HashMap<Enums.Element, Integer> elementToPriceMap = createElementToPriceMap();

    private static @NotNull HashMap<Enums.Element, Integer> createElementToIndexMap() {
        HashMap<Enums.Element, Integer> elementToIndexMap= new HashMap<>();

        elementToIndexMap.put(Enums.Element.EMBER, 0);
        elementToIndexMap.put(Enums.Element.WATER, 1);
        elementToIndexMap.put(Enums.Element.NATURE, 2);
        elementToIndexMap.put(Enums.Element.EARTH, 3);
        elementToIndexMap.put(Enums.Element.ELECTRO, 4);
        elementToIndexMap.put(Enums.Element.ICE, 5);
        elementToIndexMap.put(Enums.Element.AETHER, 6);
        elementToIndexMap.put(Enums.Element.SOUL, 7);
        elementToIndexMap.put(Enums.Element.TIME, 8);
        elementToIndexMap.put(Enums.Element.METAL, 9);

        return elementToIndexMap;
    }

    private static @NotNull HashMap<Enums.Element, Integer> createElementToPriceMap() {
        HashMap<Enums.Element, Integer> elementToPriceMap = new HashMap<>();

        elementToPriceMap.put(Enums.Element.EMBER, 150);
        elementToPriceMap.put(Enums.Element.WATER, 150);
        elementToPriceMap.put(Enums.Element.NATURE, 150);
        elementToPriceMap.put(Enums.Element.EARTH, 300);
        elementToPriceMap.put(Enums.Element.ELECTRO, 300);
        elementToPriceMap.put(Enums.Element.ICE, 300);
        elementToPriceMap.put(Enums.Element.AETHER, 650);
        elementToPriceMap.put(Enums.Element.SOUL, 650);
        elementToPriceMap.put(Enums.Element.TIME, 650);
        elementToPriceMap.put(Enums.Element.METAL, 650);

        return elementToPriceMap;
    }
}

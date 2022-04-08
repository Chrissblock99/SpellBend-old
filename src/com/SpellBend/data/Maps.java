package com.SpellBend.data;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Maps {
    public final static HashMap<Enums.Element, Integer> elementToIndexMap = createElementToIndexMap();

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
}

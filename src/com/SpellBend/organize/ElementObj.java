package com.SpellBend.organize;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ElementObj {
    private final String name;
    private final Enums.Element element;
    private final ItemStack item;
    private final ArrayList<SpellObj> spells;
    private final int price;
    private final int index;

    public ElementObj(String name, Enums.Element element, ItemStack item, ArrayList<SpellObj> spells, int price, int index) {
        this.name = name;
        this.element = element;
        this.item = item;
        this.spells = spells;
        this.price = price;
        this.index = index;
    }

    public ItemStack getSpellItem(int index) {return spells.get(index).getItem();}

    public SpellObj getSpell(int index) {return spells.get(index);}

    public String getName() {return name;}

    public Enums.Element getElement() {return element;}

    public ItemStack getItem() {return item;}

    public ArrayList<SpellObj> getSpells() {return spells;}

    public int getPrice() {return price;}

    public int getIndex() {return index;}
}

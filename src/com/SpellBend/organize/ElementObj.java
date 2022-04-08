package com.SpellBend.organize;

import com.SpellBend.data.Enums;
import com.SpellBend.playerData.Gems;
import com.SpellBend.playerData.SpellsOwned;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

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
        if (spells.size() != 5) throw new IllegalArgumentException("Spell List must be of length 5!");
        this.spells = spells;
        this.price = price;
        this.index = index;
    }

    public boolean playerOwns(@NotNull Player player) {
        return SpellsOwned.getSpellsOwned(player, index)>0;
    }

    public boolean playerCanBuy(@NotNull Player player) {
        return Gems.getGems(player) >= price;
    }

    public boolean playerOwnsSpell(@NotNull Player player, int nth) {
        return SpellsOwned.getSpellsOwned(player, index) >= nth+1;
    }

    public ItemStack getSpellItem(int index) {
        return spells.get(index).getItem();
    }

    public SpellObj getSpell(int index) {
        return spells.get(index);
    }

    public String getName() {return name;}

    public Enums.Element getElement() {return element;}

    public ItemStack getItem() {return item;}

    public ArrayList<SpellObj> getSpells() {return spells;}

    public int getPrice() {return price;}

    public int getIndex() {return index;}
}

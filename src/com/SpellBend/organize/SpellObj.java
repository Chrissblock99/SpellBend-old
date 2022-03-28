package com.SpellBend.organize;

import org.bukkit.inventory.ItemStack;

public class SpellObj {
    private final String name;
    private final ItemStack item;
    private final int price;

    public SpellObj(String name, ItemStack item, int price) {
        this.name = name;
        this.item = item;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getPrice() {
        return price;
    }
}

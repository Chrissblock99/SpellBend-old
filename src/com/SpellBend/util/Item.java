package com.SpellBend.util;

import com.SpellBend.PluginMain;
import com.SpellBend.data.Enums;
import com.SpellBend.spell.SpellHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unused"})
public class Item {
    private final static PluginMain plugin = PluginMain.getInstance();

    public static @NotNull ItemStack create(Material material, String name)
    {return create(material, name, 0);}
    public static @NotNull ItemStack create(Material material, String name, String[] lore)
    {return create(material, name, lore, 0);}
    public static @NotNull ItemStack create(Material material, String name, String key, String customData)
    {return create(material, name, 0, key, customData);}
    public static @NotNull ItemStack create(Material material, String name, String[] key, String[] customData)
    {return create(material, name, 0, key, customData);}
    public static @NotNull ItemStack create(Material material, String name, HashMap<NamespacedKey, String> persistentData)
    {return create(material, name, 0, persistentData);}
    public static @NotNull ItemStack create(Material material, String name, String[] lore, String key, String customData)
    {return create(material, name, lore, 0, key, customData);}
    public static @NotNull ItemStack create(Material material, String name, String[] lore, String[] key, String[] customData)
    {return create(material, name, lore, 0, key, customData);}
    public static @NotNull ItemStack create(Material material, String name, String[] lore, HashMap<NamespacedKey, String> persistentData)
    {return create(material, name, lore, 0, persistentData);}

    public static @NotNull ItemStack create(Material material, String name, int CustomModelData) {
        ItemStack item = new ItemStack(Material.STONE);
        ItemMeta meta = item.getItemMeta();

        item = new ItemStack(material);
        meta.setDisplayName(name);
        meta.setCustomModelData(CustomModelData);
        item.setItemMeta(meta);

        return item;
    }

    public static @NotNull ItemStack create(Material material, String name, String[] lore, int CustomModelData) {
        ItemStack item = new ItemStack(Material.STONE);
        ItemMeta meta = item.getItemMeta();

        item = new ItemStack(material);
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(CustomModelData);
        item.setItemMeta(meta);

        return item;
    }

    public static @NotNull ItemStack create(Material material, String name, int CustomModelData, String key, String customData) {
        ItemStack item = new ItemStack(Material.STONE);
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();

        item = new ItemStack(material);
        meta.setDisplayName(name);
        meta.setCustomModelData(CustomModelData);
        data.set(new NamespacedKey(plugin, key), PersistentDataType.STRING, customData);
        item.setItemMeta(meta);

        return item;
    }

    public static @NotNull ItemStack create(Material material, String name, String[] lore, int CustomModelData, String key, String customData) {
        ItemStack item = new ItemStack(Material.STONE);
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();

        item = new ItemStack(material);
        meta.setDisplayName(name);
        data.set(new NamespacedKey(plugin, key), PersistentDataType.STRING, customData);
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(CustomModelData);
        item.setItemMeta(meta);

        return item;
    }

    public static @NotNull ItemStack create(Material material, String name, int CustomModelData, @NotNull String[] key, @NotNull String[] customData) {
        ItemStack item = new ItemStack(Material.STONE);
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();

        item = new ItemStack(material);
        meta.setDisplayName(name);
        meta.setCustomModelData(CustomModelData);
        for (int i = 0;i<key.length;i++) {
            data.set(new NamespacedKey(plugin, key[i]), PersistentDataType.STRING, customData[i]);
        }
        item.setItemMeta(meta);

        return item;
    }

    public static @NotNull ItemStack create(Material material, String name, int CustomModelData, @NotNull HashMap<NamespacedKey, String> persistentData) {
        ItemStack item = new ItemStack(Material.STONE);
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();

        item = new ItemStack(material);
        meta.setDisplayName(name);
        meta.setCustomModelData(CustomModelData);
        for (Map.Entry<NamespacedKey, String> entry : persistentData.entrySet()) data.set(entry.getKey(), PersistentDataType.STRING, entry.getValue());
        item.setItemMeta(meta);

        return item;
    }

    public static @NotNull ItemStack create(Material material, String name, String[] lore, int CustomModelData, @NotNull String [] key, @NotNull String [] customData) {
        ItemStack item = new ItemStack(Material.STONE);
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();

        item = new ItemStack(material);
        meta.setDisplayName(name);
        for (int i = 0;i<key.length;i++) {
            data.set(new NamespacedKey(plugin, key[i]), PersistentDataType.STRING, customData[i]);
        }
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(CustomModelData);
        item.setItemMeta(meta);

        return item;
    }

    public static @NotNull ItemStack create(@NotNull Material material, @NotNull String name, @NotNull String[] lore, int CustomModelData, @NotNull HashMap<NamespacedKey, String> persistentData) {
        ItemStack item = new ItemStack(Material.STONE);
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();

        item = new ItemStack(material);
        meta.setDisplayName(name);
        for (Map.Entry<NamespacedKey, String> entry : persistentData.entrySet()) data.set(entry.getKey(), PersistentDataType.STRING, entry.getValue());
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(CustomModelData);
        item.setItemMeta(meta);

        return item;
    }

    public static @NotNull ItemStack edit(@NotNull ItemStack item, @NotNull String[] lore) {
        item.getItemMeta().setLore(Arrays.asList(lore));
        return item;
    }

    public static @NotNull ItemStack edit(@NotNull ItemStack item, @NotNull String key, @NotNull String customData) {
        item.getItemMeta().getPersistentDataContainer().set(new NamespacedKey(plugin, key), PersistentDataType.STRING, customData);
        return item;
    }

    public static @NotNull ItemStack edit(@NotNull ItemStack item, int CustomModelData, @NotNull String[] lore, @NotNull NamespacedKey key, @NotNull String customData) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) throw new NullPointerException("The meta of item to edit was null!");
        meta.setCustomModelData(CustomModelData);
        meta.setLore(Arrays.asList(lore));
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, customData);
        item.setItemMeta(meta);
        return item;
    }

    public static @Nullable Enums.SpellType getSpellType(@Nullable ItemStack item) {
        if (item == null) return null;
        if (item.getItemMeta() == null) return null;
        if (!item.getItemMeta().getPersistentDataContainer().has(SpellHandler.spellTypeKey, PersistentDataType.STRING)) return null;
        try {
            return Enums.SpellType.valueOf(item.getItemMeta().getPersistentDataContainer().get(SpellHandler.spellTypeKey, PersistentDataType.STRING));
        } catch (IllegalArgumentException exception) {
            Bukkit.getLogger().warning("Item " + item.getItemMeta().getDisplayName() + "§e that has an invalid SpellType!");
            return null;
        }
    }
}

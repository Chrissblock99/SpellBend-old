package game.spellbend.util;

import game.spellbend.PluginMain;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings({"unused"})
public class Item {
    private final static PluginMain plugin = PluginMain.getInstance();

    /**Creates an item
     *
     * @param material The material of the new item
     * @param name The name of the new item
     * @return The created item
     */
    public static @NotNull ItemStack create(Material material, String name)
    {return create(material, name, 0);}
    /**Creates an item
     *
     * @param material The material of the new item
     * @param name The name of the new item
     * @param lore The lore of the new item
     * @return The created item
     */
    public static @NotNull ItemStack create(Material material, String name, String[] lore)
    {return create(material, name, lore, 0);}
    /**Creates an item
     *
     * @param material The material of the new item
     * @param name The name of the new item
     * @param key The StringKey for the PersistentData of the new item
     * @param customData The StringValue for the PersistentData of the new item
     * @return The created item
     */
    public static @NotNull ItemStack create(Material material, String name, String key, String customData)
    {return create(material, name, 0, key, customData);}
    /**Creates an item
     *
     * @param material The material of the new item
     * @param name The name of the new item
     * @param key The NamespacedKey for the PersistentData of the new item
     * @param customData The StringValue for the PersistentData of the new item
     * @return The created item
     */
    public static @NotNull ItemStack create(Material material, String name, NamespacedKey key, String customData)
    {return create(material, name, 0, key, customData);}
    /**Creates an item
     *
     * @param material The material of the new item
     * @param name The name of the new item
     * @param key The String[] of keys for the PersistentData of the new item
     * @param customData The String[] of values for the PersistentData of the new item
     * @return The created item
     */
    public static @NotNull ItemStack create(Material material, String name, NamespacedKey[] key, String[] customData)
    {return create(material, name, 0, key, customData);}
    /**Creates an item
     *
     * @param material The material of the new item
     * @param name The name of the new item
     * @param persistentData A HashMap<NamespacedKey, String> as PersistentData of the new Item
     * @return The created item
     */
    public static @NotNull ItemStack create(Material material, String name, HashMap<NamespacedKey, String> persistentData)
    {return create(material, name, 0, persistentData);}
    /**Creates an item
     *
     * @param material The material of the new item
     * @param name The name of the new item
     * @param lore The lore of the new item
     * @param key The StringKey for the PersistentData of the new item
     * @param customData The StringValue for the PersistentData of the new item
     * @return The created item
     */
    public static @NotNull ItemStack create(Material material, String name, String[] lore, String key, String customData)
    {return create(material, name, lore, 0, key, customData);}
    /**Creates an item
     *
     * @param material The material of the new item
     * @param name The name of the new item
     * @param lore The lore of the new item
     * @param key The NamespacedKey[] of keys for the PersistentData of the new item
     * @param customData The String[] of values for the PersistentData of the new item
     * @return The created item
     */
    public static @NotNull ItemStack create(Material material, String name, String[] lore, NamespacedKey[] key, String[] customData)
    {return create(material, name, lore, 0, key, customData);}
    /**Creates an item
     *
     * @param material The material of the new item
     * @param name The name of the new item
     * @param lore The lore of the new item
     * @param persistentData A HashMap<NamespacedKey, String> as PersistentData of the new Item
     * @return The created item
     */
    public static @NotNull ItemStack create(Material material, String name, String[] lore, HashMap<NamespacedKey, String> persistentData)
    {return create(material, name, lore, 0, persistentData);}

    /**Creates an item
     *
     * @param material The material of the new item
     * @param name The name of the new item
     * @param CustomModelData The CustomModelData of the new item
     * @return The created item
     */
    public static @NotNull ItemStack create(Material material, String name, int CustomModelData) {
        ItemStack item = new ItemStack(Material.STONE);
        ItemMeta meta = item.getItemMeta();

        item = new ItemStack(material);
        //noinspection ConstantConditions
        meta.setDisplayName(name);
        meta.setCustomModelData(CustomModelData);
        item.setItemMeta(meta);

        return item;
    }

    /**Creates an item
     *
     * @param material The material of the new item
     * @param name The name of the new item
     * @param lore The lore of the new item
     * @param CustomModelData The CustomModelData of the new item
     * @return The created item
     */
    public static @NotNull ItemStack create(Material material, String name, String[] lore, int CustomModelData) {
        ItemStack item = new ItemStack(Material.STONE);
        ItemMeta meta = item.getItemMeta();

        item = new ItemStack(material);
        //noinspection ConstantConditions
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(CustomModelData);
        item.setItemMeta(meta);

        return item;
    }

    /**Creates an item
     *
     * @param material The material of the new item
     * @param name The name of the new item
     * @param CustomModelData The CustomModelData of the new item
     * @param key The StringKey for the PersistentData of the new item
     * @param customData The StringValue for the PersistentData of the new item
     * @return The created item
     */
    public static @NotNull ItemStack create(Material material, String name, int CustomModelData, String key, String customData) {
        ItemStack item = new ItemStack(Material.STONE);
        ItemMeta meta = item.getItemMeta();
        //noinspection ConstantConditions
        PersistentDataContainer data = meta.getPersistentDataContainer();

        item = new ItemStack(material);
        meta.setDisplayName(name);
        meta.setCustomModelData(CustomModelData);
        data.set(new NamespacedKey(plugin, key), PersistentDataType.STRING, customData);
        item.setItemMeta(meta);

        return item;
    }

    /**Creates an item
     *
     * @param material The material of the new item
     * @param name The name of the new item
     * @param CustomModelData The CustomModelData of the new item
     * @param key The NamespacedKey for the PersistentData of the new item
     * @param customData The StringValue for the PersistentData of the new item
     * @return The created item
     */
    public static @NotNull ItemStack create(Material material, String name, int CustomModelData, NamespacedKey key, String customData) {
        ItemStack item = new ItemStack(Material.STONE);
        ItemMeta meta = item.getItemMeta();
        //noinspection ConstantConditions
        PersistentDataContainer data = meta.getPersistentDataContainer();

        item = new ItemStack(material);
        meta.setDisplayName(name);
        meta.setCustomModelData(CustomModelData);
        data.set(key, PersistentDataType.STRING, customData);
        item.setItemMeta(meta);

        return item;
    }

    /**Creates an item
     *
     * @param material The material of the new item
     * @param name The name of the new item
     * @param lore The lore of the new item
     * @param CustomModelData The CustomModelData of the new item
     * @param key The StringKey for the PersistentData of the new item
     * @param customData The StringValue for the PersistentData of the new item
     * @return The created item
     */
    public static @NotNull ItemStack create(Material material, String name, String[] lore, int CustomModelData, String key, String customData) {
        ItemStack item = new ItemStack(Material.STONE);
        ItemMeta meta = item.getItemMeta();
        //noinspection ConstantConditions
        PersistentDataContainer data = meta.getPersistentDataContainer();

        item = new ItemStack(material);
        meta.setDisplayName(name);
        data.set(new NamespacedKey(plugin, key), PersistentDataType.STRING, customData);
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(CustomModelData);
        item.setItemMeta(meta);

        return item;
    }

    /**Creates an item
     *
     * @param material The material of the new item
     * @param name The name of the new item
     * @param CustomModelData The CustomModelData of the new item
     * @param key The String[] of keys for the PersistentData of the new item
     * @param customData The String[] of values for the PersistentData of the new item
     * @return The created item
     */
    public static @NotNull ItemStack create(Material material, String name, int CustomModelData, @NotNull NamespacedKey[] key, @NotNull String[] customData) {
        ItemStack item = new ItemStack(Material.STONE);
        ItemMeta meta = item.getItemMeta();
        //noinspection ConstantConditions
        PersistentDataContainer data = meta.getPersistentDataContainer();

        item = new ItemStack(material);
        meta.setDisplayName(name);
        meta.setCustomModelData(CustomModelData);
        for (int i = 0;i<key.length;i++) {
            data.set(key[i], PersistentDataType.STRING, customData[i]);
        }
        item.setItemMeta(meta);

        return item;
    }

    /**Creates an item
     *
     * @param material The material of the new item
     * @param name The name of the new item
     * @param CustomModelData The CustomModelData of the new item
     * @param persistentData A HashMap<NamespacedKey, String> as PersistentData of the new Item
     * @return The created item
     */
    public static @NotNull ItemStack create(Material material, String name, int CustomModelData, @NotNull HashMap<NamespacedKey, String> persistentData) {
        ItemStack item = new ItemStack(Material.STONE);
        ItemMeta meta = item.getItemMeta();
        //noinspection ConstantConditions
        PersistentDataContainer data = meta.getPersistentDataContainer();

        item = new ItemStack(material);
        meta.setDisplayName(name);
        meta.setCustomModelData(CustomModelData);
        for (Map.Entry<NamespacedKey, String> entry : persistentData.entrySet()) data.set(entry.getKey(), PersistentDataType.STRING, entry.getValue());
        item.setItemMeta(meta);

        return item;
    }

    /**Creates an item
     *
     * @param material The material of the new item
     * @param name The name of the new item
     * @param lore The lore of the new item
     * @param CustomModelData The CustomModelData of the new item
     * @param key The String[] of keys for the PersistentData of the new item
     * @param customData The String[] of values for the PersistentData of the new item
     * @return The created item
     */
    public static @NotNull ItemStack create(Material material, String name, String[] lore, int CustomModelData, @NotNull NamespacedKey[] key, @NotNull String[] customData) {
        ItemStack item = new ItemStack(Material.STONE);
        ItemMeta meta = item.getItemMeta();
        //noinspection ConstantConditions
        PersistentDataContainer data = meta.getPersistentDataContainer();

        item = new ItemStack(material);
        meta.setDisplayName(name);
        for (int i = 0;i<key.length;i++) {
            data.set(key[i], PersistentDataType.STRING, customData[i]);
        }
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(CustomModelData);
        item.setItemMeta(meta);

        return item;
    }

    /**Creates an item
     *
     * @param material The material of the new item
     * @param name The name of the new item
     * @param lore The lore of the new item
     * @param CustomModelData The CustomModelData of the new item
     * @param persistentData A HashMap<NamespacedKey, String> as PersistentData of the new Item
     * @return The created item
     */
    public static @NotNull ItemStack create(@NotNull Material material, @NotNull String name, @NotNull String[] lore, int CustomModelData, @NotNull HashMap<NamespacedKey, String> persistentData) {
        ItemStack item = new ItemStack(Material.STONE);
        ItemMeta meta = item.getItemMeta();
        //noinspection ConstantConditions
        PersistentDataContainer data = meta.getPersistentDataContainer();

        item = new ItemStack(material);
        meta.setDisplayName(name);
        for (Map.Entry<NamespacedKey, String> entry : persistentData.entrySet()) data.set(entry.getKey(), PersistentDataType.STRING, entry.getValue());
        meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(CustomModelData);
        item.setItemMeta(meta);

        return item;
    }
    /**Changes the parameters of the item
     *
     * @throws IllegalArgumentException If the meta of the item is null
     *
     * @param CustomModelData the CustomModelData
     * @return The edited item
     */
    public static @NotNull ItemStack edit(@NotNull ItemStack item, int CustomModelData) {
        item = item.clone();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) throw new NullPointerException("The meta of item to edit was null!");
        meta.setCustomModelData(CustomModelData);
        item.setItemMeta(meta);
        return item;
    }

    /**Changes the parameters of the item
     *
     * @param item The item to edit
     * @param lore The Lore
     * @return The edited item
     */
    public static @NotNull ItemStack edit(@NotNull ItemStack item, @NotNull String[] lore) {
        if (item.getItemMeta() == null) throw new IllegalArgumentException("Meta of item is null!");
        item.getItemMeta().setLore(Arrays.asList(lore));
        return item;
    }

    /**Changes the parameters of the item
     *
     * WARNING: this may not work
     *
     * @throws IllegalArgumentException If the meta of the item is null
     *
     * @param item The item to edit
     * @param key The PersistentData Key
     * @param customData The Persistent Data
     * @return The edited item
     */
    public static @NotNull ItemStack edit(@NotNull ItemStack item, @NotNull String key, @NotNull String customData) { //TODO this maybe does not work
        item = item.clone();
        if (item.getItemMeta() == null) throw new IllegalArgumentException("Meta of item is null!");
        item.getItemMeta().getPersistentDataContainer().set(new NamespacedKey(plugin, key), PersistentDataType.STRING, customData);
        return item;
    }

    /**Changes the parameters of the item
     *
     * @throws IllegalArgumentException If the meta of the item is null
     *
     * @param item The item to edit
     * @param CustomModelData the CustomModelData
     * @param lore The Lore
     * @param key The PersistentData Key
     * @param customData The Persistent Data
     * @return The edited item
     */
    public static @NotNull ItemStack edit(@NotNull ItemStack item, int CustomModelData, @NotNull String[] lore, @NotNull NamespacedKey key, @NotNull String customData) {
        item = item.clone();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) throw new NullPointerException("The meta of item to edit was null!");
        meta.setCustomModelData(CustomModelData);
        meta.setLore(Arrays.asList(lore));
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, customData);
        item.setItemMeta(meta);
        return item;
    }

    /**Changes the parameters of the item
     *
     * @throws IllegalArgumentException If the meta of the item is null
     *
     * @param item The item to edit
     * @param CustomModelData the CustomModelData
     * @param lore The Lore
     * @param key The PersistentData Key
     * @param customData The Persistent Data
     * @return The edited item
     */
    public static @NotNull ItemStack editAddLore(@NotNull ItemStack item, int CustomModelData, @NotNull String[] lore, @NotNull NamespacedKey key, @NotNull String customData) {
        item = item.clone();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) throw new NullPointerException("The meta of item to edit was null!");
        meta.setCustomModelData(CustomModelData);
        List<String> newLore = meta.getLore();
        if (newLore == null) newLore = new ArrayList<>();
        newLore.addAll(Arrays.asList(lore));
        meta.setLore(newLore);
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, customData);
        item.setItemMeta(meta);
        return item;
    }
}

package com.SpellBend.events;

import com.SpellBend.PluginMain;
import com.SpellBend.organize.Interfaces;
import com.SpellBend.util.EventUtil;
import com.SpellBend.data.Maps;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public class onInventoryClick implements Listener {
    private final HashMap<String, Interfaces.PlayerFunction> StringToFunction = Maps.itemNameToPlayerFunctionMap;
    public static final NamespacedKey itemActionKey = new NamespacedKey(PluginMain.getInstance(), "itemAction"); //<- DO NOT CHANGE!!

    public onInventoryClick() {
        EventUtil.register(this);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        if (!event.getCurrentItem().hasItemMeta()) return;
        ItemMeta meta = event.getCurrentItem().getItemMeta();

        if (!meta.hasCustomModelData()) return;
        int CMD = meta.getCustomModelData();

        if (CMD > 100 && CMD <= 400) {
            event.setCancelled(true);
            PersistentDataContainer data = meta.getPersistentDataContainer();
            if (data.has(itemActionKey, PersistentDataType.STRING)) StringToFunction.get(data.get(itemActionKey, PersistentDataType.STRING)).run((Player) event.getWhoClicked());
            else Bukkit.getLogger().warning("Item " + meta.getDisplayName() + "§e from " + event.getWhoClicked().getName() +
                    " has a CustomModelData for clickable but not a PersistentData ItemAction!");
            return;
        }
        if (CMD > 400 && CMD <= 700) {
            event.setCancelled(true);
        }
    }
}

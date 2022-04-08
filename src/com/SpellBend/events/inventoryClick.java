package com.SpellBend.events;

import com.SpellBend.GUI.GUIActionHandler;
import com.SpellBend.data.PersistentDataKeys;
import com.SpellBend.util.EventUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class inventoryClick implements Listener {

    public inventoryClick() {
        EventUtil.register(this);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        if (!event.getCurrentItem().hasItemMeta()) return;
        ItemMeta meta = event.getCurrentItem().getItemMeta();
        if (meta == null) return;
        if (!meta.hasCustomModelData()) return;
        int CMD = meta.getCustomModelData();

        if (CMD > 100 && CMD <= 400) {
            event.setCancelled(true);
            PersistentDataContainer data = meta.getPersistentDataContainer();
            if (data.has(PersistentDataKeys.itemActionKey, PersistentDataType.STRING))
                GUIActionHandler.runItemAction(Objects.requireNonNull(data.get(PersistentDataKeys.itemActionKey, PersistentDataType.STRING)), ((Player) event.getWhoClicked()));
            else Bukkit.getLogger().warning("Item " + meta.getDisplayName() + "§e from " + event.getWhoClicked().getName() +
                    " has a CustomModelData for clickable but not a PersistentData ItemAction!");
            return;
        }
        if (CMD > 400 && CMD <= 700) {
            event.setCancelled(true);
        }
    }
}

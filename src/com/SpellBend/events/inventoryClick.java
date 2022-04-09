package com.SpellBend.events;

import com.SpellBend.GUI.GUIActionHandler;
import com.SpellBend.data.PersistentDataKeys;
import com.SpellBend.spell.SpellHandler;
import com.SpellBend.util.EventUtil;
import com.SpellBend.util.dataUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class inventoryClick implements Listener {

    public inventoryClick() {
        EventUtil.register(this);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inv = event.getClickedInventory();
        if (inv == null) return;
        Player player = (Player) event.getWhoClicked();
        if (dataUtil.inventoryIsShop(inv)) {
            if (SpellHandler.itemIsSpell(player.getItemOnCursor()))
                player.setItemOnCursor(null);
            else if (SpellHandler.itemIsSpell(event.getCurrentItem())) //yeah so this should only be triggered if the inv SHIFTED into is a shop not if the inv SHIFTED FROM is a shop
                event.setCurrentItem(null);
        }

        if (!dataUtil.itemIsClickable(event.getCurrentItem())) return;
        event.setCancelled(true);

        //noinspection ConstantConditions
        @NotNull ItemMeta meta = event.getCurrentItem().getItemMeta();

        //noinspection ConstantConditions   we check for <= 400 here to check if it has an itemAction (we don't need to check for < 100 since we did itemIsClickable())
        if (meta.getCustomModelData() <= 400) {
            PersistentDataContainer data = meta.getPersistentDataContainer();
            if (data.has(PersistentDataKeys.itemActionKey, PersistentDataType.STRING))
                GUIActionHandler.runItemAction(Objects.requireNonNull(data.get(PersistentDataKeys.itemActionKey, PersistentDataType.STRING)), player);
            else Bukkit.getLogger().warning("Item " + meta.getDisplayName() + "§e from " + event.getWhoClicked().getName() +
                    " has a CustomModelData for clickable but not a PersistentData ItemAction!");
        }
    }
}

package game.spellbend.events;

import game.spellbend.gui.GUIActionHandler;
import game.spellbend.data.PersistentDataKeys;
import game.spellbend.spell.SpellHandler;
import game.spellbend.util.EventUtil;
import game.spellbend.util.DataUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class InventoryClick implements Listener {
    public InventoryClick() {
        EventUtil.register(this);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory clickedInv = event.getClickedInventory();
        if (clickedInv == null) return;
        Player player = (Player) event.getWhoClicked();
        Inventory itemOnCursorDestination = DataUtil.getItemOnCursorDestination(event.getClickedInventory(), event.getClick());
        Inventory itemClickedDestination = DataUtil.getItemClickedDestination(event.getView(), event.getClickedInventory(), event.getClick());
        if (itemOnCursorDestination == null && itemClickedDestination == null) return;

        if (itemOnCursorDestination != null && DataUtil.inventoryIsShop(itemOnCursorDestination)) {
            if (SpellHandler.itemIsSpell(player.getItemOnCursor())) {
                event.setCancelled(true);
                player.setItemOnCursor(null);
                return;
            } else event.setCancelled(true);
        }

        if (itemClickedDestination != null && DataUtil.inventoryIsShop(itemClickedDestination)) {
            if (SpellHandler.itemIsSpell(event.getCurrentItem())) {
                event.setCancelled(true);
                event.setCurrentItem(null);
                return;
            } else event.setCancelled(true);
        }

        if (!DataUtil.itemIsClickable(event.getCurrentItem())) return;
        event.setCancelled(true);

        //noinspection ConstantConditions
        @NotNull ItemMeta meta = event.getCurrentItem().getItemMeta();

        //noinspection ConstantConditions   we check for <= 400 here to check if it has an itemAction (we don't need to check for < 100 since we did itemIsClickable())
        if (meta.getCustomModelData() <= 400) {
            PersistentDataContainer data = meta.getPersistentDataContainer();
            if (data.has(PersistentDataKeys.itemActionKey, PersistentDataType.STRING))
                GUIActionHandler.runItemAction(Objects.requireNonNull(data.get(PersistentDataKeys.itemActionKey, PersistentDataType.STRING)), event.getClick(), player);
            else Bukkit.getLogger().warning("Item " + meta.getDisplayName() + "§e from " + event.getWhoClicked().getName() +
                    " has a CustomModelData for clickable but not a PersistentData ItemAction!");
        }
    }
}

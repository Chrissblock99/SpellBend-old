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
        Bukkit.getLogger().info("InventoryClickedEvent");
        Inventory clickedInv = event.getClickedInventory();
        if (clickedInv == null) return;
        Player player = (Player) event.getWhoClicked();
        Bukkit.getLogger().info("§b-------------------------------------");
        Inventory itemOnCursorDestination = DataUtil.getItemOnCursorDestination(event.getClickedInventory(), event.getClick());
        Inventory itemClickedDestination = DataUtil.getItemClickedDestination(event.getView(), event.getClickedInventory(), event.getClick());
        Bukkit.getLogger().info("§bchecking destinations for null");
        if (itemOnCursorDestination == null && itemClickedDestination == null) return;
        Bukkit.getLogger().info("§bnot null");

        Bukkit.getLogger().info("§bchecking cursor destination");
        if (itemOnCursorDestination != null && DataUtil.inventoryIsShop(itemOnCursorDestination)) {
            Bukkit.getLogger().info("§bcursor Destination is Shop");
            if (SpellHandler.itemIsSpell(player.getItemOnCursor())) {
                Bukkit.getLogger().info("§bitem is Spell");
                event.setCancelled(true);
                player.setItemOnCursor(null);
                return;
            } else event.setCancelled(true);
        } else Bukkit.getLogger().info("§bisn't shop");

        Bukkit.getLogger().info("§bchecking clicked destination");
        if (itemClickedDestination != null && DataUtil.inventoryIsShop(itemClickedDestination)) {
            Bukkit.getLogger().info("§bclicked Destination is Shop");
            if (SpellHandler.itemIsSpell(event.getCurrentItem())) {
                Bukkit.getLogger().info("§bitem is Spell");
                event.setCancelled(true);
                event.setCurrentItem(null);
                return;
            } else event.setCancelled(true);
        } else Bukkit.getLogger().info("§bisn't shop");

        /*if (dataUtil.inventoryIsShop(clickedInv)) {
            if (SpellHandler.itemIsSpell(player.getItemOnCursor())) {
                player.setItemOnCursor(null);
                event.setCancelled(true);
                return;
            } else event.setCancelled(true);
        }

        if (dataUtil.inventoryIsShop(event.getView().getTopInventory()) &&
                !dataUtil.inventoryIsShop(clickedInv) &&
                (event.getClick().equals(ClickType.SHIFT_LEFT) ||
                        event.getClick().equals(ClickType.SHIFT_RIGHT))) {
            if (SpellHandler.itemIsSpell(event.getCurrentItem()))
                event.setCurrentItem(null);
            else event.setCancelled(true);
        }*/

        if (!DataUtil.itemIsClickable(event.getCurrentItem())) return;
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

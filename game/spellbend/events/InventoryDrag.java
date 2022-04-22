package game.spellbend.events;

import game.spellbend.util.DataUtil;
import game.spellbend.util.EventUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class InventoryDrag implements Listener {
    public InventoryDrag() {
        EventUtil.register(this);
    }

    @EventHandler
    public void onInventoryClick(InventoryDragEvent event) {
        InventoryView invView = event.getView();
        Inventory topInv = invView.getTopInventory();
        if (!DataUtil.inventoryIsShop(topInv)) return;
        //ArrayList<Integer> changedShopSlots = new ArrayList<>();

        for (Integer rawSlot : event.getRawSlots())
            if (topInv.equals(invView.getInventory(rawSlot)))
                event.setCancelled(true);
                /*if (SpellHandler.itemIsSpell(event.getOldCursor())) {
                    if (event.getCursor() == null)
                        continue;
                    ItemStack cursor = event.getCursor();
                    cursor.setAmount(cursor.getAmount()-1);       //T ODO this should remove every item from the cursor that was placed in shop, currently does not tho
                    event.setCursor(cursor);
                }
            }*/
            /*if (topInv.equals(invView.getInventory(rawSlot)))
                changedShopSlots.add(invView.convertSlot(rawSlot));

        for (int i = 0; i<topInv.getContents().length;i++)
            Bukkit.getLogger().info("§b" + i + ": " + topInv.getItem(i));

        for (Integer slot : changedShopSlots) {
            Bukkit.getLogger().info("§b" + slot + ": " + topInv.getItem(slot));
            if (SpellHandler.itemIsSpell(event.getOldCursor()))
                topInv.setItem(slot, null);
            else {
                if (event.getCursor() == null) {
                    event.setCursor(topInv.getItem(slot));
                    topInv.setItem(slot, null);
                    continue;
                }
                ItemStack cursor = event.getCursor();
                cursor.setAmount(cursor.getAmount()+1);
                event.setCursor(cursor);
                topInv.setItem(slot, null);
            }
            event.getWhoClicked().openInventory(topInv);
        }*/
    }
}
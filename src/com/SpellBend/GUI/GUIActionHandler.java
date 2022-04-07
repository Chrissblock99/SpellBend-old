package com.SpellBend.GUI;

import com.SpellBend.data.Enums;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GUIActionHandler {
    public static void runItemAction(@NotNull String itemAction, @NotNull Player player) {
        if (itemAction.contains("openShop")) {
            itemAction = itemAction.replace("openShop", "");
            if (itemAction.isEmpty()) {
                player.openInventory(GUICreationUtil.createShop(player));
                return;
            }
            try {
                player.openInventory(GUICreationUtil.createElementGUI(player, Enums.Element.valueOf(itemAction)));
            } catch (IllegalArgumentException exception) {
                Bukkit.getLogger().warning(itemAction + " is not a Valid Element");
            }
        }
    }

}

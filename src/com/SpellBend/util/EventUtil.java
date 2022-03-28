package com.SpellBend.util;

import com.SpellBend.PluginMain;
import com.SpellBend.events.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class EventUtil {
    public static void register(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, PluginMain.getInstance());
    }

    public static void registerAllEvents() {
        new onPlayerJoin();
        new onPlayerChat();
        new onInventoryClick();
        new onPlayerQuit();

        new onPlayerLevelChange();
        //new onHangingBreak();    //cancels all so we can't use it
        new onHangingBreakByEntity();
        new onPlayerInteract();
        new onFoodLevelChange();
        new onPlayerInteractEntity();
        new onEntityDamageByEntity();
    }
}

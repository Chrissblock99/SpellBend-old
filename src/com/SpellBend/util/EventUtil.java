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
        new playerJoin();
        new playerChat();
        new inventoryClick();
        new playerLeave();

        new playerLevelChange();
        //new onHangingBreak();    //cancels all so we can't use it
        new entityBreakPainting();
        new playerInteractBlock();
        new foodLevelChange();
        new playerInteractEntity();
        new entityDamageEntity();
    }
}

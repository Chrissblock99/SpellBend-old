package game.SpellBend.util;

import game.SpellBend.PluginMain;
import game.SpellBend.events.*;
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

        new playerSwitchHeldItem();
        new playerLevelChange();
        new entityBreakPainting();
        new playerInteractBlock();
        new foodLevelChange();
        new playerInteractEntity();
        new entityDamageEntity();
    }
}

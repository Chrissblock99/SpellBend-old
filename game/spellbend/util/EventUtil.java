package game.spellbend.util;

import game.spellbend.PluginMain;
import game.spellbend.events.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class EventUtil {
    public static void register(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, PluginMain.getInstance());
    }

    public static void registerAllEvents() {
        new PlayerJoin();
        new PlayerChat();
        new InventoryClick();
        new PlayerLeave();

        new PlayerSwitchHeldItem();
        new PlayerLevelChange();
        new EntityBreakPainting();
        new PlayerInteractBlock();
        new FoodLevelChange();
        new PlayerInteractEntity();
        new EntityDamageEntity();
    }
}

package com.SpellBend.events;

import com.SpellBend.util.EventUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class onFoodLevelChange implements Listener {
    public onFoodLevelChange() {
        EventUtil.register(this);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        if (player.getGameMode() != GameMode.ADVENTURE) return;
        event.setCancelled(true);
        player.setFoodLevel(20);
    }
}

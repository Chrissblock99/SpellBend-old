package game.SpellBend.events;

import game.SpellBend.util.EventUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class foodLevelChange implements Listener {
    public foodLevelChange() {
        EventUtil.register(this);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (player.getGameMode() != GameMode.ADVENTURE) return;
        event.setCancelled(true);
        player.setFoodLevel(20);
    }
}

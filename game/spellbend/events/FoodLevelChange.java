package game.spellbend.events;

import game.spellbend.util.EventUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChange implements Listener {
    public FoodLevelChange() {
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

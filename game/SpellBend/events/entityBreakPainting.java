package game.SpellBend.events;

import game.SpellBend.util.EventUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

public class entityBreakPainting implements Listener {
  public entityBreakPainting() {
    EventUtil.register(this);
  }
  
  @EventHandler
  public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
    if (!(event.getEntity() instanceof Player player)) {
      event.setCancelled(true);
      return;
    }

    if (player.getGameMode() == GameMode.ADVENTURE) event.setCancelled(true);
  }
}
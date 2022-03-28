package com.SpellBend.events;

import com.SpellBend.util.EventUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

public class onHangingBreakByEntity implements Listener {
  public onHangingBreakByEntity() {
    EventUtil.register(this);
  }
  
  @EventHandler
  public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
    if (!(event.getEntity() instanceof Player)) {
      event.setCancelled(true);
      return;
    }
    Player player = (Player) event.getEntity();
    
    if (player.getGameMode() == GameMode.ADVENTURE) event.setCancelled(true);
  }
}
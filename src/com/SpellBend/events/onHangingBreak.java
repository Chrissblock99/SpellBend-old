package com.SpellBend.events;

import com.SpellBend.util.EventUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakEvent;

public class onHangingBreak implements Listener {
  public onHangingBreak() {
    EventUtil.register(this);
  }
  
  @EventHandler
  public void onHangingBreak(HangingBreakEvent event) {
    event.setCancelled(true);
  }
}
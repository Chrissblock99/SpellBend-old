package com.SpellBend.events;

import com.SpellBend.util.EventUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class playerSwitchHeldItem implements Listener {
    public playerSwitchHeldItem() {
        EventUtil.register(this);
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {

    }
}

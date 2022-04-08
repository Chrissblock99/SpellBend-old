package com.SpellBend.events;

import com.SpellBend.data.Enums;
import com.SpellBend.playerData.CoolDowns;
import com.SpellBend.util.EventUtil;
import com.SpellBend.util.playerDataBoard;
import com.SpellBend.util.dataUtil;
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
        Player player = event.getPlayer();
        Enums.SpellType type = dataUtil.getSpellType(player.getInventory().getItem(event.getNewSlot()));
        if (type != null && CoolDowns.getCoolDown(player, type)[0]>0.1f)
            playerDataBoard.registerPlayer(player, type);
        else playerDataBoard.deRegisterPlayer(player, type);
    }
}

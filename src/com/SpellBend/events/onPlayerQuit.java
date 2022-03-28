package com.SpellBend.events;

import com.SpellBend.organize.RankObj;
import com.SpellBend.spell.SpellHandler;
import com.SpellBend.util.EventUtil;
import com.SpellBend.util.playerDataUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class onPlayerQuit implements Listener {
    public onPlayerQuit() {
        EventUtil.register(this);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        RankObj playerRank = playerDataUtil.getMainRank(player);
        event.setQuitMessage("§8[§6-§8] " + playerRank.bracketsCC + "[" + playerRank.CCedRankName + playerRank.bracketsCC + "] §7" + playerDataUtil.getNick(player));
        SpellHandler.deRegisterPlayer(player);
        playerDataUtil.saveAll(player);
    }
}

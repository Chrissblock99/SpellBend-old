package com.SpellBend.events;

import com.SpellBend.organize.RankObj;
import com.SpellBend.spell.SpellHandler;
import com.SpellBend.util.EventUtil;
import com.SpellBend.playerData.Nick;
import com.SpellBend.playerData.Ranks;
import com.SpellBend.playerData.playerDataUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class playerLeave implements Listener {
    public playerLeave() {
        EventUtil.register(this);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        RankObj playerRank = Ranks.getMainRank(player);
        event.setQuitMessage("§8[§6-§8] " + playerRank.bracketsCC + "[" + playerRank.CCedRankName + playerRank.bracketsCC + "] §7" + Nick.getNick(player));
        SpellHandler.deRegisterPlayer(player);
        playerDataUtil.saveAll(player);
    }
}

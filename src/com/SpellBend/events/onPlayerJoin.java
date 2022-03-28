package com.SpellBend.events;

import com.SpellBend.organize.RankObj;
import com.SpellBend.spell.SpellHandler;
import com.SpellBend.util.playerDataBoard;
import com.SpellBend.util.EventUtil;
import com.SpellBend.util.playerDataUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class onPlayerJoin implements Listener {

    public onPlayerJoin() {
        EventUtil.register(this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        boolean isNew = !player.hasPlayedBefore();

        if (isNew) playerDataUtil.setupAll(player);
        playerDataUtil.loadAll(player);
        RankObj playerRank = playerDataUtil.getMainRank(player);

        Bukkit.getLogger().info("§8[§b+§8] " + playerRank.bracketsCC + "[" + playerRank.CCedRankName + playerRank.bracketsCC + "] §7" + playerDataUtil.getNick(player));
        for (Player p : player.getWorld().getPlayers()) p.sendMessage("§8[§b+§8] " + playerRank.bracketsCC + "[" + playerRank.CCedRankName + playerRank.bracketsCC + "] §7" + playerDataUtil.getNick(player));
        if (isNew) for (Player p : player.getWorld().getPlayers()) p.sendMessage("§e§lNEW §r§7» §3Welcome §b" + playerDataUtil.getNick(player) + " §3to the server!");

        SpellHandler.registerPlayer(player);
        playerDataBoard.createBoard(player);
    }
}

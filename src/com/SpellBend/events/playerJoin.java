package com.SpellBend.events;

import com.SpellBend.organize.RankObj;
import com.SpellBend.playerData.SpellsOwned;
import com.SpellBend.spell.SpellHandler;
import com.SpellBend.playerData.Nick;
import com.SpellBend.playerData.Ranks;
import com.SpellBend.util.math.MathUtil;
import com.SpellBend.util.playerDataBoard;
import com.SpellBend.util.EventUtil;
import com.SpellBend.playerData.playerDataUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class playerJoin implements Listener {

    public playerJoin() {
        EventUtil.register(this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        boolean firstPlay = !player.hasPlayedBefore();

        if (firstPlay) playerDataUtil.setupPlayerData(player);
        playerDataUtil.loadAll(player);
        RankObj playerRank = Ranks.getMainRank(player);

        Bukkit.getLogger().info("§8[§b+§8] " + playerRank.bracketsCC + "[" + playerRank.CCedRankName + playerRank.bracketsCC + "] §7" + Nick.getNick(player));
        for (Player p : player.getWorld().getPlayers()) p.sendMessage("§8[§b+§8] " + playerRank.bracketsCC + "[" + playerRank.CCedRankName + playerRank.bracketsCC + "] §7" + Nick.getNick(player));
        if (firstPlay || MathUtil.additiveArrayValue(SpellsOwned.getAllSpellsOwned(player)) == 0)
            for (Player playerInWorld : player.getWorld().getPlayers())
                playerInWorld.sendMessage("§e§lNEW §r§7» §3Welcome §b" + Nick.getNick(player) + " §3to the server!");

        SpellHandler.registerPlayer(player);
        playerDataBoard.createBoard(player);
    }
}

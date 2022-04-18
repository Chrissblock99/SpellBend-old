package game.SpellBend.events;

import game.SpellBend.organize.RankObj;
import game.SpellBend.spell.SpellHandler;
import game.SpellBend.util.EventUtil;
import game.SpellBend.playerData.Nick;
import game.SpellBend.playerData.Ranks;
import game.SpellBend.playerData.playerDataUtil;
import game.SpellBend.util.playerDataBoard;
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
        playerDataBoard.deRegisterPlayer(player);
        SpellHandler.deRegisterPlayer(player);
        playerDataUtil.saveAll(player);
    }
}

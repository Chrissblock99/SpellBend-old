package game.spellbend.events;

import game.spellbend.organize.RankObj;
import game.spellbend.spell.SpellHandler;
import game.spellbend.util.EventUtil;
import game.spellbend.playerdata.Nick;
import game.spellbend.playerdata.Ranks;
import game.spellbend.playerdata.PlayerDataUtil;
import game.spellbend.util.PlayerDataBoard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {
    public PlayerLeave() {
        EventUtil.register(this);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        RankObj playerRank = Ranks.getMainRank(player);
        event.setQuitMessage("§8[§6-§8] " + playerRank.bracketsCC + "[" + playerRank.CCedRankName + playerRank.bracketsCC + "] §7" + Nick.getNick(player));
        PlayerDataBoard.deRegisterPlayer(player);
        SpellHandler.deRegisterPlayer(player);
        PlayerDataUtil.saveAll(player);
    }
}

package game.spellbend.events;

import game.spellbend.organize.RankObj;
import game.spellbend.playerdata.SpellsOwned;
import game.spellbend.spell.SpellHandler;
import game.spellbend.playerdata.Nick;
import game.spellbend.playerdata.Ranks;
import game.spellbend.util.math.MathUtil;
import game.spellbend.util.PlayerDataBoard;
import game.spellbend.util.EventUtil;
import game.spellbend.playerdata.PlayerDataUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    public PlayerJoin() {
        EventUtil.register(this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        boolean firstPlay = !player.hasPlayedBefore();

        if (firstPlay) PlayerDataUtil.setupPlayerData(player);
        PlayerDataUtil.loadAll(player);
        RankObj playerRank = Ranks.getMainRank(player);

        Bukkit.getLogger().info("§8[§b+§8] " + playerRank.bracketsCC + "[" + playerRank.CCedRankName + playerRank.bracketsCC + "] §7" + Nick.getNick(player));
        for (Player p : player.getWorld().getPlayers()) p.sendMessage("§8[§b+§8] " + playerRank.bracketsCC + "[" + playerRank.CCedRankName + playerRank.bracketsCC + "] §7" + Nick.getNick(player));
        if (firstPlay || MathUtil.additiveArrayValue(SpellsOwned.getAllSpellsOwned(player)) == 0)
            for (Player playerInWorld : player.getWorld().getPlayers())
                playerInWorld.sendMessage("§e§lNEW §r§7» §3Welcome §b" + Nick.getNick(player) + " §3to the server!");

        SpellHandler.registerPlayer(player);
        PlayerDataBoard.createBoard(player);
    }
}

package game.SpellBend.events;

import game.SpellBend.organize.RankObj;
import game.SpellBend.playerData.SpellsOwned;
import game.SpellBend.spell.SpellHandler;
import game.SpellBend.playerData.Nick;
import game.SpellBend.playerData.Ranks;
import game.SpellBend.util.math.MathUtil;
import game.SpellBend.util.playerDataBoard;
import game.SpellBend.util.EventUtil;
import game.SpellBend.playerData.playerDataUtil;
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

package com.SpellBend.util;

import com.SpellBend.organize.RankObj;
import com.SpellBend.playerData.Gems;
import com.SpellBend.playerData.Gold;
import com.SpellBend.playerData.Ranks;
import com.SpellBend.playerData.playerDataUtil;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.*;

public class playerDataBoard implements Listener {
    public playerDataBoard() {
        if (!Bukkit.getOnlinePlayers().isEmpty())                  //creating boards for players already online
            for (Player player : Bukkit.getOnlinePlayers())
                createBoard(player);
    }

    public static void updateBoard(Player player) {
        createBoard(player);
    }

    public static void createBoard(Player player) {
        try {
            RankObj playerRank = Ranks.getMainRank(player);
            Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective obj = board.registerNewObjective("playerDataBoard", "dummy",  playerDataUtil.constructDisplayString(player));
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);

            Score line1 = obj.getScore("§3§m-------------"); line1.setScore(11);
            Score coins = obj.getScore("  §eGold: §b" + Gold.getGold(player)); coins.setScore(10);
            Score gems = obj.getScore("  §3Gems: §b" + Gems.getGems(player)); gems.setScore(9);
            Score line2 = obj.getScore("§3§m-------------§r" + ""); line2.setScore(8);
            Score kills = obj.getScore("  §cKills: §b" + player.getStatistic(Statistic.PLAYER_KILLS)); kills.setScore(7);
            Score deaths = obj.getScore("  §4Deaths: §b" + player.getStatistic(Statistic.DEATHS)); deaths.setScore(6);
            Score kdr = obj.getScore("  §cKDR: §b" + (player.getStatistic(Statistic.PLAYER_KILLS)/player.getStatistic(Statistic.DEATHS))); kdr.setScore(5);
            Score line3 = obj.getScore("§3§m-------------§r  "); line3.setScore(4);
            Score ip = obj.getScore("  §b§nSpellBend§b.minehut.gg"); ip.setScore(3);
            Score line4 = obj.getScore("§3§m-------------§r   "); line4.setScore(2);
            Score cooldowned = obj.getScore(""); cooldowned.setScore(1);
            Score cooldown = obj.getScore(" "); cooldown.setScore(0);
            player.setScoreboard(board);

        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning("Bukkit.getScoreboardManager().getNewScoreboard() returned NullPointerException " + exception);
        }
    }
}

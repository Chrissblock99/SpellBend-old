package com.SpellBend.util;

import com.SpellBend.data.Enums;
import com.SpellBend.playerData.CoolDowns;
import com.SpellBend.playerData.Gems;
import com.SpellBend.playerData.Gold;
import com.SpellBend.playerData.playerDataUtil;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;

public class playerDataBoard {
    public playerDataBoard() {
        if (!Bukkit.getOnlinePlayers().isEmpty())                  //creating boards for players already online
            for (Player player : Bukkit.getOnlinePlayers())
                createBoard(player);
    }

    public static void updateBoard(@NotNull Player player, @NotNull Enums.SpellType type) {
        try {
            //noinspection ConstantConditions this can throw a NullPointerExcepttion if getScoreBoardManager() returns null, but it gets catched
            Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective obj = board.registerNewObjective("playerDataBoard", "dummy",  playerDataUtil.constructDisplayString(player));
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);

            Score line = obj.getScore("§3§m-------------"); line.setScore(11);
            line = obj.getScore("  §eGold: §b" + Gold.getGold(player)); line.setScore(10);
            line = obj.getScore("  §3Gems: §b" + Gems.getGems(player)); line.setScore(9);
            line = obj.getScore("§3§m-------------§r" + ""); line.setScore(8);
            line = obj.getScore("  §cKills: §b" + player.getStatistic(Statistic.PLAYER_KILLS)); line.setScore(7);
            line = obj.getScore("  §4Deaths: §b" + player.getStatistic(Statistic.DEATHS)); line.setScore(6);
            line = obj.getScore("  §cKDR: §b" + (player.getStatistic(Statistic.PLAYER_KILLS)/player.getStatistic(Statistic.DEATHS))); line.setScore(5);
            line = obj.getScore("§3§m-------------§r  "); line.setScore(4);
            line = obj.getScore("  §b§nSpellBend§b.minehut.gg"); line.setScore(3);
            line = obj.getScore("§3§m-------------§r   "); line.setScore(2);
            line = obj.getScore("§7" + type.toString().charAt(0) + type.toString().substring(1).toLowerCase()); line.setScore(1);

            float[] CDInfo = CoolDowns.getCoolDown(player, type);
            StringBuilder coolDownDisplay = new StringBuilder();

            switch ((int) CDInfo[2]) {
                case -1 -> {
                    int filled = Math.round(10-(CDInfo[0]/CDInfo[1])*10);
                    coolDownDisplay.append("§a").append(Math.round(CDInfo[0]*10f)/10f).append("s §8▌▌▌▌▌▌▌▌▌▌")
                            .insert(coolDownDisplay.length()-10+filled, "§e");
                }
                case 0 -> {
                    int filled = Math.round((CDInfo[0]/CDInfo[1])*10);
                    coolDownDisplay.append("§b").append(Math.round(CDInfo[0]*10f)/10f).append("s §b▌▌▌▌▌▌▌▌▌▌")
                            .insert(coolDownDisplay.length()-10+filled, "§8");
                }
                case 1 -> {
                    int filled = Math.round(10-(CDInfo[0]/CDInfo[1])*10);
                    coolDownDisplay.append("§e").append(Math.round(CDInfo[0]*10f)/10f).append("s §a▌▌▌▌▌▌▌▌▌▌")
                            .insert(coolDownDisplay.length()-10+filled, "§8");
                }
                default -> Bukkit.getLogger().warning("CoolDownType " + CDInfo[2] + " from " + type + " of " + player.getDisplayName() + " is invalid!");
            }
            line = obj.getScore(coolDownDisplay.toString()); line.setScore(0);
            player.setScoreboard(board);

        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning("Bukkit.getScoreboardManager() returned null!");
            exception.printStackTrace();
        }
    }

    public static void updateBoard(@NotNull Player player) {
        createBoard(player);
    }

    public static void createBoard(@NotNull Player player) {
        try {
            //noinspection ConstantConditions this can throw a NullPointerExcepttion if getScoreBoardManager() returns null, but it gets catched
            Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective obj = board.registerNewObjective("playerDataBoard", "dummy",  playerDataUtil.constructDisplayString(player));
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);

            Score line = obj.getScore("§3§m-------------"); line.setScore(11);
            line = obj.getScore("  §eGold: §b" + Gold.getGold(player)); line.setScore(10);
            line = obj.getScore("  §3Gems: §b" + Gems.getGems(player)); line.setScore(9);
            line = obj.getScore("§3§m-------------§r" + ""); line.setScore(8);
            line = obj.getScore("  §cKills: §b" + player.getStatistic(Statistic.PLAYER_KILLS)); line.setScore(7);
            line = obj.getScore("  §4Deaths: §b" + player.getStatistic(Statistic.DEATHS)); line.setScore(6);
            line = obj.getScore("  §cKDR: §b" + (player.getStatistic(Statistic.PLAYER_KILLS)/player.getStatistic(Statistic.DEATHS))); line.setScore(5);
            line = obj.getScore("§3§m-------------§r  "); line.setScore(4);
            line = obj.getScore("  §b§nSpellBend§b.minehut.gg"); line.setScore(3);
            line = obj.getScore("§3§m-------------§r   "); line.setScore(2);

            Enums.SpellType type = playerDataUtil.getHeldSpellType(player);
            if (type != null && CoolDowns.getCoolDown(player, type)[0]>0.1f) {
                line = obj.getScore("§7" + type.toString().charAt(0) + type.toString().substring(1).toLowerCase()); line.setScore(1);

                float[] CDInfo = CoolDowns.getCoolDown(player, type);
                StringBuilder coolDownDisplay = new StringBuilder();

                switch ((int) CDInfo[2]) {
                    case -1 -> {
                        int filled = Math.round(10-(CDInfo[0]/CDInfo[1])*10);
                        coolDownDisplay.append("§a").append(Math.round(CDInfo[0]*10f)/10f).append("s §8▌▌▌▌▌▌▌▌▌▌")
                                .insert(coolDownDisplay.length()-10+filled, "§e");
                    }
                    case 0 -> {
                        int filled = Math.round((CDInfo[0]/CDInfo[1])*10);
                        coolDownDisplay.append("§b").append(Math.round(CDInfo[0]*10f)/10f).append("s §b▌▌▌▌▌▌▌▌▌▌")
                                .insert(coolDownDisplay.length()-10+filled, "§8");
                    }
                    case 1 -> {
                        int filled = Math.round(10-(CDInfo[0]/CDInfo[1])*10);
                        coolDownDisplay.append("§e").append(Math.round(CDInfo[0]*10f)/10f).append("s §a▌▌▌▌▌▌▌▌▌▌")
                                .insert(coolDownDisplay.length()-10+filled, "§8");
                    }
                    default -> Bukkit.getLogger().warning("CoolDownType " + CDInfo[2] + " from " + type + " of " + player.getDisplayName() + " is invalid!");
                }
                line = obj.getScore(coolDownDisplay.toString()); line.setScore(0);
            } else {
                line = obj.getScore(""); line.setScore(1);
                line = obj.getScore(" "); line.setScore(0);
            }
            player.setScoreboard(board);

        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning("Bukkit.getScoreboardManager() returned null!");
            exception.printStackTrace();
        }
    }
}

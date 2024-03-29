package game.spellbend.util;

import game.spellbend.PluginMain;
import game.spellbend.data.Enums;
import game.spellbend.playerdata.CoolDowns;
import game.spellbend.playerdata.Gems;
import game.spellbend.playerdata.Gold;
import game.spellbend.playerdata.PlayerDataUtil;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataBoard {
    private static final HashMap<UUID, Enums.SpellType> playersHoldingCooldownedItem = new HashMap<>();

    public static void start() {
        if (!Bukkit.getOnlinePlayers().isEmpty())                  //creating boards for players already online
            for (Player player : Bukkit.getOnlinePlayers())
                createBoard(player);

        new BukkitRunnable(){
            @Override
            public void run() {
                for (Map.Entry<UUID, Enums.SpellType> entry : playersHoldingCooldownedItem.entrySet()) {
                    Bukkit.getLogger().info("§b" + entry.getKey() + ": " + entry.getValue());
                    Player player = Bukkit.getPlayer(entry.getKey());
                    if (player == null) {
                        playersHoldingCooldownedItem.remove(entry.getKey());
                        Bukkit.getLogger().warning("UUID \"" + entry.getKey() + "\" registered in playersHoldingCooldownedItems is offline, removing from Map!");
                        return;
                    }
                    updateBoard(player, entry.getValue());
                }
            }
        }.runTaskTimer(PluginMain.getInstance(), 0, 2);
    }

    public static void registerPlayer(@NotNull Player player, @NotNull Enums.SpellType type) {
        playersHoldingCooldownedItem.put(player.getUniqueId(), type);
    }

    public static void deRegisterPlayer(@NotNull Player player) {
        Bukkit.getLogger().info("§bBefore removing:");
        for (Map.Entry<UUID, Enums.SpellType> entry : playersHoldingCooldownedItem.entrySet())
            Bukkit.getLogger().info("§b" + entry.getKey() + ": " + entry.getValue());

        playersHoldingCooldownedItem.remove(player.getUniqueId());
        updateBoard(player);

        Bukkit.getLogger().info("§bAfter removing:");
        for (Map.Entry<UUID, Enums.SpellType> entry : playersHoldingCooldownedItem.entrySet())
            Bukkit.getLogger().info("§b" + entry.getKey() + ": " + entry.getValue());
    }

    public static void deRegisterPlayer(@NotNull Player player, @Nullable Enums.SpellType type) {
        playersHoldingCooldownedItem.remove(player.getUniqueId());
        updateBoard(player, type);
    }

    public static void updateBoard(@NotNull Player player, @Nullable Enums.SpellType type) {
        try {
            //noinspection ConstantConditions    this can throw a NullPointerExcepttion if getScoreBoardManager() returns null, but it gets catched
            Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective obj = board.registerNewObjective("playerDataBoard", "dummy",  PlayerDataUtil.constructDisplayString(player));
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

            if (type != null && CoolDowns.getCoolDown(player, type)[0]>0.0001f) {
                line = obj.getScore("§7" + type.toString().charAt(0) + type.toString().substring(1).toLowerCase()); line.setScore(1);

                float[] CDInfo = CoolDowns.getCoolDown(player, type);
                StringBuilder coolDownDisplay = new StringBuilder();

                switch ((int) CDInfo[2]) {
                    case -1 -> {
                        int filled = Math.round((CDInfo[0]/CDInfo[1])*10);
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

    public static void updateBoard(@NotNull Player player) {
        createBoard(player);
    }

    public static void createBoard(@NotNull Player player) {
        try {
            //noinspection ConstantConditions this can throw a NullPointerExcepttion if getScoreBoardManager() returns null, but it gets catched
            Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective obj = board.registerNewObjective("playerDataBoard", "dummy",  PlayerDataUtil.constructDisplayString(player));
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

            Enums.SpellType type = DataUtil.getHeldSpellType(player);
            if (type != null && CoolDowns.getCoolDown(player, type)[0]>0.1f) {
                line = obj.getScore("§7" + type.toString().charAt(0) + type.toString().substring(1).toLowerCase()); line.setScore(1);

                float[] CDInfo = CoolDowns.getCoolDown(player, type);
                StringBuilder coolDownDisplay = new StringBuilder();

                switch ((int) CDInfo[2]) {
                    case -1 -> {
                        int filled = Math.round((CDInfo[0]/CDInfo[1])*10);
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

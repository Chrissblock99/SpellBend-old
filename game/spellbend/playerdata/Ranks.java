package game.spellbend.playerdata;

import game.spellbend.data.Lists;
import game.spellbend.data.PersistentDataKeys;
import game.spellbend.organize.RankObj;
import game.spellbend.util.PlayerDataBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Ranks {
    public static void loadRanks(@NotNull Player player) {
        if (!player.isOnline()) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is not online when trying to load Ranks, skipping loading!");
            return;
        }
        if (PersistentPlayerSessionStorage.ranks.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is already loaded when loading Ranks, skipping loading!");
            return;
        }

        PersistentDataContainer data = player.getPersistentDataContainer();
        try {
            //noinspection ConstantConditions
            ArrayList<String> ranksArray = new ArrayList<>(Arrays.asList(data.get(PersistentDataKeys.ranksKey, PersistentDataType.STRING).split(", ")));
            //noinspection RedundantCollectionOperation
            if (ranksArray.contains("")) ranksArray.remove("");
            PersistentPlayerSessionStorage.ranks.put(player.getUniqueId(), ranksArray);
        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning(player.getDisplayName() + " did not have Ranks set up, setting Ranks to \"player\"");
            data.set(PersistentDataKeys.ranksKey, PersistentDataType.STRING, "player");
            PersistentPlayerSessionStorage.ranks.put(player.getUniqueId(), new ArrayList<>(List.of("player")));
        }
    }

    public static @NotNull RankObj getMainRank(@NotNull Player player) {
        if (!PersistentPlayerSessionStorage.ranks.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToRanks map now fixing!");
            loadRanks(player);
        }
        if (PersistentPlayerSessionStorage.ranks.get(player.getUniqueId()).size() == 0) {
            Bukkit.getLogger().warning(player.getDisplayName() + " does not have any ranks!");
            return Lists.getRankByName("norank");
        }

        return Lists.getRankByName(PersistentPlayerSessionStorage.ranks.get(player.getUniqueId()).get(0));
    }

    public static boolean hasRank(@NotNull Player player, String rankName) {
        if (!PersistentPlayerSessionStorage.ranks.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToRanks map now fixing!");
            loadRanks(player);
        }
        if (Lists.getRankByName(rankName) == null) {
            Bukkit.getLogger().warning("Rank \"" + rankName + "\", tried to ask if " + player.getDisplayName() + "has it, does not exist!");
            return false;
        }
        return PersistentPlayerSessionStorage.ranks.get(player.getUniqueId()).contains(rankName);
    }

    public static @NotNull ArrayList<String> getRanks(@NotNull Player player) {
        if (!PersistentPlayerSessionStorage.ranks.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToRanks map now fixing!");
            loadRanks(player);
        }
        if (PersistentPlayerSessionStorage.ranks.get(player.getUniqueId()).size() == 0) {
            Bukkit.getLogger().warning(player.getDisplayName() + " does not have any ranks!");
            return new ArrayList<>();
        }

        return PersistentPlayerSessionStorage.ranks.get(player.getUniqueId());
    }

    public static void addRank(@NotNull Player player, String rankName) {
        if (!PersistentPlayerSessionStorage.ranks.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToRanks map now fixing!");
            loadRanks(player);
        }
        if (Lists.getRankByName(rankName) == null) {
            Bukkit.getLogger().warning("Rank \"" + rankName + "\", tried to add to " + player.getDisplayName() + ", does not exist!");
            return;
        }
        if (hasRank(player, rankName)) {
            Bukkit.getLogger().warning(player.getDisplayName() + " already has Rank \"" + rankName + "\" when trying to add it!");
            return;
        }

        PersistentPlayerSessionStorage.ranks.get(player.getUniqueId()).add(rankName);
        sortRanks(player);
        PlayerDataBoard.updateBoard(player);
    }

    public static void removeRank(@NotNull Player player, String rankName) {
        if (!PersistentPlayerSessionStorage.ranks.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToRanks map now fixing!");
            loadRanks(player);
        }
        if (Lists.getRankByName(rankName) == null) {
            Bukkit.getLogger().warning("Rank \"" + rankName + "\", tried to remove from " + player.getDisplayName() + ", does not exist!");
            return;
        }
        if (!hasRank(player, rankName)) {
            Bukkit.getLogger().warning(player.getDisplayName() + " doesn't have Rank \"" + rankName + "\" when trying to remove it!");
            return;
        }

        PersistentPlayerSessionStorage.ranks.get(player.getUniqueId()).remove(rankName);
        PlayerDataBoard.updateBoard(player);
    }

    private static void sortRanks(@NotNull Player player) {
        if (!PersistentPlayerSessionStorage.ranks.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToRanks map now fixing!");
            loadRanks(player);
        }
        PersistentPlayerSessionStorage.ranks.get(player.getUniqueId()).sort(Comparator.comparingInt(rank -> -Lists.getRankByName(rank).ranking));
    }

    public static void saveRanks(@NotNull Player player) {
        if (PersistentPlayerSessionStorage.ranks.containsKey(player.getUniqueId())) {
            player.getPersistentDataContainer().set(PersistentDataKeys.ranksKey, PersistentDataType.STRING, String.join(", ", PersistentPlayerSessionStorage.ranks.get(player.getUniqueId())));
            PersistentPlayerSessionStorage.ranks.remove(player.getUniqueId());
            return;
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToRanks map when saving, saving skipped!");
    }
}

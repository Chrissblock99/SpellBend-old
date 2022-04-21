package game.spellbend.playerdata;

import game.spellbend.data.Lists;
import game.spellbend.data.PersistentDataKeys;
import game.spellbend.organize.BadgeObj;
import game.spellbend.util.PlayerDataBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Badges {
    public static void loadBadges(@NotNull Player player) {
        if (!player.isOnline()) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is not online when trying to load Badges, skipping loading!");
            return;
        }
        if (PersistentPlayerSessionStorage.badges.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is already loaded when loading Badges, skipping loading!");
            return;
        }

        PersistentDataContainer data = player.getPersistentDataContainer();
        try {
            //noinspection ConstantConditions
            ArrayList<String> badgesArray = new ArrayList<>(Arrays.asList(data.get(PersistentDataKeys.badgesKey, PersistentDataType.STRING).split(", ")));
            //noinspection RedundantCollectionOperation
            if (badgesArray.contains("")) badgesArray.remove("");
            PersistentPlayerSessionStorage.badges.put(player.getUniqueId(), badgesArray);
        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning(player.getDisplayName() + " did not have Badges set up, setting Badges to \"\"");
            data.set(PersistentDataKeys.badgesKey, PersistentDataType.STRING, "");
            PersistentPlayerSessionStorage.badges.put(player.getUniqueId(), new ArrayList<>());
        }
    }

    public static @NotNull String getBadgesString(@NotNull Player player) {
        if (!PersistentPlayerSessionStorage.badges.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToBadges map now fixing!");
            loadBadges(player);
        }
        ArrayList<String> BadgeList = new ArrayList<>();
        for (String badgeName : PersistentPlayerSessionStorage.badges.get(player.getUniqueId())) {
            BadgeList.add(Lists.getBadgeByName(badgeName).CCedName);
        }

        String badgesString = String.join("", BadgeList);
        if (badgesString.equals("null")) return "";
        return badgesString;
    }

    public static @Nullable BadgeObj getMainBadge(@NotNull Player player) {
        if (!PersistentPlayerSessionStorage.badges.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToBadges map now fixing!");
            loadBadges(player);
        }
        if (PersistentPlayerSessionStorage.badges.get(player.getUniqueId()).size() == 0) {
            return null;
        }

        return Lists.getBadgeByName(PersistentPlayerSessionStorage.badges.get(player.getUniqueId()).get(0));
    }

    public static boolean hasBadge(@NotNull Player player, String badgeName) {
        if (!PersistentPlayerSessionStorage.badges.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToBadges map now fixing!");
            loadBadges(player);
        }
        if (Lists.getBadgeByName(badgeName) == null) {
            Bukkit.getLogger().warning("Badge \"" + badgeName + "\", tried to ask if " + player.getDisplayName() + "has it, does not exist!");
            return false;
        }
        return PersistentPlayerSessionStorage.badges.get(player.getUniqueId()).contains(badgeName);
    }

    public static @NotNull ArrayList<String> getBadges(@NotNull Player player) {
        if (!PersistentPlayerSessionStorage.badges.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToBadges map now fixing!");
            loadBadges(player);
        }
        return PersistentPlayerSessionStorage.badges.get(player.getUniqueId());
    }

    public static void addBadge(@NotNull Player player, String badgeName) {
        if (!PersistentPlayerSessionStorage.badges.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToBadges map now fixing!");
            loadBadges(player);
        }
        if (Lists.getBadgeByName(badgeName) == null) {
            Bukkit.getLogger().warning("Badge \"" + badgeName + "\", tried to add to " + player.getDisplayName() + ", does not exist!");
            return;
        }

        PersistentPlayerSessionStorage.badges.get(player.getUniqueId()).add(badgeName);
        sortBadges(player);
        PlayerDataBoard.updateBoard(player);
    }

    public static void removeBadge(@NotNull Player player, String badgeName) {
        if (!PersistentPlayerSessionStorage.badges.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToBadges map now fixing!");
            loadBadges(player);
        }
        if (Lists.getBadgeByName(badgeName) == null) {
            Bukkit.getLogger().warning("Badge \"" + badgeName  + "\", tried to add to " + player.getDisplayName() + ", does not exist!");
            return;
        }

        PersistentPlayerSessionStorage.badges.get(player.getUniqueId()).remove(badgeName);
        PlayerDataBoard.updateBoard(player);
    }

    private static void sortBadges(@NotNull Player player) {
        if (!PersistentPlayerSessionStorage.badges.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToBadges map now fixing!");
            loadBadges(player);
        }
        PersistentPlayerSessionStorage.badges.get(player.getUniqueId()).sort(Comparator.comparingInt(badge -> -Lists.getBadgeByName(badge).ranking));
    }

    public static void saveBadges(@NotNull Player player) {
        if (PersistentPlayerSessionStorage.badges.containsKey(player.getUniqueId())) {
            player.getPersistentDataContainer().set(PersistentDataKeys.badgesKey, PersistentDataType.STRING, String.join(", ", PersistentPlayerSessionStorage.badges.get(player.getUniqueId())));
            PersistentPlayerSessionStorage.badges.remove(player.getUniqueId());
            return;
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToBadges map when saving, saving skipped!");
    }
}

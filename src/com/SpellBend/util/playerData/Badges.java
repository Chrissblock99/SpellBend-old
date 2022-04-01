package com.SpellBend.util.playerData;

import com.SpellBend.data.Lists;
import com.SpellBend.organize.BadgeObj;
import com.SpellBend.organize.persistentPlayerSessionStorage;
import com.SpellBend.util.playerDataBoard;
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
        if (persistentPlayerSessionStorage.badges.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is already loaded when loading Badges, skipping loading!");
            return;
        }

        PersistentDataContainer data = player.getPersistentDataContainer();
        try {
            //noinspection ConstantConditions
            ArrayList<String> badgesArray = new ArrayList<>(Arrays.asList(data.get(playerDataUtil.badgesKey, PersistentDataType.STRING).split(", ")));
            //noinspection RedundantCollectionOperation
            if (badgesArray.contains("")) badgesArray.remove("");
            persistentPlayerSessionStorage.badges.put(player.getUniqueId(), badgesArray);
        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning(player.getDisplayName() + " did not have Badges set up, setting Badges to \"\"");
            data.set(playerDataUtil.badgesKey, PersistentDataType.STRING, "");
            persistentPlayerSessionStorage.badges.put(player.getUniqueId(), new ArrayList<>());
        }
    }

    public static @NotNull String getBadgesString(@NotNull Player player) {
        if (!persistentPlayerSessionStorage.badges.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToBadges map now fixing!");
            loadBadges(player);
        }
        ArrayList<String> BadgeList = new ArrayList<>();
        for (String badgeName : persistentPlayerSessionStorage.badges.get(player.getUniqueId())) {
            BadgeList.add(Lists.getBadgeByName(badgeName).CCedName);
        }

        String badgesString = String.join("", BadgeList);
        if (badgesString.equals("null")) return "";
        return badgesString;
    }

    public static @Nullable BadgeObj getMainBadge(@NotNull Player player) {
        if (!persistentPlayerSessionStorage.badges.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToBadges map now fixing!");
            loadBadges(player);
        }
        if (persistentPlayerSessionStorage.badges.get(player.getUniqueId()).size() == 0) {
            return null;
        }

        return Lists.getBadgeByName(persistentPlayerSessionStorage.badges.get(player.getUniqueId()).get(0));
    }

    public static boolean hasBadge(@NotNull Player player, String badgeName) {
        if (!persistentPlayerSessionStorage.badges.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToBadges map now fixing!");
            loadBadges(player);
        }
        if (Lists.getBadgeByName(badgeName) == null) {
            Bukkit.getLogger().warning("Badge \"" + badgeName + "\", tried to ask if " + player.getDisplayName() + "has it, does not exist!");
            return false;
        }
        return persistentPlayerSessionStorage.badges.get(player.getUniqueId()).contains(badgeName);
    }

    public static @NotNull ArrayList<String> getBadges(@NotNull Player player) {
        if (!persistentPlayerSessionStorage.badges.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToBadges map now fixing!");
            loadBadges(player);
        }
        return persistentPlayerSessionStorage.badges.get(player.getUniqueId());
    }

    public static void addBadge(@NotNull Player player, String badgeName) {
        if (!persistentPlayerSessionStorage.badges.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToBadges map now fixing!");
            loadBadges(player);
        }
        if (Lists.getBadgeByName(badgeName) == null) {
            Bukkit.getLogger().warning("Badge \"" + badgeName + "\", tried to add to " + player.getDisplayName() + ", does not exist!");
            return;
        }

        persistentPlayerSessionStorage.badges.get(player.getUniqueId()).add(badgeName);
        sortBadges(player);
        playerDataBoard.updateBoard(player);
    }

    public static void removeBadge(@NotNull Player player, String badgeName) {
        if (!persistentPlayerSessionStorage.badges.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToBadges map now fixing!");
            loadBadges(player);
        }
        if (Lists.getBadgeByName(badgeName) == null) {
            Bukkit.getLogger().warning("Badge \"" + badgeName  + "\", tried to add to " + player.getDisplayName() + ", does not exist!");
            return;
        }

        persistentPlayerSessionStorage.badges.get(player.getUniqueId()).remove(badgeName);
        playerDataBoard.updateBoard(player);
    }

    private static void sortBadges(@NotNull Player player) {
        if (!persistentPlayerSessionStorage.badges.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToBadges map now fixing!");
            loadBadges(player);
        }
        persistentPlayerSessionStorage.badges.get(player.getUniqueId()).sort(Comparator.comparingInt(badge -> -Lists.getBadgeByName(badge).ranking));
    }

    public static void saveBadges(@NotNull Player player) {
        if (persistentPlayerSessionStorage.badges.containsKey(player.getUniqueId())) {
            player.getPersistentDataContainer().set(playerDataUtil.badgesKey, PersistentDataType.STRING, String.join(", ", persistentPlayerSessionStorage.badges.get(player.getUniqueId())));
            persistentPlayerSessionStorage.badges.remove(player.getUniqueId());
            return;
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToBadges map when saving, saving skipped!");
    }
}

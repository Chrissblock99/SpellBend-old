package com.SpellBend.util.playerData;

import com.SpellBend.organize.persistentPlayerSessionStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class Crystals {
    public static void loadCrystals(@NotNull Player player) {
        if (!player.isOnline()) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is not online when trying to load Crystals, skipping loading!");
            return;
        }
        if (persistentPlayerSessionStorage.crystals.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is already loaded when loading Crystals, skipping loading!");
            return;
        }

        PersistentDataContainer data = player.getPersistentDataContainer();
        try {
            persistentPlayerSessionStorage.crystals.put(player.getUniqueId(), data.get(playerDataUtil.crystalsKey, PersistentDataType.INTEGER));
        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning(player.getDisplayName() + " did not have Crystals set up, setting Crystals to 0!");
            data.set(playerDataUtil.crystalsKey, PersistentDataType.INTEGER, 0);
            persistentPlayerSessionStorage.crystals.put(player.getUniqueId(), 0);
        }
    }

    public static void addCrystals(@NotNull Player player, int crystals) {
        setCrystals(player, getCrystals(player) + crystals);
    }

    public static void setCrystals(@NotNull Player player, int crystals) {
        persistentPlayerSessionStorage.crystals.put(player.getUniqueId(), crystals);
    }

    public static int getCrystals(@NotNull Player player) {
        if (persistentPlayerSessionStorage.crystals.containsKey(player.getUniqueId())) {
            return persistentPlayerSessionStorage.crystals.get(player.getUniqueId());
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToCrystals map now fixing!");
        loadCrystals(player);
        return persistentPlayerSessionStorage.crystals.get(player.getUniqueId());
    }

    public static void saveCrystals(@NotNull Player player) {
        if (persistentPlayerSessionStorage.crystals.containsKey(player.getUniqueId())) {
            player.getPersistentDataContainer().set(playerDataUtil.crystalsKey, PersistentDataType.INTEGER, persistentPlayerSessionStorage.crystals.get(player.getUniqueId()));
            persistentPlayerSessionStorage.crystals.remove(player.getUniqueId());
            return;
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToGold map when saving, saving skipped!");
    }
}

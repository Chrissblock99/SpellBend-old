package com.SpellBend.playerData;

import com.SpellBend.util.playerDataBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class Suffix {
    public static void loadSuffix(@NotNull Player player) {
        if (!player.isOnline()) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is not online when trying to load Suffix, skipping loading!");
            return;
        }
        if (persistentPlayerSessionStorage.suffix.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is already loaded when loading Suffix, skipping loading!");
            return;
        }

        PersistentDataContainer data = player.getPersistentDataContainer();
        try {
            persistentPlayerSessionStorage.suffix.put(player.getUniqueId(), data.get(playerDataUtil.suffixKey, PersistentDataType.STRING));
        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning(player.getDisplayName() + " did not have Suffix set up, setting Suffix to \"\"!");
            data.set(playerDataUtil.suffixKey, PersistentDataType.STRING, player.getDisplayName());
            persistentPlayerSessionStorage.suffix.put(player.getUniqueId(), player.getDisplayName());
        }
    }

    public static @NotNull String getSuffix(@NotNull Player player) {
        if (persistentPlayerSessionStorage.suffix.containsKey(player.getUniqueId())) {
            return persistentPlayerSessionStorage.suffix.get(player.getUniqueId());
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToSuffix map now fixing!");
        loadSuffix(player);
        return persistentPlayerSessionStorage.suffix.get(player.getUniqueId());
    }

    public static void setSuffix(@NotNull Player player, String suffix) {
        persistentPlayerSessionStorage.suffix.put(player.getUniqueId(), suffix);
        playerDataBoard.updateBoard(player);
    }

    public static void saveSuffix(@NotNull Player player) {
        if (persistentPlayerSessionStorage.suffix.containsKey(player.getUniqueId())) {
            player.getPersistentDataContainer().set(playerDataUtil.suffixKey, PersistentDataType.STRING, persistentPlayerSessionStorage.suffix.get(player.getUniqueId()));
            persistentPlayerSessionStorage.suffix.remove(player.getUniqueId());
            return;
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToSuffix map when saving, saving skipped!");
    }
}

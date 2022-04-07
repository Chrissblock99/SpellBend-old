package com.SpellBend.playerData;

import com.SpellBend.data.PersistentDataKeys;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class CrystalShards {
    public static void loadCrystalShards(@NotNull Player player) {
        if (!player.isOnline()) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is not online when trying to load CrystalShards, skipping loading!");
            return;
        }
        if (persistentPlayerSessionStorage.crystalShards.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is already loaded when loading CrystalShards, skipping loading!");
            return;
        }

        PersistentDataContainer data = player.getPersistentDataContainer();
        try {
            persistentPlayerSessionStorage.crystalShards.put(player.getUniqueId(), data.get(PersistentDataKeys.crystalShardsKey, PersistentDataType.INTEGER));
        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning(player.getDisplayName() + " did not have CrystalShards set up, setting CrystalShards to 0!");
            data.set(PersistentDataKeys.crystalShardsKey, PersistentDataType.INTEGER, 0);
            persistentPlayerSessionStorage.crystalShards.put(player.getUniqueId(), 0);
        }
    }

    public static void addCrystalShards(@NotNull Player player, int crystals) {
        setCrystalShards(player, getCrystalShards(player) + crystals);
    }

    public static void setCrystalShards(@NotNull Player player, int crystals) {
        persistentPlayerSessionStorage.crystalShards.put(player.getUniqueId(), crystals);
    }

    public static int getCrystalShards(@NotNull Player player) {
        if (persistentPlayerSessionStorage.crystalShards.containsKey(player.getUniqueId())) {
            return persistentPlayerSessionStorage.crystalShards.get(player.getUniqueId());
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToCrystalShards map now fixing!");
        loadCrystalShards(player);
        return persistentPlayerSessionStorage.crystalShards.get(player.getUniqueId());
    }

    public static void saveCrystalShards(@NotNull Player player) {
        if (persistentPlayerSessionStorage.crystalShards.containsKey(player.getUniqueId())) {
            player.getPersistentDataContainer().set(PersistentDataKeys.crystalShardsKey, PersistentDataType.INTEGER, persistentPlayerSessionStorage.crystalShards.get(player.getUniqueId()));
            persistentPlayerSessionStorage.crystalShards.remove(player.getUniqueId());
            return;
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToCrystalShards map when saving, saving skipped!");
    }
}

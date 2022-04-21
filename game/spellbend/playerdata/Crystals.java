package game.spellbend.playerdata;

import game.spellbend.data.PersistentDataKeys;
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
        if (PersistentPlayerSessionStorage.crystals.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is already loaded when loading Crystals, skipping loading!");
            return;
        }

        PersistentDataContainer data = player.getPersistentDataContainer();
        try {
            PersistentPlayerSessionStorage.crystals.put(player.getUniqueId(), data.get(PersistentDataKeys.crystalsKey, PersistentDataType.INTEGER));
        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning(player.getDisplayName() + " did not have Crystals set up, setting Crystals to 0!");
            data.set(PersistentDataKeys.crystalsKey, PersistentDataType.INTEGER, 0);
            PersistentPlayerSessionStorage.crystals.put(player.getUniqueId(), 0);
        }
    }

    public static void addCrystals(@NotNull Player player, int crystals) {
        setCrystals(player, getCrystals(player) + crystals);
    }

    public static void setCrystals(@NotNull Player player, int crystals) {
        PersistentPlayerSessionStorage.crystals.put(player.getUniqueId(), crystals);
    }

    public static int getCrystals(@NotNull Player player) {
        if (PersistentPlayerSessionStorage.crystals.containsKey(player.getUniqueId())) {
            return PersistentPlayerSessionStorage.crystals.get(player.getUniqueId());
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToCrystals map now fixing!");
        loadCrystals(player);
        return PersistentPlayerSessionStorage.crystals.get(player.getUniqueId());
    }

    public static void saveCrystals(@NotNull Player player) {
        if (PersistentPlayerSessionStorage.crystals.containsKey(player.getUniqueId())) {
            player.getPersistentDataContainer().set(PersistentDataKeys.crystalsKey, PersistentDataType.INTEGER, PersistentPlayerSessionStorage.crystals.get(player.getUniqueId()));
            PersistentPlayerSessionStorage.crystals.remove(player.getUniqueId());
            return;
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToCrystals map when saving, saving skipped!");
    }
}

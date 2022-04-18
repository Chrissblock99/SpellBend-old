package game.SpellBend.playerData;

import game.SpellBend.data.PersistentDataKeys;
import game.SpellBend.util.playerDataBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class Gems {
    public static void loadGems(@NotNull Player player) {
        if (!player.isOnline()) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is not online when trying to load Gems, skipping loading!");
            return;
        }
        if (persistentPlayerSessionStorage.ranks.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is already loaded when loading Gems, skipping loading!");
            return;
        }

        PersistentDataContainer data = player.getPersistentDataContainer();
        try {
            persistentPlayerSessionStorage.gems.put(player.getUniqueId(), data.get(PersistentDataKeys.gemsKey, PersistentDataType.INTEGER));
        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning(player.getDisplayName() + " did not have Gems set up, setting Gems to 150!");
            data.set(PersistentDataKeys.gemsKey, PersistentDataType.INTEGER, 150);
            persistentPlayerSessionStorage.gems.put(player.getUniqueId(), 150);
        }
    }

    public static void addGems(@NotNull Player player, int gems) {
        setGems(player, getGems(player) + gems);
    }

    public static void setGems(@NotNull Player player, int gems) {
        persistentPlayerSessionStorage.gems.put(player.getUniqueId(), gems);
        playerDataBoard.updateBoard(player);
    }

    public static int getGems(@NotNull Player player) {
        if (persistentPlayerSessionStorage.gems.containsKey(player.getUniqueId())) {
            return persistentPlayerSessionStorage.gems.get(player.getUniqueId());
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToGems map, now fixing!");
        loadGems(player);
        return persistentPlayerSessionStorage.gems.get(player.getUniqueId());
    }

    public static void saveGems(@NotNull Player player) {
        if (persistentPlayerSessionStorage.gems.containsKey(player.getUniqueId())) {
            player.getPersistentDataContainer().set(PersistentDataKeys.gemsKey, PersistentDataType.INTEGER, persistentPlayerSessionStorage.gems.get(player.getUniqueId()));
            persistentPlayerSessionStorage.gems.remove(player.getUniqueId());
            return;
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToGems map when saving, saving skipped!");
    }
}

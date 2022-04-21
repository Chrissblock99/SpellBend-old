package game.spellbend.playerdata;

import game.spellbend.data.PersistentDataKeys;
import game.spellbend.util.PlayerDataBoard;
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
        if (PersistentPlayerSessionStorage.ranks.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is already loaded when loading Gems, skipping loading!");
            return;
        }

        PersistentDataContainer data = player.getPersistentDataContainer();
        try {
            PersistentPlayerSessionStorage.gems.put(player.getUniqueId(), data.get(PersistentDataKeys.gemsKey, PersistentDataType.INTEGER));
        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning(player.getDisplayName() + " did not have Gems set up, setting Gems to 150!");
            data.set(PersistentDataKeys.gemsKey, PersistentDataType.INTEGER, 150);
            PersistentPlayerSessionStorage.gems.put(player.getUniqueId(), 150);
        }
    }

    public static void addGems(@NotNull Player player, int gems) {
        setGems(player, getGems(player) + gems);
    }

    public static void setGems(@NotNull Player player, int gems) {
        PersistentPlayerSessionStorage.gems.put(player.getUniqueId(), gems);
        PlayerDataBoard.updateBoard(player);
    }

    public static int getGems(@NotNull Player player) {
        if (PersistentPlayerSessionStorage.gems.containsKey(player.getUniqueId())) {
            return PersistentPlayerSessionStorage.gems.get(player.getUniqueId());
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToGems map, now fixing!");
        loadGems(player);
        return PersistentPlayerSessionStorage.gems.get(player.getUniqueId());
    }

    public static void saveGems(@NotNull Player player) {
        if (PersistentPlayerSessionStorage.gems.containsKey(player.getUniqueId())) {
            player.getPersistentDataContainer().set(PersistentDataKeys.gemsKey, PersistentDataType.INTEGER, PersistentPlayerSessionStorage.gems.get(player.getUniqueId()));
            PersistentPlayerSessionStorage.gems.remove(player.getUniqueId());
            return;
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToGems map when saving, saving skipped!");
    }
}

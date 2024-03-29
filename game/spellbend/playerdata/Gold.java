package game.spellbend.playerdata;

import game.spellbend.data.PersistentDataKeys;
import game.spellbend.util.PlayerDataBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class Gold {
    public static void loadGold(@NotNull Player player) {
        if (!player.isOnline()) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is not online when trying to load Gold, skipping loading!");
            return;
        }
        if (PersistentPlayerSessionStorage.gold.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is already loaded when loading Gold, skipping loading!");
            return;
        }

        PersistentDataContainer data = player.getPersistentDataContainer();
        try {
            PersistentPlayerSessionStorage.gold.put(player.getUniqueId(), data.get(PersistentDataKeys.goldKey, PersistentDataType.INTEGER));
        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning(player.getDisplayName() + " did not have Gold set up, setting Gold to 650!");
            data.set(PersistentDataKeys.goldKey, PersistentDataType.INTEGER, 650);
            PersistentPlayerSessionStorage.gold.put(player.getUniqueId(), 650);
        }
    }

    public static void addGold(@NotNull Player player, int gold) {
        setGold(player, getGold(player) + gold);
    }

    public static void setGold(@NotNull Player player, int gold) {
        PersistentPlayerSessionStorage.gold.put(player.getUniqueId(), gold);
        PlayerDataBoard.updateBoard(player);
    }

    public static int getGold(@NotNull Player player) {
        if (PersistentPlayerSessionStorage.gold.containsKey(player.getUniqueId())) {
            return PersistentPlayerSessionStorage.gold.get(player.getUniqueId());
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToGold map now fixing!");
        loadGold(player);
        return PersistentPlayerSessionStorage.gold.get(player.getUniqueId());
    }

    public static void saveGold(@NotNull Player player) {
        if (PersistentPlayerSessionStorage.gold.containsKey(player.getUniqueId())) {
            player.getPersistentDataContainer().set(PersistentDataKeys.goldKey, PersistentDataType.INTEGER, PersistentPlayerSessionStorage.gold.get(player.getUniqueId()));
            PersistentPlayerSessionStorage.gold.remove(player.getUniqueId());
            return;
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToGold map when saving, saving skipped!");
    }
}

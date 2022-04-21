package game.spellbend.playerdata;

import game.spellbend.data.PersistentDataKeys;
import game.spellbend.util.PlayerDataBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class Nick {
    public static void loadNick(@NotNull Player player) {
        if (!player.isOnline()) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is not online when trying to load Nick, skipping loading!");
            return;
        }
        if (PersistentPlayerSessionStorage.nick.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is already loaded when loading Nick, skipping loading!");
            return;
        }

        PersistentDataContainer data = player.getPersistentDataContainer();
        try {
            PersistentPlayerSessionStorage.nick.put(player.getUniqueId(), data.get(PersistentDataKeys.nickKey, PersistentDataType.STRING));
        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning(player.getDisplayName() + " did not have Nick set up, setting Nick to " + player.getDisplayName() + "!");
            data.set(PersistentDataKeys.nickKey, PersistentDataType.STRING, player.getDisplayName());
            PersistentPlayerSessionStorage.nick.put(player.getUniqueId(), player.getDisplayName());
        }
    }

    public static String getNick(@NotNull Player player) {
        if (PersistentPlayerSessionStorage.nick.containsKey(player.getUniqueId())) {
            return PersistentPlayerSessionStorage.nick.get(player.getUniqueId());
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToNick map now fixing!");
        loadNick(player);
        return PersistentPlayerSessionStorage.nick.get(player.getUniqueId());
    }

    public static void setNick(@NotNull Player player, String nick) {
        PersistentPlayerSessionStorage.nick.put(player.getUniqueId(), nick);
        PlayerDataBoard.updateBoard(player);
    }

    public static void saveNick(@NotNull Player player) {
        if (PersistentPlayerSessionStorage.nick.containsKey(player.getUniqueId())) {
            player.getPersistentDataContainer().set(PersistentDataKeys.nickKey, PersistentDataType.STRING, PersistentPlayerSessionStorage.nick.get(player.getUniqueId()));
            PersistentPlayerSessionStorage.nick.remove(player.getUniqueId());
            return;
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToNick map when saving, saving skipped!");
    }
}

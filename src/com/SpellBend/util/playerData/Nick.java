package com.SpellBend.util.playerData;

import com.SpellBend.organize.persistentPlayerSessionStorage;
import com.SpellBend.util.playerDataBoard;
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
        if (persistentPlayerSessionStorage.nick.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is already loaded when loading Nick, skipping loading!");
            return;
        }

        PersistentDataContainer data = player.getPersistentDataContainer();
        try {
            persistentPlayerSessionStorage.nick.put(player.getUniqueId(), data.get(playerDataUtil.nickKey, PersistentDataType.STRING));
        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning(player.getDisplayName() + " did not have Nick set up, setting Nick to " + player.getDisplayName() + "!");
            data.set(playerDataUtil.nickKey, PersistentDataType.STRING, player.getDisplayName());
            persistentPlayerSessionStorage.nick.put(player.getUniqueId(), player.getDisplayName());
        }
    }

    public static String getNick(@NotNull Player player) {
        if (persistentPlayerSessionStorage.nick.containsKey(player.getUniqueId())) {
            return persistentPlayerSessionStorage.nick.get(player.getUniqueId());
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToNick map now fixing!");
        loadNick(player);
        return persistentPlayerSessionStorage.nick.get(player.getUniqueId());
    }

    public static void setNick(@NotNull Player player, String nick) {
        persistentPlayerSessionStorage.nick.put(player.getUniqueId(), nick);
        playerDataBoard.updateBoard(player);
    }

    public static void saveNick(@NotNull Player player) {
        if (persistentPlayerSessionStorage.nick.containsKey(player.getUniqueId())) {
            player.getPersistentDataContainer().set(playerDataUtil.nickKey, PersistentDataType.STRING, persistentPlayerSessionStorage.nick.get(player.getUniqueId()));
            persistentPlayerSessionStorage.nick.remove(player.getUniqueId());
            return;
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToNick map when saving, saving skipped!");
    }
}

package game.SpellBend.playerData;

import game.SpellBend.data.Maps;
import game.SpellBend.data.Enums;
import game.SpellBend.data.PersistentDataKeys;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class SpellsOwned {
    private final static HashMap<Enums.Element, Integer> elementToIndex = Maps.elementToIndexMap;

    public static void loadSpellsOwned(@NotNull Player player) {
        if (!player.isOnline()) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is not online when trying to load SpellsOwned, skipping loading!");
            return;
        }
        if (persistentPlayerSessionStorage.spellsOwned.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is already loaded when loading SpellsOwned, skipping loading!");
            return;
        }

        PersistentDataContainer data = player.getPersistentDataContainer();
        try {
            persistentPlayerSessionStorage.spellsOwned.put(player.getUniqueId(), data.get(PersistentDataKeys.spellsOwnedKey, PersistentDataType.INTEGER_ARRAY));
        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning(player.getDisplayName() + " did not have spellsOwned set up, setting spellsOwned to int array of 28 zeros!");
            data.set(PersistentDataKeys.spellsOwnedKey, PersistentDataType.INTEGER_ARRAY, new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
            persistentPlayerSessionStorage.spellsOwned.put(player.getUniqueId(), new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
        }
    }

    public static int[] getAllSpellsOwned(@NotNull Player player) {
        if (persistentPlayerSessionStorage.spellsOwned.containsKey(player.getUniqueId())) {
            return persistentPlayerSessionStorage.spellsOwned.get(player.getUniqueId());
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToSpellsOwned map now fixing!");
        loadSpellsOwned(player);
        return persistentPlayerSessionStorage.spellsOwned.get(player.getUniqueId());
    }

    public static int getSpellsOwned(@NotNull Player player, Enums.Element element) {
        if (!persistentPlayerSessionStorage.spellsOwned.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToSpellsOwned map now fixing!");
            loadSpellsOwned(player);
        }
        return persistentPlayerSessionStorage.spellsOwned.get(player.getUniqueId())[Maps.elementToIndexMap.get(element)];
    }

    public static int getSpellsOwned(@NotNull Player player, int index) {
        if (!persistentPlayerSessionStorage.spellsOwned.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToSpellsOwned map now fixing!");
            loadSpellsOwned(player);
        }
        return persistentPlayerSessionStorage.spellsOwned.get(player.getUniqueId())[index];
    }

    public static void setSpellsOwned(@NotNull Player player, Enums.Element element, int owned) {
        int[] spellsOwned = persistentPlayerSessionStorage.spellsOwned.get(player.getUniqueId());
        spellsOwned[elementToIndex.get(element)] = owned;
        persistentPlayerSessionStorage.spellsOwned.put(player.getUniqueId(), spellsOwned);
    }

    public static void setSpellsOwned(@NotNull Player player, int index, int owned) {
        int[] spellsOwned = persistentPlayerSessionStorage.spellsOwned.get(player.getUniqueId());
        spellsOwned[index] = owned;
        persistentPlayerSessionStorage.spellsOwned.put(player.getUniqueId(), spellsOwned);
    }

    public static void saveSpellsOwned(@NotNull Player player) {
        if (persistentPlayerSessionStorage.spellsOwned.containsKey(player.getUniqueId())) {
            player.getPersistentDataContainer().set(PersistentDataKeys.spellsOwnedKey, PersistentDataType.INTEGER_ARRAY, persistentPlayerSessionStorage.spellsOwned.get(player.getUniqueId()));
            persistentPlayerSessionStorage.spellsOwned.remove(player.getUniqueId());
            return;
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToSpellsOwned map when saving, saving skipped!");
    }
}

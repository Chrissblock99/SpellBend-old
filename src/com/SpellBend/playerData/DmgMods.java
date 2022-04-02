package com.SpellBend.playerData;

import com.SpellBend.data.Lists;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class DmgMods {
    public static void loadDmgMods(@NotNull Player player) {
        if (!player.isOnline()) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is not online when trying to load DmgMods, skipping loading!");
            return;
        }
        if (persistentPlayerSessionStorage.dmgMods.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is already loaded when loading dmgMods, skipping loading!");
            return;
        }

        PersistentDataContainer data = player.getPersistentDataContainer();
        try {
            Float[] dmgMods = new Float[3];
            //noinspection ConstantConditions
            String[] stringFloats = data.get(playerDataUtil.dmgModsKey, PersistentDataType.STRING).split(", ");

            for (int i = 0;i<stringFloats.length;i++) dmgMods[i] = Float.parseFloat(stringFloats[i]);

            persistentPlayerSessionStorage.dmgMods.put(player.getUniqueId(), dmgMods);
        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning(player.getDisplayName() + " did not have dmgMods set up, setting dmgMods to 1, 1, 1");
            data.set(playerDataUtil.dmgModsKey, PersistentDataType.STRING, "1, 1, 1");
            persistentPlayerSessionStorage.dmgMods.put(player.getUniqueId(), new Float[]{1f, 1f, 1f});
        }
    }

    public static float getDmgMod(@NotNull Player player, String modName) {
        if (!persistentPlayerSessionStorage.dmgMods.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToDmgMods map now fixing!");
            loadDmgMods(player);
        }
        Float[] dmgMods = persistentPlayerSessionStorage.dmgMods.get(player.getUniqueId());
        if (modName.equals("all")) {
            float result = 1;
            for (float num : dmgMods) {
                result *= num;
            }
            return result;
        }
        return dmgMods[Lists.getDmgModTypeByName(modName).index];
    }

    public static void addDmgMod(@NotNull Player player, String modName, float mod) {
        if (!persistentPlayerSessionStorage.dmgMods.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToDmgMods map now fixing!");
            loadDmgMods(player);
        }

        Float[] dmgMods = persistentPlayerSessionStorage.dmgMods.get(player.getUniqueId());
        dmgMods[Lists.getDmgModTypeByName(modName).index] *= mod;
        persistentPlayerSessionStorage.dmgMods.put(player.getUniqueId(), dmgMods);
    }

    public static void removeDmgMod(@NotNull Player player, String modName, float mod) {
        if (!persistentPlayerSessionStorage.dmgMods.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToDmgMods map now fixing!");
            loadDmgMods(player);
        }

        Float[] dmgMods = persistentPlayerSessionStorage.dmgMods.get(player.getUniqueId());
        dmgMods[Lists.getDmgModTypeByName(modName).index] /= mod;
        persistentPlayerSessionStorage.dmgMods.put(player.getUniqueId(), dmgMods);
    }

    public static void extendDmgMod(@NotNull Player player, String modName, float mod) {
        if (!persistentPlayerSessionStorage.dmgMods.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToDmgMods map now fixing!");
            loadDmgMods(player);
        }

        Float[] dmgMods = persistentPlayerSessionStorage.dmgMods.get(player.getUniqueId());
        int index = Lists.getDmgModTypeByName(modName).index;
        if (dmgMods[index]<mod) dmgMods[index] = mod;
        persistentPlayerSessionStorage.dmgMods.put(player.getUniqueId(), dmgMods);
    }

    public static void setDmgMod(@NotNull Player player, String modName, float mod) {
        if (!persistentPlayerSessionStorage.dmgMods.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToDmgMods map now fixing!");
            loadDmgMods(player);
        }

        Float[] dmgMods = persistentPlayerSessionStorage.dmgMods.get(player.getUniqueId());
        dmgMods[Lists.getDmgModTypeByName(modName).index] = mod;
        persistentPlayerSessionStorage.dmgMods.put(player.getUniqueId(), dmgMods);
    }

    public static void saveDmgMods(@NotNull Player player) {
        if (persistentPlayerSessionStorage.dmgMods.containsKey(player.getUniqueId())) {
            Float[] dmgMods = persistentPlayerSessionStorage.dmgMods.get(player.getUniqueId());
            String[] stringFloats = new String[dmgMods.length];

            for (int i = 0;i<dmgMods.length;i++) stringFloats[i] = String.valueOf(dmgMods[i]);

            player.getPersistentDataContainer().set(playerDataUtil.dmgModsKey, PersistentDataType.STRING, String.join(", ", stringFloats));
            persistentPlayerSessionStorage.dmgMods.remove(player.getUniqueId());
            return;
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToDmgMods map when saving, saving skipped!");
    }
}

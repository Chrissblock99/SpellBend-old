package com.SpellBend.playerData;

import com.SpellBend.data.Lists;
import com.SpellBend.organize.CoolDownEntry;
import com.SpellBend.data.Enums;
import com.SpellBend.util.math.MathUtil;
import com.SpellBend.util.playerDataBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.*;

public class CoolDowns {
    public static void loadCoolDowns(@NotNull Player player) {
        if (!player.isOnline()) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is not online when trying to load CoolDowns, skipping loading!");
            return;
        }
        if (persistentPlayerSessionStorage.coolDowns.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is already loaded when loading coolDowns, skipping loading!");
            return;
        }
        ArrayList<String> coolDownEntries;

        PersistentDataContainer data = player.getPersistentDataContainer();
        try {
            //noinspection ConstantConditions
            coolDownEntries = new ArrayList<>(Arrays.asList(data.get(playerDataUtil.coolDownsKey, PersistentDataType.STRING).split(", ")));
            //noinspection RedundantCollectionOperation
            if (coolDownEntries.contains("")) coolDownEntries.remove("");
        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning(player.getDisplayName() + " did not have coolDowns set up, setting coolDowns to \"\"!");
            data.set(playerDataUtil.coolDownsKey, PersistentDataType.STRING, "");
            persistentPlayerSessionStorage.coolDowns.put(player.getUniqueId(), new HashMap<>());
            return;
        }

        HashMap<Enums.SpellType, CoolDownEntry> coolDowns = new HashMap<>();

        for (String coolDownEntry : coolDownEntries) {
            String[] entry = coolDownEntry.split(": ");
            String[] infoStrings = entry[1].split("; ");

            Bukkit.getLogger().info(entry[0]);
            for (String string : infoStrings) Bukkit.getLogger().info(string);

            try {
                coolDowns.put(Enums.SpellType.valueOf(entry[0]), new CoolDownEntry(Float.parseFloat(infoStrings[0]), playerDataUtil.timeParser.parse(infoStrings[1]), infoStrings[2]));
                Bukkit.getLogger().info(entry[0] + ": " + new CoolDownEntry(Float.parseFloat(infoStrings[0]), playerDataUtil.timeParser.parse(infoStrings[1]), infoStrings[2]));
            } catch (NumberFormatException exception) {
                Bukkit.getLogger().warning("String \"" + infoStrings[0] + "\" is supposed to be a Float but isn't! " + exception);
            } catch (IllegalArgumentException exception) {
                Bukkit.getLogger().warning("String \"" + entry[0] + "\" is supposed to be a SpellType but isn't! " + exception);
            } catch (ParseException exception) {
                Bukkit.getLogger().warning("String \"" + infoStrings[1] + "\" is supposed to be a Date but isn't! " + exception);
            }
        }

        persistentPlayerSessionStorage.coolDowns.put(player.getUniqueId(), coolDowns);
        for (String coolDownEntry : coolDownEntries) {
            String[] entry = coolDownEntry.split(": ");
            Bukkit.getLogger().info(entry[0] + ": " + getCoolDownEntry(player, Enums.SpellType.valueOf(entry[0])));
        }
    }

    /**Sets the cooldown of the player
     * regardless of present cooldown
     *
     * @param player The player to add the cooldown to
     * @param spellType The SpellType to cool down
     * @param timeInSeconds The time to cool down
     * @param CDType The CoolDownType
     */
    public static void setCoolDown(@NotNull Player player, @NotNull Enums.SpellType spellType, float timeInSeconds, String CDType) {
        if (!persistentPlayerSessionStorage.coolDowns.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not loaded in coolDowns map, now fixing!");
            loadCoolDowns(player);
        }
        persistentPlayerSessionStorage.coolDowns.get(player.getUniqueId()).put(spellType, new CoolDownEntry(timeInSeconds, new Date(), CDType));
        if (playerDataUtil.getHeldSpellType(player) == spellType) playerDataBoard.registerPlayer(player, spellType);
    }

    /**
     *
     * @param player The player to remove a cooldown from
     * @param spellType The cooldown to remove
     */
    public static void removeCoolDown(@NotNull Player player, @NotNull Enums.SpellType spellType) {
        if (!persistentPlayerSessionStorage.coolDowns.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not loaded in coolDowns map, now fixing!");
            loadCoolDowns(player);
        }
        persistentPlayerSessionStorage.coolDowns.get(player.getUniqueId()).remove(spellType);
    }

    /**Adds a cooldown to the player
     * if a cooldown is already present the larger one is assigned
     *
     * @param player The player to add the cooldown to
     * @param spellType The SpellType to cool down
     * @param timeInSeconds The time to cool down
     * @param CDType The CoolDownType
     */
    public static void extendCoolDown(@NotNull Player player, @NotNull Enums.SpellType spellType, float timeInSeconds, String CDType) {
        if (!persistentPlayerSessionStorage.coolDowns.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not loaded in coolDowns map, now fixing!");
            loadCoolDowns(player);
        }
        HashMap<Enums.SpellType, CoolDownEntry> coolDowns = persistentPlayerSessionStorage.coolDowns.get(player.getUniqueId());
        if (coolDowns.containsKey(spellType)) {
            if (coolDowns.get(spellType).getRemainingCoolDownTime() <= 0.1f) {
                coolDowns.put(spellType, new CoolDownEntry(timeInSeconds, new Date(), CDType));
                if (playerDataUtil.getHeldSpellType(player) == spellType) playerDataBoard.registerPlayer(player, spellType);
                return;
            }
            CoolDownEntry oldValues = coolDowns.get(spellType);
            if (MathUtil.ASmallerB(
                    new long[]{Lists.getCoolDownTypeByName(oldValues.coolDownType).typeInt*(-1), (long) oldValues.timeInS*1000-(new Date().getTime()-oldValues.startDate.getTime())},
                    new long[]{Lists.getCoolDownTypeByName(CDType).typeInt *(-1), (long) timeInSeconds*1000})) {
                coolDowns.put(spellType, new CoolDownEntry(timeInSeconds, new Date(), CDType));
                if (playerDataUtil.getHeldSpellType(player) == spellType) playerDataBoard.registerPlayer(player, spellType);
            }
            return;
        }
        persistentPlayerSessionStorage.coolDowns.get(player.getUniqueId()).put(spellType, new CoolDownEntry(timeInSeconds, new Date(), CDType));
        if (playerDataUtil.getHeldSpellType(player) == spellType) playerDataBoard.registerPlayer(player, spellType);
    }


    /**Adds a cooldown to the player but warning console if one is already present
     * if already present the larger cooldown is assigned
     *
     * @param player The player to add the cooldown to
     * @param spellType The SpellType to cool down
     * @param timeInSeconds The time to cool down
     * @param CDType The CoolDownType
     */
    public static void addCoolDown(@NotNull Player player, @NotNull Enums.SpellType spellType, float timeInSeconds, String CDType) {
        if (!persistentPlayerSessionStorage.coolDowns.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not loaded in coolDowns map, now fixing!");
            loadCoolDowns(player);
        }
        HashMap<Enums.SpellType, CoolDownEntry> coolDowns = persistentPlayerSessionStorage.coolDowns.get(player.getUniqueId());
        if (coolDowns.containsKey(spellType)) {
            if (coolDowns.get(spellType).getRemainingCoolDownTime() <= 0.1f) {
                coolDowns.put(spellType, new CoolDownEntry(timeInSeconds, new Date(), CDType));
                if (playerDataUtil.getHeldSpellType(player) == spellType) playerDataBoard.registerPlayer(player, spellType);
                return;
            }
            CoolDownEntry oldValues = coolDowns.get(spellType);
            Bukkit.getLogger().warning("CoolDown " + spellType + " is already present ("
                    + oldValues.getRemainingCoolDownTime() + ", " + oldValues.timeInS + ", " + oldValues.coolDownType
                    + ") when adding (" + timeInSeconds + ", " + CDType + ") to " + player.getDisplayName() + ", assigning larger coolDown!");
            if (MathUtil.ASmallerB(
                    new long[]{Lists.getCoolDownTypeByName(oldValues.coolDownType).typeInt*(-1), (long) oldValues.timeInS*1000-(new Date().getTime()-oldValues.startDate.getTime())},
                    new long[]{Lists.getCoolDownTypeByName(CDType).typeInt *(-1), (long) timeInSeconds*1000})) {
                coolDowns.put(spellType, new CoolDownEntry(timeInSeconds, new Date(), CDType));
                if (playerDataUtil.getHeldSpellType(player) == spellType) playerDataBoard.registerPlayer(player, spellType);
            }
            return;
        }
        coolDowns.put(spellType, new CoolDownEntry(timeInSeconds, new Date(), CDType));
        if (playerDataUtil.getHeldSpellType(player) == spellType) playerDataBoard.registerPlayer(player, spellType);
    }

    /**
     *
     * @param player The player to get the CoolDown from
     * @param spellType The CoolDown to get
     * @return A float[3] containing {remainingCooldownTime, TimeInS, CoolDownType}
     */
    public static float[] getCoolDown(@NotNull Player player, @NotNull Enums.SpellType spellType) {
        if (!persistentPlayerSessionStorage.coolDowns.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not loaded in coolDowns map, now fixing!");
            loadCoolDowns(player);
        }
        HashMap<Enums.SpellType, CoolDownEntry> coolDowns = persistentPlayerSessionStorage.coolDowns.get(player.getUniqueId());
        if (coolDowns.containsKey(spellType)) {
            if (coolDowns.get(spellType).getRemainingCoolDownTime() <= 0.1f){
                coolDowns.remove(spellType);
                return new float[]{0, 0, Lists.getCoolDownTypeByName("cooldown").typeInt};
            }
            CoolDownEntry entry = coolDowns.get(spellType);
            return new float[]{entry.getRemainingCoolDownTime(), entry.timeInS, Lists.getCoolDownTypeByName(entry.coolDownType).typeInt};
        }
        return new float[]{0, 0, Lists.getCoolDownTypeByName("cooldown").typeInt};
    }

    /**
     *
     * @param player The player to get the CoolDown from
     * @param spellType The CoolDown to get
     * @return A CoolDownEntry containing {TimeInS, startDate, CoolDownType}
     */
    public static CoolDownEntry getCoolDownEntry(@NotNull Player player, @NotNull Enums.SpellType spellType) {
        if (!persistentPlayerSessionStorage.coolDowns.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not loaded in coolDowns map, now fixing!");
            loadCoolDowns(player);
        }
        HashMap<Enums.SpellType, CoolDownEntry> coolDowns = persistentPlayerSessionStorage.coolDowns.get(player.getUniqueId());
        if (coolDowns.containsKey(spellType)) {
            if (coolDowns.get(spellType).getRemainingCoolDownTime() <= 0.1f){
                coolDowns.remove(spellType);
                return new CoolDownEntry(0, new Date(), "cooldown");
            }
            return coolDowns.get(spellType);
        }
        return new CoolDownEntry(0, new Date(), "cooldown");
    }

    /**
     *
     * @param player The player to get the CoolDown from
     * @return All CoolDownEntries containing {TimeInS, startDate, CoolDownType}
     */
    public static HashMap<Enums.SpellType, CoolDownEntry> getCoolDowns(@NotNull Player player) {
        if (!persistentPlayerSessionStorage.coolDowns.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not loaded in coolDowns map, now fixing!");
            loadCoolDowns(player);
        }
        HashMap<Enums.SpellType, CoolDownEntry> coolDowns = persistentPlayerSessionStorage.coolDowns.get(player.getUniqueId());
        for (Map.Entry<Enums.SpellType, CoolDownEntry> entry : coolDowns.entrySet())
            if (entry.getValue().getRemainingCoolDownTime() <= 0.1f)
                coolDowns.remove(entry.getKey());
        return coolDowns;
    }

    public static void saveCoolDowns(@NotNull Player player) {
        if (!persistentPlayerSessionStorage.coolDowns.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToCoolDowns map when saving, saving skipped!");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        boolean hasEntries = false;
        HashMap<Enums.SpellType, CoolDownEntry> coolDowns = persistentPlayerSessionStorage.coolDowns.get(player.getUniqueId());

        for (Map.Entry<Enums.SpellType, CoolDownEntry> entry : coolDowns.entrySet()) {
            CoolDownEntry info = entry.getValue();
            if (info.getRemainingCoolDownTime() <= 0.1f) {
                coolDowns.remove(entry.getKey());
                continue;
            }
            stringBuilder
                    .append(entry.getKey().toString()).append(": ")
                    .append(info.timeInS).append("; ").append(playerDataUtil.timeParser.format(info.startDate)).append("; ").append(info.coolDownType)
                    .append(", ");
            hasEntries = true;
        }
        if (hasEntries) {
            int length = stringBuilder.length();
            stringBuilder.delete(length-2,length);
        }

        player.getPersistentDataContainer().set(playerDataUtil.coolDownsKey, PersistentDataType.STRING, stringBuilder.toString());
        persistentPlayerSessionStorage.coolDowns.remove(player.getUniqueId());
    }
}

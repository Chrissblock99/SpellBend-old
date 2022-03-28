package com.SpellBend.util;

import com.SpellBend.PluginMain;
import com.SpellBend.data.Lists;
import com.SpellBend.data.Maps;
import com.SpellBend.organize.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;

//@SuppressWarnings("unused")
public class playerDataUtil {
    private final static PluginMain plugin = PluginMain.getInstance();
                                                          //DO NOT CHANGE THESE  v
    public final static NamespacedKey gemsKey = new NamespacedKey(plugin, "gems");
    public final static NamespacedKey goldKey = new NamespacedKey(plugin, "gold");
    public final static NamespacedKey spellsOwnedKey = new NamespacedKey(plugin, "spellsOwned");
    public static final NamespacedKey coolDownsKey = new NamespacedKey(plugin, "coolDowns");
    public final static NamespacedKey dmgModsKey = new NamespacedKey(plugin, "dmgMods");
    public final static NamespacedKey ranksKey = new NamespacedKey(plugin, "ranks");
    public final static NamespacedKey badgesKey = new NamespacedKey(plugin, "badges");
    public final static NamespacedKey nickKey = new NamespacedKey(plugin, "nick");
    public static final NamespacedKey suffixKey = new NamespacedKey(plugin, "suffix");
    public final static NamespacedKey crystalsKey = new NamespacedKey(plugin, "crystals");
                                                                //DO NOT CHANGE THOSE  /\
    public final static SimpleDateFormat timeParser = new SimpleDateFormat("dd-M-yyyy hh:mm:ss.SSS");
    private final static HashMap<Enums.Element, Integer> elementToIndex = Maps.elementToIndexMap;

    public static void setupAll(@NotNull Player player) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        data.set(gemsKey, PersistentDataType.INTEGER, 150);
        data.set(goldKey, PersistentDataType.INTEGER, 650);
        data.set(spellsOwnedKey, PersistentDataType.INTEGER_ARRAY, new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
        data.set(coolDownsKey, PersistentDataType.STRING, "");
        data.set(dmgModsKey, PersistentDataType.STRING, "1, 1, 1");
        data.set(ranksKey, PersistentDataType.STRING, "player");
        data.set(badgesKey, PersistentDataType.STRING, "");
        data.set(nickKey, PersistentDataType.STRING, player.getDisplayName());
        data.set(suffixKey, PersistentDataType.STRING, "");
        data.set(crystalsKey, PersistentDataType.INTEGER, 0);
    }

    public static void loadAll(@NotNull Player player) {
        loadGems(player);
        loadGold(player);
        loadSpellsOwned(player);
        loadHealth(player);
        loadCoolDowns(player);
        loadDmgMods(player);
        loadRanks(player);
        loadBadges(player);
        loadNick(player);
        loadSuffix(player);
        loadCrystals(player);
    }

    public static void saveAll(@NotNull Player player) {
        saveGems(player);
        saveGold(player);
        saveSpellsOwned(player);
        saveHealth(player);
        saveCoolDowns(player);
        saveDmgMods(player);
        saveRanks(player);
        saveBadges(player);
        saveNick(player);
        saveSuffix(player);
        saveCrystals(player);
    }

    public static @NotNull String constructDisplayString(@NotNull Player player) {
        RankObj playerRank = playerDataUtil.getMainRank(player);
        return playerRank.bracketsCC + "[" + playerRank.CCedRankName + playerRank.bracketsCC + "] " + playerRank.playerNameCC + getNick(player)
                + " "
                + ((getSuffix(player).equals("")) ? "" : ("§8[§7" + getSuffix(player) + "§8] "))
                + ((getBadgesString(player).equals("")) ? "" : ("§3[" + getBadgesString(player) + "§3] "))
                + "§7[§b" + player.getLevel() + "§7]";
    }

    //gems
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
            persistentPlayerSessionStorage.gems.put(player.getUniqueId(), data.get(gemsKey, PersistentDataType.INTEGER));
        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning(player.getDisplayName() + " did not have Gems set up, setting Gems to 150!");
            data.set(gemsKey, PersistentDataType.INTEGER, 150);
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
            player.getPersistentDataContainer().set(gemsKey, PersistentDataType.INTEGER, persistentPlayerSessionStorage.gems.get(player.getUniqueId()));
            persistentPlayerSessionStorage.gems.remove(player.getUniqueId());
            return;
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToGems map when saving, saving skipped!");
    }

    //gold
    public static void loadGold(@NotNull Player player) {
        if (!player.isOnline()) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is not online when trying to load Gold, skipping loading!");
            return;
        }
        if (persistentPlayerSessionStorage.gold.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is already loaded when loading Gold, skipping loading!");
            return;
        }

        PersistentDataContainer data = player.getPersistentDataContainer();
        try {
            persistentPlayerSessionStorage.gold.put(player.getUniqueId(), data.get(goldKey, PersistentDataType.INTEGER));
        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning(player.getDisplayName() + " did not have Gold set up, setting Gold to 650!");
            data.set(goldKey, PersistentDataType.INTEGER, 650);
            persistentPlayerSessionStorage.gold.put(player.getUniqueId(), 650);
        }
    }

    public static void addGold(@NotNull Player player, int gold) {
        setGold(player, getGold(player) + gold);
    }

    public static void setGold(@NotNull Player player, int gold) {
        persistentPlayerSessionStorage.gold.put(player.getUniqueId(), gold);
        playerDataBoard.updateBoard(player);
    }

    public static int getGold(@NotNull Player player) {
        if (persistentPlayerSessionStorage.gold.containsKey(player.getUniqueId())) {
            return persistentPlayerSessionStorage.gold.get(player.getUniqueId());
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToGold map now fixing!");
        loadGold(player);
        return persistentPlayerSessionStorage.gold.get(player.getUniqueId());
    }

    public static void saveGold(@NotNull Player player) {
        if (persistentPlayerSessionStorage.gold.containsKey(player.getUniqueId())) {
            player.getPersistentDataContainer().set(goldKey, PersistentDataType.INTEGER, persistentPlayerSessionStorage.gold.get(player.getUniqueId()));
            persistentPlayerSessionStorage.gold.remove(player.getUniqueId());
            return;
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToGold map when saving, saving skipped!");
    }

    //spells owned
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
            persistentPlayerSessionStorage.spellsOwned.put(player.getUniqueId(), data.get(spellsOwnedKey, PersistentDataType.INTEGER_ARRAY));
        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning(player.getDisplayName() + " did not have spellsOwned set up, setting spellsOwned to int array of 28 zeros!");
            data.set(spellsOwnedKey, PersistentDataType.INTEGER_ARRAY, new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
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
            player.getPersistentDataContainer().set(spellsOwnedKey, PersistentDataType.INTEGER_ARRAY, persistentPlayerSessionStorage.spellsOwned.get(player.getUniqueId()));
            persistentPlayerSessionStorage.spellsOwned.remove(player.getUniqueId());
            return;
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToSpellsOwned map when saving, saving skipped!");
    }

    public static boolean buyElement(@NotNull Player player, Enums.Element element) {

        return true;
    }

    //health
    public static void loadHealth(@NotNull Player player) {
        if (!player.isOnline()) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is not online when trying to load Health, skipping loading!");
            return;
        }
        if (persistentPlayerSessionStorage.health.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is already loaded when loading health, skipping loading!");
            return;
        }
        persistentPlayerSessionStorage.health.put(player.getUniqueId(), player.getHealth());
    }

    public static void setHealth(@NotNull Player player, double health) {
        if (!persistentPlayerSessionStorage.health.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not loaded in healthMap, now fixing!");
            loadHealth(player);
        }
        persistentPlayerSessionStorage.health.put(player.getUniqueId(), health);
        player.setHealth(health);
    }

    public static double dmgPlayer(@NotNull Player victim, @NotNull Player attacker, double dmg, @NotNull ItemStack item) {
        if (!persistentPlayerSessionStorage.health.containsKey(victim.getUniqueId())) {
            Bukkit.getLogger().warning(victim.getDisplayName() + " was not loaded in healthMap, now fixing!");
            loadHealth(victim);
        }
        double health = persistentPlayerSessionStorage.health.get(victim.getUniqueId())-dmg;

        if (health <= 0d) {
            onPlayerDeath(victim, attacker, item);
            return 0d;
        }

        persistentPlayerSessionStorage.health.put(victim.getUniqueId(), health);
        victim.setHealth(health);
        return health;
    }

    public static void onPlayerDeath(@NotNull Player victim, @NotNull Player attacker, @NotNull ItemStack item) {
        //noinspection ConstantConditions
        String msg = getNick(victim) + " was slain by " + getNick(attacker) + " using " + item.getItemMeta().getDisplayName();
        for (Player p : victim.getWorld().getPlayers()) p.sendMessage(msg);

        victim.setGameMode(GameMode.SPECTATOR);
        victim.setSpectatorTarget(attacker);

        new BukkitRunnable() {
            @Override
            public void run() {
                //tp victim to spawn here
                victim.setGameMode(GameMode.ADVENTURE);
            }
        }.runTaskLater(plugin, 100);
    }

    public static void saveHealth(@NotNull Player player) {
        if (!persistentPlayerSessionStorage.health.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToHealth map when saving, saving skipped!");
            return;
        }
        player.setHealth(persistentPlayerSessionStorage.health.get(player.getUniqueId()));
        persistentPlayerSessionStorage.health.remove(player.getUniqueId());
    }

    //coolDowns
    public static void loadCoolDowns(@NotNull Player player) {
        if (!player.isOnline()) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is not online when trying to load CoolDowns, skipping loading!");
            return;
        }
        if (persistentPlayerSessionStorage.coolDowns.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is already loaded when loading coolDowns, skipping loading!");
            return;
        }
        HashMap<Enums.SpellType, CoolDownEntry> coolDowns = new HashMap<>();
        ArrayList<String> coolDownEntries;

        PersistentDataContainer data = player.getPersistentDataContainer();
        try {
            //noinspection ConstantConditions
            coolDownEntries = new ArrayList<>(Arrays.asList(data.get(coolDownsKey, PersistentDataType.STRING).split(", ")));
            //noinspection RedundantCollectionOperation
            if (coolDownEntries.contains("")) coolDownEntries.remove("");
        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning(player.getDisplayName() + " did not have coolDowns set up, setting coolDowns to \"\"!");
            data.set(coolDownsKey, PersistentDataType.STRING, "");
            persistentPlayerSessionStorage.coolDowns.put(player.getUniqueId(), new HashMap<>());
            return;
        }

        for (String coolDownEntry : coolDownEntries) {
            String[] entry = coolDownEntry.split(": ");
            String[] infoStrings = entry[1].split("; ");

            try {
                coolDowns.put(Enums.SpellType.valueOf(entry[0]), new CoolDownEntry(Float.parseFloat(infoStrings[0]), timeParser.parse(infoStrings[1]), infoStrings[2]));
            } catch (IllegalArgumentException exception) {
                Bukkit.getLogger().warning("String \"" + entry[0] + "\" is supposed to be a SpellType but isn't!");
                return;
            } catch (ParseException exception) {
                Bukkit.getLogger().warning("String \"" + infoStrings[1] + "\" is supposed to be a Date but isn't!");
                return;
            }
        }

        persistentPlayerSessionStorage.coolDowns.put(player.getUniqueId(), coolDowns);
    }

    public static void setCoolDown(@NotNull Player player, @NotNull Enums.SpellType spellType, float timeInSeconds, String CDType) {
        if (!persistentPlayerSessionStorage.coolDowns.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not loaded in coolDowns map, now fixing!");
            loadCoolDowns(player);
        }
        persistentPlayerSessionStorage.coolDowns.get(player.getUniqueId()).put(spellType, new CoolDownEntry(timeInSeconds, new Date(), CDType));
    }

    public static void removeCoolDown(@NotNull Player player, @NotNull Enums.SpellType spellType) {
        if (!persistentPlayerSessionStorage.coolDowns.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not loaded in coolDowns map, now fixing!");
            loadCoolDowns(player);
        }
        persistentPlayerSessionStorage.coolDowns.get(player.getUniqueId()).remove(spellType);
    }

    public static void extendCoolDown(@NotNull Player player, @NotNull Enums.SpellType spellType, float timeInSeconds, String CDType) {
        if (!persistentPlayerSessionStorage.coolDowns.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not loaded in coolDowns map, now fixing!");
            loadCoolDowns(player);
        }
        HashMap<Enums.SpellType, CoolDownEntry> coolDowns = persistentPlayerSessionStorage.coolDowns.get(player.getUniqueId());
        if (coolDowns.containsKey(spellType)) {
            if (coolDowns.get(spellType).getRemainingCoolDownTime() <= 0.1f) {
                coolDowns.put(spellType, new CoolDownEntry(timeInSeconds, new Date(), CDType));
                return;
            }
            CoolDownEntry oldValues = coolDowns.get(spellType);
            if (MathUtil.ASmallerB(
                    new long[]{Lists.getCoolDownTypeByName(oldValues.coolDownType).typeInt*(-1), (long) oldValues.timeInS*1000-(new Date().getTime()-oldValues.startDate.getTime())},
                    new long[]{Lists.getCoolDownTypeByName(CDType).typeInt *(-1), (long) timeInSeconds*1000}))
                coolDowns.put(spellType, new CoolDownEntry(timeInSeconds, new Date(), CDType));
            return;
        }
        persistentPlayerSessionStorage.coolDowns.get(player.getUniqueId()).put(spellType, new CoolDownEntry(timeInSeconds, new Date(), CDType));
    }

    public static void addCoolDown(@NotNull Player player, @NotNull Enums.SpellType spellType, float timeInSeconds, String CDType) {
        if (!persistentPlayerSessionStorage.coolDowns.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not loaded in coolDowns map, now fixing!");
            loadCoolDowns(player);
        }
        HashMap<Enums.SpellType, CoolDownEntry> coolDowns = persistentPlayerSessionStorage.coolDowns.get(player.getUniqueId());
        if (coolDowns.containsKey(spellType)) {
            if (coolDowns.get(spellType).getRemainingCoolDownTime() <= 0.1f) {
                coolDowns.put(spellType, new CoolDownEntry(timeInSeconds, new Date(), CDType));
                return;
            }
            CoolDownEntry oldValues = coolDowns.get(spellType);
            Bukkit.getLogger().warning("CoolDown " + spellType + " is already present ("
                    + oldValues.getRemainingCoolDownTime() + ", " + oldValues.timeInS + ", " + oldValues.coolDownType
                    + ") when adding (" + timeInSeconds + ", " + CDType + ") to " + player.getDisplayName() + ", assigning larger coolDown!");
            if (MathUtil.ASmallerB(
                    new long[]{Lists.getCoolDownTypeByName(oldValues.coolDownType).typeInt*(-1), (long) oldValues.timeInS*1000-(new Date().getTime()-oldValues.startDate.getTime())},
                    new long[]{Lists.getCoolDownTypeByName(CDType).typeInt *(-1), (long) timeInSeconds*1000}))
                coolDowns.put(spellType, new CoolDownEntry(timeInSeconds, new Date(), CDType));
            return;
        }
        coolDowns.put(spellType, new CoolDownEntry(timeInSeconds, new Date(), CDType));
    }

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

    public static HashMap<Enums.SpellType, CoolDownEntry> getCoolDowns(@NotNull Player player) {
        if (!persistentPlayerSessionStorage.coolDowns.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not loaded in coolDowns map, now fixing!");
            loadCoolDowns(player);
        }
        HashMap<Enums.SpellType, CoolDownEntry> coolDowns = persistentPlayerSessionStorage.coolDowns.get(player.getUniqueId());
        for (Map.Entry<Enums.SpellType, CoolDownEntry> entry : coolDowns.entrySet())
            if (entry.getValue().getRemainingCoolDownTime() <= 0.1f) coolDowns.remove(entry.getKey());
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
                    .append(info.timeInS).append("; ").append(timeParser.format(info.startDate)).append("; ").append(info.coolDownType)
                    .append(", ");
            hasEntries = true;
        }
        if (hasEntries) {
            int length = stringBuilder.length();
            stringBuilder.delete(length-2,length);
        }

        player.getPersistentDataContainer().set(coolDownsKey, PersistentDataType.STRING, stringBuilder.toString());
        persistentPlayerSessionStorage.coolDowns.remove(player.getUniqueId());
    }

    //dmgMods
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
            String[] stringFloats = data.get(dmgModsKey, PersistentDataType.STRING).split(", ");

            for (int i = 0;i<stringFloats.length;i++) dmgMods[i] = Float.parseFloat(stringFloats[i]);

            persistentPlayerSessionStorage.dmgMods.put(player.getUniqueId(), dmgMods);
        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning(player.getDisplayName() + " did not have dmgMods set up, setting dmgMods to 1, 1, 1");
            data.set(dmgModsKey, PersistentDataType.STRING, "1, 1, 1");
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

            player.getPersistentDataContainer().set(dmgModsKey, PersistentDataType.STRING, String.join(", ", stringFloats));
            persistentPlayerSessionStorage.dmgMods.remove(player.getUniqueId());
            return;
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToDmgMods map when saving, saving skipped!");
    }

    //ranks
    public static void loadRanks(@NotNull Player player) {
        if (!player.isOnline()) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is not online when trying to load Ranks, skipping loading!");
            return;
        }
        if (persistentPlayerSessionStorage.ranks.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is already loaded when loading Ranks, skipping loading!");
            return;
        }

        PersistentDataContainer data = player.getPersistentDataContainer();
        try {
            //noinspection ConstantConditions
            ArrayList<String> ranksArray = new ArrayList<>(Arrays.asList(data.get(ranksKey, PersistentDataType.STRING).split(", ")));
            //noinspection RedundantCollectionOperation
            if (ranksArray.contains("")) ranksArray.remove("");
            persistentPlayerSessionStorage.ranks.put(player.getUniqueId(), ranksArray);
        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning(player.getDisplayName() + " did not have Ranks set up, setting Ranks to \"player\"");
            data.set(ranksKey, PersistentDataType.STRING, "player");
            persistentPlayerSessionStorage.ranks.put(player.getUniqueId(), new ArrayList<>(List.of("player")));
        }
    }

    public static @NotNull RankObj getMainRank(@NotNull Player player) {
        if (!persistentPlayerSessionStorage.ranks.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToRanks map now fixing!");
            loadRanks(player);
        }
        if (persistentPlayerSessionStorage.ranks.get(player.getUniqueId()).size() == 0) {
            Bukkit.getLogger().warning(player.getDisplayName() + " does not have any ranks!");
            return Lists.getRankByName("norank");
        }

        return Lists.getRankByName(persistentPlayerSessionStorage.ranks.get(player.getUniqueId()).get(0));
    }

    public static boolean hasRank(@NotNull Player player, String rankName) {
        if (!persistentPlayerSessionStorage.ranks.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToRanks map now fixing!");
            loadRanks(player);
        }
        if (Lists.getRankByName(rankName) == null) {
            Bukkit.getLogger().warning("Rank \"" + rankName + "\", tried to ask if " + player.getDisplayName() + "has it, does not exist!");
            return false;
        }
        return persistentPlayerSessionStorage.ranks.get(player.getUniqueId()).contains(rankName);
    }

    public static @NotNull ArrayList<String> getRanks(@NotNull Player player) {
        if (!persistentPlayerSessionStorage.ranks.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToRanks map now fixing!");
            loadRanks(player);
        }
        if (persistentPlayerSessionStorage.ranks.get(player.getUniqueId()).size() == 0) {
            Bukkit.getLogger().warning(player.getDisplayName() + " does not have any ranks!");
            return new ArrayList<>();
        }

        return persistentPlayerSessionStorage.ranks.get(player.getUniqueId());
    }

    public static void addRank(@NotNull Player player, String rankName) {
        if (!persistentPlayerSessionStorage.ranks.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToRanks map now fixing!");
            loadRanks(player);
        }
        if (Lists.getRankByName(rankName) == null) {
            Bukkit.getLogger().warning("Rank \"" + rankName + "\", tried to add to " + player.getDisplayName() + ", does not exist!");
            return;
        }
        if (hasRank(player, rankName)) {
            Bukkit.getLogger().warning(player.getDisplayName() + " already has Rank \"" + rankName + "\" when trying to add it!");
            return;
        }

        persistentPlayerSessionStorage.ranks.get(player.getUniqueId()).add(rankName);
        sortRanks(player);
        playerDataBoard.updateBoard(player);
    }

    public static void removeRank(@NotNull Player player, String rankName) {
        if (!persistentPlayerSessionStorage.ranks.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToRanks map now fixing!");
            loadRanks(player);
        }
        if (Lists.getRankByName(rankName) == null) {
            Bukkit.getLogger().warning("Rank \"" + rankName + "\", tried to remove from " + player.getDisplayName() + ", does not exist!");
            return;
        }
        if (!hasRank(player, rankName)) {
            Bukkit.getLogger().warning(player.getDisplayName() + " doesn't have Rank \"" + rankName + "\" when trying to remove it!");
            return;
        }

        persistentPlayerSessionStorage.ranks.get(player.getUniqueId()).remove(rankName);
        playerDataBoard.updateBoard(player);
    }

    private static void sortRanks(@NotNull Player player) {
        if (!persistentPlayerSessionStorage.ranks.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToRanks map now fixing!");
            loadRanks(player);
        }
        persistentPlayerSessionStorage.ranks.get(player.getUniqueId()).sort(Comparator.comparingInt(rank -> -Lists.getRankByName(rank).ranking));
    }

    public static void saveRanks(@NotNull Player player) {
        if (persistentPlayerSessionStorage.ranks.containsKey(player.getUniqueId())) {
            player.getPersistentDataContainer().set(ranksKey, PersistentDataType.STRING, String.join(", ", persistentPlayerSessionStorage.ranks.get(player.getUniqueId())));
            persistentPlayerSessionStorage.ranks.remove(player.getUniqueId());
            return;
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToRanks map when saving, saving skipped!");
    }

    //ranking
    public static int getRanking(@NotNull Player player) {
        int r1 = getMainRank(player).ranking;
        //noinspection ConstantConditions
        int r2 = (getMainBadge(player) == null) ? 0 : getMainBadge(player).ranking;
        return Math.max(r1, r2);
    }

    //badges
    public static void loadBadges(@NotNull Player player) {
        if (!player.isOnline()) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is not online when trying to load Badges, skipping loading!");
            return;
        }
        if (persistentPlayerSessionStorage.badges.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is already loaded when loading Badges, skipping loading!");
            return;
        }

        PersistentDataContainer data = player.getPersistentDataContainer();
        try {
            //noinspection ConstantConditions
            ArrayList<String> badgesArray = new ArrayList<>(Arrays.asList(data.get(badgesKey, PersistentDataType.STRING).split(", ")));
            //noinspection RedundantCollectionOperation
            if (badgesArray.contains("")) badgesArray.remove("");
            persistentPlayerSessionStorage.badges.put(player.getUniqueId(), badgesArray);
        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning(player.getDisplayName() + " did not have Badges set up, setting Badges to \"\"");
            data.set(badgesKey, PersistentDataType.STRING, "");
            persistentPlayerSessionStorage.badges.put(player.getUniqueId(), new ArrayList<>());
        }
    }

    public static @NotNull String getBadgesString(@NotNull Player player) {
        if (!persistentPlayerSessionStorage.badges.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToBadges map now fixing!");
            loadBadges(player);
        }
        ArrayList<String> BadgeList = new ArrayList<>();
        for (String badgeName : persistentPlayerSessionStorage.badges.get(player.getUniqueId())) {
            BadgeList.add(Lists.getBadgeByName(badgeName).CCedName);
        }

        String badgesString = String.join("", BadgeList);
        if (badgesString.equals("null")) return "";
        return badgesString;
    }

    public static @Nullable BadgeObj getMainBadge(@NotNull Player player) {
        if (!persistentPlayerSessionStorage.badges.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToBadges map now fixing!");
            loadBadges(player);
        }
        if (persistentPlayerSessionStorage.badges.get(player.getUniqueId()).size() == 0) {
            return null;
        }

        return Lists.getBadgeByName(persistentPlayerSessionStorage.badges.get(player.getUniqueId()).get(0));
    }

    public static boolean hasBadge(@NotNull Player player, String badgeName) {
        if (!persistentPlayerSessionStorage.badges.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToBadges map now fixing!");
            loadBadges(player);
        }
        if (Lists.getBadgeByName(badgeName) == null) {
            Bukkit.getLogger().warning("Badge \"" + badgeName + "\", tried to ask if " + player.getDisplayName() + "has it, does not exist!");
            return false;
        }
        return persistentPlayerSessionStorage.badges.get(player.getUniqueId()).contains(badgeName);
    }

    public static @NotNull ArrayList<String> getBadges(@NotNull Player player) {
        if (!persistentPlayerSessionStorage.badges.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToBadges map now fixing!");
            loadBadges(player);
        }
        return persistentPlayerSessionStorage.badges.get(player.getUniqueId());
    }

    public static void addBadge(@NotNull Player player, String badgeName) {
        if (!persistentPlayerSessionStorage.badges.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToBadges map now fixing!");
            loadBadges(player);
        }
        if (Lists.getBadgeByName(badgeName) == null) {
            Bukkit.getLogger().warning("Badge \"" + badgeName + "\", tried to add to " + player.getDisplayName() + ", does not exist!");
            return;
        }

        persistentPlayerSessionStorage.badges.get(player.getUniqueId()).add(badgeName);
        sortBadges(player);
        playerDataBoard.updateBoard(player);
    }

    public static void removeBadge(@NotNull Player player, String badgeName) {
        if (!persistentPlayerSessionStorage.badges.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToBadges map now fixing!");
            loadBadges(player);
        }
        if (Lists.getBadgeByName(badgeName) == null) {
            Bukkit.getLogger().warning("Badge \"" + badgeName  + "\", tried to add to " + player.getDisplayName() + ", does not exist!");
            return;
        }

        persistentPlayerSessionStorage.badges.get(player.getUniqueId()).remove(badgeName);
        playerDataBoard.updateBoard(player);
    }

    private static void sortBadges(@NotNull Player player) {
        if (!persistentPlayerSessionStorage.badges.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToBadges map now fixing!");
            loadBadges(player);
        }
        persistentPlayerSessionStorage.badges.get(player.getUniqueId()).sort(Comparator.comparingInt(badge -> -Lists.getBadgeByName(badge).ranking));
    }

    public static void saveBadges(@NotNull Player player) {
        if (persistentPlayerSessionStorage.badges.containsKey(player.getUniqueId())) {
            player.getPersistentDataContainer().set(badgesKey, PersistentDataType.STRING, String.join(", ", persistentPlayerSessionStorage.badges.get(player.getUniqueId())));
            persistentPlayerSessionStorage.badges.remove(player.getUniqueId());
            return;
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToBadges map when saving, saving skipped!");
    }

    //nick
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
            persistentPlayerSessionStorage.nick.put(player.getUniqueId(), data.get(nickKey, PersistentDataType.STRING));
        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning(player.getDisplayName() + " did not have Nick set up, setting Nick to " + player.getDisplayName() + "!");
            data.set(nickKey, PersistentDataType.STRING, player.getDisplayName());
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
            player.getPersistentDataContainer().set(nickKey, PersistentDataType.STRING, persistentPlayerSessionStorage.nick.get(player.getUniqueId()));
            persistentPlayerSessionStorage.nick.remove(player.getUniqueId());
            return;
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToNick map when saving, saving skipped!");
    }

    //suffix
    public static void loadSuffix(@NotNull Player player) {
        if (!player.isOnline()) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is not online when trying to load Suffix, skipping loading!");
            return;
        }
        if (persistentPlayerSessionStorage.suffix.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is already loaded when loading Suffix, skipping loading!");
            return;
        }

        PersistentDataContainer data = player.getPersistentDataContainer();
        try {
            persistentPlayerSessionStorage.suffix.put(player.getUniqueId(), data.get(suffixKey, PersistentDataType.STRING));
        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning(player.getDisplayName() + " did not have Suffix set up, setting Suffix to \"\"!");
            data.set(suffixKey, PersistentDataType.STRING, player.getDisplayName());
            persistentPlayerSessionStorage.suffix.put(player.getUniqueId(), player.getDisplayName());
        }
    }

    public static @NotNull String getSuffix(@NotNull Player player) {
        if (persistentPlayerSessionStorage.suffix.containsKey(player.getUniqueId())) {
            return persistentPlayerSessionStorage.suffix.get(player.getUniqueId());
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToSuffix map now fixing!");
        loadSuffix(player);
        return persistentPlayerSessionStorage.suffix.get(player.getUniqueId());
    }

    public static void setSuffix(@NotNull Player player, String suffix) {
        persistentPlayerSessionStorage.suffix.put(player.getUniqueId(), suffix);
        playerDataBoard.updateBoard(player);
    }

    public static void saveSuffix(@NotNull Player player) {
        if (persistentPlayerSessionStorage.suffix.containsKey(player.getUniqueId())) {
            player.getPersistentDataContainer().set(suffixKey, PersistentDataType.STRING, persistentPlayerSessionStorage.suffix.get(player.getUniqueId()));
            persistentPlayerSessionStorage.suffix.remove(player.getUniqueId());
            return;
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToSuffix map when saving, saving skipped!");
    }

    //crystals
    public static void loadCrystals(@NotNull Player player) {
        if (!player.isOnline()) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is not online when trying to load Crystals, skipping loading!");
            return;
        }
        if (persistentPlayerSessionStorage.crystals.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is already loaded when loading Crystals, skipping loading!");
            return;
        }

        PersistentDataContainer data = player.getPersistentDataContainer();
        try {
            persistentPlayerSessionStorage.crystals.put(player.getUniqueId(), data.get(crystalsKey, PersistentDataType.INTEGER));
        } catch (NullPointerException exception) {
            Bukkit.getLogger().warning(player.getDisplayName() + " did not have Crystals set up, setting Crystals to 0!");
            data.set(crystalsKey, PersistentDataType.INTEGER, 0);
            persistentPlayerSessionStorage.crystals.put(player.getUniqueId(), 0);
        }
    }

    public static void addCrystals(@NotNull Player player, int crystals) {
        setCrystals(player, getCrystals(player) + crystals);
    }

    public static void setCrystals(@NotNull Player player, int crystals) {
        persistentPlayerSessionStorage.crystals.put(player.getUniqueId(), crystals);
    }

    public static int getCrystals(@NotNull Player player) {
        if (persistentPlayerSessionStorage.crystals.containsKey(player.getUniqueId())) {
            return persistentPlayerSessionStorage.crystals.get(player.getUniqueId());
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToCrystals map now fixing!");
        loadCrystals(player);
        return persistentPlayerSessionStorage.crystals.get(player.getUniqueId());
    }

    public static void saveCrystals(@NotNull Player player) {
        if (persistentPlayerSessionStorage.crystals.containsKey(player.getUniqueId())) {
            player.getPersistentDataContainer().set(crystalsKey, PersistentDataType.INTEGER, persistentPlayerSessionStorage.crystals.get(player.getUniqueId()));
            persistentPlayerSessionStorage.crystals.remove(player.getUniqueId());
            return;
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToGold map when saving, saving skipped!");
    }

    //crystalShards

    //cosmetics
}

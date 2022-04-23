package game.spellbend.playerdata;

import game.spellbend.data.PersistentDataKeys;
import game.spellbend.organize.RankObj;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class PlayerDataUtil {
    /**Sets up all the PersistentData of the player
     *
     * @param player The player who's PersistentData to set up
     */
    public static void setupPlayerData(@NotNull Player player) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        data.set(PersistentDataKeys.gemsKey, PersistentDataType.INTEGER, 150);
        data.set(PersistentDataKeys.goldKey, PersistentDataType.INTEGER, 650);
        data.set(PersistentDataKeys.spellsOwnedKey, PersistentDataType.INTEGER_ARRAY, new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
        data.set(PersistentDataKeys.coolDownsKey, PersistentDataType.STRING, "");
        data.set(PersistentDataKeys.punishmentsKey, PersistentDataType.STRING, "");
        data.set(PersistentDataKeys.dmgModsKey, PersistentDataType.STRING, "1, 1, 1");
        data.set(PersistentDataKeys.ranksKey, PersistentDataType.STRING, "player");
        data.set(PersistentDataKeys.badgesKey, PersistentDataType.STRING, "");
        data.set(PersistentDataKeys.nickKey, PersistentDataType.STRING, player.getDisplayName());
        data.set(PersistentDataKeys.suffixKey, PersistentDataType.STRING, "");
        data.set(PersistentDataKeys.crystalsKey, PersistentDataType.INTEGER, 0);
        data.set(PersistentDataKeys.crystalShardsKey, PersistentDataType.INTEGER, 0);
    }

    /**Loads all the persistentData of the player
     *
     * @param player The player to load the PersistentData of
     */
    public static void loadAll(@NotNull Player player) {
        try {
            Gems.loadGems(player);
        } catch (Exception e) {
            Bukkit.getLogger().warning("The loading of " + player.getDisplayName() + "'s Gems generated an exception!");
            e.printStackTrace();
        }
        try {
            Gold.loadGold(player);
        } catch (Exception e) {
            Bukkit.getLogger().warning("The loading of " + player.getDisplayName() + "'s Gold generated an exception!");
            e.printStackTrace();
        }
        try {
            SpellsOwned.loadSpellsOwned(player);
        } catch (Exception e) {
            Bukkit.getLogger().warning("The loading of " + player.getDisplayName() + "'s SpellsOwned generated an exception!");
            e.printStackTrace();
        }
        try {
            Health.loadHealth(player);
        } catch (Exception e) {
            Bukkit.getLogger().warning("The loading of " + player.getDisplayName() + "'s Health generated an exception!");
            e.printStackTrace();
        }
        try {
            CoolDowns.loadCoolDowns(player);
        } catch (Exception e) {
            Bukkit.getLogger().warning("The loading of " + player.getDisplayName() + "'s Cooldowns generated an exception!");
            e.printStackTrace();
        }
        try {
            DmgMods.loadDmgMods(player);
        } catch (Exception e) {
            Bukkit.getLogger().warning("The loading of " + player.getDisplayName() + "'s DmgMods generated an exception!");
            e.printStackTrace();
        }
        try {
            Punishments.loadPunishments(player);
        } catch (Exception e) {
            Bukkit.getLogger().warning("The loading of " + player.getDisplayName() + "'s Punishments generated an exception!");
            e.printStackTrace();
        }
        try {
            Ranks.loadRanks(player);
        } catch (Exception e) {
            Bukkit.getLogger().warning("The loading of " + player.getDisplayName() + "'s Ranks generated an exception!");
            e.printStackTrace();
        }
        try {
            Badges.loadBadges(player);
        } catch (Exception e) {
            Bukkit.getLogger().warning("The loading of " + player.getDisplayName() + "'s Badges generated an exception!");
            e.printStackTrace();
        }
        try {
            Nick.loadNick(player);
        } catch (Exception e) {
            Bukkit.getLogger().warning("The loading of " + player.getDisplayName() + "'s Nick generated an exception!");
            e.printStackTrace();
        }
        try {
            Suffix.loadSuffix(player);
        } catch (Exception e) {
            Bukkit.getLogger().warning("The loading of " + player.getDisplayName() + "'s Suffix generated an exception!");
            e.printStackTrace();
        }
        try {
            Crystals.loadCrystals(player);
        } catch (Exception e) {
            Bukkit.getLogger().warning("The loading of " + player.getDisplayName() + "'s Crystals generated an exception!");
            e.printStackTrace();
        }
        try {
            CrystalShards.loadCrystalShards(player);
        } catch (Exception e) {
            Bukkit.getLogger().warning("The loading of " + player.getDisplayName() + "'s CrystalShards generated an exception!");
            e.printStackTrace();
        }
    }

    /**Saves all PersistentData of the player
     *
     * @param player The player to save the PersistentData of
     */
    public static void saveAll(@NotNull Player player) {
        try{
            Gems.saveGems(player);
        } catch (Exception e) {
            Bukkit.getLogger().warning("The saving of " + player.getDisplayName() + "'s Gems generated an exception!");
            e.printStackTrace();
        }
        try {
            Gold.saveGold(player);
        } catch (Exception e) {
            Bukkit.getLogger().warning("The saving of " + player.getDisplayName() + "'s Gold generated an exception!");
            e.printStackTrace();
        }
        try {
            SpellsOwned.saveSpellsOwned(player);
        } catch (Exception e) {
            Bukkit.getLogger().warning("The saving of " + player.getDisplayName() + "'s SpellsOwned generated an exception!");
            e.printStackTrace();
        }
        try {
            Health.saveHealth(player);
        } catch (Exception e) {
            Bukkit.getLogger().warning("The saving of " + player.getDisplayName() + "'s Health generated an exception!");
            e.printStackTrace();
        }
        try {
            CoolDowns.saveCoolDowns(player);
        } catch (Exception e) {
            Bukkit.getLogger().warning("The saving of " + player.getDisplayName() + "'s CoolDowns generated an exception!");
            e.printStackTrace();
        }
        try {
            DmgMods.saveDmgMods(player);
        } catch (Exception e) {
            Bukkit.getLogger().warning("The saving of " + player.getDisplayName() + "'s DmgMods generated an exception!");
            e.printStackTrace();
        }
        try {
            Punishments.savePunishments(player);
        } catch (Exception e) {
            Bukkit.getLogger().warning("The saving of " + player.getDisplayName() + "'s Punishments generated an exception!");
            e.printStackTrace();
        }
        try {
            Ranks.saveRanks(player);
        } catch (Exception e) {
            Bukkit.getLogger().warning("The saving of " + player.getDisplayName() + "'s Ranks generated an exception!");
            e.printStackTrace();
        }
        try {
            Badges.saveBadges(player);
        } catch (Exception e) {
            Bukkit.getLogger().warning("The saving of " + player.getDisplayName() + "'s Badges generated an exception!");
            e.printStackTrace();
        }
        try {
            Nick.saveNick(player);
        } catch (Exception e) {
            Bukkit.getLogger().warning("The saving of " + player.getDisplayName() + "'s Nick generated an exception!");
            e.printStackTrace();
        }
        try {
            Suffix.saveSuffix(player);
        } catch (Exception e) {
            Bukkit.getLogger().warning("The saving of " + player.getDisplayName() + "'s Suffix generated an exception!");
            e.printStackTrace();
        }
        try {
            Crystals.saveCrystals(player);
        } catch (Exception e) {
            Bukkit.getLogger().warning("The saving of " + player.getDisplayName() + "'s Crystals generated an exception!");
            e.printStackTrace();
        }
        try {
            CrystalShards.saveCrystalShards(player);
        } catch (Exception e) {
            Bukkit.getLogger().warning("The saving of " + player.getDisplayName() + "'s CrystalShards generated an exception!");
            e.printStackTrace();
        }
    }

    /**Constructs a String containing the Players rank, name, badges, suffix and level
     * should mostly be used in chat
     *
     * @param player The player to Construct the DisplayString of
     * @return The String showing the players rank, badges etc
     */
    public static @NotNull String constructDisplayString(@NotNull Player player) {
        RankObj playerRank = Ranks.getMainRank(player);
        return playerRank.bracketsCC + "[" + playerRank.CCedRankName + playerRank.bracketsCC + "] " + playerRank.playerNameCC + Nick.getNick(player)
                + " "
                + ((Suffix.getSuffix(player).equals("")) ? "" : ("§8[§7" + Suffix.getSuffix(player) + "§8] "))
                + ((Badges.getBadgesString(player).equals("")) ? "" : ("§3[" + Badges.getBadgesString(player) + "§3] "))
                + "§7[§b" + player.getLevel() + "§7]";
    }

    /**Returns the highest ranking of the players badges and Ranks
     *
     * @param player The player to get the ranking of
     * @return The ranking
     */
    public static int getRanking(@NotNull Player player) {
        int r1 = Ranks.getMainRank(player).ranking;
        //noinspection ConstantConditions
        int r2 = (Badges.getMainBadge(player) == null) ? 0 : Badges.getMainBadge(player).ranking;
        return Math.max(r1, r2);
    }
}

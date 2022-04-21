package game.spellbend.playerdata;

import game.spellbend.data.PersistentDataKeys;
import game.spellbend.organize.RankObj;
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
        Gems.loadGems(player);
        Gold.loadGold(player);
        SpellsOwned.loadSpellsOwned(player);
        Health.loadHealth(player);
        CoolDowns.loadCoolDowns(player);
        DmgMods.loadDmgMods(player);
        Ranks.loadRanks(player);
        Badges.loadBadges(player);
        Nick.loadNick(player);
        Suffix.loadSuffix(player);
        Crystals.loadCrystals(player);
        CrystalShards.loadCrystalShards(player);
    }

    /**Saves all PersistentData of the player
     *
     * @param player The player to save the PersistentData of
     */
    public static void saveAll(@NotNull Player player) {
        Gems.saveGems(player);
        Gold.saveGold(player);
        SpellsOwned.saveSpellsOwned(player);
        Health.saveHealth(player);
        CoolDowns.saveCoolDowns(player);
        DmgMods.saveDmgMods(player);
        Ranks.saveRanks(player);
        Badges.saveBadges(player);
        Nick.saveNick(player);
        Suffix.saveSuffix(player);
        Crystals.saveCrystals(player);
        CrystalShards.saveCrystalShards(player);
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

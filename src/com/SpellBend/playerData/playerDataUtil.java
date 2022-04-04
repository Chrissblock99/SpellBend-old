package com.SpellBend.playerData;

import com.SpellBend.PluginMain;
import com.SpellBend.data.Elements;
import com.SpellBend.data.Enums;
import com.SpellBend.organize.*;
import com.SpellBend.spell.SpellHandler;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;

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
    public final static NamespacedKey crystalShardsKey = new NamespacedKey(plugin, "crystalShards");
                                                                        //DO NOT CHANGE THOSE  /\
    public final static SimpleDateFormat timeParser = new SimpleDateFormat("dd-M-yyyy hh:mm:ss.SSS");

    /**Sets up all the PersistentData of the player
     *
     * @param player The player who's PersistentData to set up
     */
    public static void setupPlayerData(@NotNull Player player) {
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
        data.set(crystalShardsKey, PersistentDataType.INTEGER, 0);
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

    /**Buys the specified spell for the player doing safety checks
     *
     * @param player The player to buy a spell
     * @param element The element to buy
     * @return If the element was bought or not
     */
    public static boolean buyElement(@NotNull Player player, Enums.Element element) {
        ElementObj Element = Elements.getElementByEnum(element);
        if (SpellsOwned.getSpellsOwned(player, element) != 0) {
            Bukkit.getLogger().warning(player.getDisplayName() + " somehow had access to the buy function of " + element + " after buying it!");
            //noinspection ConstantConditions
            player.sendMessage("§9SHOP §8» §cYou already own " + Element.getItem().getItemMeta().getDisplayName() + "§c!");
            return false;
        }
        int price = Element.getPrice();
        if (Gems.getGems(player)<price) {
            player.sendMessage("§9SHOP §8» §cNot Enough §bGems§c! Need §b" + (price - Gems.getGems(player)) + "§c more!");
            return false;
        }

        SpellsOwned.setSpellsOwned(player, element, 1);
        Gems.addGems(player, -price);
        //noinspection ConstantConditions
        player.sendMessage("§9SHOP §8» §ePurchased " + Element.getItem().getItemMeta().getDisplayName() + " §6for §b" + price + " Gems§e!");
        return true;
    }

    /**Buys the specified spell for the player doing safety checks
     *
     * @param player The player to buy a spell
     * @param element The element to buy the spell from
     * @param index The spell to buy
     * @return If the spell was bought or not
     */
    public static boolean buySpell(@NotNull Player player, Enums.Element element, int index) {
        SpellObj Spell = Elements.getElementByEnum(element).getSpell(index);
        if (SpellsOwned.getSpellsOwned(player, element) == index && index != 0) {  //TODO overthink if this actually works because i hae no clue
            Bukkit.getLogger().warning(player.getDisplayName() + " somehow had access to the buy function of " + Spell.getName() + " after buying it!");
            //noinspection ConstantConditions
            player.sendMessage("§9SHOP §8» §cYou already own " + Spell.getItem().getItemMeta().getDisplayName() + "§c!");
            return false;
        }
        int price = Spell.getPrice();
        if (Gold.getGold(player)<price) {
            player.sendMessage("§9SHOP §8» §cNot Enough §eGold§c! Need §e" + (price - Gold.getGold(player)) + "§c more!");
            return false;
        }

        SpellsOwned.setSpellsOwned(player, element, index+1);
        Gold.addGold(player, -price);
        //noinspection ConstantConditions
        player.sendMessage("§9SHOP §8» §ePurchased " + Spell.getItem().getItemMeta().getDisplayName() + " §6for §e" + price + " Gold§e!");
        return true;
    }

    /**Returns the SpellType of the item the player is holding
     * and null if it isn't a Spell
     *
     * @param player The player to get the SpellType from
     * @return The SpellType of the current hold item
     */
    public static @Nullable Enums.SpellType getHeldSpellType(@NotNull Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getItemMeta() == null) return null;
        if (!item.getItemMeta().getPersistentDataContainer().has(SpellHandler.spellTypeKey, PersistentDataType.STRING)) return null;
        try {
            return Enums.SpellType.valueOf(item.getItemMeta().getPersistentDataContainer().get(SpellHandler.spellTypeKey, PersistentDataType.STRING));
        } catch (IllegalArgumentException exception) {
            Bukkit.getLogger().warning(player.getDisplayName() + " has item " + item.getItemMeta().getDisplayName() + "§e that has an invalid SpellType!");
            return null;
        }
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

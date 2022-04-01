package com.SpellBend.spell;

import com.SpellBend.PluginMain;
import com.SpellBend.organize.Enums;
import com.SpellBend.util.playerData.CoolDowns;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class SpellHandler {
    private static final ArrayList<String> spellNames = createSpellNamesList();
    private static final ArrayList<String> spellTypes = createSpellTypesList();
    private static final PluginMain plugin = PluginMain.getInstance();
    public static final NamespacedKey spellNameKey = new NamespacedKey(plugin, "spellName"); //<- DO NOT CHANGE
    public static final NamespacedKey spellTypeKey = new NamespacedKey(plugin, "spellType"); //<- DO NOT CHANGE
    public static HashMap<UUID, Float> stunTime = new HashMap<>();
    public static HashMap<UUID, ArrayList<Spell>> activeSpells = new HashMap<>();

    private static @NotNull ArrayList<String> createSpellTypesList() {
        ArrayList<String> spellTypesList = new ArrayList<>();
        for (Enums.SpellType type : Enums.SpellType.values()) spellTypesList.add(type.toString());
        return spellTypesList;
    }

    private static @NotNull ArrayList<String> createSpellNamesList() {
        ArrayList<String> spellNamesList = new ArrayList<>();

        //Ember
        //spellNamesList.add("Magma_Burst");
        spellNamesList.add("Ember_Blast");
        /*spellNamesList.add("Scorching_Column");
        spellNamesList.add("Blazing_Spin");*/
        spellNamesList.add("Fiery_Rage");
        //Water
        /*spellNamesList.add("Hydro_Blast");
        spellNamesList.add("Water_Spray");
        spellNamesList.add("Water_Torrent");
        spellNamesList.add("Sea_Shield");
        spellNamesList.add("Rising_Tide");
        //Nature
        spellNamesList.add("Verdant_Spored");
        spellNamesList.add("Poison_Darts");
        spellNamesList.add("Autumn_Winds");
        spellNamesList.add("Vine_Grab");
        spellNamesList.add("Natures_Aegis");
        //Earth
        spellNamesList.add("Serrated_Earth");
        spellNamesList.add("Landslide");
        spellNamesList.add("Burrow");
        spellNamesList.add("Seismic_Smash");
        spellNamesList.add("Rock_Body");
        //Electro
        spellNamesList.add("Lightning_Bolt");
        spellNamesList.add("Flash");
        spellNamesList.add("Seismic_Shock");
        spellNamesList.add("Lightning_Chain");
        spellNamesList.add("Galvanise");
        //Ice
        spellNamesList.add("Twisted_Flurry");
        spellNamesList.add("Frostbite");
        spellNamesList.add("Winters_Heave");
        spellNamesList.add("Avalanche");
        spellNamesList.add("Glacial_Armament");
        //Aether
        spellNamesList.add("Meteor_Belt");
        spellNamesList.add("Black_Hole");
        spellNamesList.add("Stellar_Slam");
        spellNamesList.add("Aethers_Wrath");
        spellNamesList.add("Cosmic_Smash");
        //Soul
        spellNamesList.add("Scatter_Bones");
        spellNamesList.add("Soul_Drain");
        spellNamesList.add("Seeking_Skull");
        spellNamesList.add("Gashing_Fossils");
        spellNamesList.add("Phantoms_Curse");
        //Time
        spellNamesList.add("Darts_of_TIme");
        spellNamesList.add("Escape_through_Time");
        spellNamesList.add("Deathly_Hour");
        spellNamesList.add("Chronopunch");
        spellNamesList.add("Temporal_Illusion");
        //Metal
        spellNamesList.add("Catapult");
        spellNamesList.add("Alloyed_Barrier");
        spellNamesList.add("Metarang");
        spellNamesList.add("Razor_Spin");
        spellNamesList.add("Ancient_Temper");*/

        return spellNamesList;
    }


    public static boolean itemIsSpell(@NotNull ItemStack item) {
        if (!item.hasItemMeta()) return false;
        //noinspection ConstantConditions
        if (!item.getItemMeta().hasCustomModelData()) return false;
        int CMD = item.getItemMeta().getCustomModelData();
        if (!(CMD > 0 && CMD < 101)) return false;
        PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
        if (data.has(spellNameKey, PersistentDataType.STRING) && data.has(spellNameKey, PersistentDataType.STRING)) {
            if (spellNames.contains(item.getItemMeta().getPersistentDataContainer().get(spellNameKey, PersistentDataType.STRING))) {
                if (data.has(spellTypeKey, PersistentDataType.STRING)) {
                    if (spellTypes.contains(data.get(spellTypeKey, PersistentDataType.STRING))) return true;
                    else Bukkit.getLogger().warning("Item " + item.getItemMeta().getDisplayName() + "§e has a valid spellName Attribute but not a valid spellType Attribute!");
                } else Bukkit.getLogger().warning("Item " + item.getItemMeta().getDisplayName() + "§e has a valid spellName Attribute but does not have a spellType Attribute!");
            }
        }
        return false;
    }

    public static void activateSpell(@NotNull Player player, @NotNull ItemStack item) {
        if (!activeSpells.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not registered in activeSpells map, now fixing!");
            registerPlayer(player);
        }
        if (!item.hasItemMeta()) {
            Bukkit.getLogger().warning("SpellItem, " + player.getDisplayName() + " tried to activate, does not have metaData, skipping activation!");
            return;
        }
        //noinspection ConstantConditions
        PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
        if (!data.has(spellNameKey, PersistentDataType.STRING)) {
            Bukkit.getLogger().warning("PersistentDataContainer of SpellItem, " + player.getDisplayName() + " tried to activate, does not have a spellName attribute, skipping activation!");
            return;
        }

        String spellName = item.getItemMeta().getPersistentDataContainer().get(spellNameKey, PersistentDataType.STRING);
        if (!spellNames.contains(spellName)) {
            Bukkit.getLogger().warning("Spell " + spellName + player.getDisplayName() + " tried to activate is not registered, skipping activation!");
            return;
        }
        Enums.SpellType spellType;
        if (!data.has(spellTypeKey, PersistentDataType.STRING)) {
            Bukkit.getLogger().warning("PersistentDataContainer of SpellItem, " + player.getDisplayName() + " tried to activate, does not have a spellType attribute, skipping activation!");
            return;
        }
        String spellTypeString = data.get(spellTypeKey, PersistentDataType.STRING);
        if (!spellTypes.contains(spellTypeString)) {
            Bukkit.getLogger().warning("PersistentDataContainer of SpellItem, " + player.getDisplayName() + " tried to activate, does not have a valid spellType attribute, skipping activation!");
            return;
        }
        spellType = Enums.SpellType.valueOf(spellTypeString);
        if (CoolDowns.getCoolDown(player, spellType)[0] != 0) {
            return;
        }



        try {
            activeSpells.get(player.getUniqueId()).add((Spell) Class.forName("com.SpellBend.spell." + spellName).getDeclaredConstructor(new Class[]{Player.class, ItemStack.class}).newInstance(player, item));
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            Bukkit.getLogger().warning("An Error occurred in the SpellHandler when instancing " + spellName + " for " + player.getDisplayName() + ": " + exception);
        }
    }

    public static void stunPlayer(@NotNull Player player, int time) {
        for (Spell spell : activeSpells.get(player.getUniqueId())) {
            if (spell instanceof stunable) ((stunable) spell).onUserStun(time);
        }
    }

    public static void killPlayer(@NotNull Player victim, @NotNull Player killer) {
        for (Spell spell : activeSpells.get(victim.getUniqueId())) {
            if (spell instanceof killable) ((killable) spell).onUserDeath(killer);
        }
    }

    public static void registerPlayer(@NotNull Player player) {
        if (stunTime.containsKey(player.getUniqueId()))
            Bukkit.getLogger().warning(player.getDisplayName() + " is already loaded when registering him to stunTime, skipping registering!");
        else stunTime.put(player.getUniqueId(), 0f);

        if (activeSpells.containsKey(player.getUniqueId()))
            Bukkit.getLogger().warning(player.getDisplayName() + " is already loaded when registering him to activeSpells, skipping registering!");
        else activeSpells.put(player.getUniqueId(), new ArrayList<>());
    }

    public static void deRegisterPlayer(@NotNull Player player) {
        if (activeSpells.containsKey(player.getUniqueId())) {
            //we can't use foreach as that would throw a ConcurrentModificationException (list modified while iterating over it) which we are bypassing by doing it like this
            ArrayList<Spell> playersActiveSpells = activeSpells.get(player.getUniqueId());
            for (int i = playersActiveSpells.size()-1; i>=0 ;i--) playersActiveSpells.get(i).onUserLeave();
            activeSpells.remove(player.getUniqueId());

        } else Bukkit.getLogger().warning(player.getDisplayName() + " was not registered in activeSpells map when deleting, skipping deletion!");
        if (stunTime.containsKey(player.getUniqueId())) {
            stunTime.remove(player.getUniqueId());
        } else Bukkit.getLogger().warning(player.getDisplayName() + " was not registered in stunTime map when deleting, skipping deletion!");
    }
}

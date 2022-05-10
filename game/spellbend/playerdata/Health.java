package game.spellbend.playerdata;

import game.spellbend.PluginMain;
import game.spellbend.organize.DamageEntry;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

public class Health {
    private final static PluginMain plugin = PluginMain.getInstance();

    public static void registerPlayer(@NotNull Player player) {
        if (!player.isOnline()) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is not online when trying to register Health, skipping it!");
            return;
        }
        if (PersistentPlayerSessionStorage.health.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is already registered when registering health, skipping it!");
            return;
        }
        PersistentPlayerSessionStorage.health.put(player.getUniqueId(), new ArrayList<>());
        player.setHealth(20d);
    }

    public static double getHealth(@NotNull Player player) {
        if (!PersistentPlayerSessionStorage.health.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not registered in healthMap, now fixing!");
            registerPlayer(player);
        }
        double health = 20d;
        for (DamageEntry entry : PersistentPlayerSessionStorage.health.get(player.getUniqueId()))
            health -= entry.getDamage();
        return health;
    }

    /**
     *
     * @param player The player to heal
     * @param heal The amount of health to heal
     * @return The player's health after healing
     */
    public static double healPlayer(@NotNull Player player, double heal) {
        if (heal < 0)
            throw new IllegalArgumentException("Healing amount cannot be negative!");
        if (!PersistentPlayerSessionStorage.health.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not registered in healthMap, now fixing!");
            registerPlayer(player);
        }
        ArrayList<DamageEntry> damageEntries = PersistentPlayerSessionStorage.health.get(player.getUniqueId());

        int lastIndex;
        while (true) {
            if (damageEntries.size() == 0)
                break;
            lastIndex = damageEntries.size()-1;

            damageEntries.get(lastIndex).setDamage(damageEntries.get(lastIndex).getDamage()-heal);
            heal = damageEntries.get(lastIndex).getDamage();
            if (heal > 0)
                break;
            damageEntries.remove(lastIndex);
            if (heal == 0)
                break;
            heal *= -1;
        }

        return getHealth(player);
    }

    /**
     *
     * @param victim The attacked player
     * @param attacker The attacking player
     * @param rawDamage The damage dealt not modified by dmgMods
     * @param item The item used to damage
     * @return The damage dealt after modification by dmgMods (and limited to health left)
     */
    public static double dmgPlayer(@NotNull Player victim, @NotNull Player attacker, double rawDamage, @NotNull ItemStack item) {
        if (rawDamage < 0)
            throw new IllegalArgumentException("Damage cannot be negative!");
        if (!PersistentPlayerSessionStorage.health.containsKey(victim.getUniqueId())) {
            Bukkit.getLogger().warning(victim.getDisplayName() + " was not registered in healthMap, now fixing!");
            registerPlayer(victim);
        }

        double dmg = rawDamage * DmgMods.getDmgMod(attacker, "all");
        double healthBefore = getHealth(victim);
        PersistentPlayerSessionStorage.health.get(victim.getUniqueId()).add(0, new DamageEntry(attacker.getUniqueId(),
                (getHealth(victim)-dmg <= 0) ? healthBefore : dmg)); //the health before dmg is equal to health needed to kill
        double health = getHealth(victim);

        if (health <= 0d) {
            onPlayerDeath(victim, attacker, item);
            return healthBefore; //the health before dmg is equal to health needed to kill
        }

        victim.setHealth(health);
        return dmg;
    }

    public static void onPlayerDeath(@NotNull Player victim, @NotNull Player killer, @NotNull ItemStack item) {
        //noinspection ConstantConditions
        String msg = Nick.getNick(victim) + " was slain by " + Nick.getNick(killer) + " using " + item.getItemMeta().getDisplayName();
        for (Player p : victim.getWorld().getPlayers())
            p.sendMessage(msg);

        ArrayList<DamageEntry> uniqueAttackers = new ArrayList<>();

        for (DamageEntry damageEntry : PersistentPlayerSessionStorage.health.get(victim.getUniqueId())) {
            UUID attackerUUID = damageEntry.getPlayerUUID();
            boolean foundAlreadyExistingEntry = false;

            for (DamageEntry uniqueEntry : uniqueAttackers) //checking if such an attacker already is in list
                if (attackerUUID.equals(uniqueEntry.getPlayerUUID())) {
                    uniqueEntry.setDamage(uniqueEntry.getDamage() + damageEntry.getDamage());
                    foundAlreadyExistingEntry = true;
                    break;
                }
            if (!foundAlreadyExistingEntry)
                uniqueAttackers.add(damageEntry);
        }

        UUID killerUUID = killer.getUniqueId();
        for (DamageEntry entry : uniqueAttackers) {
            Player player = Bukkit.getPlayer(entry.getPlayerUUID());
            if (player == null)
                continue;

            double percentage = entry.getDamage()/20d;
            int gems = (int) Math.ceil(3*percentage);
            int gold = (int) Math.ceil(10*percentage);

            player.sendMessage("§e" + ((killerUUID.equals(entry.getPlayerUUID())) ? "Kill" : "Assist") + "! §6+" + gold + " Gold §8| §b+" + gems + " Gems");
            Gems.addGems(player, gems);
            Gold.addGold(player, gold);
        }

        victim.setGameMode(GameMode.SPECTATOR);
        victim.setSpectatorTarget(killer);
        PersistentPlayerSessionStorage.health.get(victim.getUniqueId()).clear(); //basically setting health back to max

        new BukkitRunnable() {
            @Override
            public void run() {
                //tp victim to spawn here
                victim.setGameMode(GameMode.ADVENTURE);
            }
        }.runTaskLater(plugin, 100);
    }

    public static void deRegisterPlayer(@NotNull Player player) {
        if (!PersistentPlayerSessionStorage.health.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToHealth map when deRegistering, skipping it!");
            return;
        }
        PersistentPlayerSessionStorage.health.remove(player.getUniqueId());
    }
}

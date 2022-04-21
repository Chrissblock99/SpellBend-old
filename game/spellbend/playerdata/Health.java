package game.spellbend.playerdata;

import game.spellbend.PluginMain;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class Health {
    private final static PluginMain plugin = PluginMain.getInstance();

    public static void loadHealth(@NotNull Player player) {
        if (!player.isOnline()) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is not online when trying to load Health, skipping loading!");
            return;
        }
        if (PersistentPlayerSessionStorage.health.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is already loaded when loading health, skipping loading!");
            return;
        }
        PersistentPlayerSessionStorage.health.put(player.getUniqueId(), player.getHealth());
    }

    public static void setHealth(@NotNull Player player, double health) {
        if (!PersistentPlayerSessionStorage.health.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not loaded in healthMap, now fixing!");
            loadHealth(player);
        }
        PersistentPlayerSessionStorage.health.put(player.getUniqueId(), health);
        player.setHealth(health);
    }

    public static double dmgPlayer(@NotNull Player victim, @NotNull Player attacker, double dmg, @NotNull ItemStack item) {
        if (!PersistentPlayerSessionStorage.health.containsKey(victim.getUniqueId())) {
            Bukkit.getLogger().warning(victim.getDisplayName() + " was not loaded in healthMap, now fixing!");
            loadHealth(victim);
        }
        double health = PersistentPlayerSessionStorage.health.get(victim.getUniqueId())-dmg;

        if (health <= 0d) {
            onPlayerDeath(victim, attacker, item);
            return 0d;
        }

        PersistentPlayerSessionStorage.health.put(victim.getUniqueId(), health);
        victim.setHealth(health);
        return health;
    }

    public static void onPlayerDeath(@NotNull Player victim, @NotNull Player attacker, @NotNull ItemStack item) {
        //noinspection ConstantConditions
        String msg = Nick.getNick(victim) + " was slain by " + Nick.getNick(attacker) + " using " + item.getItemMeta().getDisplayName();
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
        if (!PersistentPlayerSessionStorage.health.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToHealth map when saving, saving skipped!");
            return;
        }
        player.setHealth(PersistentPlayerSessionStorage.health.get(player.getUniqueId()));
        PersistentPlayerSessionStorage.health.remove(player.getUniqueId());
    }
}

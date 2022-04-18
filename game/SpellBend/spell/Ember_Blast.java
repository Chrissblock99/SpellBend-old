package game.SpellBend.spell;

import game.SpellBend.PluginMain;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class Ember_Blast extends Spell {
    Player player;
    BukkitTask runnable;
    Spell instance;

    public Ember_Blast(@NotNull Player player, @NotNull ItemStack item) {
        this.player = player;
        instance = this;

        Fireball fireball = player.launchProjectile(Fireball.class);
        fireball.setIsIncendiary(false);

        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                fireball.remove();
                SpellHandler.activeSpells.get(player.getUniqueId()).remove(instance);
            }
        }.runTaskLater(PluginMain.getInstance(), 200);
    }

    @Override
    public void onUserLeave() {

    }

    @Override
    public void cancelSpell() {
        runnable.cancel();
        SpellHandler.activeSpells.get(player.getUniqueId()).remove(instance);
    }
}

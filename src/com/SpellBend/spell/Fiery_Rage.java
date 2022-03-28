package com.SpellBend.spell;

import com.SpellBend.PluginMain;
import com.SpellBend.organize.CoolDownEntry;
import com.SpellBend.organize.Enums;
import com.SpellBend.util.playerDataUtil;
import com.SpellBend.util.VectorConversion;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Fiery_Rage extends Spell implements killable {
    Player player;
    BukkitTask windupTask;
    BukkitTask activeTask;
    Spell instance;
    Enums.SpellType spellType;

    @SuppressWarnings("ConstantConditions")
    public Fiery_Rage(@NotNull Player player, @NotNull ItemStack item) {
        this.player = player;
        instance = this;
        spellType = Enums.SpellType.valueOf(item.getItemMeta().getPersistentDataContainer().get(SpellHandler.spellTypeKey, PersistentDataType.STRING));
        windup();
    }

    private void windup() {
        player.setGravity(false);
        playerDataUtil.setCoolDown(player, spellType, 1f, "windup");

        windupTask = new BukkitRunnable() {
            final int startRot = Math.round(player.getLocation().getYaw());
            int time = 0;

            @Override
            public void run() {
                Location location = player.getLocation();
                location.setYaw(startRot + time);
                player.teleport(location);

                //summon particles here

                if (time == 360) {
                    windupTask.cancel();
                    launchPlayer();
                    activate();
                }
                time += 18;
            }
        }.runTaskTimer(PluginMain.getInstance(), 0, 1);
    }

    private void launchPlayer() {
        Vector vel = player.getLocation().getDirection();

        double pitch0TO1 = (VectorConversion.getPitch(vel)+90)/180;
        vel = VectorConversion.setPitch(vel, (float) Math.cbrt(pitch0TO1)*180 - 90);

        vel.multiply(0.9 + Math.pow(pitch0TO1, 4));
        player.setGravity(true);
        player.setVelocity(vel);
    }

    private void activate() {
        playerDataUtil.setCoolDown(player, spellType, 10f, "active");
        playerDataUtil.addDmgMod(player, "spell", 1.5f);

        activeTask = new BukkitRunnable() {
            int time = 0;

            @Override
            public void run() {
                //summon particles here

                if (time == 200) {
                    playerDataUtil.removeDmgMod(player, "spell", 1.5f);
                    playerDataUtil.setCoolDown(player, spellType, 30f, "cooldown");

                    activeTask.cancel();
                    SpellHandler.activeSpells.get(player.getUniqueId()).remove(instance);
                }
                time++;
            }
        }.runTaskTimer(PluginMain.getInstance(), 0, 1);
    }

    @Override
    public void onUserDeath(Player killer) {
        playerDataUtil.setCoolDown(player, spellType, 30f, "cooldown");
        cancelSpell();
    }

    @Override
    public void onUserLeave() {
        CoolDownEntry entry = playerDataUtil.getCoolDownEntry(player, spellType);
        switch (entry.coolDownType) {
            case ("windup") -> playerDataUtil.setCoolDown(player, spellType, entry.timeInS+40f, "cooldown");
            case ("active") -> playerDataUtil.setCoolDown(player, spellType, entry.timeInS+30f, "cooldown");
        }
        cancelSpell();
    }

    @Override
    public void cancelSpell() {
        if (!windupTask.isCancelled()) {
            windupTask.cancel();
            player.setGravity(true);
            SpellHandler.activeSpells.get(player.getUniqueId()).remove(instance);
            return;
        }
        if (!activeTask.isCancelled()) {
            playerDataUtil.removeDmgMod(player, "spell", 1.5f);
            activeTask.cancel();
        }
        SpellHandler.activeSpells.get(player.getUniqueId()).remove(instance);
    }
}

package com.SpellBend.spell;

import com.SpellBend.PluginMain;
import com.SpellBend.organize.CoolDownEntry;
import com.SpellBend.organize.Enums;
import com.SpellBend.util.MathUtil;
import com.SpellBend.util.playerData.CoolDowns;
import com.SpellBend.util.playerData.DmgMods;
import com.SpellBend.util.VectorConversion;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
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
        CoolDowns.setCoolDown(player, spellType, 1f, "windup");

        windupTask = new BukkitRunnable() {
            final int startRot = Math.round(player.getLocation().getYaw());
            int time = 0;

            @Override
            public void run() {
                Location location = player.getLocation();
                location.setYaw(startRot + time);
                player.teleport(location);

                Particle.DustTransition dustOptions = new Particle.DustTransition(
                        Color.fromRGB((int) MathUtil.random(210d, 255d), (int) MathUtil.random(80d, 120d), (int) MathUtil.random(0d, 40d)),
                        Color.fromRGB((int) MathUtil.random(210d, 255d), (int) MathUtil.random(80d, 120d), (int) MathUtil.random(0d, 40d)),
                        (float) MathUtil.random(1.3d, 2d));
                player.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, player.getLocation().add(Math.cos((time*(1f/3f))*MathUtil.DEGTORAD+Math.PI*(1f/3f)),
                        time/180f, Math.sin((time*(1f/3f))*MathUtil.DEGTORAD+Math.PI*(1f/3f))), 1, dustOptions);
                player.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, player.getLocation().add(Math.cos((time*(1f/3f))*MathUtil.DEGTORAD+Math.PI),
                        time/180f, Math.sin((time*(1f/3f))*MathUtil.DEGTORAD+Math.PI)), 1, dustOptions);
                player.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, player.getLocation().add(Math.cos((time*(1f/3f))*MathUtil.DEGTORAD+Math.PI*(5f/3f)),
                        time/180f, Math.sin((time*(1f/3f))*MathUtil.DEGTORAD+Math.PI*(5f/3f))), 1, dustOptions);

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
        CoolDowns.setCoolDown(player, spellType, 10f, "active");
        DmgMods.addDmgMod(player, "spell", 1.5f);

        activeTask = new BukkitRunnable() {
            int time = 200;

            @Override
            public void run() {
                Particle.DustTransition dustOptions = new Particle.DustTransition(
                        Color.fromRGB((int) MathUtil.random(210d, 255d), (int) MathUtil.random(80d, 120d), (int) MathUtil.random(0d, 40d)),
                        Color.fromRGB((int) MathUtil.random(210d, 255d), (int) MathUtil.random(80d, 120d), (int) MathUtil.random(0d, 40d)),
                        (float) (0.2d + MathUtil.random(time/80d, 1d+time/80d)));
                player.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, player.getLocation().add(0d, 1d, 0d), 1, dustOptions);

                if (time == 0) {
                    DmgMods.removeDmgMod(player, "spell", 1.5f);
                    CoolDowns.setCoolDown(player, spellType, 30f, "cooldown");

                    activeTask.cancel();
                    SpellHandler.activeSpells.get(player.getUniqueId()).remove(instance);
                }
                time--;
            }
        }.runTaskTimer(PluginMain.getInstance(), 0, 1);
    }

    @Override
    public void onUserDeath(Player killer) {
        CoolDowns.setCoolDown(player, spellType, 30f, "cooldown");
        cancelSpell();
    }

    @Override
    public void onUserLeave() {
        CoolDownEntry entry = CoolDowns.getCoolDownEntry(player, spellType);
        switch (entry.coolDownType) {
            case ("windup") -> CoolDowns.setCoolDown(player, spellType, entry.timeInS+40f, "cooldown");
            case ("active") -> CoolDowns.setCoolDown(player, spellType, entry.timeInS+30f, "cooldown");
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
            DmgMods.removeDmgMod(player, "spell", 1.5f);
            activeTask.cancel();
        }
        SpellHandler.activeSpells.get(player.getUniqueId()).remove(instance);
    }
}

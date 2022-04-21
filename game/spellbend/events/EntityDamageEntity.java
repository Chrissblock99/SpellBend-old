package game.spellbend.events;

import game.spellbend.util.EventUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageEntity implements Listener {
    public EntityDamageEntity() {
        EventUtil.register(this);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) if (((Player) event.getDamager()).getGameMode() == GameMode.ADVENTURE) {
            event.setCancelled(true);
            return;
        }
        if (event.getEntity() instanceof Player) if (((Player) event.getEntity()).getGameMode() == GameMode.ADVENTURE) {
            event.setCancelled(true);
            return;
        }
        if (!(event.getEntity() instanceof Player) && !(event.getDamager() instanceof Player)) event.setCancelled(true);
    }
}

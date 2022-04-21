package game.spellbend.events;

import game.spellbend.util.EventUtil;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractEntity implements Listener {
    public PlayerInteractEntity() {
        EventUtil.register(this);
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.ADVENTURE) event.setCancelled(true);  //basically noInteract for normal players
    }
}

package game.spellbend.events;

import game.spellbend.spell.SpellHandler;
import game.spellbend.util.EventUtil;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractBlock implements Listener {
    public PlayerInteractBlock() {
        EventUtil.register(this);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.ADVENTURE)  {
            event.setCancelled(true);  //basically noInteract for normal players
            //spellHandling
            if (event.hasItem()) if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                //noinspection ConstantConditions
                if (SpellHandler.itemIsRegisteredSpell(event.getItem())) SpellHandler.activateSpell(event.getPlayer(), event.getItem());
            }
        }
    }
}

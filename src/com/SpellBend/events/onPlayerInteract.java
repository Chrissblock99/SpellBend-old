package com.SpellBend.events;

import com.SpellBend.spell.SpellHandler;
import com.SpellBend.util.EventUtil;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class onPlayerInteract implements Listener {
    public onPlayerInteract() {
        EventUtil.register(this);
    }

    @SuppressWarnings("MethodNameSameAsClassName")
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.ADVENTURE)  {
            event.setCancelled(true);  //basically noInteract for normal players
            //spellHandling
            if (event.hasItem()) if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                //noinspection ConstantConditions
                if (SpellHandler.itemIsSpell(event.getItem())) SpellHandler.activateSpell(event.getPlayer(), event.getItem());
            }
        }
    }
}

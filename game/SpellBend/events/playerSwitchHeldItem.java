package game.SpellBend.events;

import game.SpellBend.data.Enums;
import game.SpellBend.playerData.CoolDowns;
import game.SpellBend.util.EventUtil;
import game.SpellBend.util.playerDataBoard;
import game.SpellBend.util.dataUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class playerSwitchHeldItem implements Listener {
    public playerSwitchHeldItem() {
        EventUtil.register(this);
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        Enums.SpellType type = dataUtil.getSpellType(player.getInventory().getItem(event.getNewSlot()));
        if (type != null && CoolDowns.getCoolDown(player, type)[0]>0.1f)
            playerDataBoard.registerPlayer(player, type);
        else playerDataBoard.deRegisterPlayer(player, type);
    }
}

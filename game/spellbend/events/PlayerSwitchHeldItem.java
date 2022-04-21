package game.spellbend.events;

import game.spellbend.data.Enums;
import game.spellbend.playerdata.CoolDowns;
import game.spellbend.util.EventUtil;
import game.spellbend.util.PlayerDataBoard;
import game.spellbend.util.DataUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class PlayerSwitchHeldItem implements Listener {
    public PlayerSwitchHeldItem() {
        EventUtil.register(this);
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        Enums.SpellType type = DataUtil.getSpellType(player.getInventory().getItem(event.getNewSlot()));
        if (type != null && CoolDowns.getCoolDown(player, type)[0]>0.1f)
            PlayerDataBoard.registerPlayer(player, type);
        else PlayerDataBoard.deRegisterPlayer(player, type);
    }
}

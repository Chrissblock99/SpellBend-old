package game.SpellBend.events;

import game.SpellBend.util.EventUtil;
import game.SpellBend.playerData.playerDataUtil;
import game.SpellBend.util.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class playerChat implements Listener {
    public playerChat() {
        EventUtil.register(this);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        event.setFormat(playerDataUtil.constructDisplayString(player) + " » §f" + TextUtil.filterMessage(event.getMessage()));
    }
}

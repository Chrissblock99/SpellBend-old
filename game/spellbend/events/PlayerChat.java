package game.spellbend.events;

import game.spellbend.util.EventUtil;
import game.spellbend.playerdata.PlayerDataUtil;
import game.spellbend.util.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener {
    public PlayerChat() {
        EventUtil.register(this);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        event.setFormat(PlayerDataUtil.constructDisplayString(player) + " » §f" + TextUtil.filterMessage(event.getMessage()));
    }
}

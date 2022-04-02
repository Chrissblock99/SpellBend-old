package com.SpellBend.events;

import com.SpellBend.util.EventUtil;
import com.SpellBend.playerData.Gems;
import com.SpellBend.playerData.Gold;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class playerLevelChange implements Listener {
    public playerLevelChange() {
        EventUtil.register(this);
    }

    @EventHandler
    public void onPlayerLevelChange(PlayerLevelChangeEvent event) {
        Player player = event.getPlayer();
        int levelsChanged = event.getNewLevel()- event.getOldLevel();

        Gems.addGems(player, 15*levelsChanged);
        Gold.addGold(player, 50*levelsChanged);
        player.sendMessage( (levelsChanged==1) ? "\n§a§lLEVEL-UP §7» §e+50 Gold §8| §b+15 Gems\n§t" : "\n§a§l" + levelsChanged + " LEVEL-UPS §7» §e+" + 50*levelsChanged + " Gold §8| §b+" + 15*levelsChanged + " Gems\n§t");
    }
}

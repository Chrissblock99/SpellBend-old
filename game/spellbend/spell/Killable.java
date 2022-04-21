package game.spellbend.spell;

import org.bukkit.entity.Player;

public interface Killable {
    void onUserDeath(Player killer);
}

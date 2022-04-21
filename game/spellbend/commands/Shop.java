package game.spellbend.commands;

import game.spellbend.util.EventUtil;
import game.spellbend.gui.GUICreationUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class Shop implements Listener {
    public Shop() {
        EventUtil.register(this);
        new CommandBase("shop", true) {
            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) {
                Player player = (Player) sender;
                player.openInventory(GUICreationUtil.createShop(player));
                return true;
            }

            @Override
            public @NotNull String getUsage() {
                return "Please use \"/shop\", \"/spells\" or \"/spellbook\" without any extra arguments.";
            }
        }.enableDelay(10);
    }
}

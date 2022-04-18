package game.SpellBend.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class random {
    public random() {
        new CommandBase("random", true) {
            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) {
                ((Player) sender).setFoodLevel(20);
                return true;
            }

            @Override
            public @NotNull String getUsage() {
                return "Please use \"/random\" without any extra parameters.";
            }
        }.enableDelay(200); //this is 10 seconds rn I will need to modify this to be the animation time later
    }
}

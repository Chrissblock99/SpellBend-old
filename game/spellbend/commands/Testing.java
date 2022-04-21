package game.spellbend.commands;

import game.spellbend.data.Enums;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class Testing {
    public Testing() {
        HashMap<String, AdvancedSubCommand> subCommands = new HashMap<>();

        subCommands.put("test", new AdvancedSubCommand(new Class[]{String.class}) {
            @Override
            public boolean onCommand(CommandSender sender, ArrayList<Object> arguments) {
                sender.sendMessage("/testing results to: \"" + arguments.get(0) + "\"");
                return true;
            }
        });
        subCommands.put("test nr2", new AdvancedSubCommand(new Class[0]) {
            @Override
            public boolean onCommand(CommandSender sender, ArrayList<Object> arguments) {
                sender.sendMessage("/testing test nr2 has worked");
                return true;
            }
        });
        subCommands.put("other test", new AdvancedSubCommand(new Class[]{Player.class}) {
            @Override
            public boolean onCommand(CommandSender sender, ArrayList<Object> arguments) {
                sender.sendMessage("/testing other test results: \"" + ((Player) arguments.get(0)).getDisplayName() + "\"");
                return true;
            }
        });
        subCommands.put("many arguments", new AdvancedSubCommand(new Class[]{Integer.class, Enums.SpellType.class}) {
            @Override
            public boolean onCommand(CommandSender sender, ArrayList<Object> arguments) {
                sender.sendMessage("/testing many arguments results: \"" + ((Integer) arguments.get(0)).toString() + "\" \"" + arguments.get(1).toString() + "\"");
                return true;
            }
        });


        new AdvancedCommandBase("testing", "/testing (this is the Usage String)", subCommands){}.setPermission("SpellBend.testing");
    }
}

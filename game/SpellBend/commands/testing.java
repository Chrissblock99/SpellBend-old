package game.SpellBend.commands;

import game.SpellBend.data.Enums;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class testing {
    public testing() {
        HashMap<String, advancedSubCommand> subCommands = new HashMap<>();

        subCommands.put("test", new advancedSubCommand(new Class[]{String.class}) {
            @Override
            public boolean onCommand(CommandSender sender, ArrayList<Object> arguments) {
                sender.sendMessage("/testing results to: \"" + arguments.get(0) + "\"");
                return true;
            }
        });
        subCommands.put("test nr2", new advancedSubCommand(new Class[0]) {
            @Override
            public boolean onCommand(CommandSender sender, ArrayList<Object> arguments) {
                sender.sendMessage("/testing test nr2 has worked");
                return true;
            }
        });
        subCommands.put("other test", new advancedSubCommand(new Class[]{Player.class}) {
            @Override
            public boolean onCommand(CommandSender sender, ArrayList<Object> arguments) {
                sender.sendMessage("/testing other test results: \"" + ((Player) arguments.get(0)).getDisplayName() + "\"");
                return true;
            }
        });
        subCommands.put("many arguments", new advancedSubCommand(new Class[]{Integer.class, Enums.SpellType.class}) {
            @Override
            public boolean onCommand(CommandSender sender, ArrayList<Object> arguments) {
                sender.sendMessage("/testing many arguments results: \"" + ((Integer) arguments.get(0)).toString() + "\" \"" + arguments.get(1).toString() + "\"");
                return true;
            }
        });


        new advancedCommandBase("testing", "/testing (this is the Usage String)", subCommands){}.setPermission("SpellBend.testing");
    }
}

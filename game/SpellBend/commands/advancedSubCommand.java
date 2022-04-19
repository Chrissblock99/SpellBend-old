package game.SpellBend.commands;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;

@SuppressWarnings("rawtypes")
public abstract class advancedSubCommand {
    public final Class[] arguments;
    public final String argumentString;

    public advancedSubCommand(Class[] arguments) {
        this.arguments = arguments;
        StringBuilder stringBuilder = new StringBuilder();
        for (Class argument : arguments) {
            stringBuilder.append("<").append(argument.getName()).append("> ");
        }
        if (!stringBuilder.isEmpty()) stringBuilder.delete(stringBuilder.length()-1, stringBuilder.length());
        this.argumentString = stringBuilder.toString();
    }

    public advancedSubCommand(Class[] arguments, String[] argumentStrings) {
        this.arguments = arguments;
        StringBuilder stringBuilder = new StringBuilder();
        for (String argument : argumentStrings) {
            stringBuilder.append("<").append(argument).append("> ");
        }
        if (!stringBuilder.isEmpty()) stringBuilder.delete(stringBuilder.length()-1, stringBuilder.length());
        this.argumentString = stringBuilder.toString();
    }

    public abstract boolean onCommand(CommandSender sender, ArrayList<Object> arguments);
}
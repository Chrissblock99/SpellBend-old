package game.spellbend.commands;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;

@FunctionalInterface
public interface SubCommand {
  boolean onCommand(CommandSender sender, ArrayList<String> arguments);
}

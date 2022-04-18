package game.SpellBend.commands;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public interface subCommand {
  boolean onCommand(CommandSender sender, ArrayList<String> arguments);
}

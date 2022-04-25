package game.spellbend.commands;

import game.spellbend.organize.BadgeObj;
import game.spellbend.organize.RankObj;
import game.spellbend.util.DataUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class AdvancedCommandBase extends BukkitCommand implements CommandExecutor {
    public final HashMap<String, AdvancedSubCommand> subCommands;
    public final String command;
    public final String usage;
    public RankObj rankNeeded = null;
    public BadgeObj badgeNeeded = null;
    public int rankingNeeded = 1;

    public AdvancedCommandBase(@NotNull String command, @NotNull String usage, @NotNull HashMap<String, AdvancedSubCommand> subCommands) {
        super(command);
        this.subCommands = subCommands;
        this.command = command;
        this.usage = usage;

        CommandMap commandMap = getCommandMap();
        if (commandMap != null) {
            commandMap.register(command, this);
        }
    }

    public CommandMap getCommandMap() {
        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);

                return (CommandMap) field.get(Bukkit.getPluginManager());
            }
        } catch(NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public AdvancedCommandBase setRankNeeded(RankObj rank) {
        this.rankNeeded = rank;
        return this;
    }

    public AdvancedCommandBase setBadgeNeeded(BadgeObj badge) {
        this.badgeNeeded = badge;
        return this;
    }

    public AdvancedCommandBase setRankingNeeded(int ranking) {
        this.rankingNeeded = ranking;
        return this;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] arguments) {
        String noPermissionMsg = DataUtil.senderHasPermission(sender, rankNeeded, badgeNeeded, rankingNeeded);
        if (noPermissionMsg != null) {
            sender.sendMessage(noPermissionMsg);
            return true;
        }

        final ArrayList<AdvancedSubCommand> validSubCommands = new ArrayList<>();
        String path = "";
        String latestValidPath = "";
        String potentialErrorMessage = "§4No Error was found.";
        ArrayList<String> argumentList = new ArrayList<>(Arrays.asList(arguments));

        if (subCommands.containsKey(path)) validSubCommands.add(subCommands.get(path));
        for (String argument : arguments) {
            if (!path.equals("")) path = (path + " " + argument);
            else path = arguments[0];

            if (subCommands.containsKey(path)) {
                AdvancedSubCommand cmd = subCommands.get(path);
                if (((path.equals("")) ? (0) : (path.split(" ").length)) + cmd.arguments.length == argumentList.size()) {
                    latestValidPath = path;
                    validSubCommands.add(cmd);
                } else potentialErrorMessage = ("§4Wrong amount of arguments, this subCommand requires " + cmd.arguments.length + " arguments!");
            }
        }
        for (String string : latestValidPath.split(" ")) argumentList.remove(string);

        if (validSubCommands.size() == 0) {
            sender.sendMessage("§4Wrong command usage, no valid subCommand found! This is a potential error:");
            sender.sendMessage(potentialErrorMessage);

            if (potentialErrorMessage.equals("§4No Error was found.")) {
                sender.sendMessage("Valid subCommands are:");
                for (Map.Entry<String, AdvancedSubCommand> entry: subCommands.entrySet()) {
                    sender.sendMessage("/" + command + " " + entry.getKey() + " " + entry.getValue().argumentString);
                }
                return true;
            }

            sender.sendMessage(usage);
            return true;
        }

        AdvancedSubCommand subCommand = validSubCommands.get(validSubCommands.size()-1);
        ArrayList<Object> parsedArguments = new ArrayList<>();
        for (int i = 0;i<subCommand.arguments.length;i++) {
            String parseFrom = argumentList.get(i);
            //noinspection rawtypes
            Class parseTo = subCommand.arguments[i];

            //player
            if (parseTo == Player.class) {
                Player player = Bukkit.getPlayerExact(parseFrom);
                if (player != null) {
                    parsedArguments.add(player);
                    continue;
                }
                sender.sendMessage("§4Player " + parseFrom + " at subCommand argument " + (i+1) + " is offline/has not been found!");
                return true;
            }

            //int
            if (parseTo == Integer.class) {
                try {
                    Integer integer = Integer.valueOf(parseFrom);
                    parsedArguments.add(integer);
                    continue;
                } catch (NumberFormatException exception) {
                    sender.sendMessage("§4SubCommand argument " + (i+1) + " \"" + parseFrom + "\" is not an Integer!");
                    return true;
                }
            }

            //String
            if (parseTo == String.class) {
                parsedArguments.add(parseFrom);
                continue;
            }

            //enum
            if (parseTo.getSuperclass() == Enum.class) {
                try {
                    //noinspection rawtypes, unchecked
                    Enum type = Enum.valueOf(parseTo, parseFrom.toUpperCase());
                    parsedArguments.add(type);
                    continue;
                } catch (IllegalArgumentException exception) {
                    sender.sendMessage("§4SubCommand argument " + (i+1) + " \"" + parseFrom + "\" is not a " + parseTo.getName() + "!");
                    return true;
                } catch (NullPointerException exception) {
                    sender.sendMessage("§4A NullPointerException was thrown when parsing \"" + parseFrom + "\" to " + parseTo.getName() + "!");
                    return true;
                }
            }

            //double
            if (parseTo == Double.class) {
                try {
                    Double num = Double.valueOf(parseFrom);
                    parsedArguments.add(num);
                    continue;
                } catch (NumberFormatException exception) {
                    sender.sendMessage("§4SubCommand argument " + (i+1) + " \"" + parseFrom + "\" is not a Double!");
                    return true;
                }
            }

            //float
            if (parseTo == Float.class) {
                try {
                    Float num = Float.valueOf(parseFrom);
                    parsedArguments.add(num);
                    continue;
                } catch (NumberFormatException exception) {
                    sender.sendMessage("§4SubCommand argument " + (i+1) + " \"" + parseFrom + "\" is not a Float!");
                    return true;
                }
            }

            //boolean
            if (parseTo == Boolean.class) {
                Boolean bool = null;
                if (parseFrom.equals("true")) bool = true;
                if (parseFrom.equals("false")) bool = false;
                if (bool != null) {
                    parsedArguments.add(bool);
                    continue;
                } else {
                    sender.sendMessage("§4SubCommand argument " + (i+1) + " \"" + parseFrom + "\" is not a \"true\" or \"false\"!");
                    return true;
                }
            }

            //long
            if (parseTo == Long.class) {
                try {
                    Long num = Long.valueOf(parseFrom);
                    parsedArguments.add(num);
                    continue;
                } catch (NumberFormatException exception) {
                    sender.sendMessage("§4SubCommand argument " + (i+1) + " \"" + parseFrom + "\" is not a Long!");
                    return true;
                }
            }

            //short
            if (parseTo == Short.class) {
                try {
                    Short num = Short.valueOf(parseFrom);
                    parsedArguments.add(num);
                    continue;
                } catch (NumberFormatException exception) {
                    sender.sendMessage("§4SubCommand argument " + (i+1) + " \"" + parseFrom + "\" is not a Short!");
                    return true;
                }
            }

            //byte
            if (parseTo == Byte.class) {
                try {
                    Byte num = Byte.valueOf(parseFrom);
                    parsedArguments.add(num);
                    continue;
                } catch (NumberFormatException exception) {
                    sender.sendMessage("§4SubCommand argument " + (i+1) + " \"" + parseFrom + "\" is not a Byte!");
                    return true;
                }
            }

            sender.sendMessage("§4Wrong command usage, subCommand argument " + (i+1) + " is supposed to be of type " + parseTo.getName() + "!");
            sender.sendMessage("this is actually wrong as if the mention class isn't mentioned here it can be of that type but it just didn't parse it");
            return true;
        }

        return subCommand.onCommand(sender, parsedArguments);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] arguments) {
        return execute(sender, alias, arguments);
    }

    @Override
    public @NotNull String getUsage() {return usage;}
}

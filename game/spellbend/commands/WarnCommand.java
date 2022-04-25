package game.spellbend.commands;

import game.spellbend.data.Enums;
import game.spellbend.data.Lists;
import game.spellbend.moderation.*;
import game.spellbend.playerdata.Punishments;
import game.spellbend.util.TimeSpan;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class WarnCommand {
    public WarnCommand() {
        new CommandBase("warn", 2, -1) {
            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) {
                if (arguments[0].equals("list")) {
                    if (arguments.length == 2)
                        return listWarns(sender, arguments);
                    else {
                        sender.sendMessage("§c/warn list <player> can only take 2 arguments!");
                        return true;
                    }
                }
                if (arguments.length >= 4)
                    return giveWarn(sender, arguments);
                sender.sendMessage("§cNo subCommand found! valid subcommands:");
                sendUsage(sender);
                return true;
            }

            @Override
            public @NotNull String getUsage() {
                return "/warn <player> <timeInSeconds> <rule> <reason>\n" +
                        "/warn list <player>";
            }
        }.setRankingNeeded(Lists.getRankByName("helper").ranking);
    }

    static boolean listWarns(CommandSender sender, String[] arguments) {
        Player player = Bukkit.getPlayerExact(arguments[1]);
        if (player == null) {
            sender.sendMessage("§cPlayer \"" + arguments[1] + "\" is not online or doesn't exist!");
            return true;
        }

        ArrayList<Punishment> punishments = Punishments.getPunishments(player);
        sender.sendMessage("§c" + player.getDisplayName() + "'s Warns:");
        for (Punishment punishment : punishments)
            if (punishment instanceof Warn warn)
                sender.sendMessage(PunishmentsCommand.stringifyWarn(warn));
        if (punishments.size() == 0)
            sender.sendMessage("none");
        return true;
    }

    private static boolean giveWarn(CommandSender sender, String[] arguments) {
        Player player = Bukkit.getPlayerExact(arguments[0]);
        if (player == null) {
            sender.sendMessage("§cPlayer \"" + arguments[0] + "\" is not online or doesn't exist!");
            return true;
        }

        long timeInMS;
        try {
            timeInMS = Long.parseLong(arguments[1])*1000L;
        } catch (NumberFormatException nfe) {
            sender.sendMessage("§f\"" + arguments[1] + "\" is not a valid Number!");
            return true;
        }
        if (timeInMS <= 0) {
            sender.sendMessage("§fTime " + arguments[1] + " cannot be negative or 0!");
            return true;
        }

        Enums.Rule rule;
        try {
            rule = Enums.Rule.valueOf(arguments[2].toUpperCase());
        } catch (IllegalArgumentException iae) {
            sender.sendMessage("§f\"" + arguments[2] + "\" is not a known Rule! Known Rules:");
            for (Enums.Rule knownRule : Enums.Rule.values())
                sender.sendMessage("§f" + knownRule);
            return true;
        }

        ArrayList<String> restArgs = new ArrayList<>(Arrays.asList(arguments));
        restArgs.remove(0);
        restArgs.remove(0);
        restArgs.remove(0);
        String reason = String.join(" ", restArgs);


        Warn warn = new Warn(new TimeSpan(new Date(), new Date(new Date().getTime() + timeInMS)), rule, reason);
        Punishments.addPunishment(player, warn);
        if (!Punishments.hasPunishment(player, warn)) {
            sender.sendMessage("§cWarning failed! Adding the warn to " + arguments[0] + " did not add it??");
            return true;
        }
        player.sendMessage("§cYou have been warned for §e" + rule.toString().replace("_", " ").toLowerCase() + "§c with reason §e" + reason + "\n" +
                "The warn will stay for " + timeInMS/1000 + " seconds.");
        sender.sendMessage("§aSuccessfully warned §f" + arguments[0] + "§a for §f" + timeInMS/1000 + "§a seconds for §e" + arguments[2] + "§a with reason §e" + reason);
        return true;
    }

    /*private static boolean removeWarn(CommandSender sender, String[] arguments) {
        Player player = Bukkit.getPlayer(arguments[1]);
        if (player == null) {
            sender.sendMessage("§cPlayer \"" + arguments[1] + "\" is not online or doesn't exist!");
            return true;
        }

        long startTimeInMS;
        try {
            startTimeInMS = Long.parseLong(arguments[2])*1000L;
        } catch (NumberFormatException nfe) {
            sender.sendMessage("§f\"" + arguments[2] + "\" is not a valid Number!");
            return true;
        }

        long endTimeInMS;
        try {
            endTimeInMS = Long.parseLong(arguments[3])*1000L;
        } catch (NumberFormatException nfe) {
            sender.sendMessage("§f\"" + arguments[3] + "\" is not a valid Number!");
            return true;
        }

        Enums.Rule rule;
        try {
            rule = Enums.Rule.valueOf(arguments[4].toUpperCase());
        } catch (IllegalArgumentException iae) {
            sender.sendMessage("§f\"" + arguments[4] + "\" is not a known Rule! Known Rules:");
            for (Enums.Rule knownRule : Enums.Rule.values())
                sender.sendMessage("§f" + knownRule);
            return true;
        }

        ArrayList<String> restArgs = new ArrayList<>(Arrays.asList(arguments));
        restArgs.remove(0);
        restArgs.remove(0);
        restArgs.remove(0);
        restArgs.remove(0);
        restArgs.remove(0);
        String reason = String.join(" ", restArgs);


        Warn warn = new Warn(new TimeSpan(new Date(), new Date(new Date().getTime() + timeInMS)), rule, reason);
    }*/

}
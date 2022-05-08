package game.spellbend.commands;

import game.spellbend.data.Enums;
import game.spellbend.data.Lists;
import game.spellbend.moderation.*;
import game.spellbend.playerdata.Punishments;
import game.spellbend.organize.TimeSpan;
import game.spellbend.util.essentialscodecopy.DateUtil;
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

                if (arguments[0].equals("remove")) {
                    if (arguments.length == 3 || arguments.length == 4)
                        return removeWarn(sender, arguments);
                    else {
                        sender.sendMessage("§c/warn remove <player> <ID> [index] can only take 3 or 4 arguments!");
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
                return """
                        /warn <player> <time> <rule> <reason>
                        /warn list <player>
                        /warn remove <player> <ID> [index]""";
            }
        }.setRankingNeeded(Lists.getRankByName("helper").ranking);
    }

    public static boolean listWarns(CommandSender sender, String[] arguments) {
        Player player = Bukkit.getPlayerExact(arguments[1]);
        if (player == null) {
            sender.sendMessage("§cPlayer \"" + arguments[1] + "\" is not online or doesn't exist!");
            return true;
        }

        ArrayList<Punishment> punishments = Punishments.getPunishments(player);
        punishments.removeIf(p -> !(p instanceof Warn));
        sender.sendMessage("§c" + player.getDisplayName() + "'s Warns:");
        for (Punishment punishment : punishments)
            sender.sendMessage(PunishmentsCommand.stringifyWarn((Warn) punishment));
        if (punishments.size() == 0)
            sender.sendMessage("none");
        return true;
    }

    public static boolean giveWarn(CommandSender sender, String[] arguments) {
        Player player = Bukkit.getPlayerExact(arguments[0]);
        if (player == null) {
            sender.sendMessage("§cPlayer \"" + arguments[0] + "\" is not online or doesn't exist!");
            return true;
        }

        Date endDate;
        try {
            endDate = new Date(DateUtil.parseDateDiff(arguments[1], true));
        } catch (Exception e) {
            sender.sendMessage("§f\"" + arguments[1] + "\" is not a valid Time!");
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


        Warn warn = new Warn(new TimeSpan(new Date(), endDate), rule, reason);
        Punishments.addPunishment(player, warn);
        if (!Punishments.hasPunishment(player, warn)) {
            sender.sendMessage("§cWarning failed! Adding the warn to " + arguments[0] + " did not add it??");
            return true;
        }
        player.sendMessage("§cYou have been warned for §e" + rule.toString().replace("_", " ").toLowerCase() + "§c with reason §e" + reason + "\n" +
                "The warn will stay till " + endDate);
        sender.sendMessage("§aSuccessfully warned §f" + arguments[0] + "§a till §f" + endDate + "§a for §e" + arguments[2] + "§c ID: §f" + warn.hashCode() + "§a with reason §e" + reason);
        return true;
    }

    public static boolean removeWarn(CommandSender sender, String[] arguments) {
        Player player = Bukkit.getPlayerExact(arguments[1]);
        if (player == null) {
            sender.sendMessage("§cPlayer \"" + arguments[1] + "\" is not online or doesn't exist!");
            return true;
        }

        int ID;
        try {
            ID = Integer.parseInt(arguments[2]);
        } catch (NumberFormatException nfe) {
            sender.sendMessage(arguments[2] + " is not a valid number!");
            return true;
        }

        ArrayList<Punishment> warnList = new ArrayList<>();
        for (Punishment punishment : Punishments.getPunishment(player, ID))
            if (punishment instanceof Warn warn)
                warnList.add(warn);
        Punishment punishment = PunishmentsCommand.getPunishmentFromSameHashCodeList(warnList, arguments, sender);

        if (punishment == null)
            return true;

        Punishments.removePunishment(player, punishment);
        if (Punishments.getPunishments(player).contains(punishment)) {
            sender.sendMessage("§cRemoving the warn failed! Removing the warn from " + arguments[1] + " did not remove it??");
            return true;
        }
        sender.sendMessage("§aSuccessfully removed the warn from §f" + arguments[1]);
        return true;
    }
}
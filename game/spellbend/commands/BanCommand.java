package game.spellbend.commands;

import game.spellbend.data.Enums;
import game.spellbend.data.Lists;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class BanCommand {
    public BanCommand() {
        new CommandBase("ban", 2, -1) {
            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) {
                if (arguments[0].equals("list")) {
                    if (arguments.length == 2)
                        return listBans(sender, arguments);
                    else {
                        sender.sendMessage("§c/ban list <player> can only take 2 arguments!");
                        return true;
                    }
                }

                if (arguments[0].equals("remove")) {
                    if (arguments.length == 2)
                        return removeBan(sender, arguments);
                    else {
                        sender.sendMessage("§c/ban remove <player> can only take 2 arguments!");
                        return true;
                    }
                }

                if (arguments.length >= 4)
                    return giveBan(sender, arguments);
                sender.sendMessage("§cNo subCommand found! valid subcommands:");
                sendUsage(sender);
                return true;
            }

            @Override
            public @NotNull String getUsage() {
                return "/ban <player> <timeInSeconds> <rule> <reason>\n" +
                        "/ban list <player>";
            }
        }.setRankingNeeded(Lists.getRankByName("helper").ranking);
    }

    public static boolean listBans(CommandSender sender, String[] arguments) {
        BanEntry banEntry = Bukkit.getBanList(BanList.Type.NAME).getBanEntry(arguments[1]);
        if (banEntry == null) {
            sender.sendMessage(arguments[1] + " is not banned.");
            return true;
        }

        sender.sendMessage("§c" + arguments[1] + "'s Ban:");
        sender.sendMessage(PunishmentsCommand.stringifyBanEntry(banEntry));
        return true;
    }

    public static boolean giveBan(CommandSender sender, String[] arguments) {
        String player = arguments[0];

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

        Date endDate = new Date(new Date().getTime() + timeInMS);
        Bukkit.getBanList(BanList.Type.NAME).addBan(player, rule + " " + reason, endDate, sender.getName());
        Player playerToKick = Bukkit.getPlayerExact(player);
        if (playerToKick != null)
            playerToKick.kickPlayer("You have been banned for:\n" + rule + " " + reason + "\nYour ban will expire " + endDate);
        sender.sendMessage("§aBanned §f" + player + "§a for §f" + timeInMS/1000 + "§a seconds for §e" + arguments[2] + "§a with reason §e" + reason);
        return true;
    }

    public static boolean removeBan(CommandSender sender, String[] arguments) {
        String player = arguments[1];

        BanList banList = Bukkit.getBanList(BanList.Type.NAME);
        BanEntry banEntry = banList.getBanEntry(player);
        if (banEntry == null) {
            sender.sendMessage("§c" + player + " is not banned!");
            return true;
        }

        banList.pardon(player);
        sender.sendMessage("§aRemoved " + PunishmentsCommand.stringifyBanEntry(banEntry));
        return true;
    }
}
package game.spellbend.commands;

import game.spellbend.data.Enums;
import game.spellbend.data.Lists;
import game.spellbend.moderation.HoldMsgs;
import game.spellbend.moderation.Mute;
import game.spellbend.moderation.Punishment;
import game.spellbend.organize.TimeSpan;
import game.spellbend.playerdata.Punishments;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class MuteCommand {
    public MuteCommand() {
        new CommandBase("mute", 2, -1) {
            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) {
                if (arguments[0].equals("list")) {
                    if (arguments.length == 2)
                        return listMutes(sender, arguments);
                    else {
                        sender.sendMessage("§c/mute list <player> can only take 2 arguments!");
                        return true;
                    }
                }

                if (arguments[0].equals("remove")) {
                    if (arguments.length == 3 || arguments.length == 4)
                        return removeMute(sender, arguments);
                    else {
                        sender.sendMessage("§c/mute remove <player> <ID> [index] can only take 3 or 4 arguments!");
                        return true;
                    }
                }

                if (arguments.length >= 4)
                    return giveMute(sender, arguments);
                sender.sendMessage("§cNo subCommand found! valid subcommands:");
                sendUsage(sender);
                return true;
            }

            @Override
            public @NotNull String getUsage() {
                return """
                        /mute <player> <timeInSeconds> <rule> <reason>
                        /mute list <player>
                        /mute remove <player> <ID> [index]""";
            }
        }.setRankingNeeded(Lists.getRankByName("helper").ranking);
    }

    public static boolean listMutes(CommandSender sender, String[] arguments) {
        Player player = Bukkit.getPlayerExact(arguments[1]);
        if (player == null) {
            sender.sendMessage("§cPlayer \"" + arguments[1] + "\" is not online or doesn't exist!");
            return true;
        }

        ArrayList<Punishment> punishments = Punishments.getPunishments(player);
        punishments.removeIf(p -> !(p instanceof Mute));
        sender.sendMessage("§c" + player.getDisplayName() + "'s Mutes:");
        for (Punishment punishment : punishments)
            sender.sendMessage(PunishmentsCommand.stringifyMute((Mute) punishment));
        if (punishments.size() == 0)
            sender.sendMessage("none");
        return true;
    }

    public static boolean giveMute(CommandSender sender, String[] arguments) {
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


        Mute mute = new Mute(new TimeSpan(new Date(), new Date(new Date().getTime() + timeInMS)), rule, reason);
        Punishments.addPunishment(player, mute);
        if (!Punishments.hasPunishment(player, mute)) {
            sender.sendMessage("§cMuting failed! Adding the mute to " + arguments[0] + " did not add it??");
            return true;
        }
        player.sendMessage("§cYou have been muted for §e" + rule.toString().replace("_", " ").toLowerCase() + "§c with reason §e" + reason + "\n" +
                "The mute will stay for " + timeInMS/1000 + " seconds.");
        sender.sendMessage("§aSuccessfully muted §f" + arguments[0] + "§a for §f" + timeInMS/1000 + "§a seconds for §e" + arguments[2] + "§c ID: §f" + mute.hashCode() + "§a with reason §e" + reason);
        return true;
    }

    public static boolean removeMute(CommandSender sender, String[] arguments) {
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

        ArrayList<Punishment> muteList = new ArrayList<>();
        for (Punishment punishment : Punishments.getPunishment(player, ID))
            if (punishment instanceof Mute mute)
                muteList.add(mute);
        Punishment punishment = PunishmentsCommand.getPunishmentFromSameHashCodeList(muteList, arguments, sender);

        if (punishment == null)
            return true;

        Punishments.removePunishment(player, punishment);
        if (Punishments.getPunishments(player).contains(punishment)) {
            sender.sendMessage("§cRemoving the mute failed! Removing the mute from " + arguments[1] + " did not remove it??");
            return true;
        }
        sender.sendMessage("§aSuccessfully removed the mute from §f" + arguments[1]);
        return true;
    }
}
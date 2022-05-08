package game.spellbend.commands;

import game.spellbend.data.Enums;
import game.spellbend.data.Lists;
import game.spellbend.moderation.HoldMsgs;
import game.spellbend.moderation.Punishment;
import game.spellbend.playerdata.PlayerDataUtil;
import game.spellbend.playerdata.Punishments;
import game.spellbend.organize.TimeSpan;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import game.spellbend.util.essentialscodecopy.DateUtil;

import java.util.*;

public class HoldMsgsCommand {
    @Getter private static final HashMap<UUID, HashMap<Integer, String>> playerIDMessageMap = new HashMap<>();

    public HoldMsgsCommand() {
        new CommandBase("holdmsgs", 2, -1) {
            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) {
                if (arguments[0].equals("list")) {
                    if (arguments.length == 2)
                        return listHoldMsgs(sender, arguments);
                    else {
                        sender.sendMessage("§c/holdmsgs list <player> can only take 2 arguments!");
                        return true;
                    }
                }

                if (arguments[0].equals("remove")) {
                    if (arguments.length == 3 || arguments.length == 4)
                        return removeHoldMsgs(sender, arguments);
                    else {
                        sender.sendMessage("§c/holdmsgs remove <player> <ID> [index] can only take 3 or 4 arguments!");
                        return true;
                    }
                }

                if (arguments[0].equals("confirm")) {
                    if (arguments.length == 3)
                        return confirmHoldMsgs(sender, arguments);
                    else {
                        sender.sendMessage("§c/holdmsgs confirm <player> <ID> can only take 3 arguments!");
                        return true;
                    }
                }

                if (arguments.length >= 4)
                    return giveHoldMsgs(sender, arguments);
                sender.sendMessage("§cNo subCommand found! valid subcommands:");
                sendUsage(sender);
                return true;
            }

            @Override
            public @NotNull String getUsage() {
                return """
                        /holdmsgs <player> <time> <rule> <reason>
                        /holdmsgs list <player>
                        /holdmsgs confirm <player>
                        /holdmsgs remove <player> <ID> [index]""";
            }
        }.setRankingNeeded(Lists.getRankByName("helper").ranking);
    }

    public static boolean listHoldMsgs(CommandSender sender, String[] arguments) {
        Player player = Bukkit.getPlayerExact(arguments[1]);
        if (player == null) {
            sender.sendMessage("§cPlayer \"" + arguments[1] + "\" is not online or doesn't exist!");
            return true;
        }

        ArrayList<Punishment> punishments = Punishments.getPunishments(player);
        punishments.removeIf(p -> !(p instanceof HoldMsgs));
        sender.sendMessage("§c" + player.getDisplayName() + "'s HoldMsgs:");
        for (Punishment punishment : punishments)
            sender.sendMessage(PunishmentsCommand.stringifyHoldMsgs((HoldMsgs) punishment));
        if (punishments.size() == 0)
            sender.sendMessage("none");
        return true;
    }

    public static boolean giveHoldMsgs(CommandSender sender, String[] arguments) {
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

        UUID msgChecker = null;
        if (sender instanceof Player msgCheckerPlayer)
            msgChecker = msgCheckerPlayer.getUniqueId();

        HoldMsgs holdMsgs = new HoldMsgs(new TimeSpan(new Date(), endDate), msgChecker, rule, reason);
        Punishments.addPunishment(player, holdMsgs);
        if (!Punishments.hasPunishment(player, holdMsgs)) {
            sender.sendMessage("§cHoldMsg-ing failed! Adding the holdMsgs to " + arguments[0] + " did not add it??");
            return true;
        }
        player.sendMessage("§cYour messages will be put on control for §e" + rule.toString().replace("_", " ").toLowerCase() + "§c with reason §e" + reason + "\n" +
                "The holdMsgs will stay till " + endDate);
        sender.sendMessage("§aSuccessfully holdMsgs-ed §f" + arguments[0] + "§a till §f" + endDate + "§a for §e" + arguments[2] + "§c ID: §f" + holdMsgs.hashCode() + "§a with reason §e" + reason);
        return true;
    }

    public static boolean confirmHoldMsgs(CommandSender sender, String[] arguments) {
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

        UUID playerUUID = player.getUniqueId();
        if (!playerIDMessageMap.containsKey(playerUUID)) {
            sender.sendMessage("§cThis player has no messages to check!");
            return true;
        }
        HashMap<Integer, String> IDToMessageMap = playerIDMessageMap.get(playerUUID);
        if (!IDToMessageMap.containsKey(ID)) {
            sender.sendMessage("§c" + arguments[1] + " has no messages to check with ID: " + ID);
            return true;
        }
        String message = IDToMessageMap.get(ID);

        String constructedMessage = PlayerDataUtil.constructDisplayString(player) + " §8» §f" + message;
        for (Player p : player.getWorld().getPlayers())
            p.sendMessage(constructedMessage);
        Bukkit.getLogger().info(constructedMessage);

        IDToMessageMap.remove(ID);
        if (IDToMessageMap.isEmpty())
            playerIDMessageMap.remove(playerUUID);
        return true;
    }

    public static boolean removeHoldMsgs(CommandSender sender, String[] arguments) {
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

        ArrayList<Punishment> holdMsgsList = new ArrayList<>();
        for (Punishment punishment : Punishments.getPunishment(player, ID))
            if (punishment instanceof HoldMsgs holdMsgs)
                holdMsgsList.add(holdMsgs);
        Punishment punishment = PunishmentsCommand.getPunishmentFromSameHashCodeList(holdMsgsList, arguments, sender);

        if (punishment == null)
            return true;

        Punishments.removePunishment(player, punishment);
        if (Punishments.getPunishments(player).contains(punishment)) {
            sender.sendMessage("§cRemoving the holdMsgs failed! Removing the holdMsgs from " + arguments[1] + " did not remove it??");
            return true;
        }
        sender.sendMessage("§aSuccessfully removed the holdMsgs from §f" + arguments[1]);
        return true;
    }
}
package game.spellbend.commands;

import game.spellbend.data.Lists;
import game.spellbend.moderation.*;
import game.spellbend.playerdata.Punishments;
import game.spellbend.util.DataUtil;
import org.bukkit.BanEntry;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class PunishmentsCommand {
    public PunishmentsCommand() {
        new CommandBase("punishments", 0, 1) {
            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) {
                if (arguments.length == 1) {
                    String noPermissionMsg = DataUtil.senderHasPermission(sender, null, null, Lists.getRankByName("helper").ranking);
                    if (noPermissionMsg != null) {
                        sender.sendMessage(noPermissionMsg);
                        return true;
                    }

                    Player player = Bukkit.getPlayerExact(arguments[0]);
                    if (player == null) {
                        sender.sendMessage("§cPlayer \"" + arguments[0] + "\" is not online or doesn't exist!");
                        return true;
                    }
                    return listPunishments(sender, player);
                }

                if (sender instanceof ConsoleCommandSender) {
                    sender.sendMessage("§cOnly players can list their own Punishments!");
                    return true;
                }

                if (sender instanceof Player player)
                    return listPunishments(sender, player);

                sender.sendMessage("§cOnly Players can use this command!");
                return true;
            }

            @Override
            public @NotNull String getUsage() {
                return "/punishments";
            }
        }.enableDelay(100);
    }

    public static boolean listPunishments(CommandSender sender, Player player) {
        ArrayList<Punishment> punishments = Punishments.getPunishments(player);
        sender.sendMessage("§c" + player.getDisplayName() + "'s Punishments:");
        for (Punishment punishment : punishments)
            sender.sendMessage(stringifyPunishment(punishment));
        if (punishments.size() == 0)
            sender.sendMessage("none");
        return true;
    }

    public static @NotNull String stringifyWarn(@NotNull Warn warn) {
        return "§8from §f" + warn.getStartDate() + "§8 to §f" +
                warn.getEndDate() + "§8 for §f" +
                warn.getRule() + "§8 with reason §f" +
                warn.getReason() + "§c ID: §f" +
                warn.hashCode();
    }

    public static @NotNull String stringifyMute(@NotNull Mute mute) {
        return "§8from §f" + mute.getStartDate() + "§8 to §f" +
                mute.getEndDate() + "§8 for §f" +
                mute.getRule() + "§8 with reason §f" +
                mute.getReason() + "§c ID: §f" +
                mute.hashCode();
    }

    static @NotNull String stringifyBanEntry(BanEntry banEntry) {
        return banEntry.getTarget() + "§8 is banned since §f" +
                banEntry.getCreated() + "§8 till §f" +
                banEntry.getExpiration() + "§8 with reason §f" +
                banEntry.getReason() + "§8 by §f" +
                banEntry.getSource();
    }

    public static @NotNull String stringifyHoldMsgs(@NotNull HoldMsgs holdMsgs) {
        Player player = Bukkit.getPlayer(holdMsgs.getMsgChecker());
        String msgChecker = ((player == null) ? "Not online (UUID: " + holdMsgs.getMsgChecker().toString() + ")" : player.getDisplayName());
        return "§8from §f" + holdMsgs.getStartDate() + "§8 to §f" +
                holdMsgs.getEndDate() + "§8 for §f" +
                holdMsgs.getRule() + "§8 with reason §f" +
                holdMsgs.getReason() + "§8 msgChecker: §f" +
                msgChecker + "§c ID: §f" +
                holdMsgs.hashCode();
    }

    public static @NotNull String stringifyPunishment(@NotNull Punishment punishment) {
        if (punishment instanceof Warn warn)
            return "Warn: " + stringifyWarn(warn);
        if (punishment instanceof Mute mute)
            return "Mute: " + stringifyMute(mute);
        if (punishment instanceof HoldMsgs holdMsgs)
            return "HoldMsgs: " + stringifyHoldMsgs(holdMsgs);
        return "Unknown Punishment: " + punishment;
    }

    /**
     *
     * @param punishments The punishmentsList to get from
     * @param arguments String[] which may have a 4th argument to specify which punishment to choose if hashCodes collide
     * @param sender The CommandSender
     * @return The punishment if only 1 entry in list <br>
     * null if multiple, 0 entries or an error occurred <br>
     * in this case the sender is notified
     */
    public static @Nullable Punishment getPunishmentFromSameHashCodeList(ArrayList<Punishment> punishments, String[] arguments, CommandSender sender) {
        if (punishments.size() == 1)
            return punishments.get(0);

        if (punishments.size() == 0) {
            sender.sendMessage("§cThere is no Punishment with that ID!");
            return null;
        }

        if (arguments.length == 4) {
            int nth;
            try {
                nth = Integer.parseInt(arguments[3]);
            } catch (NumberFormatException nfe) {
                sender.sendMessage(arguments[3] + " is not a valid number!");
                return null;
            }

            if (nth >= 0 && nth < punishments.size())
                return punishments.get(nth);
            else {
                sender.sendMessage("§cThat index does not exist! Valid indexes:");
                for (int i = 0;i<punishments.size();i++)
                    sender.sendMessage("§c" + i + ": §f" + stringifyPunishment(punishments.get(i)));
                return null;
            }
            //this point of the if is unreachable as we return before
        }

        sender.sendMessage("§cThere are multiple Punishments with that ID! Please repeat the command with an index argument of the following:");
        for (int i = 0;i<punishments.size();i++)
            sender.sendMessage("§c" + i + ": §f" + stringifyPunishment(punishments.get(i)));
        return null;
    }
}
package game.spellbend.commands;

import game.spellbend.data.Lists;
import game.spellbend.moderation.*;
import game.spellbend.playerdata.Punishments;
import game.spellbend.util.DataUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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

    static boolean listPunishments(CommandSender sender, Player player) {
        ArrayList<Punishment> punishments = Punishments.getPunishments(player);
        sender.sendMessage("§c" + player.getDisplayName() + "'s Punishments:");
        for (Punishment punishment : punishments) {
            if (punishment instanceof Warn warn)
                sender.sendMessage("Warn: " + stringifyWarn(warn));
            else if (punishment instanceof Mute mute)
                sender.sendMessage("Mute: " + stringifyMute(mute));
            else if (punishment instanceof Ban ban)
                sender.sendMessage("Ban: " + stringifyBan(ban));
            else if (punishment instanceof HoldMsgs holdMsgs)
                sender.sendMessage("HoldMsgs: " + stringifyHoldMsgs(holdMsgs));
            else
                sender.sendMessage("Unknown Punishment: " + punishment);
        }
        if (punishments.size() == 0)
            sender.sendMessage("none");
        return true;
    }

    public static @NotNull String stringifyWarn(@NotNull Warn warn) {
        return "§8from §f" + warn.getStartDate() + "§8 to §f" +
                warn.getEndDate() + "§8 for §f" +
                warn.getRule() + "§8 with reason §f" +
                warn.getReason();
    }

    public static @NotNull String stringifyMute(@NotNull Mute mute) {
        return "§8from §f" + mute.getStartDate() + "§8 to §f" +
                mute.getEndDate() + "§8 for §f" +
                mute.getRule() + "§8 with reason §f" +
                mute.getReason();
    }

    public static @NotNull String stringifyBan(@NotNull Ban ban) {
        return "§8from §f" + ban.getStartDate() + "§8 to §f" +
                ban.getEndDate() + "§8 for §f" +
                ban.getRule() + "§8 with reason §f" +
                ban.getReason();
    }

    public static @NotNull String stringifyHoldMsgs(@NotNull HoldMsgs holdMsgs) {
        Player player = Bukkit.getPlayer(holdMsgs.getMsgChecker());
        String msgChecker = ((player == null) ? "Not online (UUID: " + holdMsgs.getMsgChecker().toString() + ")" : player.getDisplayName());
        return "§8from §f" + holdMsgs.getStartDate() + "§8 to §f" +
                holdMsgs.getEndDate() + "§8 for §f" +
                holdMsgs.getRule() + "§8 with reason §f" +
                holdMsgs.getReason() + "§8 msgChecker: §f" +
                msgChecker;
    }
}
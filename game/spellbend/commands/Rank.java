package game.spellbend.commands;

import game.spellbend.playerdata.Ranks;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Rank {
    private final HashMap<String, SubCommand> subCommands = new HashMap<>();

    public Rank() {
        subCommands.put("add", (sender, arguments) -> {
            if (arguments.size() != 2) {
                sender.sendMessage("§4Wrong command usage: /rank add <rank> <player>");
            }

            String rankName = arguments.get(0);
            String playerName = arguments.get(1);
            Player player = Bukkit.getPlayerExact(playerName);

            if (player == null) {
                sender.sendMessage("§4You may not add ranks to offline Players.");
                return true;
            }

            if (Ranks.hasRank(player, rankName)) {
                sender.sendMessage("§4" + playerName + " already has Rank \"" + rankName + "\"!");
                return true;
            }

            Ranks.addRank(player, rankName);
            if (!Ranks.hasRank(player, rankName)) {
                Bukkit.getLogger().warning("Something went wrong when adding Rank \"" + rankName + "\" to " + playerName + "!");
                sender.sendMessage("§4Something went wrong when adding Rank \"" + rankName + "\" to " + playerName + "!");
                return true;
            }
            sender.sendMessage("§aSuccessfully added Rank " + rankName + " to " + playerName + ".");
            return true;
        });
        subCommands.put("has", (sender, arguments) -> {
            if (arguments.size() != 2) {
                sender.sendMessage("§4Wrong command usage: /rank has <rank> <player>");
            }

            String rankName = arguments.get(0);
            String playerName = arguments.get(1);
            Player player = Bukkit.getPlayerExact(playerName);

            if (player == null) {
                sender.sendMessage("§4You may not ask for ranks of offline Players.");
                return true;
            }

            sender.sendMessage(playerName + ((Ranks.hasRank(player, rankName)) ? " §ahas" : " §4does not have") + " §fRank \"" + rankName + "\".");
            return true;
        });
        subCommands.put("remove", (sender, arguments) -> {
            if (arguments.size() != 2) {
                sender.sendMessage("§4Wrong command usage: /rank remove <rank> <player>");
            }

            String rankName = arguments.get(0);
            String playerName = arguments.get(1);
            Player player = Bukkit.getPlayerExact(playerName);

            if (player == null) {
                sender.sendMessage("§4You may not remove ranks from offline Players.");
                return true;
            }

            if (!Ranks.hasRank(player, rankName)) {
                sender.sendMessage("§4" + playerName + " doesn't have Rank \"" + rankName + "\"!");
                return true;
            }

            Ranks.removeRank(player, rankName);
            if (Ranks.hasRank(player, rankName)) {
                Bukkit.getLogger().warning("Something went wrong when removing Rank \"" + rankName + "\" from " + playerName + "!");
                sender.sendMessage("§4Something went wrong when removing Rank \"" + rankName + "\" from " + playerName + "!");
                return true;
            }
            sender.sendMessage("§aSuccessfully removed Rank " + rankName + " from " + playerName + ".");
            return true;
        });
        subCommands.put("list", (sender, arguments) -> {
            if (arguments.size() != 1) {
                sender.sendMessage("§4Wrong command usage: /rank list <player>");
            }

            String playerName = arguments.get(0);
            Player player = Bukkit.getPlayerExact(playerName);

            if (player == null) {
                sender.sendMessage("§4You may not list ranks of offline Players.");
                return true;
            }

            sender.sendMessage(String.join(", ", Ranks.getRanks(player)));
            return true;
        });

        new CommandBase("rank", 2, 3) {
            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) {
                if (!subCommands.containsKey(arguments[0])) {
                    sender.sendMessage("§4Wrong command usage, please use add|remove|has|list as 2nd statement.");
                    return true;
                }
                ArrayList<String> argumentList = new ArrayList<>(Arrays.asList(arguments));
                argumentList.remove(0);
                return subCommands.get(arguments[0]).onCommand(sender, argumentList);
            }

            @Override
            public @NotNull String getUsage() {
                return "/rank <add|remove|has> <rank> <player> or /rank list <player>";
            }
        }.setPermission("SpellBend.rank");
    }
}

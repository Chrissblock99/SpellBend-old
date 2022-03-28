package com.SpellBend.commands;

import com.SpellBend.util.playerDataUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class badge {
    private HashMap<String, subCommand> subCommands = new HashMap<>();

    public badge() {
        subCommands.put("add", (sender, arguments) -> {
            if (arguments.size() != 2) {
                sender.sendMessage("§4Wrong command usage: /badge add <badge> <player>");
            }

            String badgeName = arguments.get(0);
            String playerName = arguments.get(1);
            Player player = Bukkit.getPlayerExact(playerName);

            if (player == null) {
                sender.sendMessage("§4You may not add badges to offline Players.");
                return true;
            }

            if (playerDataUtil.hasBadge(player, badgeName)) {
                sender.sendMessage("§4" + playerName + " already has Badge \"" + badgeName + "\"!");
                return true;
            }

            playerDataUtil.addBadge(player, badgeName);
            if (!playerDataUtil.hasBadge(player, badgeName)) {
                Bukkit.getLogger().warning("Something went wrong when adding Badge \"" + badgeName + "\" to " + playerName + "!");
                sender.sendMessage("§4Something went wrong when adding Badge \"" + badgeName + "\" to " + playerName + "!");
                return true;
            }
            sender.sendMessage("§aSuccessfully added Badge " + badgeName + " to " + playerName + ".");
            return true;
        });
        subCommands.put("has", (sender, arguments) -> {
            if (arguments.size() != 2) {
                sender.sendMessage("§4Wrong command usage: /badge has <badge> <player>");
            }

            String badgeName = arguments.get(0);
            String playerName = arguments.get(1);
            Player player = Bukkit.getPlayerExact(playerName);

            if (player == null) {
                sender.sendMessage("§4You may not ask for badges of offline Players.");
                return true;
            }

            sender.sendMessage(playerName + ((playerDataUtil.hasBadge(player, badgeName)) ? " §ahas" : " §4does not have") + " §fBadge \"" + badgeName + "\".");
            return true;
        });
        subCommands.put("remove", (sender, arguments) -> {
            if (arguments.size() != 2) {
                sender.sendMessage("§4Wrong command usage: /badge remove <badge> <player>");
            }

            String badgeName = arguments.get(0);
            String playerName = arguments.get(1);
            Player player = Bukkit.getPlayerExact(playerName);

            if (player == null) {
                sender.sendMessage("§4You may not remove badges from offline Players.");
                return true;
            }

            if (!playerDataUtil.hasBadge(player, badgeName)) {
                sender.sendMessage("§4" + playerName + " doesn't have Badge \"" + badgeName + "\"!");
                return true;
            }

            playerDataUtil.removeBadge(player, badgeName);
            if (playerDataUtil.hasBadge(player, badgeName)) {
                Bukkit.getLogger().warning("Something went wrong when removing Badge \"" + badgeName + "\" from " + playerName + "!");
                sender.sendMessage("§4Something went wrong when removing Badge \"" + badgeName + "\" from " + playerName + "!");
                return true;
            }
              sender.sendMessage("§aSuccessfully removed Badge " + badgeName + " from " + playerName + ".");
              return true;
        });
        subCommands.put("list", (sender, arguments) -> {
            if (arguments.size() != 1) {
              sender.sendMessage("§4Wrong command usage: /badge list <player>");
            }

            String playerName = arguments.get(0);
            Player player = Bukkit.getPlayerExact(playerName);

            if (player == null) {
              sender.sendMessage("§4You may not list badges of offline Players.");
              return true;
            }

            sender.sendMessage(playerDataUtil.getBadgesString(player));
            return true;
        });

        new CommandBase("badge", 2, 3) {
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
                return "/badge <add|remove|has> <badge> <player> or /badge list <player>";
            }
        }.setPermission("SpellBend.badge");
    }
}

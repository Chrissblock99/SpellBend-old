package com.SpellBend.commands;

import com.SpellBend.util.playerData.Nick;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class nick {
    public nick() {
        new CommandBase("nick", 1, 2) {
            @Override
            public boolean onCommand(CommandSender sender, String[] arguments){
                if (arguments.length == 1) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage("§4Wrong command usage: /nick <player> <name>!");
                        return true;
                    }

                    Player player = (Player) sender;
                    String name = arguments[0];

                    Nick.setNick(player, name);
                    if (!Nick.getNick(player).equals(name)) {
                        Bukkit.getLogger().warning("Something went wrong when setting " + player.getDisplayName() + "'s nick to " + name + "!");
                        player.sendMessage("§4Something went wrong when setting your nick to " + name + ".");
                        return true;
                    }
                    player.sendMessage("§aSuccessfully set your nick to " + name);
                } else {
                    Player player = Bukkit.getPlayerExact(arguments[0]);
                    String name = arguments[1];

                    if (player == null) {
                        sender.sendMessage("§4You may not nick offline players.");
                        return true;
                    }

                    Nick.setNick(player, name);
                    if (!Nick.getNick(player).equals(name)) {
                        Bukkit.getLogger().warning("Something went wrong when setting " + player.getDisplayName() + "'s nick to " + name + "!");
                        sender.sendMessage("§4Something went wrong when setting " + player.getDisplayName() + "'s nick to " + name + ".");
                        return true;
                    }
                    sender.sendMessage("§aSuccessfully set " + player.getDisplayName() + "'s nick to " + name);
                }
                return true;
            }

            @Override
            public @NotNull String getUsage() {
                return "/nick <name> or /nick <player> <name>";
            }
        }.setPermission("SpellBend.nick");
    }
}

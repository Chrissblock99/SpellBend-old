package game.SpellBend.commands;

import game.SpellBend.PluginMain;
import game.SpellBend.organize.BadgeObj;
import game.SpellBend.organize.RankObj;
import game.SpellBend.playerData.Badges;
import game.SpellBend.playerData.Ranks;
import game.SpellBend.playerData.playerDataUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class CommandBase extends BukkitCommand implements CommandExecutor {
    private List<String> delayedPlayers = null;
    private long delay = 0;
    private final int minArguments;
    private final int maxArguments;
    private final boolean playerOnly;
    public RankObj rankNeeded = null;
    public BadgeObj badgeNeeded = null;
    public int rankingNeeded = 1;

    public CommandBase(String command) {this(command, 0);}
    public CommandBase(String command, boolean playerOnly) {this(command, 0, playerOnly);}
    public CommandBase(String command, int requiredArguments) {this(command, requiredArguments, requiredArguments);}
    public CommandBase(String command, int minArguments, int maxArguments) {this(command, minArguments, maxArguments, false);}
    public CommandBase(String command, int requiredArguments, boolean playerOnly) {this(command, requiredArguments, requiredArguments, playerOnly);}

    public CommandBase(String command, int minArguments, int maxArguments, boolean playerOnly) {
        super(command);

        this.minArguments = minArguments;
        this.maxArguments = maxArguments;
        this.playerOnly = playerOnly;

        CommandMap commandMap = getCommandMap();
        if (commandMap != null) {
            commandMap.register(command, this);
        }
    }

    public CommandMap getCommandMap() {
        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);

                return (CommandMap) field.get(Bukkit.getPluginManager());
            }
        } catch(NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public CommandBase enableDelay(int delay) {
        this.delay = delay;
        this.delayedPlayers = new ArrayList<>();
        return this;
    }

    public CommandBase setRankNeeded(RankObj rank) {
        this.rankNeeded = rank;
        return this;
    }

    public CommandBase setBadgeNeeded(BadgeObj badge) {
        this.badgeNeeded = badge;
        return this;
    }

    public CommandBase setRankingNeeded(int ranking) {
        this.rankingNeeded = ranking;
        return this;
    }

    public void removeDelay(@NotNull Player player) {
        this.delayedPlayers.remove(player.getName());
    }

    public void sendUsage(@NotNull CommandSender sender) {
        sender.sendMessage(getUsage());
    }

    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, String @NotNull [] arguments) {
        if (arguments.length < minArguments || (arguments.length > maxArguments && maxArguments != -1)) {
            sender.sendMessage("§4Incorrect amount of arguments, " + minArguments + " to " + maxArguments + " required!");
            sendUsage(sender);
            return true;
        }

        if (playerOnly && !(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        if (sender instanceof Player) {
            String ErrorMsg = "§cThere is no permission to gain access to this command!";
            boolean shallReturn = true;

            String permission = getPermission();
            if (permission != null) {
                if (sender.hasPermission(permission)) shallReturn = false;
                else ErrorMsg = "§cYou do not have permission to use this command. " + permission;
            }

            if (rankNeeded != null) {
                if (Ranks.hasRank(((Player) sender), rankNeeded.rankName)) shallReturn = false;
                else ErrorMsg = "§cYou do not have the required Rank to use this command! " + rankNeeded.rankName;
            }

            if (badgeNeeded != null) {
                if (Badges.hasBadge(((Player) sender), badgeNeeded.badgeName)) shallReturn = false;
                else ErrorMsg = "§cYou do not have the required Badge to use this command! " + badgeNeeded.badgeName;
            }

            if (rankingNeeded != -1) {
                if (playerDataUtil.getRanking(((Player) sender))>=rankingNeeded) shallReturn = false;
                else ErrorMsg = "§cYou do not have the required Ranking to use this command! " + rankingNeeded;
            }

            if (shallReturn) {
                sender.sendMessage(ErrorMsg);
                return true;
            }
        } else if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage("§4Only Players or the Console can use this command!");
            return true;
        }

        if (delayedPlayers != null && sender instanceof Player) {
            Player player = (Player) sender;
            if (delayedPlayers.contains(player.getName())) {
                player.sendMessage("§cPlease wait " + " before using this command again.");
                return true;
            }

            //this can be done more efficiently by using a time variable
            //and checking for the time to be outdated on next call
            //instead of using a countdown
            //this would also require a removal from list on leave for efficiency reasons
            delayedPlayers.add(player.getName());
            Bukkit.getScheduler().scheduleSyncDelayedTask(PluginMain.getInstance(), () -> delayedPlayers.remove(player.getName()), delay);
        }

        if (!onCommand(sender, arguments)) {
            sendUsage(sender);
        }

        return true;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] arguments) {
        return this.onCommand(sender, arguments);
    }

    public abstract boolean onCommand(CommandSender sender, String[] arguments);

    public abstract @NotNull String getUsage();
}

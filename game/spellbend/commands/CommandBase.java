package game.spellbend.commands;

import game.spellbend.PluginMain;
import game.spellbend.organize.BadgeObj;
import game.spellbend.organize.RankObj;
import game.spellbend.util.DataUtil;
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
    private long delayInTicks = 0;
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

    public CommandBase enableDelay(int delayInTicks) {
        this.delayInTicks = delayInTicks;
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

        String noPermissionMsg = DataUtil.senderHasPermission(sender, rankNeeded, badgeNeeded, rankingNeeded);
        if (noPermissionMsg != null) {
            sender.sendMessage(noPermissionMsg);
            return true;
        }

        if (delayedPlayers != null && sender instanceof Player player) {
            if (delayedPlayers.contains(player.getName())) {
                player.sendMessage("§cPlease wait " + " before using this command again.");
                return true;
            }

            //this can be done more efficiently by using a time variable
            //and checking for the time to be outdated on next call
            //instead of using a countdown
            //this would also require a removal from list on leave for efficiency reasons
            delayedPlayers.add(player.getName());
            Bukkit.getScheduler().scheduleSyncDelayedTask(PluginMain.getInstance(), () -> delayedPlayers.remove(player.getName()), delayInTicks);
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

package game.spellbend.playerdata;

import game.spellbend.data.Enums;
import game.spellbend.data.PersistentDataKeys;
import game.spellbend.moderation.*;
import game.spellbend.util.TextUtil;
import game.spellbend.organize.TimeSpan;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Punishments {
    public static @NotNull String stringifyWarn(@NotNull Warn warn) {
        return warn.getStartDate().getTime() + "; " +
                warn.getEndDate().getTime() + "; " +
                warn.getRule() + "; " +
                TextUtil.removeStrings(warn.getReason(), new String[]{", ", ": ", "; "});
    }

    public static @Nullable Warn parseWarn(@NotNull String warn) {
        String[] arguments = warn.split("; ");
        if (arguments.length != 4) throw new IllegalStateException("Arguments length to construct warn Object is not 4!");
        try {
            return new Warn(new TimeSpan(new Date(Long.parseLong(arguments[0])), new Date(Long.parseLong(arguments[1]))), Enums.Rule.valueOf(arguments[2]), arguments[3]);
        } catch (NumberFormatException exception) {
            Bukkit.getLogger().warning("\"" + arguments[0] + "\" is supposed to be a Long but isn't! or\n" +
                    "\"" + arguments[0] + "is supposed to be a Long but isn't!");
            exception.printStackTrace();
            return null;
        } catch (IllegalArgumentException exception) {
            Bukkit.getLogger().warning("\"" + arguments[2] + "\" is supposed to be a Rule enum but isn't! or\n" +
                    "\"" + arguments[0] + "; " + arguments[1] + "\" is not a valid TimeSpan because startDate is after endDate!");
            exception.printStackTrace();
            return null;
        }
    }

    public static @NotNull String stringifyMute(@NotNull Mute mute) {
        return mute.getStartDate().getTime() + "; " +
                mute.getEndDate().getTime() + "; " +
                mute.getRule() + "; " +
                TextUtil.removeStrings(mute.getReason(), new String[]{", ", ": ", "; "});
    }

    public static @Nullable Mute parseMute(@NotNull String mute) {
        String[] arguments = mute.split("; ");
        if (arguments.length != 4) throw new IllegalStateException("Arguments length to construct Mute Object is not 4!");
        try {
            return new Mute(new TimeSpan(new Date(Long.parseLong(arguments[0])), new Date(Long.parseLong(arguments[1]))), Enums.Rule.valueOf(arguments[2]), arguments[3]);
        } catch (NumberFormatException exception) {
            Bukkit.getLogger().warning("\"" + arguments[0] + "\" is supposed to be a Long but isn't! or\n" +
                    "\"" + arguments[0] + "is supposed to be a Long but isn't!");
            exception.printStackTrace();
            return null;
        } catch (IllegalArgumentException exception) {
            Bukkit.getLogger().warning("\"" + arguments[2] + "\" is supposed to be a Rule enum but isn't! or\n" +
                    "\"" + arguments[0] + "; " + arguments[1] + "\" is not a valid TimeSpan because startDate is after endDate!");
            exception.printStackTrace();
            return null;
        }
    }

    public static @NotNull String stringifyHoldMsgs(@NotNull HoldMsgs holdMsgs) {
        return holdMsgs.getStartDate().getTime() + "; " +
                holdMsgs.getEndDate().getTime() + "; " +
                holdMsgs.getMsgChecker() + "; " +
                holdMsgs.getRule() + "; " +
                TextUtil.removeStrings(holdMsgs.getReason(), new String[]{", ", ": ", "; "});
    }

    public static @Nullable HoldMsgs parseHoldMsgs(@NotNull String holdMsgs) {
        String[] arguments = holdMsgs.split("; ");
        if (arguments.length != 5) throw new IllegalStateException("Arguments length to construct warn Object is not 5!");
        try {
            if (arguments[2].equals("null"))
                return new HoldMsgs(new TimeSpan(new Date(Long.parseLong(arguments[0])), new Date(Long.parseLong(arguments[1]))),
                        null, Enums.Rule.valueOf(arguments[3]), arguments[4]);
            return new HoldMsgs(new TimeSpan(new Date(Long.parseLong(arguments[0])), new Date(Long.parseLong(arguments[1]))),
                    UUID.fromString(arguments[2]), Enums.Rule.valueOf(arguments[3]), arguments[4]);
        } catch (NumberFormatException exception) {
            Bukkit.getLogger().warning("\"" + arguments[0] + "\" is supposed to be a Long but isn't! or\n" +
                    "\"" + arguments[0] + "is supposed to be a Long but isn't!");
            exception.printStackTrace();
            return null;
        } catch (IllegalArgumentException exception) {
            Bukkit.getLogger().warning("\"" + arguments[3] + "\" is supposed to be a Rule enum but isn't! or\n" +
                    "\"" + arguments[2] + "\" is supposed to be a UUID but isn't! or\n" +
                    "\"" + arguments[0] + "; " + arguments[1] + "\" is not a valid TimeSpan because startDate is after endDate!");
            exception.printStackTrace();
            return null;
        }
    }

    public static @NotNull String stringifyPunishments(@NotNull ArrayList<Punishment> punishments) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Punishment punishment : punishments) {
            stringBuilder.append(", ");

            if (punishment instanceof Warn warn)
                stringBuilder.append("Warn: ").append(stringifyWarn(warn));
            else if (punishment instanceof Mute mute)
                stringBuilder.append("Mute: ").append(stringifyMute(mute));
            else if (punishment instanceof HoldMsgs holdMsgs)
                stringBuilder.append("HoldMsgs: ").append(stringifyHoldMsgs(holdMsgs));
            else {
                Bukkit.getLogger().warning(punishment + " is not a known Punishment, discarding it in stringification!");
                stringBuilder.replace(stringBuilder.length()-2, stringBuilder.length(), "");
            }
        }

        if (stringBuilder.length() >= 2)
            stringBuilder.replace(0, 2, "");
        return stringBuilder.toString();
    }

    public static @NotNull ArrayList<Punishment> parsePunishments(@NotNull String punishments) {
        ArrayList<String> punishmentsList = new ArrayList<>(Arrays.asList(punishments.split(", ")));
        punishmentsList.remove("");
        ArrayList<Punishment> parsedPunishmentsList = new ArrayList<>();

        for (String punishmentString : punishmentsList) {
            String[] parts = punishmentString.split(": ");
            switch (parts[0]) {
                case "Warn" -> parsedPunishmentsList.add(parseWarn(parts[1]));
                case "Mute" -> parsedPunishmentsList.add(parseMute(parts[1]));
                case "HoldMsgs" -> parsedPunishmentsList.add(parseHoldMsgs(parts[1]));
                default -> Bukkit.getLogger().warning("\"" + parts[1] + "\" is not a known Punishment!");
            }
            if (parsedPunishmentsList.remove(null))
                Bukkit.getLogger().warning("Failed to parse Punishment \"" + parts[1] + "\" discarding it!");
        }

        return parsedPunishmentsList;
    }

    public static void loadPunishments(@NotNull Player player) {
        if (!player.isOnline()) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is not online when trying to load Punishments, skipping loading!");
            return;
        }
        if (PersistentPlayerSessionStorage.punishments.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " is already loaded when loading Punishments, skipping loading!");
            return;
        }

        PersistentDataContainer data = player.getPersistentDataContainer();
        String punishments = data.get(PersistentDataKeys.punishmentsKey, PersistentDataType.STRING);

        if (punishments == null) {
            Bukkit.getLogger().warning(player.getDisplayName() + " did not have Punishments set up, setting Punishments to empty ArrayList!");
            data.set(PersistentDataKeys.punishmentsKey, PersistentDataType.STRING, "");
            PersistentPlayerSessionStorage.punishments.put(player.getUniqueId(), new ArrayList<>());
            return;
        }

        PersistentPlayerSessionStorage.punishments.put(player.getUniqueId(), parsePunishments(punishments));
    }

    public static void addPunishment(@NotNull Player player, @NotNull Punishment punishment) {
        if (!PersistentPlayerSessionStorage.punishments.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToPunishments map, now fixing!");
            loadPunishments(player);
        }
        PersistentPlayerSessionStorage.punishments.get(player.getUniqueId()).add(punishment);
        Bukkit.getLogger().info("§bAdded " + punishment + " to " + player.getDisplayName() + ".");
    }

    public static ArrayList<Punishment> getPunishment(@NotNull Player player, int hashCode) {
        ArrayList<Punishment> punishments = new ArrayList<>(1);
        for ( Punishment punishment : getPunishments(player)) //removing all run out Punishments and handling player not logged case
            if (punishment.hashCode() == hashCode)
                punishments.add(punishment);
        return punishments;
    }

    public static ArrayList<Punishment> getPunishments(@NotNull Player player) {
        if (!PersistentPlayerSessionStorage.punishments.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToPunishments map, now fixing!");
            loadPunishments(player);
        }
        ArrayList<Punishment> punishments = PersistentPlayerSessionStorage.punishments.get(player.getUniqueId());
        for (int i = punishments.size()-1;i>=0;i--) {
            Punishment punishment = punishments.get(i);
            if (punishment.getTime().getRemainingTimeInS() <= 0) {
                Bukkit.getLogger().info("§bThe time of " + player.getDisplayName() + "'s punishment ran out, removing it!\n" + punishment);
                punishments.remove(punishment);
            }
        }
        return punishments;
    }

    @SuppressWarnings("rawtypes")
    public static boolean playerHasPunishmentType(@NotNull Player player, @NotNull Class type) {
        for (Punishment punishment : getPunishments(player))
            if (type.isInstance(punishment))
                return true;
        return false;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean hasPunishment(@NotNull Player player, @NotNull Punishment punishment) {
        return getPunishments(player).contains(punishment); //removing all run out Punishments and handling player not logged case
    }

    public static void removePunishment(@NotNull Player player, @NotNull Punishment punishment) {
        if (!PersistentPlayerSessionStorage.punishments.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToPunishments map, now fixing!");
            loadPunishments(player);
        }
        Bukkit.getLogger().info("§bRemoving " + player.getDisplayName() + "'s punishment because the method for that has been called!\n" + punishment);
        if (PersistentPlayerSessionStorage.punishments.get(player.getUniqueId()).remove(punishment))
            Bukkit.getLogger().info("§bRemoved.");
        else Bukkit.getLogger().warning("The Object was not contained in the players list!");
    }

    public static void clearPunishments(@NotNull Player player) {
        if (!PersistentPlayerSessionStorage.punishments.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToPunishments map, now fixing!");
            loadPunishments(player);
        }
        Bukkit.getLogger().info("§bClearing " + player.getDisplayName() + "'s punishments because method for that has been called!");
        for (Punishment punishment : PersistentPlayerSessionStorage.punishments.get(player.getUniqueId()))
            Bukkit.getLogger().info("§b" + punishment);
        PersistentPlayerSessionStorage.punishments.get(player.getUniqueId()).clear();
    }

    public static void savePunishments(@NotNull Player player) {
        if (!PersistentPlayerSessionStorage.punishments.containsKey(player.getUniqueId())) {
            Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToPunishments map when saving, skipping saving!");
            return;
        }

        player.getPersistentDataContainer().set(PersistentDataKeys.punishmentsKey, PersistentDataType.STRING,
                stringifyPunishments(PersistentPlayerSessionStorage.punishments.get(player.getUniqueId())));
        PersistentPlayerSessionStorage.punishments.remove(player.getUniqueId());
    }
}

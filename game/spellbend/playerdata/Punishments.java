package game.spellbend.playerdata;

import game.spellbend.data.Enums;
import game.spellbend.data.PersistentDataKeys;
import game.spellbend.moderation.*;
import game.spellbend.util.TextUtil;
import game.spellbend.util.TimeSpan;
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
            return null;
        } catch (IllegalArgumentException exception) {
            Bukkit.getLogger().warning("\"" + arguments[2] + "\" is supposed to be a Rule enum but isn't!");
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
            return null;
        } catch (IllegalArgumentException exception) {
            Bukkit.getLogger().warning("\"" + arguments[2] + "\" is supposed to be a Rule enum but isn't!");
            return null;
        }
    }

    public static @NotNull String stringifyBan(@NotNull Ban ban) {
        return ban.getStartDate().getTime() + "; " +
                ban.getEndDate().getTime() + "; " +
                ban.getRule() + "; " +
                TextUtil.removeStrings(ban.getReason(), new String[]{", ", ": ", "; "});
    }

    public static @Nullable Ban parseBan(@NotNull String ban) {
        String[] arguments = ban.split("; ");
        if (arguments.length != 4) throw new IllegalStateException("Arguments length to construct Ban Object is not 4!");
        try {
            return new Ban(new TimeSpan(new Date(Long.parseLong(arguments[0])), new Date(Long.parseLong(arguments[1]))), Enums.Rule.valueOf(arguments[2]), arguments[3]);
        } catch (NumberFormatException exception) {
            Bukkit.getLogger().warning("\"" + arguments[0] + "\" is supposed to be a Long but isn't! or\n" +
                    "\"" + arguments[0] + "is supposed to be a Long but isn't!");
            return null;
        } catch (IllegalArgumentException exception) {
            Bukkit.getLogger().warning("\"" + arguments[2] + "\" is supposed to be a Rule enum but isn't!");
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
            return new HoldMsgs(new TimeSpan(new Date(Long.parseLong(arguments[0])), new Date(Long.parseLong(arguments[1]))), UUID.fromString(arguments[2]), Enums.Rule.valueOf(arguments[3]), arguments[4]);
        } catch (NumberFormatException exception) {
            Bukkit.getLogger().warning("\"" + arguments[0] + "\" is supposed to be a Long but isn't! or\n" +
                    "\"" + arguments[0] + "is supposed to be a Long but isn't!");
            return null;
        } catch (IllegalArgumentException exception) {
            Bukkit.getLogger().warning("\"" + arguments[3] + "\" is supposed to be a Rule enum but isn't! or\n" +
                    "\"" + arguments[2] + "\" is supposed to be a UUID but isn't!");
            return null;
        }
    }

    public static @NotNull String stringifyPunishments(@NotNull ArrayList<Punishment> punishments) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Punishment punishment : punishments) {
            if (punishment instanceof Warn warn)
                stringBuilder.append("Warn: ").append(stringifyWarn(warn));
            else if (punishment instanceof Mute mute)
                stringBuilder.append("Mute: ").append(stringifyMute(mute));
            else if (punishment instanceof Ban ban)
                stringBuilder.append("Ban: ").append(stringifyBan(ban));
            else if (punishment instanceof HoldMsgs holdMsgs)
                stringBuilder.append("HoldMsgs: ").append(stringifyHoldMsgs(holdMsgs));
            else {
                Bukkit.getLogger().warning(punishment + " is not a known Punishment, discarding it in stringification!");
                stringBuilder.replace(stringBuilder.length()-2, stringBuilder.length(), "");
            }

            stringBuilder.append(", ");
        }

        if (stringBuilder.length() >= 2)
            stringBuilder.replace(stringBuilder.length()-2, stringBuilder.length(), "");
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
                case "Ban" -> parsedPunishmentsList.add(parseBan(parts[1]));
                case "HoldMsgs" -> parsedPunishmentsList.add(parseHoldMsgs(parts[1]));
                default -> Bukkit.getLogger().warning("\"" + parts[1] + "\" is not a known Punishment!");
            }
            if (parsedPunishmentsList.remove(null))
                Bukkit.getLogger().warning("Failed to parse \"" + parts[1] + "\" discarding it!");
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

    public static ArrayList<Punishment> getPunishments(@NotNull Player player) {
        if (PersistentPlayerSessionStorage.punishments.containsKey(player.getUniqueId())) {
            return PersistentPlayerSessionStorage.punishments.get(player.getUniqueId());
        }
        Bukkit.getLogger().warning(player.getDisplayName() + " was not logged in UUIDToPunishments map, now fixing!");
        loadPunishments(player);
        return PersistentPlayerSessionStorage.punishments.get(player.getUniqueId());
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

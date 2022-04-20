package game.SpellBend.moderation;

import game.SpellBend.util.TimeSpan;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SerializableAs("HoldMsgs")
public class HoldMsgs extends Punishment {
    private final UUID msgChecker;

    public HoldMsgs (TimeSpan time, UUID msgChecker, String reason) {
        super(time, reason);
        this.msgChecker = msgChecker;
    }

    public HoldMsgs (Punishment punishment, UUID msgChecker) {
        super(punishment);
        this.msgChecker = msgChecker;
    }

    public TimeSpan getTime() {
        return getTime();
    }

    public UUID getMsgChecker() {
        return msgChecker;
    }

    public String getReason() {
        return getReason();
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("punishment", getInstance());
        map.put("msgChecker", msgChecker.toString());
        return map;
    }

    public static @NotNull HoldMsgs deserialize(@NotNull Map<String, Object> map) {
        //noinspection unchecked
        return new HoldMsgs(Punishment.deserialize((Map<String, Object>) map.get("punishment")), UUID.fromString((String) map.get("msgChecker")));
    }
}
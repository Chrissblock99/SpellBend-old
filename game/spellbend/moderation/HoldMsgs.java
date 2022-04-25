package game.spellbend.moderation;

import game.spellbend.data.Enums;
import game.spellbend.util.TimeSpan;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
@SerializableAs("HoldMsgs")
public class HoldMsgs extends Punishment {
    private final UUID msgChecker;
    private final Enums.Rule rule;

    public HoldMsgs (TimeSpan time, UUID msgChecker, Enums.Rule rule, String reason) {
        super(time, reason);
        this.msgChecker = msgChecker;
        this.rule = rule;
    }

    public HoldMsgs (Punishment punishment, UUID msgChecker, Enums.Rule rule) {
        super(punishment);
        this.msgChecker = msgChecker;
        this.rule = rule;
    }

    public TimeSpan getTime() {
        return super.getTime();
    }

    public String getReason() {
        return super.getReason();
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("punishment", getInstance());
        map.put("rule", rule.toString());
        map.put("msgChecker", msgChecker.toString());
        return map;
    }

    public static @NotNull HoldMsgs deserialize(@NotNull Map<String, Object> map) {
        //noinspection unchecked
        return new HoldMsgs(Punishment.deserialize((Map<String, Object>) map.get("punishment")), UUID.fromString((String) map.get("msgChecker")), Enums.Rule.valueOf((String) map.get("rule")));
    }
}
package game.spellbend.moderation;

import game.spellbend.data.Enums;
import game.spellbend.organize.TimeSpan;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
@SerializableAs("Mute")
public class Mute extends Punishment {
    private final Enums.Rule rule;

    public Mute (TimeSpan time, Enums.Rule rule, String reason) {
        super(time, reason);
        this.rule = rule;
    }

    public Mute (Punishment punishment, Enums.Rule rule) {
        super(punishment);
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
        map.put("rule", rule.name());
        return map;
    }

    public static @NotNull Mute deserialize(@NotNull Map<String, Object> map) {
        //noinspection unchecked
        return new Mute(Punishment.deserialize((Map<String, Object>) map.get("punishment")), Enums.Rule.valueOf((String) map.get("rule")));
    }
}
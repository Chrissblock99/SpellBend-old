package game.SpellBend.moderation;

import game.SpellBend.data.Enums;
import game.SpellBend.util.TimeSpan;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("Warn")
public class Warn extends Punishment implements ConfigurationSerializable {
    private final Enums.Rule rule;

    public Warn (TimeSpan time, Enums.Rule rule, String reason) {
        super(time, reason);
        this.rule = rule;
    }

    public Warn (Punishment punishment, Enums.Rule rule) {
        super(punishment);
        this.rule = rule;
    }

    public TimeSpan getTime() {
        return getTime();
    }

    public Enums.Rule getRule() {
        return rule;
    }

    public String getReason() {
        return getReason();
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("punishment", getInstance());
        map.put("rule", rule.name());
        return map;
    }

    public static @NotNull Warn deserialize(@NotNull Map<String, Object> map) {
        //noinspection unchecked
        return new Warn(Punishment.deserialize((Map<String, Object>) map.get("punishment")), Enums.Rule.valueOf((String) map.get("rule")));
    }
}

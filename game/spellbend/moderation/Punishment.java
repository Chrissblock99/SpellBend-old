package game.spellbend.moderation;

import game.spellbend.util.TimeSpan;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
@EqualsAndHashCode
@SerializableAs("Punishment")
public class Punishment implements ConfigurationSerializable {
    private final TimeSpan time;
    private final String reason;

    public Punishment (TimeSpan time, String reason) {
        this.time = time;
        this.reason = reason;
    }

    public Punishment (@NotNull Punishment punishment) {
        this.time = punishment.getTime();
        this.reason = punishment.getReason();
    }

    public Punishment getInstance() {
        return this;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("startDate", time.getStartDate().getTime());
        map.put("endDate", time.getEndDate().getTime());
        map.put("reason", reason);
        return map;
    }

    public static @NotNull Punishment deserialize(@NotNull Map<String, Object> map) {
        return new Punishment(new TimeSpan(new Date((long) map.get("startDate")), new Date((long) map.get("endDate"))), (String) map.get("reason"));
    }
}

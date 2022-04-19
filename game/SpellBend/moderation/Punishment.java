package game.SpellBend.moderation;

import game.SpellBend.util.TimeSpan;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Punishment implements ConfigurationSerializable {
    private final TimeSpan time;
    private final String reason;

    public Punishment (TimeSpan time, String reason) {
        this.time = time;
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public TimeSpan getTime() {
        return time;
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

    @Override
    public @NotNull String toString() {
        return "Punishment(time=" + time + ", reason=String(" + reason + "))";
    }

    @Override
    public boolean equals (Object o) {
        if (o == this) return true;
        if (!(o instanceof Punishment)) return false;

        Punishment obj = (Punishment) o;
        return time.equals(obj.time) &&
                reason.equals(obj.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, reason);
    }
}

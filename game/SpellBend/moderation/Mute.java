package game.SpellBend.moderation;

import game.SpellBend.data.Enums;
import game.SpellBend.util.TimeSpan;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Mute extends Punishment {
    private final Enums.Rule rule;

    public Mute (TimeSpan time, Enums.Rule rule, String reason) {
        super(time, reason);
        this.rule = rule;
    }

    public TimeSpan getTime() {
        return super.getTime();
    }

    public Enums.Rule getRule() {
        return rule;
    }

    public String getReason() {
        return super.getReason();
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("startDate", super.getTime().getStartDate().getTime());
        map.put("endDate", super.getTime().getEndDate().getTime());
        map.put("rule", rule.name());
        map.put("reason", super.getReason());
        return map;
    }

    public static @NotNull Mute deserialize(@NotNull Map<String, Object> map) {
        return new Mute(new TimeSpan(new Date((long) map.get("startDate")), new Date((long) map.get("endDate"))), Enums.Rule.valueOf((String) map.get("rule")), (String) map.get("reason"));
    }
}
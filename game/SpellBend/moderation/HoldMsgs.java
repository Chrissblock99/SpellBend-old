package game.SpellBend.moderation;

import game.SpellBend.util.TimeSpan;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HoldMsgs extends Punishment {
    private final UUID msgChecker;

    public HoldMsgs (TimeSpan time, UUID msgChecker, String reason) {
        super(time, reason);
        this.msgChecker = msgChecker;
    }

    public TimeSpan getTime() {
        return super.getTime();
    }

    public Player getMsgChecker() {
        return Bukkit.getPlayer(msgChecker);
    }

    public String getReason() {
        return super.getReason();
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("startDate", super.getTime().getStartDate().getTime());
        map.put("endDate", super.getTime().getEndDate().getTime());
        map.put("msgChecker", msgChecker.toString());
        map.put("reason", super.getReason());
        return map;
    }

    public static @NotNull HoldMsgs deserialize(@NotNull Map<String, Object> map) {
        return new HoldMsgs(new TimeSpan(new Date((long) map.get("startDate")), new Date((long) map.get("endDate"))),UUID.fromString((String) map.get("msgChecker")), (String) map.get("reason"));
    }
}
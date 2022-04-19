package game.SpellBend.util;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Objects;

public class TimeSpan {
    private final Date startDate;
    private final Date endDate;

    public TimeSpan (@NotNull Date startDate, int timeInS) {
        if (timeInS>=0) throw new IllegalArgumentException("The time cannot be 0 or smaller!");
        this.startDate = startDate;
        this.endDate = new Date(startDate.getTime()+timeInS*1000L);
    }

    public TimeSpan (@NotNull Date startDate, @NotNull Date endDate) {
        if (!endDate.after(startDate)) throw new IllegalArgumentException("EndDate cannot be before startDate!");
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getRemainingTimeInS() {
        return (int) (endDate.getTime()-startDate.getTime())/1000;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    @Override
    public @NotNull String toString() {
        return "TimeSpan(startDate=" + startDate + ", endDate=" + endDate + ")";
    }

    @Override
    public boolean equals (Object o) {
        if (o == this) return true;
        if (!(o instanceof TimeSpan)) return false;

        TimeSpan obj = (TimeSpan) o;
        return startDate.equals(obj.startDate) &&
                endDate.equals(obj.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }
}

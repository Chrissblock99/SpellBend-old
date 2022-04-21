package game.spellbend.util;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

@Getter
@ToString
@EqualsAndHashCode
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
}

package game.spellbend.organize;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class CoolDownEntry {
    public final float timeInS;
    public final Date startDate;
    public final String coolDownType;

    public CoolDownEntry(float timeInS, Date startDate, String coolDownType) {
        this.timeInS = timeInS;
        this.startDate = startDate;
        this.coolDownType = coolDownType;
    }

    public boolean equals(@NotNull CoolDownEntry compareTo) {
        return timeInS == compareTo.timeInS && startDate.equals(compareTo.startDate) && coolDownType.equals(compareTo.coolDownType);
    }

    public float getRemainingCoolDownTime() {
        return timeInS-(new Date().getTime()-startDate.getTime())/1000f;
    }

    @Override
    public String toString () {
        return timeInS + ", " + startDate + ", " + coolDownType;
    }
}

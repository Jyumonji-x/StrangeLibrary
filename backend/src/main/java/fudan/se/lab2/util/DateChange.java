package fudan.se.lab2.util;

import java.util.Calendar;
import java.util.Date;

public class DateChange {
    private final Date date;

    public DateChange(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public Date getDateOfDaysLater(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,days);
        return calendar.getTime();
    }

    public Date getDateOfSecondsLater(int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND,seconds);
        return calendar.getTime();
    }
}

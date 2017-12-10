package wad.service;

import java.util.Calendar;

import org.springframework.stereotype.Service;

@Service
public class TimeService {

    public int getCurrentWeekNumber() {
        Calendar now = Calendar.getInstance();
        int week = now.get(Calendar.WEEK_OF_YEAR);

        return week;
    }

    public int getLastWeekNumber() {
        Calendar now = Calendar.getInstance();

        int lastWeek = now.get(Calendar.WEEK_OF_YEAR - 1);
        if (lastWeek == 0) {
            now.set(Calendar.YEAR - 1, Calendar.DECEMBER, 31);
        }

        return lastWeek;
    }

    public int getCurrentYear() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);

        return year;
    }
}

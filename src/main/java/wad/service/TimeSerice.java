package wad.service;

import java.util.Calendar;

import org.springframework.stereotype.Service;

@Service
public class TimeSerice {

    public int getCurrentWeekNumber() {
        Calendar now = Calendar.getInstance();
        int week = now.get(Calendar.WEEK_OF_YEAR);

        return week;
    }
    
    public int getCurrentYear() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);

        return year;
    }
}
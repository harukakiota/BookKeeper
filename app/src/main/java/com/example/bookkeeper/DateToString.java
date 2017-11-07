package com.example.bookkeeper;

import java.util.Calendar;

/**
 * Created by Юлия on 22.05.2017.
 */

public class DateToString {

    private static final int UNIX_CONVERT = 86400000;
    private static final int UTC_CONST = 10800000;

    public static String getDate(Calendar date) {
        return new String(date.get(Calendar.DAY_OF_MONTH) + "/" + (date.get(Calendar.MONTH) + 1) + "/" + date
                .get(Calendar.YEAR));
    }

    public static String convertFromUnix(int unixdate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((long)UNIX_CONVERT*unixdate);
        return new String(getDate(calendar));
    }

    public static int convertToUnix (Calendar calendar) {
        return (int) Math.floor((calendar.getTimeInMillis()+ UTC_CONST) / UNIX_CONVERT);
    }
}

package com.aosp_repo.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {

    public static String[] getDate() {
        Calendar cal = Calendar.getInstance();
        String string = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());

        return string.split("/", 3);
    }

    public static String[] getTime(boolean use24HourFormat) {
        Calendar cal = Calendar.getInstance();
        String string;

        if (use24HourFormat)
            string = new SimpleDateFormat("HH:mm:ss").format(cal.getTime());
        else
            string = new SimpleDateFormat("hh:mm:ss").format(cal.getTime());

        return string.split(":", 3);
    }

    public static int getDay() {
        return Integer.parseInt(getDate()[0]);
    }

    public static int getMonth() {
        return Integer.parseInt(getDate()[1]);
    }

    public static int getYear() {
        return Integer.parseInt(getDate()[2]);
    }

    public static int getHour(boolean hour24Format) {
        return Integer.parseInt(getTime(hour24Format)[0]);
    }

    public static int getMinutes() {
        return Integer.parseInt(getTime(false)[0]);
    }

    public static int getSeconds() {
        return Integer.parseInt(getTime(false)[0]);
    }

}

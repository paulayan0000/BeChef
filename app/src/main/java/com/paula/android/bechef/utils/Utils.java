package com.paula.android.bechef.utils;

import android.content.Context;

import com.paula.android.bechef.R;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {

    public static int convertDpToPixel(float dp, Context context) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public static String getRelevantTime(Context context, String utcTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone(Constants.TIME_ZONE));
        Date gpsUTCDate;
        try {
            gpsUTCDate = dateFormat.parse(utcTime);

            long diff = new Date().getTime() - gpsUTCDate.getTime();
            long r;
            if (diff > Constants.ONE_YEAR) {
                r = (diff / Constants.ONE_YEAR);
                return r + context.getString(R.string.years_age);
            }
            if (diff > Constants.ONE_MONTH) {
                r = (diff / Constants.ONE_MONTH);
                return r + context.getString(R.string.months_age);
            }
            if (diff > Constants.ONE_WEEK) {
                r = (diff / Constants.ONE_WEEK);
                return r + context.getString(R.string.weeks_age);
            }
            if (diff > Constants.ONE_DAY) {
                r = (diff / Constants.ONE_DAY);
                return r + context.getString(R.string.days_ago);
            }
            if (diff > Constants.ONE_HOUR) {
                r = (diff / Constants.ONE_HOUR);
                return r + context.getString(R.string.hours_age);
            }
            if (diff > Constants.ONE_MINUTE) {
                r = (diff / Constants.ONE_MINUTE);
                return r + context.getString(R.string.minutes_ago);
            }
        } catch (ParseException e) {
            return "";
        }
        return context.getString(R.string.recently);
    }

    public static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
}

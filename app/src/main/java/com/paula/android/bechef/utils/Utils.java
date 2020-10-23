package com.paula.android.bechef.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import androidx.core.graphics.drawable.DrawableCompat;

public class Utils {
    private final static long minute = 60 * 1000; // 1分鐘
    private final static long hour = 60 * minute; // 1小時
    private final static long day = 24 * hour; // 1天
    private final static long week = 7 * day; // 1週
    private final static long month = 31 * day; // 1月
    private final static long year = 12 * month; // 1年

    public static float convertDpToPixel(float dp, Context context) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static String getCreatedTime(String utcTime) {
        SimpleDateFormat utcFormater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gpsUTCDate;
        try {
            gpsUTCDate = utcFormater.parse(utcTime);

            long diff = new Date().getTime() - gpsUTCDate.getTime();
            long r;
            if (diff > year) {
                r = (diff / year);
                return r + "年前";
            }
            if (diff > month) {
                r = (diff / month);
                return r + "個月前";
            }
            if (diff > week) {
                r = (diff / week);
                return r + "週前";
            }
            if (diff > day) {
                r = (diff / day);
                return r + "天前";
            }
            if (diff > hour) {
                r = (diff / hour);
                return r + "小時前";
            }
            if (diff > minute) {
                r = (diff / minute);
                return r + "分鐘前";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "剛剛";
    }

    public static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
}

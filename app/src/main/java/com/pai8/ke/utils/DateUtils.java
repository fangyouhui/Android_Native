package com.pai8.ke.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期/时间
 * Created by gh on 2018/7/27.
 */
public class DateUtils {

    public static final String FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String FORMAT_YYYY_MM_DD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YYYY_MM_DD_HHMM = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_HHMMSS = "HH:mm:ss";
    public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * Don't let anyone instantiate this class.
     */
    private DateUtils() {
        throw new Error("Do not need instantiate!");
    }

    public static String formatYYYYMMDD(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_YYYY_MM_DD);
        String date = "";
        try {
            if (strDate.length() == 13) {
                date = formatter.format(new Date(Long.parseLong(strDate)));
            } else {
                date = formatter.format(new Date(Integer.parseInt(strDate) * 1000L));
            }
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";


    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurDate() {
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_YYYY_MM_DD_HHMMSS);
        return formatter.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 获取当前时间
     *
     * @param format
     * @return
     */
    public static String getCurDate(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 毫秒转时间
     *
     * @param millis
     * @return
     */
    public static String millisToTime(String format, long millis) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        return formatter.format(millis);
    }
}

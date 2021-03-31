package com.pai8.ke.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    /**
     * 10,13位int型的时间戳转换为String(yyyy-MM-dd HH:mm:ss)
     *
     * @param time
     * @return
     */
    public static String timeStampToString(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
        String times;
        if (time.length() == 13) {
            long i = Long.parseLong(time);
            times = sdr.format(new Date(i));
        } else {
            int i = Integer.parseInt(time);
            times = sdr.format(new Date(i * 1000L));
        }
        return times;
    }
}

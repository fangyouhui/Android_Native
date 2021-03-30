package com.pai8.ke;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void testTime() {
        String time = "1647043200";
        String startTime = timeStampToString(time);
        System.out.println(startTime);
    }

    /**
     * 10位int型的时间戳转换为String(yyyy-MM-dd HH:mm:ss)
     *
     * @param time
     * @return
     */
    private String timeStampToString(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    //将时间戳转化为对应的时间(10位或者13位都可以)
    public static String formatTime(long time) {
        String times = null;
        if (String.valueOf(time).length() > 10) {// 10位的秒级别的时间戳
            times = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time * 1000));
        } else {// 13位的秒级别的时间戳
            times = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
        }
        return times;
    }

}






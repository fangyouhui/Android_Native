package com.pai8.ke.utils;

import android.util.Log;

import com.pai8.ke.app.BuildType;
import com.pai8.ke.app.MyApp;

/**
 * Log工具类
 * Created by gh on 2020/11/2.
 */
public class LogUtils {

    private static final String TAG = "fetaphon";

    private LogUtils() {

        throw new UnsupportedOperationException("cannot be instantiated");

    }

    public static boolean isDebug() {
        return MyApp.getBuildType() != BuildType.RELEASE;
    }

    //================================= 下面四个是默认tag的函数===================================
    public static void i(String msg) {
        if (isDebug())
            Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (isDebug())
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (isDebug())
            Log.e(TAG, msg);
    }

    public static void v(String msg) {
        if (isDebug())
            Log.v(TAG, msg);
    }

    //================================= 下面四个是自定义tag的函数===================================
    public static void i(String tag, String msg) {
        if (isDebug())
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug())
            Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isDebug())
            Log.e(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug())
            Log.v(tag, msg);
    }
}

package com.pai8.ke.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.pai8.ke.app.MyApp;

/**
 * Toast统一管理类
 * Created by gh on 2020/11/2.
 */
public class ToastUtils {

    private static Toast mToast;

    private static int mPosition = Gravity.CENTER;

    private ToastUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        if (mToast == null) {
            mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            mToast.setGravity(mPosition, 0, 0);
        } else {
            mToast.setText(message);
        }
        mToast.show();
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(CharSequence message) {
        if (mToast == null) {
            mToast = Toast.makeText(MyApp.getMyApp(), message, Toast.LENGTH_SHORT);
            mToast.setGravity(mPosition, 0, 0);
        } else {
            mToast.setText(message);
        }
        mToast.show();
    }


    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (mToast == null) {
            mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            mToast.setGravity(mPosition, 0, 0);
        } else {
            mToast.setText(message);
        }
        mToast.show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, message, duration);
            mToast.setGravity(mPosition, 0, 0);
        } else {
            mToast.setText(message);
        }
        mToast.show();
    }

    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

}
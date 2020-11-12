package com.pai8.ke.receiver;

import android.content.Context;

import com.pai8.ke.utils.LogUtils;

import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class MyJPushMessageReceiver extends JPushMessageReceiver {

    private static final String TAG = "MyJPushMessageReceiver";

    @Override
    public void onRegister(Context context, String s) {
        super.onRegister(context, s);
        LogUtils.d(TAG, "onRegister:" + s);
    }

    @Override
    public void onConnected(Context context, boolean b) {
        super.onConnected(context, b);
        LogUtils.d(TAG, "onConnected:" + b);
    }

    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        super.onMessage(context, customMessage);
        LogUtils.d(TAG, "onMessage:" + customMessage.message);

    }


}

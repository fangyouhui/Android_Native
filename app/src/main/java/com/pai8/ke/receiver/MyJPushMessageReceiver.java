package com.pai8.ke.receiver;

import android.content.Context;

import com.pai8.ke.activity.video.ChatActivity;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.utils.LogUtils;

import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
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
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onAliasOperatorResult(context, jPushMessage);
        LogUtils.d(TAG, "onAliasOperatorResult:" + jPushMessage.toString());
    }

    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        super.onMessage(context, customMessage);
        LogUtils.d(TAG, "onMessage:" + customMessage.message);

    }

    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageArrived(context, notificationMessage);
        LogUtils.d(TAG, "onNotifyMessageArrived:" + notificationMessage.toString());
        EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_PUSH));
    }

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageOpened(context, notificationMessage);
        LogUtils.d(TAG, "onNotifyMessageOpened:" + notificationMessage.toString());
    }

}

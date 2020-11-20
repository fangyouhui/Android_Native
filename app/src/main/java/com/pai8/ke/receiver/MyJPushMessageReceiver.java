package com.pai8.ke.receiver;

import android.content.Context;

import com.pai8.ke.activity.video.ChatActivity;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.entity.PushBiz;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.manager.ActivityManager;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.utils.GsonUtils;
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
        try {
            pushBiz(context, GsonUtils.fromJson(notificationMessage.notificationExtras, PushBiz.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageOpened(context, notificationMessage);
        LogUtils.d(TAG, "onNotifyMessageOpened:" + notificationMessage.toString());
    }

    /**
     * 推送自定义业务
     *
     * @param context
     * @param pushBiz
     */
    private void pushBiz(Context context, PushBiz pushBiz) {
        // 1-音频提醒，2-视频提醒，3-拒接
        switch (pushBiz.getM_type()) {
            case "1":
                if (!ActivityManager.getInstance().isChatActivity()) {
                    ChatActivity.launch(context, ChatActivity.BIZ_TYPE_AUDIO, ChatActivity.INTENT_TYPE_WAIT
                            , pushBiz.getContent());
                }
                break;
            case "2":
                if (!ActivityManager.getInstance().isChatActivity()) {
                    ChatActivity.launch(context, ChatActivity.BIZ_TYPE_VIDEO, ChatActivity.INTENT_TYPE_WAIT,
                            pushBiz.getContent());
                }
                break;
            case "3":
                EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_PUSH, pushBiz));
                break;
        }
    }
}

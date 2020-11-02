package com.pai8.ke.utils;


import com.pai8.ke.base.BaseEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * EventBus工具类
 * 订阅、取消订阅、发布
 * Created by gh on 2018/7/27.
 */
public class EventBusUtils {

    /**
     * 注册
     *
     * @param subscriber
     */
    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    /**
     * 解除注册
     *
     * @param subscriber
     */
    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    /**
     * 发送事件
     *
     * @param event
     */
    public static void sendEvent(BaseEvent event) {
        EventBus.getDefault().post(event);
    }

    /**
     * 发送粘性事件
     *
     * @param event
     */
    public static void sendStickyEvent(BaseEvent event) {
        EventBus.getDefault().postSticky(event);
    }

    public static void removeStickyEvent(BaseEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
    }

}

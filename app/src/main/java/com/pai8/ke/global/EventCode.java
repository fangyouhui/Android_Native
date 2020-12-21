package com.pai8.ke.global;

/*
 * 事件总线Code
 * Created by gh on 2020/11/5.
 */
public interface EventCode {
    // 举报/投诉成功
    int EVENT_REPORT = 0x01;
    // 选择商铺
    int EVENT_CHOOSE_SHOP = 0x02;
    // 选择地址
    int EVENT_CHOOSE_ADDRESS = 0x03;
    // 微信code
    int EVENT_WX_CODE = 0x04;
    // 推送
    int EVENT_PUSH = 0x05;
    // 支付结果
    int EVENT_PAY_RESULT = 0xff6;
    // 优惠券列表变化
    int EVENT_COUPON = 0xff7;
    int EVENT_HOME_TAB = 0xff8;
    int EVENT_VIDEO_LIST_REFRESH = 0xff9;
    int EVENT_VIDEO_ITEM = 0xff0A;
    // 登录状态
    int EVENT_LOGIN_STATUS = 0xff0b;
    // 版本更新
    int EVENT_UPDATE = 0xff0c;
}

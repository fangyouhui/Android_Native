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
}

package com.pai8.ke.manager;


import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BaseEvent;
import com.pai8.ke.entity.event.LoginStatusEvent;
import com.pai8.ke.entity.resp.MyInfoResp;
import com.pai8.ke.entity.UserInfo;
import com.pai8.ke.global.EventCode;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.utils.PreferencesUtils;
import com.pai8.ke.utils.StringUtils;

import retrofit2.http.PUT;

/**
 * 账户统一管理
 * Created by gh on 2020/11/2.
 */
public class AccountManager {

    private AccountManager() {

    }

    public static AccountManager getInstance() {
        return AccountManagerHolder.mInstance;
    }

    public static class AccountManagerHolder {
        public static AccountManager mInstance = new AccountManager();
    }

    /**
     * 是否登录
     */
    public boolean isLogin() {
        return StringUtils.isNotEmpty(getToken());
    }

    /**
     * 获取用户id
     */
    public String getUid() {
        String uid = (String) PreferencesUtils.get(MyApp.getMyApp(), "uid", "");
        if (StringUtils.isNotEmpty(uid)) return uid;
        return (String) PreferencesUtils.get(MyApp.getMyApp(), "id", "");
    }

    /**
     * 获取Token
     */
    public String getToken() {
        return (String) PreferencesUtils.get(MyApp.getMyApp(), "token", "");
    }

    /**
     * 保存用户信息
     */
    public void saveUserInfo(UserInfo userInfo) {
        if (userInfo == null) return;
        MyApp myApp = MyApp.getMyApp();
        PreferencesUtils.put(myApp, "id", StringUtils.strSafe(userInfo.getId()));
        PreferencesUtils.put(myApp, "uid", StringUtils.strSafe(userInfo.getUid()));
        PreferencesUtils.put(myApp, "name", StringUtils.strSafe(userInfo.getName()));
        PreferencesUtils.put(myApp, "user_nickname", StringUtils.strSafe(userInfo.getUser_nickname()));
        PreferencesUtils.put(myApp, "avatar", StringUtils.strSafe(userInfo.getAvatar()));
        PreferencesUtils.put(myApp, "phone", StringUtils.strSafe(userInfo.getPhone()));
        PreferencesUtils.put(myApp, "token", StringUtils.strSafe(userInfo.getToken()));
        MyApp.setJPushAlias();
    }

    /**
     * 获取用户信息
     */
    public UserInfo getUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId((String) PreferencesUtils.get(MyApp.getMyApp(), "id", ""));
        String uid = (String) PreferencesUtils.get(MyApp.getMyApp(), "uid", "");
        if (StringUtils.isEmpty(uid)) {
            userInfo.setUid((String) PreferencesUtils.get(MyApp.getMyApp(), "id", ""));
        } else {
            userInfo.setUid(uid);
        }
        userInfo.setName((String) PreferencesUtils.get(MyApp.getMyApp(), "name", ""));
        userInfo.setUser_nickname((String) PreferencesUtils.get(MyApp.getMyApp(), "user_nickname", ""));
        userInfo.setAvatar((String) PreferencesUtils.get(MyApp.getMyApp(), "avatar", ""));
        userInfo.setPhone((String) PreferencesUtils.get(MyApp.getMyApp(), "phone", ""));
        userInfo.setToken((String) PreferencesUtils.get(MyApp.getMyApp(), "token", ""));
        return userInfo;
    }

    /**
     * 清除用户信息
     */
    public void clearUserInfo() {
        PreferencesUtils.put(MyApp.getMyApp(), "id", "");
        PreferencesUtils.put(MyApp.getMyApp(), "uid", "");
        PreferencesUtils.put(MyApp.getMyApp(), "name", "");
        PreferencesUtils.put(MyApp.getMyApp(), "user_nickname", "");
        PreferencesUtils.put(MyApp.getMyApp(), "avatar", "");
        PreferencesUtils.put(MyApp.getMyApp(), "phone", "");
        PreferencesUtils.put(MyApp.getMyApp(), "token", "");
        clearShopInfo();
    }

    /**
     * 注销账号
     */
    public void logout() {
        clearUserInfo();
        EventBusUtils.sendEvent(new BaseEvent(EventCode.EVENT_LOGIN_STATUS, new LoginStatusEvent(LoginStatusEvent.LOGOUT)));
        ActivityManager.getInstance().finishToHome();
    }

    public void saveShopInfo(MyInfoResp infoResp) {
        if (infoResp == null) return;
        PreferencesUtils.put(MyApp.getMyApp(), "shop_id", StringUtils.strSafe(infoResp.getShop_id()));
        PreferencesUtils.put(MyApp.getMyApp(), "verify_status", infoResp.getVerify_status());
        PreferencesUtils.put(MyApp.getMyApp(), "manage", StringUtils.strSafe(infoResp.getManage()));
    }

    public MyInfoResp getShopInfo() {
        MyInfoResp myInfoResp = new MyInfoResp();
        myInfoResp.setManage((String) PreferencesUtils.get(MyApp.getMyApp(), "manage", ""));
        myInfoResp.setShop_id((String) PreferencesUtils.get(MyApp.getMyApp(), "shop_id", ""));
        myInfoResp.setVerify_status((int) PreferencesUtils.get(MyApp.getMyApp(), "verify_status", 0));
        return myInfoResp;
    }

    public String getShopId() {
        return (String) PreferencesUtils.get(MyApp.getMyApp(), "shop_id", "");
    }

    //是否是二级管理
    public boolean isManage() {
        String manage = (String) PreferencesUtils.get(MyApp.getMyApp(), "manage", "");
        return (StringUtils.isNotEmpty(manage) && !StringUtils.equals("0", manage));
    }

    public void clearShopInfo() {
        PreferencesUtils.put(MyApp.getMyApp(), "shop_id", "");
        PreferencesUtils.put(MyApp.getMyApp(), "verify_status", 0);
        PreferencesUtils.put(MyApp.getMyApp(), "manage", "");
    }

}

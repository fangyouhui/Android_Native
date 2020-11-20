package com.pai8.ke.manager;


import com.pai8.ke.app.MyApp;
import com.pai8.ke.entity.resp.UserInfo;
import com.pai8.ke.utils.PreferencesUtils;
import com.pai8.ke.utils.StringUtils;

import cn.jpush.android.api.JPushInterface;

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
        return (String) PreferencesUtils.get(MyApp.getMyApp(), "uid", "");
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
        PreferencesUtils.put(myApp, "id", StringUtils.strSafe(userInfo.getUid()));
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
        userInfo.setUid((String) PreferencesUtils.get(MyApp.getMyApp(), "uid", ""));
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
    }

    /**
     * 注销账号
     */
    public void logout() {
        clearUserInfo();
        ActivityManager.getInstance().finishToHome();
    }

}

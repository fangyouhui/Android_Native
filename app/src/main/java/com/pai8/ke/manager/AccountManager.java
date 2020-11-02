package com.pai8.ke.manager;


import com.pai8.ke.app.MyApp;
import com.pai8.ke.utils.PreferencesUtils;
import com.pai8.ke.utils.StringUtils;

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
        return StringUtils.isNotEmpty(getUid());
    }

    /**
     * 获取用户id
     */
    public String getUid() {
        return (String) PreferencesUtils.get(MyApp.getMyApp(), "userId", "");
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
    public void saveUserInfo() {
    }

    /**
     * 获取用户信息
     */
    public void getUserInfo() {

    }

    /**
     * 清除用户信息
     */
    public void clearUserInfo() {

    }

    /**
     * 注销账号
     */
    public void logout() {
        clearUserInfo();
        ActivityManager.getInstance().finishToHome();
    }

}

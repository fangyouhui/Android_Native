package com.pai8.ke.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.pai8.ke.global.GlobalConstants;

public class MyApp extends Application {

    private static MyApp mContext;
    private static Handler mHandler;
    private static BuildType mBuildType;

    public static MyApp getMyApp() {
        return mContext;
    }

    public static Handler getMyAppHandler() {
        return mHandler;
    }

    /**
     * 当前版本环境，打包版本只需要修改此配置
     * RELEASE-生产, TEST-测试环境, DEV-开发环境
     *
     * @return
     */
    public static BuildType getBuildType() {
        return BuildType.RELEASE;
    }

    /**
     * 获取HttpBaseUrl
     */
    public static String getHttpBaseUrl() {
        if (mBuildType == BuildType.RELEASE) {
            return GlobalConstants.HTTP_URL_RELEASE;
        }
        if (mBuildType == BuildType.TEST) {
            return GlobalConstants.HTTP_URL_TEST;
        }
        if (mBuildType == BuildType.DEV) {
            return GlobalConstants.HTTP_URL_DEV;
        }
        return "";
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


}

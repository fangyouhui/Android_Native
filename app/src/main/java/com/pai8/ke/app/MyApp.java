package com.pai8.ke.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.danikula.videocache.HttpProxyCacheServer;
import com.dueeeke.videoplayer.ijk.IjkPlayerFactory;
import com.dueeeke.videoplayer.player.VideoViewConfig;
import com.dueeeke.videoplayer.player.VideoViewManager;
import com.gh.qiniushortvideo.QNShortVideo;
import com.hjq.bar.TitleBar;
import com.hjq.bar.initializer.LightBarInitializer;
import com.pai8.ke.BuildConfig;
import com.pai8.ke.R;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.manager.QNRTCManager;
import com.pai8.ke.utils.AMapLocationUtils;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.LogUtils;
import com.pai8.ke.utils.PreferencesUtils;
import com.pai8.ke.utils.ResUtils;
import com.pai8.ke.utils.StringUtils;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;

import java.util.ArrayList;
import java.util.List;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cn.jpush.android.api.JPushInterface;

public class MyApp extends Application {

    private static MyApp mContext;
    private static Handler mHandler;
    private UploadManager mUpLoadManager;
    private HttpProxyCacheServer proxy;
    public static AMapLocation mAMapLocation;

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
        return (Boolean) PreferencesUtils.get(mContext, "isTest", true) ? BuildType.TEST : BuildType.RELEASE;
    }

    public static boolean toggleBuildType() {
        boolean current = (Boolean) PreferencesUtils.get(mContext, "isTest", false);
        PreferencesUtils.put(mContext, "isTest", !current);
        return !current;
    }

    /**
     * 获取HttpBaseUrl
     */
    public static String getHttpBaseUrl() {
        if (getBuildType() == BuildType.RELEASE) {
            return GlobalConstants.HTTP_URL_RELEASE;
        }
        if (getBuildType() == BuildType.TEST) {
            return GlobalConstants.HTTP_URL_TEST;
        }
        if (getBuildType() == BuildType.DEV) {
            return GlobalConstants.HTTP_URL_DEV;
        }
        return "";
    }

    /**
     * 获取经纬度
     *
     * @return
     */
    public static List<String> getLngLat() {
        List<String> locations = new ArrayList<>();
        String longitude = (String) PreferencesUtils.get(getMyApp(), "longitude", "120.610868");
        String latitude = (String) PreferencesUtils.get(getMyApp(), "latitude", "31.329679");
        String address = (String) PreferencesUtils.get(getMyApp(), "address", "");
        locations.add(longitude);
        locations.add(latitude);
        locations.add(address);
        return locations;
    }

    public static String getCity() {
        return (String) PreferencesUtils.get(getMyApp(), "city", "苏州市");
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        if (mHandler == null) {
            mHandler = new Handler();
        }
        initTitleBar();
        Configuration config = new Configuration.Builder()
                .connectTimeout(90)              // 链接超时。默认90秒
                .useConcurrentResumeUpload(true) // 使用并发上传，使用并发上传时，除最后一块大小不定外，其余每个块大小固定为4M，
                .concurrentTaskCount(3)          // 并发上传线程数量为3
                .responseTimeout(90)             // 服务器响应超时。默认90秒
                .zone(FixedZone.zone0)           // 设置区域，不指定会自动选择。指定不同区域的上传域名、备用域名、备用IP。
                .build();
        mUpLoadManager = new UploadManager(config);
        //七牛云音视频
        QNRTCManager.getInstance().init();
        //七牛短视频
        QNShortVideo.init(this);
        //JPush
        JPushInterface.setDebugMode(getBuildType() != BuildType.RELEASE);
        JPushInterface.init(this);
        //DkPlayer
        VideoViewManager.setConfig(VideoViewConfig.newBuilder()
                //使用使用IjkPlayer解码
                .setPlayerFactory(IjkPlayerFactory.create())
                .setLogEnabled(true)
                .build());
        AMapLocationUtils.init(this);
        CustomActivityOnCrash.install(this);
    }

    public static void getLocation() {
        AMapLocationUtils.getLocation(location -> {
            LogUtils.d("AMap Location:" + location.getAddress());
            mAMapLocation = location;
            PreferencesUtils.put(mContext, "latitude", location.getLatitude() + "");
            PreferencesUtils.put(mContext, "longitude", location.getLongitude() + "");
            PreferencesUtils.put(mContext, "address", location.getAddress());
            PreferencesUtils.put(mContext, "city", location.getCity());
        }, false);
    }

    public static HttpProxyCacheServer getProxy() {
        MyApp app = (MyApp) mContext.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .maxCacheFilesCount(100)
                .build();
    }

    public static void setJPushAlias() {
        String uid = AccountManager.getInstance().getUid();
        if (StringUtils.isNotEmpty(uid)) {
            LogUtils.d("Jpush：设置别名：" + uid);
            JPushInterface.setAlias(getMyApp().getApplicationContext(), 1, uid);
        }
    }

    public UploadManager getUploadManager() {
        return mUpLoadManager;
    }

    private void initTitleBar() {
        TitleBar.setDefaultInitializer(new LightBarInitializer() {
            @Override
            public TextView getLeftView(Context context) {
                return super.getLeftView(context);
            }

            @Override
            public TextView getTitleView(Context context) {
                return super.getTitleView(context);
            }

            @Override
            public TextView getRightView(Context context) {
                return super.getRightView(context);
            }

            @Override
            public View getLineView(Context context) {
                return super.getLineView(context);
            }

            @Override
            public Drawable getBackgroundDrawable(Context context) {
                return super.getBackgroundDrawable(context);
            }

            @Override
            public Drawable getBackIcon(Context context) {
                return ResUtils.getDrawable(R.mipmap.ic_navi_back);
            }

            @Override
            public int getHorizontalPadding(Context context) {
                return super.getHorizontalPadding(context);
            }

            @Override
            public int getVerticalPadding(Context context) {
                return super.getVerticalPadding(context);
            }
        });

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        ImageLoadUtils.clearImageMemoryCache(this);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        switch (level) {
            case TRIM_MEMORY_COMPLETE://内存不足，并且该进程在后台进程列表最后一个，马上就要被清理
                break;
            case TRIM_MEMORY_MODERATE://内存不足，并且该进程在后台进程列表的中部
                break;
            case TRIM_MEMORY_BACKGROUND://内存不足，并且该进程是后台进程
                break;
            case TRIM_MEMORY_UI_HIDDEN://内存不足，并且该进程的UI已经不可见了
                ImageLoadUtils.clearImageMemoryCache(this);
                break;
            case TRIM_MEMORY_RUNNING_CRITICAL://内存不足(后台进程不足3个)，并且该进程优先级比较高，需要清理内存
                break;
            case TRIM_MEMORY_RUNNING_LOW://内存不足(后台进程不足5个)，并且该进程优先级比较高，需要清理内存
                break;
            case TRIM_MEMORY_RUNNING_MODERATE://内存不足(后台进程超过5个)，并且该进程优先级比较高，需要清理内存
                break;
        }
        ImageLoadUtils.onTrimMemory(this, level);
    }

}

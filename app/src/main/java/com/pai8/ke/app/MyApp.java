package com.pai8.ke.app;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.hjq.bar.TitleBar;
import com.hjq.bar.initializer.LightBarInitializer;
import com.pai8.ke.R;
import com.pai8.ke.global.GlobalConstants;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.ResUtils;

public class MyApp extends Application {

    private static MyApp mContext;
    private static Handler mHandler;

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
        return BuildType.TEST;
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

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        if (mHandler == null) {
            mHandler = new Handler();
        }
        initTitleBar();
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

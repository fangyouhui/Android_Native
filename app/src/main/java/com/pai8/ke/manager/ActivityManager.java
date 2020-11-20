package com.pai8.ke.manager;

import android.app.Activity;
import android.content.ComponentName;


import com.pai8.ke.activity.MainActivity;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity统一管理
 * Created by gh on 2018/8/22.
 */
public class ActivityManager {

    private static List<Activity> mActivityStack = new ArrayList<>();

    private ActivityManager() {

    }

    public static ActivityManager getInstance() {
        return ActivityManagerHolder.mInstance;
    }

    public static class ActivityManagerHolder {

        public static ActivityManager mInstance = new ActivityManager();

    }

    public void addActivity(Activity activity) {

        mActivityStack.add(activity);

    }

    public void removeActivity(Activity activity) {

        mActivityStack.remove(activity);

    }

    public void finishToHome() {

        if (CollectionUtils.isEmpty(mActivityStack)) {
            return;
        }
        for (Activity activity : mActivityStack) {
            if (activity != null && !activity.isFinishing() && !(activity instanceof MainActivity)) {
                activity.finish();
            }
        }
        mActivityStack.clear();

    }

    public void finishAllActivity() {

        if (CollectionUtils.isEmpty(mActivityStack)) {
            return;
        }
        for (Activity activity : mActivityStack) {
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
        mActivityStack.clear();

    }

    public String getTopActivity() {
        try {
            MyApp a = MyApp.getMyApp();
            android.app.ActivityManager manager =
                    (android.app.ActivityManager) a.getSystemService(a.ACTIVITY_SERVICE);
            List<android.app.ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
            if (runningTaskInfos != null) {
                ComponentName topActivity = runningTaskInfos.get(0).topActivity;
                return (runningTaskInfos.get(0).topActivity).toString();
            } else
                return null;
        } catch (Exception e) {

        }
        return null;
    }

    public boolean isChatActivity() {
        return ActivityManager.getInstance().getTopActivity().contains("ChatActivity");
    }

}

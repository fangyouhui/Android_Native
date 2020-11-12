package com.pai8.ke.utils;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import com.pai8.ke.app.MyApp;
import com.permissionx.guolindev.PermissionX;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

public final class PermissionUtils {

    public static String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
    };

    /**
     * Return whether <em>you</em> have granted the permissions.
     *
     * @param permissions The permissions.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isGranted(final String... permissions) {
        for (String permission : permissions) {
            if (!isGranted(permission)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isGranted(final String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                || PackageManager.PERMISSION_GRANTED
                == ContextCompat.checkSelfPermission(MyApp.getMyApp(), permission);
    }

    /**
     * Launch the application's details settings.
     */
    public static void launchAppDetailsSettings() {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + MyApp.getMyApp().getPackageName()));
        MyApp.getMyApp().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static void apply(FragmentActivity activity, RequestCallBack callBack, String... permissions) {
        PermissionX.init(activity)
                .permissions(permissions)
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> {
                    scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "我已明白");
                })
                .onForwardToSettings((scope, deniedList) -> {
                    scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "我已明白");
                })
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        callBack.granted();
                    } else {
                        callBack.denied(deniedList);
                    }
                });
    }


    public interface RequestCallBack {
        void granted();

        void denied(List<String> deniedList);
    }
}


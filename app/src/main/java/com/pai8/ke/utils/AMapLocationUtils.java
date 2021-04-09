package com.pai8.ke.utils;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;

/**
 * 高德定位
 */
public class AMapLocationUtils {

    private static AMapLocationClient mlocationClient;
    public static AMapLocationClientOption mLocationOption = null;
    public static AMapLocation sLocation = null;

    public static void init(Context context) {
        mlocationClient = new AMapLocationClient(context);
        mLocationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式,Battery_Saving为低功耗模式,Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(false);
        // 定位间隔
        mLocationOption.setInterval(60 * 1000);
        mlocationClient.setLocationOption(mLocationOption);
    }


    /**
     * 获取位置
     *
     * @param listener
     */
    public static void getLocation(MyLocationListener listener, boolean isOnceLocation) {
        if (sLocation == null || sLocation.getErrorCode() != 0 || !isValid(sLocation)) {
            getCurrentLocation(listener, isOnceLocation);
            return;
        }
        listener.result(sLocation);
    }

    /**
     * 获取最新位置
     *
     * @param listener
     */
    public static void getCurrentLocation(final MyLocationListener listener, final boolean isOnceLocation) {
        if (mlocationClient == null) {
            return;
        }
        mlocationClient.startLocation();
        mlocationClient.setLocationListener(location -> {
            if (location == null || location.getErrorCode() != 0 || !isValid(location)) {
                return;
            }
            sLocation = location;
            listener.result(location);
            if (isOnceLocation) {
                mlocationClient.stopLocation();
            }
        });
    }

    /**
     * 销毁定位
     */
    public static void destroy() {
        if (mlocationClient != null) {
            if (mlocationClient.isStarted()) {
                mlocationClient.stopLocation();
            }
            mlocationClient.onDestroy();
        }
    }

    public interface MyLocationListener {
        void result(AMapLocation location);
    }

    private static boolean isValid(AMapLocation location) {
        return location.getLatitude() != 0.0 || location.getLongitude() != 0.0;
    }

    private static final double EARTH_RADIUS = 6378137.0;

    public static double getDistance(double longitude, double latitue, double longitude2, double latitue2) {
        double lat1 = rad(latitue);
        double lat2 = rad(latitue2);
        double a = lat1 - lat2;
        double b = rad(longitude) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }


}

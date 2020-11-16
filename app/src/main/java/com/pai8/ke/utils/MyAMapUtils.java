package com.pai8.ke.utils;

public class MyAMapUtils {

    public static String getFormatDistance(float distance) {
        if (distance < 50) {
            return "<50m";
        } else if (distance >= 50 && distance < 1000) {
            return ((int) distance) + "m";
        } else if (distance >= 1000) {
            double v = Math.rint(distance / 100) / 10;
            return v + "km";
        } else {
            return "";
        }
    }
}

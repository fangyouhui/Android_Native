package com.pai8.ke.utils;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

public class TextViewUtils {

    public static void drawableTop(TextView textView, @DrawableRes int id) {
        Drawable drawable = ResUtils.getDrawable(id);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, drawable, null, null);
    }

    public static void drawableBottom(TextView textView, @DrawableRes int id) {
        Drawable drawable = ResUtils.getDrawable(id);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, null, null, drawable);
    }

    public static void drawableLeft(TextView textView, @DrawableRes int id) {
        Drawable drawable = ResUtils.getDrawable(id);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(drawable, null, null, null);
    }

    public static void drawableRight(TextView textView, @DrawableRes int id) {
        Drawable drawable = ResUtils.getDrawable(id);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, null, drawable, null);
    }
}

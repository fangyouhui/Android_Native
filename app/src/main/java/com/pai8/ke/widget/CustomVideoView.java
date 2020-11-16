package com.pai8.ke.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.VideoView;


public class CustomVideoView extends VideoView {
    private static final String TAG = CustomVideoView.class.getSimpleName();
    private int parentWidth = 0;
    private int parentHeight = 0;
    private int gravity = CENTER;

    public static final int CENTER = 0;
    public static final int CENTER_HORIZONTAL = 1;
    public static final int FULL_SCREEN = 2;


    public CustomVideoView(Context context) {
        super(context);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (gravity == FULL_SCREEN) {
            int width = getDefaultSize(getWidth(), widthMeasureSpec);
            int height = getDefaultSize(getHeight(), heightMeasureSpec);
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        ViewGroup mViewGroup = (ViewGroup) getParent();
        if (null != mViewGroup) {
            parentWidth = mViewGroup.getWidth();
            parentHeight = mViewGroup.getHeight();
            Log.e(TAG, "onMeasure: " + parentWidth + "," + parentHeight);
        }
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        int h = b - t;
        int w = r - l;
        switch (gravity) {
            case CENTER:
                //center模式
                if (parentHeight > h) {
                    int offsetH = parentHeight - h;
                    t += +offsetH / 2;
                    b += offsetH / 2;
                }
                if (parentWidth > w) {
                    int offsetW = parentWidth - w;
                    l += offsetW / 2;
                    r += offsetW / 2;
                }
                break;
            case CENTER_HORIZONTAL:
                //垂直居中
                if (parentHeight > h) {
                    int offsetH = parentHeight - h;
                    t += +offsetH / 2;
                    b += offsetH / 2;
                }
                break;
        }
        Log.e(TAG, "layout: " + l + "," + t + "," + r + "," + b);
        super.layout(l, t, r, b);
    }

    public void setGravityType(int bannerGravity) {
        if (gravity != bannerGravity) {
            gravity = bannerGravity;

            requestLayout();
            invalidate();
        }
    }
}

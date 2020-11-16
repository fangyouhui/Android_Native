package com.pai8.ke.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.gh.qiniushortvideo.utils.Utils;

public class MarkerView extends View {

    ObjectAnimator mTAnimator1;
    ObjectAnimator mTAnimator2;

    public MarkerView(Context context) {
        this(context, null);
    }

    public MarkerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarkerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public ObjectAnimator transactionAnimWithMarker() {
        if (getVisibility() != View.VISIBLE)
            return null;
        if (mTAnimator1 == null) {
            mTAnimator1 = ObjectAnimator.ofFloat(this, "translationY", getTranslationY(),
                    getTranslationY() - Utils.dip2px(getContext().getApplicationContext(), 20));
            mTAnimator2 = ObjectAnimator.ofFloat(this, "translationY",
                    getTranslationY() - Utils.dip2px(getContext().getApplicationContext(), 20),
                    getTranslationY());
            mTAnimator1.setInterpolator(new DecelerateInterpolator());
            mTAnimator1.setDuration(400);
            mTAnimator2.setInterpolator(new AccelerateInterpolator());
            mTAnimator2.setDuration(200);
            AnimatorSet mSet1 = new AnimatorSet();
            mSet1.play(mTAnimator1).before(mTAnimator2);
            mSet1.start();
        } else {
            if (!mTAnimator1.isRunning() && !mTAnimator2.isRunning()) {
                mTAnimator1 = ObjectAnimator.ofFloat(this, "translationY", getTranslationY(),
                        getTranslationY() - Utils.dip2px(getContext().getApplicationContext(), 20));
                mTAnimator2 = ObjectAnimator.ofFloat(this, "translationY",
                        getTranslationY() - Utils.dip2px(getContext().getApplicationContext(), 20),
                        getTranslationY());
                mTAnimator1.setInterpolator(new DecelerateInterpolator());
                mTAnimator1.setDuration(400);
                mTAnimator2.setInterpolator(new AccelerateInterpolator());
                mTAnimator2.setDuration(200);
                AnimatorSet mSet1 = new AnimatorSet();
                mSet1.play(mTAnimator1).before(mTAnimator2);
                mSet1.start();
            }
        }
        return mTAnimator2;
    }
}
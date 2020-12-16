package com.pai8.ke.activity.takeaway.utils;

import android.os.CountDownTimer;

/**
 */

public class TimeCount extends CountDownTimer {
    OnListener listener;

    public TimeCount(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        listener.onTick(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        listener.onFinish();
    }

    public interface  OnListener{
        void onTick(long millisUntilFinished);
        void onFinish();
    }

    public void setListener(OnListener listener){
        this.listener=listener;
    }
}

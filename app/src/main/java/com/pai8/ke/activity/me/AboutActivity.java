package com.pai8.ke.activity.me;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjq.bar.OnTitleBarListener;
import com.pai8.ke.R;
import com.pai8.ke.activity.account.LoginActivity;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.utils.AppUtils;
import com.pai8.ke.utils.ToastUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

public class AboutActivity extends BaseActivity {
    @BindView(R.id.tv_app_version)
    TextView tvAppVersion;

    @BindView(R.id.iv)
    ImageView image;
    private Timer mTimer = new Timer();
    private TimerTask mTimerTask = null;

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void initView() {
        mTitleBar.setTitle("关于软件");
        tvAppVersion.setText("V" + AppUtils.getVerName());
        mTitleBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                finish();
            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {

            }
        });
        image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    mTimerTask = new TimerTask() {
                        @Override
                        public void run() {
                            if(isCancel) return;
                            runOnUiThread(() -> {
                                AccountManager.getInstance().logout();
                                boolean isTest = MyApp.toggleBuildType();
                                ToastUtils.showShort("切换到" + (isTest ? "测试环境" : "正式环境") + "成功");
                                launch(LoginActivity.class);
                            });
                        }
                    };
                    mTimer.schedule(mTimerTask, 10000);
                } else if(event.getAction() == MotionEvent.ACTION_UP) {
                    mTimerTask.isCancel = true;
                }
                return true;
            }
        });
    }

    static abstract class TimerTask extends java.util.TimerTask {
        boolean isCancel = false;
    }

}

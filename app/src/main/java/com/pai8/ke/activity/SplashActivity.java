package com.pai8.ke.activity;

import com.pai8.ke.R;
import com.pai8.ke.activity.me.LoginActivity;
import com.pai8.ke.base.BaseActivity;

public class SplashActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        if (mAccountManager.isLogin()) {
            launch(MainActivity.class);
        } else {
            launch(LoginActivity.class);
        }
        finish();
    }
}

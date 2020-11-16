package com.pai8.ke.activity;

import com.pai8.ke.R;
import com.pai8.ke.activity.account.LoginActivity;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.utils.AMapLocationUtils;
import com.pai8.ke.utils.PermissionUtils;

import java.util.List;

public class SplashActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        PermissionUtils.apply(this, new PermissionUtils.RequestCallBack() {
            @Override
            public void granted() {
                if (mAccountManager.isLogin()) {
                    launch(MainActivity.class);
                } else {
                    launch(LoginActivity.class);
                }
                finish();
            }

            @Override
            public void denied(List<String> deniedList) {

            }
        }, PermissionUtils.PERMISSIONS);
    }
}

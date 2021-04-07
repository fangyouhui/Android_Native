package com.pai8.ke.activity;

import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.util.ThreadUtils;
import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.NoViewModel;
import com.pai8.ke.app.MyApp;
import com.pai8.ke.utils.PermissionUtils;
import com.pai8.ke.utils.ToastUtils;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SplashActivity extends BaseActivity<NoViewModel, com.pai8.ke.databinding.ActivitySplashBinding> {

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        PermissionUtils.apply(this, new PermissionUtils.RequestCallBack() {
            @Override
            public void granted() {
                MyApp.getLocation();
                router();
            }

            @Override
            public void denied(List<String> deniedList) {
                ToastUtils.showShort("缺少必要的权限，请前往设置里开启");
            }
        }, PermissionUtils.PERMISSIONS);
    }

    private void router() {
        ThreadUtils.runOnUiThreadDelayed(() -> {
            startActivity(new Intent(getBaseContext(), MainActivity.class));
            finish();
        }, 2000);
    }

}

package com.pai8.ke.activity.me;

import android.os.Bundle;
import android.view.View;

import com.hjq.bar.OnTitleBarListener;
import com.pai8.ke.R;
import com.pai8.ke.activity.account.LoginActivity;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.manager.ActivityManager;

import androidx.appcompat.app.AlertDialog;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 地址选择
 * Created by gh on 2020/11/16.
 */
public class SettingActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {
        mTitleBar.setTitle("设置");
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
    }

    @OnClick({R.id.tv_btn_logout, R.id.rl_1, R.id.rl_2, R.id.rl_3, R.id.rl_4, R.id.rl_5})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_btn_logout:
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle("提示")
                        .setMessage("确认退出当前登录？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确认", (dialog, which) -> {
                            AccountManager.getInstance().logout();
                            launch(LoginActivity.class);
                        })
                        .show();
                break;
            case R.id.rl_1:
                break;
            case R.id.rl_2:
                break;
            case R.id.rl_3://隐私政策
                break;
            case R.id.rl_4://用户协议
                break;
            case R.id.rl_5://关于软件
                launch(AboutActivity.class);
                break;
        }
    }

}

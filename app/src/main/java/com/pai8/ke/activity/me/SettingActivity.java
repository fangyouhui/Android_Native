package com.pai8.ke.activity.me;

import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.hjq.bar.OnTitleBarListener;
import com.pai8.ke.R;
import com.pai8.ke.activity.account.LoginActivity;
import com.pai8.ke.activity.common.WebViewActivity;
import com.pai8.ke.api.Api;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.base.retrofit.BaseObserver;
import com.pai8.ke.base.retrofit.RxSchedulers;
import com.pai8.ke.entity.resp.MyInfoResp;
import com.pai8.ke.manager.AccountManager;

import butterknife.OnClick;

/**
 * 地址选择
 * Created by gh on 2020/11/16.
 */
public class SettingActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_setting_copy;
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
                // 账号安全（临时改为账号注销）
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle("提示")
                        .setMessage("注销账号后，您的所有数据将被清空，是否继续？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("继续", (dialog, which) -> {
                            Api.getInstance().memberLogout()
                                    .doOnSubscribe(disposable -> {
                                    })
                                    .compose(RxSchedulers.io_main())
                                    .subscribe(new BaseObserver<Object>() {
                                        @Override
                                        protected void onSuccess(Object o) {
                                            AccountManager.getInstance().logout();
                                            launch(LoginActivity.class);
                                        }

                                        @Override
                                        protected void onError(String msg, int errorCode) {

                                        }
                                    });
                        })
                        .show();
                break;
            case R.id.rl_2:
                break;
            case R.id.rl_3://隐私政策
                WebViewActivity.launch(this, "http://test.5pai8.com/agreement/privacyProtocol/index.html",
                        "隐私政策");
                break;
            case R.id.rl_4://用户协议
                WebViewActivity.launch(this, "http://test.5pai8.com/agreement/serverProtocol/index.html",
                        "用户协议");
                break;
            case R.id.rl_5://关于软件
                launch(AboutActivity.class);
                break;
        }
    }

}

package com.pai8.ke.activity.me;

import android.view.View;
import android.widget.TextView;

import com.hjq.bar.OnTitleBarListener;
import com.pai8.ke.R;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.utils.AppUtils;

import butterknife.BindView;

public class AboutActivity extends BaseActivity {
    @BindView(R.id.tv_app_version)
    TextView tvAppVersion;

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
    }

}

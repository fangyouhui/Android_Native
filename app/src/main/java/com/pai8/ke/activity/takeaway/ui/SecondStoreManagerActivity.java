package com.pai8.ke.activity.takeaway.ui;

import android.content.Intent;
import android.view.View;

import com.pai8.ke.R;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.BasePresenter;

public class SecondStoreManagerActivity extends BaseMvpActivity implements View.OnClickListener {

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_second_store_manager;
    }

    @Override
    public void initView() {

        setImmersionBar(R.id.iv_top);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_back_all:
                finish();
                break;

        }

    }
}

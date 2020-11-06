package com.pai8.ke.activity.takeaway.ui;

import android.content.Intent;
import android.view.View;

import com.pai8.ke.R;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.BasePresenter;

public class MarketingManagerActivity extends BaseMvpActivity implements View.OnClickListener {
    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_marketing_manager;
    }

    @Override
    public void initView() {
        setImmersionBar(R.id.base_tool_bar);
        findViewById(R.id.tv_add_coupon).setOnClickListener(this);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.toolbar_back_all){
            finish();
        }else if(v.getId() == R.id.tv_add_coupon){
            startActivity(new Intent(this,AddCouponActivity.class));
        }
    }
}

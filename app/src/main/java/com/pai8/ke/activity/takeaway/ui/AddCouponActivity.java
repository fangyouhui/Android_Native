package com.pai8.ke.activity.takeaway.ui;

import android.view.View;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.BasePresenter;

public class AddCouponActivity extends BaseMvpActivity implements View.OnClickListener {


    private TextView mTvTime;

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_coupon;
    }

    @Override
    public void initView() {
        setImmersionBar(R.id.base_tool_bar);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
        mTvTime = findViewById(R.id.tv_choose_time);
        mTvTime.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.toolbar_back_all){
            finish();
        }else if(v.getId() == R.id.tv_choose_time){


        }
    }
}

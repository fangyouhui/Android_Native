package com.pai8.ke.activity.takeaway.order;

import android.view.View;

import com.pai8.ke.R;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.BasePresenter;

public class ConfirmOrderActivity extends BaseMvpActivity implements View.OnClickListener {


    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_confirm_order;
    }

    @Override
    public void initView() {
        setImmersionBar(R.id.base_tool_bar);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.toolbar_back_all){
            finish();
        }
    }
}

package com.pai8.ke.activity.takeaway.ui;

import android.view.View;

import com.pai8.ke.R;
import com.pai8.ke.base.BaseActivity;

public class AddressActivity extends BaseActivity implements View.OnClickListener {


    @Override
    public int getLayoutId() {
        return R.layout.activity_address;
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

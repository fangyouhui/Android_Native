package com.pai8.ke.activity.takeaway.ui;

import android.view.View;

import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.Constants;
import com.pai8.ke.activity.takeaway.entity.event.NotifyEvent;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.BasePresenter;

import org.greenrobot.eventbus.EventBus;

public class MerchantSettledThreeActivity extends BaseMvpActivity implements View.OnClickListener {
    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_merchant_settled_three;
    }

    @Override
    public void initView() {

        setImmersionBar(R.id.base_tool_bar);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
        findViewById(R.id.tv_next).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.toolbar_back_all){
            finish();
        }else if(v.getId() == R.id.tv_next){
            EventBus.getDefault().post(new NotifyEvent(Constants.EVENT_TYPE_MERCHANT_SETTLED));
            finish();
        }
    }
}

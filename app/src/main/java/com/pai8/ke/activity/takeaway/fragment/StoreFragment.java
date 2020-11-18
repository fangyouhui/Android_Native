package com.pai8.ke.activity.takeaway.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.Constants;
import com.pai8.ke.activity.takeaway.contract.StoreFragmentContract;
import com.pai8.ke.activity.takeaway.entity.event.ShopDataEvent;
import com.pai8.ke.activity.takeaway.entity.resq.StoreInfo;
import com.pai8.ke.activity.takeaway.presenter.StoreFragmentPresenter;
import com.pai8.ke.base.BaseMvpFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class StoreFragment extends BaseMvpFragment<StoreFragmentPresenter> implements View.OnClickListener, StoreFragmentContract.View {

    private TextView mTvStoreName;
    private TextView mTvAddress,mTvPhone;

    @Override
    public StoreFragmentPresenter initPresenter() {
        return new StoreFragmentPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frament_store;
    }

    @Override
    protected void initView(Bundle arguments) {
        EventBus.getDefault().register(this);
        mTvStoreName = mRootView.findViewById(R.id.tv_store_name);
        mTvAddress = mRootView.findViewById(R.id.tv_address);
        mTvPhone = mRootView.findViewById(R.id.tv_phone);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ShopDataEvent event) {
        if (event.type == Constants.EVENT_TYPE_SHOP_CONTENT) {
            StoreInfo data = event.data.shop_info;

            mTvStoreName.setText(data.shop_name);
            mTvAddress.setText(data.address);
            mTvPhone.setText(data.mobile);

        }

    }


    @Override
    protected void initData() {
        super.initData();


    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

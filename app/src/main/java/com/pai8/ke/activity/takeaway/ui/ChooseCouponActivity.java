package com.pai8.ke.activity.takeaway.ui;

import android.view.View;
import android.widget.TextView;

import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.contract.ChooseCouponContract;
import com.pai8.ke.activity.takeaway.presenter.ChooseCouponPresenter;
import com.pai8.ke.base.BaseMvpActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChooseCouponActivity extends BaseMvpActivity<ChooseCouponPresenter> implements View.OnClickListener, ChooseCouponContract.View {


    private TextView mTvTime;

    private RecyclerView mRecyclerView;


    @Override
    public ChooseCouponPresenter initPresenter() {
        return new ChooseCouponPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_coupon;
    }

    @Override
    public void initView() {
        setImmersionBar(R.id.base_tool_bar);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
        mRecyclerView = findViewById(R.id.rv_coupon);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.toolbar_back_all){
            finish();
        }
    }
}

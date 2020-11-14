package com.pai8.ke.activity.takeaway.ui;

import android.view.View;

import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.DeliveryAddressAdapter;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DeliveryAddressActivity  extends BaseMvpActivity implements View.OnClickListener {


    private RecyclerView mRvAddress;
    private DeliveryAddressAdapter mAdapter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_delivery_address;
    }

    @Override
    public void initView() {
        setImmersionBar(R.id.base_tool_bar);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
        mRvAddress = findViewById(R.id.rv_address);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvAddress.setLayoutManager(layoutManager);

    }


    @Override
    public void initData() {
        super.initData();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("1");
        }
        mAdapter = new DeliveryAddressAdapter(list);
        mRvAddress.setAdapter(mAdapter);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.toolbar_back_all){
            finish();
        }
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }
}


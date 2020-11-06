package com.pai8.ke.activity.takeaway.ui;

import android.content.Intent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.OrderAdapter;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderProcessingActivity extends BaseMvpActivity implements View.OnClickListener {

    private RecyclerView mRvOrder;
    private OrderAdapter mAdapter;

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_processing;
    }

    @Override
    public void initView() {

        setImmersionBar(R.id.base_tool_bar);
        findViewById(R.id.base_tool_bar).setOnClickListener(this);
        mRvOrder = findViewById(R.id.rv_order);
        mRvOrder.setLayoutManager(new LinearLayoutManager(this));



    }

    @Override
    public void initData() {
        super.initData();


        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("1");
        }
        mAdapter = new OrderAdapter(list);
        mRvOrder.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                startActivity(new Intent(OrderProcessingActivity.this, StoreActivity.class));
            }
        });


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.base_tool_bar){
            finish();
        }
    }
}

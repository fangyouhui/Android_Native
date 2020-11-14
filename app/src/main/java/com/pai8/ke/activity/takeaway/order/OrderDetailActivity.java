package com.pai8.ke.activity.takeaway.order;

import android.view.View;
import android.view.ViewGroup;

import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.OrderDetailAdapter;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderDetailActivity extends BaseMvpActivity implements View.OnClickListener {


    private RecyclerView mRvOrderDetail;
    private OrderDetailAdapter mAdapter;

    private View mViewHead,mViewFooter;


    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void initView() {
        setImmersionBar(R.id.base_tool_bar);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
        findViewById(R.id.toolbar_more).setOnClickListener(this);
        mRvOrderDetail = findViewById(R.id.rv_order_detail);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvOrderDetail.setLayoutManager(layoutManager);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add("1");
        }
        mAdapter = new OrderDetailAdapter(list);
        mRvOrderDetail.setAdapter(mAdapter);
        mViewHead = getLayoutInflater().inflate(R.layout.activity_order_detail_head, (ViewGroup) mRvOrderDetail.getParent(), false);
        mViewFooter = getLayoutInflater().inflate(R.layout.activity_order_detail_footer, (ViewGroup) mRvOrderDetail.getParent(), false);
        mAdapter.addHeaderView(mViewHead);
        mAdapter.addFooterView(mViewFooter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.toolbar_back_all){
            finish();
        }else if(v.getId() == R.id.toolbar_more){   //更多

        }
    }
}

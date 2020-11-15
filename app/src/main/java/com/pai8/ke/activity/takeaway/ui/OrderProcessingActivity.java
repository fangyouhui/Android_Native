package com.pai8.ke.activity.takeaway.ui;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.OrderAdapter;
import com.pai8.ke.activity.takeaway.adapter.OrderStatusAdapter;
import com.pai8.ke.activity.takeaway.entity.req.OrderStatusInfo;
import com.pai8.ke.activity.takeaway.order.OrderDetailActivity;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.BasePresenter;
import com.pai8.ke.widget.BottomDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderProcessingActivity extends BaseMvpActivity implements View.OnClickListener {

    private RecyclerView mRvOrder;
    private OrderAdapter mAdapter;

    private BottomDialog mOrderFilterDialog;


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
        findViewById(R.id.toolbar_iv_menu).setOnClickListener(this);
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

                startActivity(new Intent(OrderProcessingActivity.this, OrderDetailActivity.class));
            }
        });


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.base_tool_bar){
            finish();
        }else if(v.getId() == R.id.toolbar_iv_menu){
            showBottomDialog();
        }
    }


    private void showBottomDialog() {
        View view = View.inflate(this, R.layout.dialog_order_filter, null);
        ImageButton itnClose = view.findViewById(R.id.itn_close);
        RecyclerView rvOrderFilter = view.findViewById(R.id.rv_order_filter);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        rvOrderFilter.setLayoutManager(layoutManager);


        List<OrderStatusInfo> statusInfos = new ArrayList<>();
        OrderStatusInfo statusInfo = new OrderStatusInfo();
        statusInfo.name = "待接单";
        statusInfos.add(statusInfo);
        OrderStatusInfo statusInfo1 = new OrderStatusInfo();
        statusInfo1.name = "已接单";
        statusInfos.add(statusInfo1);
        OrderStatusInfo statusInfo2 = new OrderStatusInfo();
        statusInfo2.name = "待配送";
        statusInfos.add(statusInfo2);
        OrderStatusInfo statusInfo3 = new OrderStatusInfo();
        statusInfo3.name = "配送中";
        statusInfos.add(statusInfo3);
        OrderStatusInfo statusInfo4 = new OrderStatusInfo();
        statusInfo4.name = "已完成";
        statusInfos.add(statusInfo4);
        OrderStatusInfo statusInfo5 = new OrderStatusInfo();
        statusInfo5.name = "申请退款";
        statusInfos.add(statusInfo5);

        OrderStatusAdapter adapter = new OrderStatusAdapter(statusInfos);
        rvOrderFilter.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter1, View view, int position) {
                adapter.choosePosition(position);
            }
        });

        itnClose.setOnClickListener(view1 -> {
            mOrderFilterDialog.dismiss();
        });

        if (mOrderFilterDialog == null) {
            mOrderFilterDialog = new BottomDialog(this, view);
        }
        mOrderFilterDialog.setIsCanceledOnTouchOutside(true);
        mOrderFilterDialog.show();
    }


}

package com.pai8.ke.activity.takeaway.ui;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.OrderStatusAdapter;
import com.pai8.ke.activity.takeaway.adapter.ShopOrderAdapter;
import com.pai8.ke.activity.takeaway.contract.ShopOrderContract;
import com.pai8.ke.activity.takeaway.entity.OrderInfo;
import com.pai8.ke.activity.takeaway.entity.req.OrderStatusInfo;
import com.pai8.ke.activity.takeaway.presenter.ShopOrderPresenter;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.widget.BottomDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderProcessingActivity extends BaseMvpActivity<ShopOrderPresenter> implements View.OnClickListener, ShopOrderContract.View {

    private RecyclerView mRvOrder;
    private ShopOrderAdapter mAdapter;
    private BottomDialog mOrderFilterDialog;
    private int page = 1;
    private String status = "";

    @Override
    public ShopOrderPresenter initPresenter() {
        return new ShopOrderPresenter(this);
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
        mAdapter = new ShopOrderAdapter(null);
        mRvOrder.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                OrderInfo orderInfo = mAdapter.getData().get(position);
                //0为接单 1为拒绝订单 2为同意退款申请 3为拒绝退款申请 4为订单制作完成 5为订单配送操作
                if(view.getId() == R.id.tv_cancel){
                    if(orderInfo.order_status == 1){  //拒绝接单
                        mPresenter.shopDealOrder(orderInfo.order_no,1);
                    }else if(orderInfo.order_status == 5){  //拒绝退款
                        mPresenter.shopDealOrder(orderInfo.order_no,3);
                    }
                }else if(view.getId() == R.id.tv_food_status){
                    if(orderInfo.order_status == 1){  //接单
                        mPresenter.shopDealOrder(orderInfo.order_no,0);
                    }else if(orderInfo.order_status == 5){   //同意退款
                        mPresenter.shopDealOrder(orderInfo.order_no,2);

                    }
                }
            }
        });
        mPresenter.orderList(status,page);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                mPresenter.orderList(status,page);

            }
        },mRvOrder);

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
        TextView tvConfirm = view.findViewById(R.id.tv_next);
        RecyclerView rvOrderFilter = view.findViewById(R.id.rv_order_filter);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        rvOrderFilter.setLayoutManager(layoutManager);

        //0为待支付 1为已支付 2为商家已接单 7为订单制作完成 3为配送中 4为订单已完成 5为订单已申请退款 6订单被拒绝退款 8为订单已退款 9为订单已取消 -1为支付超时 -2订单拒绝接单
        List<OrderStatusInfo> statusInfos = new ArrayList<>();
        OrderStatusInfo statusInfo = new OrderStatusInfo();
        statusInfo.name = "待接单";
        statusInfo.status = "1";
        statusInfos.add(statusInfo);
        OrderStatusInfo statusInfo1 = new OrderStatusInfo();
        statusInfo1.name = "已接单";
        statusInfo1.status = "2";
        statusInfos.add(statusInfo1);
        OrderStatusInfo statusInfo2 = new OrderStatusInfo();
        statusInfo2.name = "待配送";
        statusInfo2.status = "7";
        statusInfos.add(statusInfo2);
        OrderStatusInfo statusInfo3 = new OrderStatusInfo();
        statusInfo3.name = "配送中";
        statusInfo3.status = "3";
        statusInfos.add(statusInfo3);
        OrderStatusInfo statusInfo4 = new OrderStatusInfo();
        statusInfo4.name = "已完成";
        statusInfo4.status = "4";
        statusInfos.add(statusInfo4);
        OrderStatusInfo statusInfo5 = new OrderStatusInfo();
        statusInfo5.name = "申请退款";
        statusInfo5.status = "5";
        statusInfos.add(statusInfo5);


        StringBuilder stringBuilder = new StringBuilder();

        OrderStatusAdapter adapter = new OrderStatusAdapter(statusInfos);
        rvOrderFilter.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter1, View view, int position) {
                adapter.choosePosition(position);
                stringBuilder.delete(0,stringBuilder.length());
                for(int i=0;i<adapter.getData().size();i++){
                    if(adapter.getData().get(i).isSelect){
                        stringBuilder.append(adapter.getData().get(i).status);
                        stringBuilder.append("/");
                    }
                }


            }
        });

        itnClose.setOnClickListener(view1 -> {
            mOrderFilterDialog.dismiss();
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                mPresenter.orderList(stringBuilder.substring(0,stringBuilder.length()-1),page);
                mOrderFilterDialog.dismiss();
            }
        });
        if (mOrderFilterDialog == null) {
            mOrderFilterDialog = new BottomDialog(this, view);
        }
        mOrderFilterDialog.setIsCanceledOnTouchOutside(true);
        mOrderFilterDialog.show();
    }


    @Override
    public void getShopListSuccess(List<OrderInfo> data) {
        if(page == 1){
            mAdapter.setNewData(data);
        }else{
            mAdapter.addData(data);
            if(data.size()<10){
                mAdapter.setEnableLoadMore(false);
            }

        }
    }

    @Override
    public void getStatusSuccess(String data) {
        mPresenter.orderList(status,page);
    }
}

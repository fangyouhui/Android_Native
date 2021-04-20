package com.pai8.ke.activity.takeaway.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.OrderStatusAdapter;
import com.pai8.ke.activity.takeaway.adapter.ShopOrderAdapter;
import com.pai8.ke.activity.takeaway.contract.ShopOrderContract;
import com.pai8.ke.activity.takeaway.contract.shopGroupManagerContract;
import com.pai8.ke.activity.takeaway.entity.OrderInfo;
import com.pai8.ke.activity.takeaway.entity.req.OrderStatusInfo;
import com.pai8.ke.activity.takeaway.order.ShopOrderDetailActivity;
import com.pai8.ke.activity.takeaway.presenter.AddGroupGoodPresenter;
import com.pai8.ke.activity.takeaway.presenter.ShopOrderPresenter;
import com.pai8.ke.activity.takeaway.ui.OrderProcessingActivity;
import com.pai8.ke.activity.takeaway.widget.CheckListener;
import com.pai8.ke.base.BaseMvpFragment;
import com.pai8.ke.utils.EventBusUtils;
import com.pai8.ke.widget.BottomDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class shopWaiMainFragment extends BaseMvpFragment<ShopOrderPresenter> implements View.OnClickListener, ShopOrderContract.View{

    private RecyclerView mRvOrder;
    private ShopOrderAdapter mAdapter;
    private int page = 1;
    private final String status = "";

    @Override
    public void onClick(View view) {

    }

    @Override
    public void getStatusSuccess(List<String> data) {
        mPresenter.orderList(status,page);
    }

    @Override
    public void getShopGroupListSuccess(List<OrderInfo> data) {

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
    public ShopOrderPresenter initPresenter() {
        return new ShopOrderPresenter(this);
    }

    /**
     * 设置根布局资源id
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_order_main;
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetResult(Map<String,String> map){
        page =1;
        String name = map.get("name");
        String page = map.get("page");

        mPresenter.orderList(name,1);

    }
    /**
     * 初始化View
     *
     * @param arguments 接收到的从其他地方传递过来的参数
     */
    @Override
    protected void initView(Bundle arguments) {

    }

    @Override
    public void initData() {
        super.initData();
        mRvOrder = mRootView.findViewById(R.id.rv_order);
        mRvOrder.setLayoutManager(new LinearLayoutManager(getContext()));
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        mAdapter = new ShopOrderAdapter(null);
        mRvOrder.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(mActivity, ShopOrderDetailActivity.class)
                        .putExtra("order",mAdapter.getData().get(position)));

            }
        });
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
                    }else if(orderInfo.order_status == 2){  //制作完成
                        mPresenter.shopDealOrder(orderInfo.order_no,4);
                    }else if(orderInfo.order_status == 7){  //送出
                        mPresenter.shopDealOrder(orderInfo.order_no,5);
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
}

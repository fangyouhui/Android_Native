package com.pai8.ke.activity.takeaway.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.lhs.library.base.BaseFragment;
import com.pai8.ke.activity.takeaway.adapter.ShopTakeawayOrderAdapter;
import com.pai8.ke.activity.takeaway.entity.OrderInfo;
import com.pai8.ke.activity.takeaway.order.ShopOrderDetailActivity;
import com.pai8.ke.databinding.ActivityOrderMainBinding;
import com.pai8.ke.viewmodel.ShopTakeawayOrderViewModel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.jetbrains.annotations.Nullable;

/**
 * 店铺外卖订单列表处理
 */
public class ShopTakeawayOrderListFragment extends BaseFragment<ShopTakeawayOrderViewModel, ActivityOrderMainBinding> {

    private ShopTakeawayOrderAdapter mAdapter;
    private int page = 1;
    private String status = "";

    public void filter(String orderStatus) {
        status = orderStatus;
        page = 1;
        initData();
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.recyclerView.setAdapter(mAdapter = new ShopTakeawayOrderAdapter(getContext(), null));
        mBinding.smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                initData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                initData();
            }
        });

        mAdapter.setItemListener(new ShopTakeawayOrderAdapter.ItemListener() {
            @Override
            public void onCancelListener(OrderInfo orderInfo, int position) {
                if (orderInfo.order_status == 1) {  //拒绝接单
                    mViewModel.shopDealOrder(orderInfo.order_no, 1);
                    orderInfo.order_status = -2;
                } else if (orderInfo.order_status == 5) {  //拒绝退款
                    mViewModel.shopDealOrder(orderInfo.order_no, 3);
                    orderInfo.order_status = 6;
                }
            }

            @Override
            public void onStatusListener(OrderInfo orderInfo, int position) {
                if (orderInfo.order_status == 1) {  //接单
                    mViewModel.shopDealOrder(orderInfo.order_no, 0);
                    orderInfo.order_status = 2;
                } else if (orderInfo.order_status == 5) {   //同意退款
                    mViewModel.shopDealOrder(orderInfo.order_no, 2);
                    orderInfo.order_status = 5;
                } else if (orderInfo.order_status == 2) {  //制作完成
                    mViewModel.shopDealOrder(orderInfo.order_no, 4);
                    orderInfo.order_status = 4;
                } else if (orderInfo.order_status == 7) {  //送出
                    mViewModel.shopDealOrder(orderInfo.order_no, 5);
                    orderInfo.order_status = 3;
                }
            }

            @Override
            public void onItemClick(OrderInfo item, int position) {
                startActivity(new Intent(getContext(), ShopOrderDetailActivity.class).putExtra("order", mAdapter.getData().get(position)));
            }
        });


    }

    @Override
    public void initData() {
        mViewModel.shopOrderList(status, page);
    }

    @Override
    public void addObserve() {
        mViewModel.getShopOrderListData().observe(this, data -> {
            if (mBinding.smartRefreshLayout.isRefreshing()) {
                mAdapter.setData(data);
                mBinding.smartRefreshLayout.finishRefresh();
                return;
            }
            if (mBinding.smartRefreshLayout.isLoading()) {
                if (data == null || data.isEmpty()) {
                    mBinding.smartRefreshLayout.finishLoadMoreWithNoMoreData();
                }
                mAdapter.addData(data);
                mBinding.smartRefreshLayout.finishLoadMore();
                return;

            }
            mAdapter.setData(data);

        });

        mViewModel.getShopDealOrderData().observe(this, data -> {
            mAdapter.notifyDataSetChanged();
        });
    }

}

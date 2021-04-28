package com.pai8.ke.activity.takeaway.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.BaseFragment;
import com.pai8.ke.activity.takeaway.adapter.ShopOrderGroupAdapter;
import com.pai8.ke.activity.takeaway.order.ShopOrderDetailActivity;
import com.pai8.ke.databinding.FragmentShopGroupOrderListBinding;
import com.pai8.ke.viewmodel.ShopTakeawayOrderViewModel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.jetbrains.annotations.Nullable;

/**
 * 店家团购订单列表
 */
public class ShopGroupOrderListFragment extends BaseFragment<ShopTakeawayOrderViewModel, FragmentShopGroupOrderListBinding> {
    private ShopOrderGroupAdapter mAdapter;
    private int page = 1;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.recyclerView.setAdapter(mAdapter = new ShopOrderGroupAdapter(null));
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

        mAdapter.setOnItemClickListener((adapter, view, position) -> startActivity(new Intent(getContext(), ShopOrderDetailActivity.class)
                .putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, mAdapter.getData().get(position).order_no)));
    }

    @Override
    public void addObserve() {
        mViewModel.getShopOrderListData().observe(this, data -> {
            if (mBinding.smartRefreshLayout.isRefreshing()) {
                mAdapter.setNewData(data);
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
            mAdapter.setNewData(data);
        });

    }

    @Override
    public void initData() {
        mViewModel.groudOrderList(page);
    }
}

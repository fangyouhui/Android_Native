package com.pai8.ke.shop.ui;

import android.os.Bundle;

import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.BaseFragment;
import com.lhs.library.base.BaseRecyclerViewAdapter;
import com.pai8.ke.databinding.FragmentBusinessGroupBuyListBinding;
import com.pai8.ke.entity.ShopGroupListResult;
import com.pai8.ke.shop.adapter.BusinessGroupBuyAdapter;
import com.pai8.ke.shop.viewmodel.BusinessHomeViewModel;

import org.jetbrains.annotations.Nullable;

public class BusinessGroupBuyListFragment extends BaseFragment<BusinessHomeViewModel, FragmentBusinessGroupBuyListBinding> {
    private String shopId;
    private int page = 1;
    private BusinessGroupBuyAdapter adapter;

    public static BusinessGroupBuyListFragment newInstance(String shopId) {
        BusinessGroupBuyListFragment fragment = new BusinessGroupBuyListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BaseAppConstants.BundleConstant.ARG_PARAMS_0, shopId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopId = getArguments().getString(BaseAppConstants.BundleConstant.ARG_PARAMS_0, "");
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.recyclerView.setAdapter(adapter = new BusinessGroupBuyAdapter(getContext(), null, false));
        adapter.setListener(new BaseRecyclerViewAdapter.BaseItemTouchListener<ShopGroupListResult>() {
            @Override
            public void onItemClick(ShopGroupListResult item, int position) {
                // TODO: 3/29/21 团购商品详情

            }
        });
    }

    @Override
    public void addObserve() {
        mViewModel.getShopGroupListData().observe(getViewLifecycleOwner(), data -> {
            BusinessHomeActivity businessHomeActivity = (BusinessHomeActivity) getActivity();
            if (page == 1) {
                adapter.setData(data);
                businessHomeActivity.finishRefresh();
            } else {
                if (data == null || data.isEmpty()) {
                    businessHomeActivity.finishLoadMore(true);
                } else {
                    adapter.addData(data);
                    businessHomeActivity.finishLoadMore(false);
                }
            }

        });
    }

    @Override
    public void initData() {
        mViewModel.getShopGroupList(shopId, page + "");
    }

    public void onRefresh() {
        page = 1;
        initData();
    }

    public void onLoadMore() {
        page++;
        initData();
    }
}

package com.pai8.ke.shop.ui;

import android.content.Intent;
import android.os.Bundle;

import com.lhs.library.base.BaseAppConstants;
import com.lhs.library.base.BaseFragment;
import com.pai8.ke.databinding.FragmentShopProductListBinding;
import com.pai8.ke.entity.GroupBuyTypeResult;
import com.pai8.ke.entity.SmartRefreshMessageEvent;
import com.pai8.ke.shop.adapter.ShopProductAdapter;
import com.pai8.ke.shop.viewmodel.ShopMainViewModel;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.Nullable;

public class ShopProductListFragment extends BaseFragment<ShopMainViewModel, FragmentShopProductListBinding> {
    private GroupBuyTypeResult bean;
    private ShopProductAdapter adapter;

    public static ShopProductListFragment newInstance(GroupBuyTypeResult bean) {
        ShopProductListFragment fragment = new ShopProductListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BaseAppConstants.BundleConstant.ARG_PARAMS_0, bean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        bean = (GroupBuyTypeResult) getArguments().getSerializable(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
        mBinding.recyclerView.setAdapter(adapter = new ShopProductAdapter(getContext(), null, false));
        adapter.setListener((item, position) -> {
            Intent intent = new Intent(getContext(), ShopProductDetailActivity.class);
            intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0, item.getShop_id() + "");
            intent.putExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_1, item.getId() + "");
            startActivity(intent);
        });
    }

    private int page = 1;

    @Override
    public void addObserve() {
        SmartRefreshMessageEvent smartRefreshMessageEvent = new SmartRefreshMessageEvent();
        mViewModel.getGetGroupGoodsListData().observe(getViewLifecycleOwner(), data -> {
            if (page == 1) {
                adapter.setData(data);
                smartRefreshMessageEvent.smartRefreshType = SmartRefreshMessageEvent.FINISH_REFRESH;
            } else {
                adapter.addData(data);
                smartRefreshMessageEvent.smartRefreshType = (data == null || data.isEmpty()) ? SmartRefreshMessageEvent.FINISH_LOAD_MORE_WITH_NO_MORE : SmartRefreshMessageEvent.FINISH_LOAD_MORE;
            }
            EventBus.getDefault().post(smartRefreshMessageEvent);
        });
    }

    @Override
    public void initData() {
        mViewModel.getGroupGoodsList(String.valueOf(page), bean.getId() + "");
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

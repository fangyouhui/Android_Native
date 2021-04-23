package com.pai8.ke.shop.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ToastUtils;
import com.lhs.library.base.BaseFragment;
import com.pai8.ke.databinding.FragmentTabShopMainBinding;
import com.pai8.ke.entity.BannerResult;
import com.pai8.ke.entity.GroupBuyTypeResult;
import com.pai8.ke.entity.SmartRefreshMessageEvent;
import com.pai8.ke.groupBuy.adapter.GroupBuyBannerAdapter;
import com.pai8.ke.groupBuy.adapter.ViewPagerAdapter;
import com.pai8.ke.shop.viewmodel.ShopMainViewModel;
import com.pai8.ke.utils.EventBusUtils;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TabShopMainFragment extends BaseFragment<ShopMainViewModel, FragmentTabShopMainBinding> {

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtils.register(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ShopProductListFragment showFragment =
                        (ShopProductListFragment) tabFragmentAdapter.getFragment(mBinding.viewPager.getCurrentItem());
                showFragment.onLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                ShopProductListFragment showFragment =
                        (ShopProductListFragment) tabFragmentAdapter.getFragment(mBinding.viewPager.getCurrentItem());
                showFragment.onRefresh();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SmartRefreshMessageEvent event) {
        if (event.smartRefreshType == SmartRefreshMessageEvent.FINISH_REFRESH) {
            mBinding.smartRefreshLayout.finishRefresh();
        } else if (event.smartRefreshType == SmartRefreshMessageEvent.FINISH_LOAD_MORE) {
            mBinding.smartRefreshLayout.finishLoadMore();
        } else {
            mBinding.smartRefreshLayout.finishLoadMoreWithNoMoreData();
        }
    }

    @Override
    public void initData() {
        mViewModel.getBannerList();
        mViewModel.getFoodSortList();
    }

    private ViewPagerAdapter tabFragmentAdapter;

    @Override
    public void addObserve() {
        mViewModel.getGetFoodSortListData().observe(this, data -> {
            tabFragmentAdapter = new ViewPagerAdapter(getChildFragmentManager());
            for (GroupBuyTypeResult datum : data) {
                tabFragmentAdapter.addFragment(ShopProductListFragment.newInstance(datum), datum.getType_name());
            }
            mBinding.viewPager.setNoScroll(true);
            mBinding.viewPager.setOffscreenPageLimit(2);
            mBinding.viewPager.setAdapter(tabFragmentAdapter);
            mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
        });
        mViewModel.getBannerListData().observe(this, data -> setBanner(data));
    }

    private void setBanner(List<BannerResult> banners) {
        GroupBuyBannerAdapter bannerAdapter = new GroupBuyBannerAdapter(banners);
        mBinding.banner.setAdapter(bannerAdapter);
        mBinding.banner.setOnBannerListener((data, position) -> {
            BannerResult banner = (BannerResult) data;
            ToastUtils.showShort("敬请期待");
        });
    }

}

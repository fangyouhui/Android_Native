package com.pai8.ke.groupBuy.ui;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ToastUtils;
import com.lhs.library.base.BaseActivity;
import com.pai8.ke.databinding.ActivityGroupBuyMainBinding;
import com.pai8.ke.entity.BusinessTypeResult;
import com.pai8.ke.entity.GetGroupShopListResult;
import com.pai8.ke.groupBuy.adapter.GroupBuyBannerAdapter;
import com.pai8.ke.groupBuy.adapter.ViewPagerAdapter;
import com.pai8.ke.groupBuy.viewmodel.GroupBuyMainViewModel;
import com.pai8.ke.utils.PreferencesUtils;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 团购店铺首页
 */
public class GroupBuyMainActivity extends BaseActivity<GroupBuyMainViewModel, ActivityGroupBuyMainBinding> {

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        addObserve();
        String poiName = (String) PreferencesUtils.get(this, "poiName", "");
        mBinding.tvGps.setText(TextUtils.isEmpty(poiName) ? "未知" : poiName);

        mBinding.tvSearch.setOnClickListener(v -> {
        });

        mBinding.smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                GroupBuyBusinessListFragment groupBuyTypeListFragment =
                        (GroupBuyBusinessListFragment) tabFragmentAdapter.getFragment(mBinding.viewPager.getCurrentItem());
                groupBuyTypeListFragment.onLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                GroupBuyBusinessListFragment groupBuyTypeListFragment =
                        (GroupBuyBusinessListFragment) tabFragmentAdapter.getFragment(mBinding.viewPager.getCurrentItem());
                groupBuyTypeListFragment.onRefresh();
            }
        });

    }

    public void finishRefresh() {
        mBinding.smartRefreshLayout.finishRefresh();
    }

    public void finishLoadMore(boolean withNoMore) {
        if (withNoMore) {
            mBinding.smartRefreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            mBinding.smartRefreshLayout.finishLoadMore();
        }
    }

    public void setBanner(List<GetGroupShopListResult.Banner> banners) {
        GroupBuyBannerAdapter bannerAdapter = new GroupBuyBannerAdapter(banners);
        mBinding.banner.setAdapter(bannerAdapter);
        mBinding.banner.setOnBannerListener((data, position) -> {
            GetGroupShopListResult.Banner banner = (GetGroupShopListResult.Banner) data;
            ToastUtils.showShort("敬请期待");
        });
    }

    @Override
    public void initData() {
        mViewModel.businessType();
    }

    private ViewPagerAdapter tabFragmentAdapter = new ViewPagerAdapter(getSupportFragmentManager());

    @Override
    public void addObserve() {
        mViewModel.getBusinessTypeData().observe(this, data -> {
            for (BusinessTypeResult datum : data) {
                tabFragmentAdapter.addFragment(GroupBuyBusinessListFragment.newInstance(datum), datum.getType_name());
            }
            mBinding.viewPager.setNoScroll(true);
            mBinding.viewPager.setOffscreenPageLimit(2);
            mBinding.viewPager.setAdapter(tabFragmentAdapter);
            mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
        });
    }
}

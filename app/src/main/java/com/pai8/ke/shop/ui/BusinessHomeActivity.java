package com.pai8.ke.shop.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.PhoneUtils;
import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.pai8.ke.databinding.ActivityBusinessHomeBinding;
import com.pai8.ke.entity.GetGroupShopListResult;
import com.pai8.ke.entity.GroupShopInfoResult;
import com.pai8.ke.groupBuy.adapter.ViewPagerAdapter;
import com.pai8.ke.shop.viewmodel.BusinessHomeViewModel;
import com.pai8.ke.utils.ImageLoadUtils;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.jetbrains.annotations.Nullable;

public class BusinessHomeActivity extends BaseActivity<BusinessHomeViewModel, ActivityBusinessHomeBinding> {
    private ViewPagerAdapter tabFragmentAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    private GetGroupShopListResult.ShopList bean;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        bean = (GetGroupShopListResult.ShopList) getIntent().getSerializableExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
        mBinding.btnBack.setOnClickListener(v -> finish());

        tabFragmentAdapter.addFragment(BusinessGroupBuyListFragment.newInstance(bean.getId() + ""), "团购");
        tabFragmentAdapter.addFragment(BusinessVideoListFragment.newInstance(bean.getId() + ""), "商家视频");
        tabFragmentAdapter.addFragment(BusinessVideoListFragment.newInstance(bean.getId() + ""), "外卖");
        mBinding.viewPager.setNoScroll(true);
        mBinding.viewPager.setOffscreenPageLimit(2);
        mBinding.viewPager.setAdapter(tabFragmentAdapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);

        mBinding.btnCall.setOnClickListener(v -> {
            if (mBinding.btnCall.getTag() != null) {
                String phone = (String) mBinding.btnCall.getTag();
                PhoneUtils.dial(phone);
            }
        });

        mBinding.smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getCurrentFragment().onLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getCurrentFragment().onRefresh();
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

    private BusinessGroupBuyListFragment getCurrentFragment() {
        int pos = mBinding.viewPager.getCurrentItem();
        return (BusinessGroupBuyListFragment) tabFragmentAdapter.getFragment(pos);
    }

    @Override
    public void addObserve() {
        mViewModel.getGetGroupShopInfoData().observe(this, data -> {
            bindView(data);
        });
    }

    @Override
    public void initData() {
        mViewModel.getGroupShopInfo(bean.getId() + "");
    }

    private void bindView(GroupShopInfoResult shopInfo) {
        ImageLoadUtils.loadImage(shopInfo.getShop_img(), mBinding.ivLogo);
        mBinding.tvName.setText(shopInfo.getShop_name());
        mBinding.tvFraction.setText(shopInfo.getScore() + "");
        mBinding.tvTotalSales.setText(String.format("总销量 %d件", shopInfo.getMonth_sale_count()));
        mBinding.tvAddress.setText(shopInfo.getCity() + shopInfo.getDistrict() + shopInfo.getAddress());
        mBinding.tvPhone.setText(String.format("电话：%s", shopInfo.getMobile()));
        mBinding.btnCall.setTag(shopInfo.getMobile());
    }
}

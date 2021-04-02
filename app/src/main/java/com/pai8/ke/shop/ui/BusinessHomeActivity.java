package com.pai8.ke.shop.ui;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.PhoneUtils;
import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.pai8.ke.databinding.ActivityBusinessHomeBinding;
import com.pai8.ke.entity.GroupShopInfoResult;
import com.pai8.ke.groupBuy.adapter.ViewPagerAdapter;
import com.pai8.ke.shop.viewmodel.BusinessHomeViewModel;
import com.pai8.ke.utils.ImageLoadUtils;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.jetbrains.annotations.Nullable;

/**
 * 商家店铺首页
 */
public class BusinessHomeActivity extends BaseActivity<BusinessHomeViewModel, ActivityBusinessHomeBinding> {
    private ViewPagerAdapter tabFragmentAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    private String shopId;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        shopId = getIntent().getStringExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
        mBinding.btnBack.setOnClickListener(v -> finish());

        tabFragmentAdapter.addFragment(BusinessGroupBuyListFragment.newInstance(shopId), "团购");
        tabFragmentAdapter.addFragment(BusinessVideoListFragment.newInstance(shopId + ""), "商家视频");
        //     tabFragmentAdapter.addFragment(BusinessVideoListFragment.newInstance(shopId + ""), "外卖");
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
        mBinding.btnContactMerchant.setOnClickListener(v -> mBinding.btnCall.callOnClick());
        mBinding.btnReceiveDiscount.setOnClickListener(v -> { //领取优惠
            mViewModel.shopCouponList(shopId);
        });
        mBinding.smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                int pos = mBinding.viewPager.getCurrentItem();
                if (pos == 0) {
                    BusinessGroupBuyListFragment businessGroupBuyListFragment = (BusinessGroupBuyListFragment) tabFragmentAdapter.getFragment(pos);
                    businessGroupBuyListFragment.onLoadMore();
                } else {
                    BusinessVideoListFragment businessVideoListFragment = (BusinessVideoListFragment) tabFragmentAdapter.getFragment(pos);
                    businessVideoListFragment.onLoadMore();
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                int pos = mBinding.viewPager.getCurrentItem();
                if (pos == 0) {
                    BusinessGroupBuyListFragment businessGroupBuyListFragment = (BusinessGroupBuyListFragment) tabFragmentAdapter.getFragment(pos);
                    businessGroupBuyListFragment.onRefresh();
                } else {
                    BusinessVideoListFragment businessVideoListFragment = (BusinessVideoListFragment) tabFragmentAdapter.getFragment(pos);
                    businessVideoListFragment.onRefresh();
                }
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

    @Override
    public void addObserve() {
        mViewModel.getGetGroupShopInfoData().observe(this, data -> bindGroupShopInfo(data));
        mViewModel.getShopCouponListData().observe(this, data -> {
            if (data.getExpress_coupon_list().isEmpty() && data.getOrder_coupon_list().isEmpty()) {//无任何优惠卷
                // TODO: 4/2/21 无优惠卷对话框 
                return;
            }

        });
    }

    @Override
    public void initData() {
        mViewModel.getGroupShopInfo(shopId);
    }

    private void bindGroupShopInfo(GroupShopInfoResult shopInfo) {
        ImageLoadUtils.loadImage(shopInfo.getShop_img(), mBinding.ivLogo);
        mBinding.tvName.setText(shopInfo.getShop_name());
        mBinding.tvFraction.setText(shopInfo.getScore() + "");
        mBinding.tvMonthSaleCount.setText(String.format("月售 %d", shopInfo.getMonth_sale_count()));
        mBinding.tvIntroduction.setText("简介：" + (TextUtils.isEmpty(shopInfo.getShop_desc()) ? "暂无简介" : shopInfo.getShop_desc()));
        mBinding.tvAddress.setText(shopInfo.getCity() + shopInfo.getDistrict() + shopInfo.getAddress());
        mBinding.tvPhone.setText(String.format("电话：%s", shopInfo.getMobile()));
        mBinding.btnCall.setTag(shopInfo.getMobile());
    }
}

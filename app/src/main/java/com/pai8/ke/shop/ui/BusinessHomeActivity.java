package com.pai8.ke.shop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.blankj.utilcode.util.PhoneUtils;
import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.pai8.ke.activity.account.LoginActivity;
import com.pai8.ke.activity.common.NaviActivity;
import com.pai8.ke.activity.common.ShareBottomDialogFragment;
import com.pai8.ke.databinding.ActivityBusinessHomeBinding;
import com.pai8.ke.entity.GroupShopInfoResult;
import com.pai8.ke.fragment.CouponGetDialogFragment;
import com.pai8.ke.groupBuy.adapter.ViewPagerAdapter;
import com.pai8.ke.manager.AccountManager;
import com.pai8.ke.shop.viewmodel.BusinessHomeViewModel;
import com.pai8.ke.utils.ImageLoadUtils;
import com.pai8.ke.utils.MyAMapUtils;
import com.pai8.ke.utils.PreferencesUtils;
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
            CouponGetDialogFragment couponGetDialogFragment = CouponGetDialogFragment.newInstance(shopId);
            couponGetDialogFragment.showNow(getSupportFragmentManager(), "CouponGetDialogFragment");
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
        mBinding.btnFavorites.setOnClickListener(v -> {
            if (!AccountManager.getInstance().isLogin()) {
                startActivity(new Intent(this, LoginActivity.class));
                return;
            }
            if (mBinding.btnFavorites.isSelected()) {
                mViewModel.shopUncollect(shopId);
                return;
            }
            mViewModel.shopCollect(shopId);

        });

        mBinding.btnNav.setOnClickListener(v -> {
            GroupShopInfoResult shopInfo = mViewModel.getGetGroupShopInfoData().getValue();
            if (shopInfo != null) {
                String curLatitude = (String) PreferencesUtils.get(getBaseContext(), "latitude", "0");
                String curLongitude = (String) PreferencesUtils.get(getBaseContext(), "longitude", "0");
                // 单位为米
                LatLng latLng01 = new LatLng(Double.valueOf(shopInfo.getLatitude()), Double.valueOf(shopInfo.getLongitude()));
                LatLng latLng02 = new LatLng(Double.valueOf(curLatitude), Double.valueOf(curLongitude));
                //单位米
                float distance = AMapUtils.calculateLineDistance(latLng01, latLng02);
                NaviActivity.launch(this, shopInfo.getAddress(), MyAMapUtils.getFormatDistance(distance), shopInfo.getLongitude(), shopInfo.getLatitude());
            }

        });

        mBinding.ivShare.setOnClickListener(v -> {
            ShareBottomDialogFragment dialogFragment = ShareBottomDialogFragment.newInstance(2, shopId + "");
            dialogFragment.show(getSupportFragmentManager(), "share");
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
        mViewModel.getShopCollectData().observe(this, data -> {
            mBinding.btnFavorites.setSelected(data);
        });
        mViewModel.isUserFollowData().observe(this, data -> {
            mBinding.btnFavorites.setSelected(data);
        });

    }

    @Override
    public void initData() {
        mViewModel.getGroupShopInfo(shopId);
        mViewModel.isUserFollow(shopId);
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

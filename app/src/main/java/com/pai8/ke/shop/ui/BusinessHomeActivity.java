package com.pai8.ke.shop.ui;

import android.os.Bundle;

import com.blankj.utilcode.util.PhoneUtils;
import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.BaseAppConstants;
import com.pai8.ke.databinding.ActivityBusinessHomeBinding;
import com.pai8.ke.entity.GetGroupShopListResult;
import com.pai8.ke.entity.GroupShopInfoResult;
import com.pai8.ke.groupBuy.adapter.ViewPagerAdapter;
import com.pai8.ke.shop.viewmodel.BusinessHomeViewModel;
import com.pai8.ke.utils.ImageLoadUtils;

import org.jetbrains.annotations.Nullable;

public class BusinessHomeActivity extends BaseActivity<BusinessHomeViewModel, ActivityBusinessHomeBinding> {
    private String[] titles = new String[]{"团购", "商家视频", "外卖"};
    private ViewPagerAdapter tabFragmentAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    private GetGroupShopListResult.ShopList bean;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        bean = (GetGroupShopListResult.ShopList) getIntent().getSerializableExtra(BaseAppConstants.BundleConstant.ARG_PARAMS_0);
        mBinding.btnBack.setOnClickListener(v -> finish());
        for (String title : titles) {
            tabFragmentAdapter.addFragment(new BusinessGroupBuyListFragment(), title);
        }
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

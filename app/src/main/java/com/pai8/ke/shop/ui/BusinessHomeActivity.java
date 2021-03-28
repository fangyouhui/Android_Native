package com.pai8.ke.shop.ui;

import android.os.Bundle;

import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.NoViewModel;
import com.pai8.ke.databinding.ActivityBusinessHomeBinding;
import com.pai8.ke.groupBuy.adapter.ViewPagerAdapter;

import org.jetbrains.annotations.Nullable;

public class BusinessHomeActivity extends BaseActivity<NoViewModel, ActivityBusinessHomeBinding> {
    private String[] titles = new String[]{"团购", "商家视频", "外卖"};
    private ViewPagerAdapter tabFragmentAdapter = new ViewPagerAdapter(getSupportFragmentManager());

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.btnBack.setOnClickListener(v -> finish());
        for (String title : titles) {
            tabFragmentAdapter.addFragment(new BusinessGroupBuyListFragment(), title);
        }
        mBinding.viewPager.setNoScroll(true);
        mBinding.viewPager.setOffscreenPageLimit(2);
        mBinding.viewPager.setAdapter(tabFragmentAdapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
    }
}

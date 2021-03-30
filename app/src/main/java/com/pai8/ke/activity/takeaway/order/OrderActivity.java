package com.pai8.ke.activity.takeaway.order;

import android.os.Bundle;

import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.NoViewModel;
import com.pai8.ke.databinding.ActivityOrderBinding;
import com.pai8.ke.groupBuy.adapter.ViewPagerAdapter;

import org.jetbrains.annotations.Nullable;

public class OrderActivity extends BaseActivity<NoViewModel, ActivityOrderBinding> {

    private ViewPagerAdapter tabFragmentAdapter = new ViewPagerAdapter(getSupportFragmentManager());

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        //  tabFragmentAdapter.addFragment(new OrderFragment(), "外卖"); 会闪退，时间紧迫，后续有空处理
        tabFragmentAdapter.addFragment(new GroupOrderListFragment(), "团购");
        mBinding.viewPager.setOffscreenPageLimit(2);
        mBinding.viewPager.setAdapter(tabFragmentAdapter);
        mBinding.tabLayout.setViewPager(mBinding.viewPager);
    }

}

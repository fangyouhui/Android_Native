package com.pai8.ke.activity.takeaway.ui;

import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.ViewPagerAdapter;
import com.pai8.ke.activity.takeaway.fragment.StoreFragment;
import com.pai8.ke.base.BaseActivity;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class StoreActivity extends BaseActivity implements View.OnClickListener {
    private ArrayList<Fragment> fragments;

    @Override
    public int getLayoutId() {
        return R.layout.activity_store;
    }

    @Override
    public void initView() {
        setImmersionBar(R.id.iv_store);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
        SlidingTabLayout mTabLayout = findViewById(R.id.tabLayout);
        ViewPager mViewPager = findViewById(R.id.vp_balance);
        fragments = new ArrayList<>();
        fragments.add(new StoreFragment());
        fragments.add(new StoreFragment());
        fragments.add(new StoreFragment());
        String[] mTitles = new String[]{"商品","评价","商家"};
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments, mTitles));
        mTabLayout.setViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.toolbar_back_all){
            finish();
        }
    }
}

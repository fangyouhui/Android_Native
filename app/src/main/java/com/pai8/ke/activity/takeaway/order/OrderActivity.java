package com.pai8.ke.activity.takeaway.order;

import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.pai8.ke.R;
import com.pai8.ke.activity.takeaway.adapter.ViewPagerAdapter;
import com.pai8.ke.base.BaseMvpActivity;
import com.pai8.ke.base.BasePresenter;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class OrderActivity extends BaseMvpActivity implements View.OnClickListener {

    private ArrayList<Fragment> fragments;
    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_good_manager;
    }

    @Override
    public void initView() {
        setImmersionBar(R.id.base_tool_bar);
        findViewById(R.id.toolbar_back_all).setOnClickListener(this);
        SlidingTabLayout mTabLayout = findViewById(R.id.tabLayout);
        ViewPager mViewPager = findViewById(R.id.vp_balance);
        fragments = new ArrayList<>();
        fragments.add(new OrderFragment());
        fragments.add(new OrderFragment());
        String[] mTitles = new String[]{"外卖","团购"};
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments, mTitles));
        mTabLayout.setViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(0);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_back_all) {
            finish();
        }
    }
}
